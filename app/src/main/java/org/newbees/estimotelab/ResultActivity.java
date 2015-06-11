package org.newbees.estimotelab;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;


public class ResultActivity extends Activity {

    private static final int DLG_LOADING = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        showDialog(DLG_LOADING);
        AVQuery<AVObject> query = new AVQuery<>("");
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DLG_LOADING:
                return new ProgressDialog.Builder(this)
                        .setMessage("loading...")
                        .create();
            default:
                return null;
        }

    }
}
