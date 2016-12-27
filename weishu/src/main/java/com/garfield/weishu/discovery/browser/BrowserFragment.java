package com.garfield.weishu.discovery.browser;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.garfield.baselib.utils.system.InputUtils;
import com.garfield.baselib.utils.system.L;
import com.garfield.baselib.utils.system.SystemUtil;
import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

/**
 * Created by gaowei3 on 2016/11/24.
 */

public class BrowserFragment extends AppBaseFragment {

    public static final String BROWSER_CONTENT = "content";
    public static final String BROWSER_TYPE = "type";
    public static final String BROWSER_CONTENT_SIZE = "size";

    public static final int TYPE_BROWSER = 0;
    public static final int TYPE_URL = 1;
    public static final int TYPE_STRING = 2;

    @BindView(R.id.fragment_browser_webview_container)
    FrameLayout mWebViewContainer;

    @BindView(R.id.fragment_browser_input)
    View mInputContainer;

    @BindView(R.id.fragment_browser_url_set)
    LinearLayout mUrlSet;

    @BindView(R.id.fragment_browser_url)
    EditText mEditText;

    @BindView(R.id.fragment_browser_webview)
    WebView mWebView;

    @BindView(R.id.fragment_browser_retry)
    View mRetry;

    @BindView(R.id.fragment_browser_progress)
    ProgressBar mProgressBar;

    @BindView(R.id.fragment_browser_mask)
    View mMask;

    /**
     * url会改变，type不变
     */
    private String mUrl;
    private int mType;
    private int mSize;
    private WebSettings webSettings;

    public static BrowserFragment newInstance(String content, int type) {
        Bundle args = new Bundle();
        args.putString(BROWSER_CONTENT, content);
        args.putInt(BROWSER_TYPE, type);
        BrowserFragment fragment = new BrowserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static BrowserFragment newInstance(String content, int type, int size) {
        Bundle args = new Bundle();
        args.putString(BROWSER_CONTENT, content);
        args.putInt(BROWSER_TYPE, type);
        args.putInt(BROWSER_CONTENT_SIZE, size);
        BrowserFragment fragment = new BrowserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_browser;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);

        if (savedInstanceState == null) {
            mUrl = getArguments().getString(BROWSER_CONTENT);
        } else {
            mUrl = savedInstanceState.getString("url");
        }
        mType = getArguments().getInt(BROWSER_TYPE);
        mSize = getArguments().getInt(BROWSER_CONTENT_SIZE);

        initWebView();
        mEditText.setOnFocusChangeListener(mOnFocusChangeListener);
        startLoadBundle();
    }

    @OnClick(R.id.fragment_browser_retry)
    void startLoadBundle() {
        if (mType == TYPE_BROWSER) {
            mInputContainer.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(mUrl)) {
                mUrlSet.setVisibility(View.VISIBLE);
            }
        } else if (mType == TYPE_URL) {
            if (mUrl != null) {
                int index = mUrl.indexOf(".com");
                if (index >= 3 && mUrl.substring(index - 3, index).equals("163")) {
                    SystemUtil.setStatusColor(mActivity, getResources().getColor(R.color.red));
                }
            }
        }
        checkAndLoadUrl(mUrl);
        mRetry.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.fragment_browser_confirm)
    public void openEditUrl() {
        mUrl = mEditText.getText().toString();
        checkAndLoadUrl(mUrl);
    }

    private void checkAndLoadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (url.startsWith("www.")) {
            url = "http://" + url;
        }

        if (mType == TYPE_STRING) {
            mWebView.loadData(url, "text/html; charset=UTF-8", null);
        } else {
            mWebView.loadUrl(url);
        }
    }

    @SuppressLint("setJavaScriptEnabled")
    private void initWebView() {
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);
        webSettings = mWebView.getSettings();
        webSettings.setCacheMode(LOAD_NO_CACHE);
        if (mType != TYPE_STRING) {
            webSettings.setJavaScriptEnabled(true);
        }
        if (mSize != 0) {
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setSupportZoom(true);
            webSettings.setTextZoom(mSize);
        }
    }

    @OnClick(R.id.fragment_browser_mask)
    void clickMask() {
        mEditText.clearFocus();
    }

    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                InputUtils.hideInputMethod(mRootView);
                mMask.setVisibility(View.GONE);
            } else {
                mMask.setVisibility(View.VISIBLE);
            }
        }
    };

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            L.d("onPageStarted: " +url);
            mProgressBar.setVisibility(View.VISIBLE);
            mEditText.setText(url);
            mEditText.clearFocus();
            if (mType != TYPE_STRING) {
                mUrl = url;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            L.d("onPageFinished");
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            L.d("onReceivedError: " + description);
            mRetry.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.INVISIBLE);
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressBar.setProgress(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            L.d("web title: " + title);
        }
    };

    @Override
    protected boolean onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            mRetry.setVisibility(View.GONE);
            return true;
        } else if (mType == TYPE_BROWSER) {
            if (mUrlSet.getVisibility() == View.GONE) {
                mWebView.removeAllViews();
                mWebView.destroy();
                WebView webView = new WebView(getContext());
                mWebViewContainer.removeView(mWebView);
                mWebViewContainer.addView(webView, 0);
                mWebView = webView;
                initWebView();
                mUrlSet.setVisibility(View.VISIBLE);
                mRetry.setVisibility(View.GONE);
                mEditText.setText("");
                return true;
            }
        }
        return super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("url", mUrl);
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWebView.stopLoading();
        if (mType == TYPE_URL) {
            SystemUtil.setStatusColor(mActivity, getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
    }

    @OnClick(R.id.include_url_set_netease)
    void startNetease() {
        mUrlSet.setVisibility(View.GONE);
        checkAndLoadUrl("http://news.163.com/");
    }

    @OnClick(R.id.include_url_set_baidu)
    void startBaidu() {
        mUrlSet.setVisibility(View.GONE);
        checkAndLoadUrl("http://www.baidu.com/");
    }

    @OnClick(R.id.include_url_set_tecent)
    void startTecent() {
        mUrlSet.setVisibility(View.GONE);
        checkAndLoadUrl("http://news.qq.com/");
    }
}
