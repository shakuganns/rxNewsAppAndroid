package ecjtu.net.demon.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebChromeClient;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_activity);
        initActionBar();

        webView = (WebView) findViewById(R.id.webView);
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
        ws.setAppCacheMaxSize(8 * 1024 * 1024);
        ws.setDatabasePath(cachepath);
        ws.setAppCacheEnabled(true);
        ws.setDatabaseEnabled(true);
        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.addJavascriptInterface(new JavaScriptInterface(this), "interface");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
//                if(errorCode == 404){
                view.loadUrl("file:///android_asset/404/404.htm");
//                }
            }
        });
    }

    private void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setTitle("日小新服务");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
