package ecjtu.net.demon.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ecjtu.net.demon.R;
import ecjtu.net.demon.utils.JavaScriptInterface;

/**
 * Created by shakugan on 15/10/26.
 */
public class rxCommentsActivity extends BaseActivity {

    private WebView webView;
    private String title;
    private String url;
    private ViewGroup webViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentViewLayout(R.layout.comments_activity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_activity);
        initActionBar();
        getSupportActionBar().setTitle("日新评论");

        webViewContainer = (ViewGroup) findViewById(R.id.webView);
        webView = new WebView(getApplicationContext());
        webViewContainer.addView(webView);

        final Intent intent = getIntent();
        url = intent.getStringExtra("url");
        Log.i("tag", url);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.i("tag", "webview是真的拿到了title");
                rxCommentsActivity.this.title = title;
                getSupportActionBar().setTitle(title);
                super.onReceivedTitle(view, title);
            }
        });
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setDomStorageEnabled(true);
        String cachepath = getFilesDir().getAbsolutePath()+"/ws/";
        ws.setAppCachePath(cachepath);
//        ws.setAppCacheMaxSize(8 * 1024 * 1024);
//        ws.setDatabasePath(cachepath);
        ws.setAppCacheEnabled(true);
        ws.setDatabaseEnabled(true);
//        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.addJavascriptInterface(new JavaScriptInterface(this), "interface");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                view.loadUrl("file:///android_asset/404/404.htm");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webViewContainer.removeAllViews();
        webView.destroy();
        webView = null;
    }
}
