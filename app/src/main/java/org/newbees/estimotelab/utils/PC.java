package org.newbees.estimotelab.utils;

import android.app.Activity;

/**
 * Created by erichua on 6/14/15.
 */
public class PC {
    /**
     * 检查 intent和bundle是否为空
     * @param activity
     */
    public static void checkExtra(Activity activity) {
        if (activity == null
                || activity.getIntent() == null
                || activity.getIntent().getExtras() == null) {
            throw new IllegalArgumentException("empty intent");
        }
    }
}
