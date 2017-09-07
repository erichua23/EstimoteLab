package org.newbees.estimotelab.ui;

import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.newbees.estimotelab.R;
import org.newbees.estimotelab.utils.PC;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class WebActivity extends BaseActivity {
    @InjectView(R.id.mainWv)
    WebView mainWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        ButterKnife.inject(this);
        PC.checkExtra(this);
        String url = getIntent().getExtras().getString("URL");

        setTitle("");
        if (getActionBar() != null) {
//            getActionBar().setHomeButtonEnabled(true);
//            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().hide();
        }

        mainWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setTitle(mainWv.getTitle());
            }
        });
        mainWv.getSettings().setGeolocationEnabled(true);
        mainWv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mainWv.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        mainWv.loadUrl(url);
    }
}
