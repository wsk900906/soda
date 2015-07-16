package com.soapp.asoda.Util;

import android.content.Context;
import android.content.Intent;

import com.soapp.asoda.R;

/**
 * Created by Swifty.Wang on 2015/7/3.
 */
public class ShareUtil {

    public static void shareApp(Context context){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = context.getString(R.string.sharetxt);
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context.getString(R.string.app_title));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.shareto)));
    }
}
