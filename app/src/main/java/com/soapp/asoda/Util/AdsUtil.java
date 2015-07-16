package com.soapp.asoda.Util;

import android.content.Context;
import android.util.Log;

import net.slidingmenu.tools.AdManager;
import net.slidingmenu.tools.onlineconfig.OnlineConfigCallBack;
import net.slidingmenu.tools.st.SpotManager;

/**
 * Created by macpro001 on 8/6/15.
 */
public class AdsUtil {

    private static final String YOUMIID = "a322aba33caf3f0b";
    private static final java.lang.String YOUMISECRET = "bfe3006e5d1770d5";
    private static final String showAds_key = "SHOWADS";
    private static boolean showAds = true;
    private static double rate = 0.6;

    public static void getValue(final Context context) {
        initSDK(context);
        AdManager.getInstance(context).asyncGetOnlineConfig(showAds_key, new OnlineConfigCallBack() {
            @Override
            public void onGetOnlineConfigSuccessful(String key, String value) {
                // TODO Auto-generated method stub
                //获取在线参数成功
                Log.e("ads", key + value);
                if (value.equals("1")) {
                    showAds = true;
                    showSpot(context);
                } else {
                    showAds = false;
                }
            }

            @Override
            public void onGetOnlineConfigFailed(String key) {
                // TODO Auto-generated method stub
                // 获取在线参数失败，可能原因有：键值未设置或为空、网络异常、服务器异常
                //关闭广告
                Log.e("ads", key);
                showAds = false;
            }
        });
    }

    public static void initSDK(Context context) {
        AdManager.getInstance(context).init(YOUMIID, YOUMISECRET, false);
        SpotManager.getInstance(context).loadSpotAds();
        SpotManager.getInstance(context).setSpotOrientation(SpotManager.ORIENTATION_PORTRAIT);
        SpotManager.getInstance(context).setAnimationType(SpotManager.ANIM_NONE);
    }

    public static void showSpot(Context context) {
        if (showAds && Math.random() < rate) {
            SpotManager.getInstance(context).showSpotAds(context);
        }
    }


}
