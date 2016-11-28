package com.garfield.weishu.discovery.browser;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.garfield.baselib.utils.system.InputUtils;
import com.garfield.baselib.utils.system.L;
import com.garfield.baselib.utils.system.SystemUtil;
import com.garfield.weishu.R;
import com.garfield.weishu.contact.FriendProfileFragment;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import javax.xml.transform.sax.SAXTransformerFactory;

import butterknife.BindView;
import butterknife.OnClick;

import static com.garfield.weishu.app.AppCache.USER_ACCOUNT;

/**
 * Created by gaowei3 on 2016/11/24.
 */

public class BrowserFragment extends AppBaseFragment {

    public static final String BROWSER_CONTENT = "content";
    public static final String BROWSER_TYPE = "type";

    public static final int TYPE_NULL = 0;
    public static final int TYPE_URL = 1;
    public static final int TYPE_DATA = 2;

    @BindView(R.id.fragment_browser_input)
    View mInputContainer;

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

    private String mUrl;

    {
        setAnimationEnable(false);
    }

    public static BrowserFragment newInstance(String content, int type) {
        Bundle args = new Bundle();
        args.putString(BROWSER_CONTENT, content);
        args.putInt(BROWSER_TYPE, type);
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

        if (savedInstanceState != null) {
            mUrl = savedInstanceState.getString("url");
        }

        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);

        mEditText.setOnFocusChangeListener(mOnFocusChangeListener);
        startLoadBundle();


    }

    @OnClick(R.id.fragment_browser_retry)
    void startLoadBundle() {
        WebSettings webSettings = mWebView.getSettings();
        String content = getArguments().getString(BROWSER_CONTENT);
        int type = getArguments().getInt(BROWSER_TYPE);
        if (type == TYPE_NULL) {
            webSettings.setJavaScriptEnabled(true);
            mInputContainer.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(mUrl)) {
                mWebView.loadUrl(mUrl);
            }
        } else if (type == TYPE_URL) {
            webSettings.setJavaScriptEnabled(true);
            if (content != null) {
                if (content.startsWith("www.")) {
                    content = "http://" + content;
                }
                if (content.startsWith("http://news.163.com")) {
                    SystemUtil.setStatusColor(mActivity, getResources().getColor(R.color.red));
                }
            }
            mWebView.loadUrl(content);
        } else if (type == TYPE_DATA) {
            webSettings.setTextZoom(120);
            mWebView.loadData(content, "text/html; charset=UTF-8", null);
        }
        mRetry.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.fragment_browser_confirm)
    public void startLoad() {
        mUrl = mEditText.getText().toString();
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = "http://www.baidu.com";
        } else {
            if (mUrl.startsWith("www.")) {
                mUrl = "http://" + mUrl;
            }
        }
        mWebView.loadUrl(mUrl);
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
            mUrl = url;
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
            return true;
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
        SystemUtil.setStatusColor(mActivity, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
