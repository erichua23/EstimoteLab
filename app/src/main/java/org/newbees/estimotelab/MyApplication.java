package org.newbees.estimotelab;

import android.app.Application;

import com.estimote.sdk.EstimoteSDK;

/**
 * Created by erichua on 6/11/15.
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        EstimoteSDK.initialize(this, getString(R.string.app_id), getString(R.string.app_token));
        EstimoteSDK.enableDebugLogging(true);
    }
}
