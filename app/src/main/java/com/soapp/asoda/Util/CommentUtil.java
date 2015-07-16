package com.soapp.asoda.Util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.soapp.asoda.R;

/**
 * Created by Swifty.Wang on 2015/7/3.
 */
public class CommentUtil {
    public static void commentApp(Context context) {
        try {
            String mAddress = "market://details?id=" + context.getPackageName();
            Intent marketIntent = new Intent("android.intent.action.VIEW");
            marketIntent.setData(Uri.parse(mAddress));
            context.startActivity(marketIntent);
        }catch (Exception e){
            ToastUtil.show(context,context.getString(R.string.commentFailed));
        }
    }
}
