package org.newbees.estimotelab.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import org.newbees.estimotelab.R;


public class ResultActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        showDialog(DLG_LOADING);
    }

}
