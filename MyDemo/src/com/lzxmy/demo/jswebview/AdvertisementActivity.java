package com.lzxmy.demo.jswebview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.lzxmy.demo.R;


/**
 * 广告webView 2015-2-14 @author lzx
 * JS 交互
 */
public class AdvertisementActivity extends Activity {
    WebView webview;
    String url = "";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertisemen_layout);
        if (getIntent().getExtras() != null) {
            url = getIntent().getExtras().getString("url");
        }
        webview = (WebView) findViewById(R.id.webView);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setJavaScriptEnabled(true);
        // if (DownloadUtil.getInstance().isNetworkConnected(
        // AdvertisementActivity.this)) {
        // ShowProgressBar.showDiolog(AdvertisementActivity.this, "连接中");
        // }
        // webview.setWebChromeClient(new WebChromeClient() {
        // @Override
        // public void onProgressChanged(WebView view, int newProgress) {
        // // TODO Auto-generated method stub
        // if (newProgress >= 80) {
        // ShowProgressBar.removeDiolog();
        // }
        // super.onProgressChanged(view, newProgress);
        // }
        // });
        // webview.setWebViewClient(null);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
//                ShowProgressBar.removeDiolog();
//                ShowToast.showTips(AdvertisementActivity.this, description);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        webview.addJavascriptInterface(new JSInterface(), "xswstudent");

        if (url == null && url.equals("")) {
            webview.loadUrl("http://www.51xuanshi.com");
        } else {
            webview.loadUrl(url);
        }
//        settitle("选师无忧");
//        setLeft("");
    }


    public final class JSInterface {
        // JavaScript脚本代码可以调用的函数onClick()处理
        @JavascriptInterface
        public void clickOnAndroid(final String teacherid) {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AdvertisementActivity.this, "clickOnAndroid", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent();
//                    intent.putExtra("teacherid", teacherid);
//                    intent.setClass(AdvertisementActivity.this, HomeInfoActivity.class);
//                    startActivity(intent);
                }
            });
        }

        @JavascriptInterface
        public void clickOnTeacher() {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(AdvertisementActivity.this, "clickOnTeacher", Toast.LENGTH_LONG).show();
                    webview.loadUrl("javascript:wave(ddd)");
//                    Intent intent = new Intent();
//                    intent.setClass(AdvertisementActivity.this, SubjectActivity.class);
//                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//    @Override
//    boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//            webView.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
