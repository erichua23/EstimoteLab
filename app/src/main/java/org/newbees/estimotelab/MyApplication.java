package org.newbees.estimotelab;

import android.app.Application;

import org.newbees.estimotelab.model.UserInfo;

/**
 * Created by erichua on 6/11/15.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    private UserInfo currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
//
//        MyApplication.instance = this;
//
//        AVObject.registerSubclass(BeaconMessage.class);
//        AVObject.registerSubclass(CheckIn.class);
//
//        EstimoteSDK.initialize(this, getString(R.string.app_id), getString(R.string.app_token));
//        EstimoteSDK.enableDebugLogging(true);
//
//        AVOSCloud.initialize(this, "4gspkm52usou3w9607tkets7v3sof8cydvfl7g2xptc0z8ji", "erk8f5gl3r4s3i83g3c9vu2ip4h6w9jsd9w93es9eq3mw6ru");
//        AVOSCloud.setDebugLogEnabled(true);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public UserInfo getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserInfo currentUser) {
        this.currentUser = currentUser;
    }

}
