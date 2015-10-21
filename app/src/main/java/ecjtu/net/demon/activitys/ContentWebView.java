package ecjtu.net.demon.activitys;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.net.Uri;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import ecjtu.net.demon.R;
import ecjtu.net.demon.utils.ToastMsg;

/**
 * Created by homker on 2015/4/26.
 * 日新网新闻客户端
 */
public class ContentWebView extends BaseActivity {

    public String title;
    private WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_nocomment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setTitle("日小新服务");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = (WebView) findViewById(R.id.webView);
        final Intent intent = getIntent();
        url = intent.getStringExtra("url");
        Log.i("tag", url);
        if(intent.getStringExtra("url").equals("http://app.ecjtu.net/")) {
            ToastMsg.builder.display("如果无法下载请点击右上角前往fir.im下载更新", 1000);
        }
        webView.loadUrl(url);
        webView.setDownloadListener(new rxWebViewDownloadListener());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                Log.i("tag", "webview是真的拿到了title");
                ContentWebView.this.title = title;
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
        webView.addJavascriptInterface(new JavaScriptInterface(), "interface");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (getIntent().getStringExtra("url").equals("http://app.ecjtu.net/")) {
            getMenuInflater().inflate(R.menu.menu_updata, menu);
        } else  getMenuInflater().inflate(R.menu.menu_webview, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

//        if (id == R.id.action_refresh) {
//            webView.reload();
//            ToastMsg.builder.display("正在刷新",300);
//            return true;
//        }
        if (id == R.id.go_fir) {
            webView.loadUrl("http://fir.im/rixin");
        }

        if (id == R.id.share){
            share(url, title);
        }
        if (id == R.id.homeAsUp) {
            finish();
            return true;
        }
        if (id == android.R.id.home) {
            Intent upIntent = NavUtils.getParentActivityIntent(ContentWebView.this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(upIntent)
                        .startActivities();
            } else {
                Log.i("tag", "nihao" + String.valueOf(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, upIntent);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void share(String url, String title){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "分享到"));
    }

    /**
     * Created by shakugan on 15/9/26.
     */
    public class rxWebViewDownloadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }


    /**
     * web页面调用借口类
     */

    final class JavaScriptInterface {

        JavaScriptInterface() {
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        public String getStudentId() {
            Log.i("TAG","js接口调用～～");
            return NewMain.userEntity.getStudentID();
        }

    }

    /**
     * Provides a hook for calling "alert" from javascript. Useful for
     * debugging your javascript.
     */
    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            result.confirm();
            return true;
        }
    }

}
