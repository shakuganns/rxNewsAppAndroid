package ecjtu.net.demon.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import ecjtu.net.demon.activitys.ContentWebView;
import ecjtu.net.demon.activitys.NewMain;

/**
 * Created by shakugan on 15/10/30.
 * web页面调用接口类
 */
public class JavaScriptInterface {

    private Context context;

    public JavaScriptInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void go(String url) {
        Intent intent = new Intent(context,ContentWebView.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

    /**
     * This is not called on the UI thread. Post a runnable to invoke
     * loadUrl on the UI thread.
     */
    @JavascriptInterface
    public String getStudentId() {
        Log.i("TAG", "js接口调用～～");
        return NewMain.userEntity.getStudentID();
    }

}
