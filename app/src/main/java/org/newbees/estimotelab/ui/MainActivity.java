package org.newbees.estimotelab.ui;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avospush.notification.NotificationCompat;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.orhanobut.logger.Logger;

import org.newbees.estimotelab.MyApplication;
import org.newbees.estimotelab.NotifyId;
import org.newbees.estimotelab.R;
import org.newbees.estimotelab.RequestId;
import org.newbees.estimotelab.model.BeaconMessage;
import org.newbees.estimotelab.ui.adapter.MessageDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    private BeaconManager beaconManager = new BeaconManager(this);
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("rid", null, null, null);

    private static final int REQUEST_ENABLE_BT = 1234;

    private long lastDiscovered = 0;

    @InjectView(R.id.mainLv)
    ListView mainLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        if (MyApplication.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        AVQuery<BeaconMessage> query = new AVQuery<>();
        query.findInBackground(new FindCallback<BeaconMessage>() {
            @Override
            public void done(List<BeaconMessage> list, AVException e) {
                if (e != null) {
                    Logger.d(e.fillInStackTrace().getMessage());
                    return;
                }

                mainLv.setAdapter(new MessageDetailAdapter(list));
                Logger.d("list.size(): " + list.size());

                mainLv.setOnItemClickListener(MainActivity.this);
            }
        });

        AVAnalytics.trackAppOpened(getIntent());

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beaconList) {
                if (lastDiscovered != 0 && (System.currentTimeMillis() - lastDiscovered < 20000)) {
                    return;
                }
                lastDiscovered = System.currentTimeMillis();


                Logger.d("onBeaconsDiscovered: " + beaconList.size());
                Logger.d("region: " + region.toString());
                if (beaconList.size() > 0) {
                    List<String> majorList = new ArrayList<String>();

                    Beacon bestBeacon = null;
                    for (Beacon b : beaconList) {
                        majorList.add("" + b.getMajor());
                        if (bestBeacon == null) {
                            bestBeacon = b;
                        } else {
                            double distanceNew = Math.min(Utils.computeAccuracy(b), 6.0);
                            double distanceOld = Math.min(Utils.computeAccuracy(bestBeacon), 6.0);
                            if (distanceNew > distanceOld) {
                                bestBeacon = b;
                            }
                        }

                        Logger.d(b.toString());
                    }

                    AVQuery<AVObject> query = new AVQuery<AVObject>("BeaconMessage");
                    query.whereContains("major", "" + bestBeacon.getMajor());
                    Logger.d("" + bestBeacon.getMajor());
                    query.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            if (e != null) {
                                Logger.d(e.fillInStackTrace().getMessage());
                                return;
                            }
                            for (AVObject o : list) {
                                Logger.d("" + o.toString());
                            }
                            Logger.d("message list: " + list.size());
                            if (list.size() > 0) {
                                AVObject msg = list.get(0);
                                // TODO get detail ad message
                                String msgTitle = msg.getString("msgTitle");
                                String msgDetail = msg.getString("msgDetail");

                                postNotification(msgTitle, msgDetail);
                            }
                        }
                    });
                }
            }
        });
    }

    private void postNotification(String msgTitle, String msgDetail) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(msgTitle)
                        .setContentText(msgDetail);

        try {
            Class cls = Class.forName("org.newbees.estimotelab.ui.CheckInActivity");

            Intent resultIntent = new Intent(MainActivity.this, cls);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this);
            stackBuilder.addParentStack(cls);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            RequestId.REQ_ID_AD,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);


            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NotifyId.NOTIFY_AD_ID, mBuilder.build());
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If Bluetooth is not enabled, let user enable it.
        if (!beaconManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    try {
                        beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
                    } catch (RemoteException e) {
                        Logger.e("Cannot start ranging", e);
                    }
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Should be invoked in #onStop.
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
        } catch (RemoteException e) {
            Logger.e("Cannot stop but it does not matter now", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.disconnect();
    }

    @Override
    public void onItemClick(AdapterView<MessageDetailAdapter> parent, View view, int position, long id) {
        BeaconMessage beaconMessage = parent.getAdapter().getItem(position);
        beaconMessage.getMsgUrl();
    }
}


