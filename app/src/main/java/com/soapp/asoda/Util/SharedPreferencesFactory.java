package com.soapp.asoda.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Swifty.Wang on 2015/6/26.
 */
public class SharedPreferencesFactory {
    private static final String Name = "Cache";

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static void removeString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
    public static String grabString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);
    }
    public static boolean getSharedEnable(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }
    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
