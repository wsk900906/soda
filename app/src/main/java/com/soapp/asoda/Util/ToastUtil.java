package com.soapp.asoda.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Swifty.Wang on 2015/6/30.
 */
public class ToastUtil {
    public static void show(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
