package org.newbees.estimotelab.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by erichua on 2/7/15.
 */
public class Toaster {
    public static void shortShow(Context ctx, String msg) {
        Toast.makeText(ctx, "" + msg, Toast.LENGTH_SHORT).show();
    }

    public static void longShow(Context ctx, String msg) {
        Toast.makeText(ctx, "" + msg, Toast.LENGTH_LONG).show();
    }
}
