package org.newbees.estimotelab.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import org.newbees.estimotelab.Const;
import org.newbees.estimotelab.MyApplication;
import org.newbees.estimotelab.NotifyId;
import org.newbees.estimotelab.R;
import org.newbees.estimotelab.RequestId;
import org.newbees.estimotelab.model.BeaconMessage;
import org.newbees.estimotelab.model.CheckIn;
import org.newbees.estimotelab.ui.adapter.MessageDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private BeaconManager beaconManager = new BeaconManager(this);
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("rid", null, null, null);
    private static final int REQUEST_ENABLE_BT = 1234;
    private long lastDiscovered = 0;
    List<String> notifiedMessage = new ArrayList<>();
    int notifyId = 0;

    @InjectView(R.id.mainLv)
    ListView mainLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        ButterKnife.inject(this);

        if (MyApplication.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        AVQuery<BeaconMessage> query = AVObject.getQuery(BeaconMessage.class);
        query.orderByAscending("createAt");
        query.findInBackground(new FindCallback<BeaconMessage>() {
            @Override
            public void done(List<BeaconMessage> list, AVException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }

                MessageDetailAdapter listAdapter = new MessageDetailAdapter(list);
                mainLv.setAdapter(listAdapter);
                Logger.d("list.size(): " + list.size());
                mainLv.setOnItemClickListener(MainActivity.this);
            }
        });

        AVAnalytics.trackAppOpened(getIntent());

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beaconList) {
                if (lastDiscovered != 0 && (System.currentTimeMillis() - lastDiscovered < 5000)) {
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

                    AVQuery<BeaconMessage> query = AVObject.getQuery(BeaconMessage.class);
                    query.whereContains("major", "" + bestBeacon.getMajor());
                    Logger.d("" + bestBeacon.getMajor());
                    query.findInBackground(new FindCallback<BeaconMessage>() {
                        @Override
                        public void done(List<BeaconMessage> list, AVException e) {
                            if (e != null) {
                                Logger.d(e.fillInStackTrace().getMessage());
                                return;
                            }
                            for (AVObject o : list) {
                                Logger.d("" + o.toString());
                            }
                            Logger.d("message list: " + list.size());
                            for (BeaconMessage msg: list) {
                                postNotification(msg);
                            }
                        }
                    });
                }
            }
        });
    }

    private void postNotification(BeaconMessage message) {
        if (notifiedMessage.contains(message.getObjectId())) {
            return;
        }
        notifiedMessage.add(message.getObjectId());
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(message.getMsgTitle())
                        .setContentText(message.getMsgDetail());

        Intent resultIntent = new Intent();
        if ("checkIn".equals(message.getMsgType())) {
            resultIntent.setClass(this, CheckInActivity.class);
        } else {
            resultIntent.setClass(this, WebActivity.class);
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this);
        stackBuilder.addParentStack(CheckInActivity.class);
        resultIntent.putExtra(Const.EXTRA_KEY_MSG, message);
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
        mNotificationManager.notify(notifyId++, mBuilder.build());
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BeaconMessage msg = ((MessageDetailAdapter) parent.getAdapter()).getItem(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Const.EXTRA_KEY_MSG, msg);
        if ("checkIn".equals(msg.getMsgType())) {
            launchActivity(CheckInActivity.class, bundle);
        } else {
            launchActivity(WebActivity.class, bundle);
        }
    }
}


