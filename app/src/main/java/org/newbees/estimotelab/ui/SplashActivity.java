package org.newbees.estimotelab.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import org.newbees.estimotelab.R;


public class SplashActivity extends BaseActivity {

    Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putString("URL", "http://yucezhe.com/labs/route/demo.html");
                launchActivity(WebActivity.class, bundle);
                finish();
            }
        }, 300);
    }
}
