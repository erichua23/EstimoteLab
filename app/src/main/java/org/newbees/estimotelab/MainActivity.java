package org.newbees.estimotelab;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.orhanobut.logger.Logger;

import java.util.List;


public class MainActivity extends Activity {
    private BeaconManager beaconManager = new BeaconManager(this);
    private static final Region ALL_ESTIMOTE_BEACONS = new Region("rid", null, null, null);

    private static final int REQUEST_ENABLE_BT = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                Logger.d("onBeaconsDiscovered: " + list.size());
                Logger.d("region: " + region.toString());
                for (Beacon b: list) {
                    Logger.d(b.toString());
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


