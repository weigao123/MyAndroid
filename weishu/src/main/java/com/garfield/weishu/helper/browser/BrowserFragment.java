package com.garfield.weishu.helper.browser;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.garfield.baselib.utils.system.L;
import com.garfield.weishu.R;
import com.garfield.weishu.ui.fragment.AppBaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gaowei3 on 2016/11/24.
 */

public class BrowserFragment extends AppBaseFragment {

    @BindView(R.id.fragment_browser_webview)
    WebView mWebView;

    @BindView(R.id.fragment_browser_url)
    EditText mEditText;

    private String mUrl;

    @Override
    protected int onGetFragmentLayout() {
        return R.layout.fragment_browser;
    }

    @Override
    protected void onInitViewAndData(View rootView, Bundle savedInstanceState) {
        super.onInitViewAndData(rootView, savedInstanceState);
        setEnterAnimatorEnable(false);
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);

//        mWebView.clearCache(true);
//        mWebView.destroy();
        if (savedInstanceState != null) {
            mUrl = savedInstanceState.getString("url");
            mWebView.loadUrl(mUrl);
        }
        //mWebView.reload();
    }

    @OnClick(R.id.fragment_browser_confirm)
    public void startLoad() {
        mUrl = mEditText.getText().toString();
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = "http://www.baidu.com";
        }
        mWebView.loadUrl(mUrl);
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            L.d("web progress: " + newProgress);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
