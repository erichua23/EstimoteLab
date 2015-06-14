package org.newbees.estimotelab.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import org.newbees.estimotelab.MyApplication;
import org.newbees.estimotelab.R;
import org.newbees.estimotelab.ui.BaseActivity;
import org.newbees.estimotelab.ui.MainActivity;
import org.newbees.estimotelab.ui.SignInActivity;


public class SplashActivity extends BaseActivity {

    Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MyApplication.getInstance().getCurrentUser() == null) {
                    launchActivity(SignInActivity.class);
                    finish();
                    return;
                } else {
                    launchActivity(MainActivity.class);
                    finish();
                    return;
                }
            }
        }, 300);
    }
}
