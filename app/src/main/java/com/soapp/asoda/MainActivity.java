package com.soapp.asoda;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.soapp.asoda.Util.AdsUtil;
import com.soapp.asoda.Util.CommentUtil;
import com.soapp.asoda.Util.DecodeXML;
import com.soapp.asoda.Util.ShareUtil;
import com.soapp.asoda.Util.SharedPreferencesFactory;
import com.soapp.asoda.Util.ToastUtil;
import com.soapp.asoda.dialog.MyDialogStyle;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import org.apache.http.util.EncodingUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    List<Market> markets;
    static List<Market> enableMarket = new ArrayList<Market>();
    static String query = null;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    //static ActionBar actionBar;
    private long exitTime = 0;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initial ads
        initAds();
        //initial analysis & auto_update
        initUmeng();
        markets = DecodeXML.decodeMarket(this);
        markets = addSearchEnable(markets);
        if (noMarketSelect()) {
            showMarketDialogCanCancel(false);
        }
        initView();
    }

    private void initUmeng() {
        AnalyticsConfig.enableEncrypt(true);
        //If you want show the updateInform even though without a wifi environment.
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
    }

    private void initAds() {
        //init youmi sdk
        AdsUtil.getValue(this);
    }

    private void showMarketDialogCanCancel(boolean b) {
        MyDialogStyle myDialogStyle = new MyDialogStyle(this);
        myDialogStyle.showCheckboxDialog(markets, b);
        myDialogStyle.setSelectMarketFinish(new SelectMarketFinish() {
            @Override
            public void SelectMarketFinishListener() {
                MobclickAgent.onEvent(MainActivity.this, getString(R.string.UMENG_MARKET), getMapString());
                initView();
            }
        });
    }

    private Map<String, String> getMapString() {
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < markets.size(); i++) {
            if (markets.get(i).isEnable()) {
                map.put(markets.get(i).getName(), "true");
            }
        }
        return map;
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

    }


    private boolean noMarketSelect() {
        for (int i = 0; i < markets.size(); i++) {
            if (markets.get(i).isEnable()) {
                return false;
            }
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MainActivity.query = query;
                initView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_setting) {
            showMarketDialogCanCancel(true);
        } else if (id == R.id.action_share) {
            ShareUtil.shareApp(this);
        } else if (id == R.id.action_comment) {
            CommentUtil.commentApp(this);
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            enableMarket = getUserMarket(markets);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return enableMarket.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return enableMarket.get(position).getName();
        }
    }

    private List<Market> getUserMarket(List<Market> markets) {
        List<Market> list = new ArrayList<Market>();
        for (Market market : markets) {
            if (market.isEnable()) {
                list.add(market);
            }
        }
        return list;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        private static int section;
        PullToRefreshWebView pullToRefreshWebView;
        ProgressBar progressBar;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("index", sectionNumber);
            fragment.setArguments(bundle);
            section = sectionNumber;
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            pullToRefreshWebView = (PullToRefreshWebView) rootView.findViewById(R.id.webview);
            final WebView webView = pullToRefreshWebView.getRefreshableView();
            pullToRefreshWebView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            pullToRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<WebView>() {
                @Override
                public void onPullDownToRefresh(PullToRefreshBase<WebView> refreshView) {
                    loadSearch(webView, getArguments().getInt("index"), query);
                    ToastUtil.show(getActivity(), getString(R.string.refreshing));
                    pullToRefreshWebView.onRefreshComplete();
                }

                @Override
                public void onPullUpToRefresh(PullToRefreshBase<WebView> refreshView) {

                }
            });
            progressBar = (ProgressBar) rootView.findViewById(R.id.pb);
            webSetting(webView);
            webView.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                    Uri uri = Uri.parse(s);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress != 100) {
                        progressBar.setProgress(newProgress);
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    progressBar.setVisibility(View.VISIBLE);
                    loadUrl(getActivity(), webView, url);
                    return true;
                }
            });
            loadSearch(webView, getArguments().getInt("index"), query);
            return rootView;
        }

        private void webSetting(WebView webView) {
            webView.clearCache(false);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setDatabaseEnabled(true);
        }

        private void loadSearch(WebView webView, int index, String query) {
            if (query != null) {
                MobclickAgent.onEvent(getActivity(), getString(R.string.UMENG_SEARCH), query);
                if (enableMarket.get(index).getMethod().toLowerCase().equals("get")) {
                    Log.e("search", enableMarket.get(index).getUrl().replace("%s", query));
                    loadUrl(getActivity(), webView, enableMarket.get(index).getUrl().replace("%s", query));
                } else {
                    String postData = enableMarket.get(index).getMethod() + "=" + query;
                    postUrl(getActivity(), webView, enableMarket.get(index).getUrl(), EncodingUtils.getBytes(postData, "BASE64"));
                }
            } else {
                ToastUtil.show(getActivity(), getString(R.string.pleaseinputSearch));
            }


        }

        private void postUrl(Context context, WebView webView, String url, byte[] base64s) {
            if (hasNetWork(context)) {
                webView.postUrl(url, base64s);
            } else {
                ToastUtil.show(context, getString(R.string.pleaseconnectNetwork));
            }
        }

        private void loadUrl(Context context, WebView webView, String url) {
            if (hasNetWork(context)) {
                webView.loadUrl(url);
            } else {
                ToastUtil.show(context, getString(R.string.pleaseconnectNetwork));
            }
        }

        private boolean hasNetWork(Context context) {
            ConnectivityManager cm =
                    (ConnectivityManager) context
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
        }

    }

    private List<Market> addSearchEnable(List<Market> markets) {
        for (int i = 0; i < markets.size(); i++) {
            markets.get(i).setEnable(SharedPreferencesFactory.getSharedEnable(this, markets.get(i).getName()));
        }
        return markets;
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.show(this, getString(R.string.pleasepressexit));
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        query = null;
        super.onDestroy();
    }
}
