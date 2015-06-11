package org.newbees.estimotelab;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avospush.notification.NotificationCompat;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private BeaconManager beaconManager = new BeaconManager(this);
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("rid", null, null, null);

    private static final int REQUEST_ENABLE_BT = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AVAnalytics.trackAppOpened(getIntent());

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                Logger.d("onBeaconsDiscovered: " + list.size());
                Logger.d("region: " + region.toString());
                if (list != null && list.size() > 0) {
                    List<String> proximityUUIDList = new ArrayList<String>();
                    for (Beacon b : list) {
                        proximityUUIDList.add(b.getProximityUUID());
                        Logger.d(b.toString());
                    }

                    AVQuery<AVObject> query = new AVQuery<AVObject>("BeaconMessage");
                    query.whereContainedIn("proximityUUID", proximityUUIDList);
//                    query.whereContainedIn("proximityUUID", ""proximityUUIDList);
                    query.findInBackground(new FindCallback<AVObject>() {
                        @Override
                        public void done(List<AVObject> list, AVException e) {
                            if (e != null) {
                                Logger.d(e.fillInStackTrace().getMessage());
                                return;
                            }
                            Logger.d("message list: " + list.size());
                            if (list.size() > 0) {
                                AVObject msg = list.get(0);
                                // TODO get detail ad message
                                String msgTitle = msg.getString("msgTitle");
                                String msgDetail = msg.getString("msgDetail");

                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(MainActivity.this)
                                                .setSmallIcon(R.mipmap.ic_launcher)
                                                .setContentTitle(msgTitle)
                                                .setContentText(msgDetail);

                                Intent resultIntent = new Intent(MainActivity.this, ResultActivity.class);
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this);
                                stackBuilder.addParentStack(ResultActivity.class);
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
                            }
                        }
                    });
                }
            }
        });
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
}


