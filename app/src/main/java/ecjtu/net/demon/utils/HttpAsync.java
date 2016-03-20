package ecjtu.net.demon.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by homker on 2015/4/7.
 */
public class HttpAsync {
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static OkHttpClient okHttpClient = new OkHttpClient();
    static { client.setTimeout(5000); }
    public static void get(String url,AsyncHttpResponseHandler res){
        client.get(url,res);
    }
    public static void get(String url,RequestParams params,AsyncHttpResponseHandler res){
        client.get(url,params,res);
    }
    public static void get(String url,JsonHttpResponseHandler res){
        client.get(url,res);
    }
    public static void get(String url, RequestParams params ,JsonHttpResponseHandler res){
        client.get(url,params,res);
    }
    public static void get(String url,BinaryHttpResponseHandler handler){
        client.get(url,handler);
    }
    public static void get(String url,FileAsyncHttpResponseHandler fileHandler){
        client.get(url, fileHandler);
    }
    public static void post(String url, RequestParams params, JsonHttpResponseHandler res){
        client.post(url,params,res);
    }
    public static AsyncHttpClient getClient(){
        return client;
    }

    public static void get(String url,Callback callback) {
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }
}
