package org.newbees.estimotelab.ui;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by erichua on 6/13/15.
 */
public class BaseActivity extends Activity{

    protected void launchActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
