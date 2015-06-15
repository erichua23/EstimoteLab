package org.newbees.estimotelab.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by erichua on 6/13/15.
 */
public class BaseActivity extends AppCompatActivity {
    protected static final int DLG_LOADING = 1;

    protected void launchActivity(Class<? extends Activity> activityClass, Bundle data) {
        Intent intent = new Intent(this, activityClass);
        if (null != data) {
            intent.putExtras(data);
        }
        startActivity(intent);
    }

    protected void launchActivity(Class<? extends Activity> activityClass) {
        launchActivity(activityClass, null);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DLG_LOADING:
                Dialog dlg = new ProgressDialog.Builder(this)
                        .setMessage("loading...")
                        .create();
                dlg.setCanceledOnTouchOutside(false);
                return dlg;
            default:
                return null;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
