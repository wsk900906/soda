package com.soapp.asoda.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.handmark.pulltorefresh.library.PullToRefreshWebView;

/**
 * Created by Swifty.Wang on 2015/7/20.
 */
public class WebViewPager extends ViewPager {
    public WebViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof PullToRefreshWebView) {
            return ((PullToRefreshWebView) v).canScrollHor(-dx);
        } else {
            if ((x < this.getWidth() / 4) || (x > this.getWidth() / 4 * 3)) {
                return super.canScroll(v, checkV, dx, x, y);
            } else {
                return true;
            }
        }
    }
}