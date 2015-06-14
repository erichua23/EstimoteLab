package org.newbees.estimotelab.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.newbees.estimotelab.Const;
import org.newbees.estimotelab.R;
import org.newbees.estimotelab.model.BeaconMessage;
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
        BeaconMessage msg = getIntent().getExtras().getParcelable(Const.EXTRA_KEY_MSG);

        setTitle("");
        if (getActionBar() != null) {
            getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mainWv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setTitle(mainWv.getTitle());
            }
        });
        mainWv.loadUrl(msg.getMsgUrl());
    }
}
