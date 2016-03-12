package ecjtu.net.demon.activitys;

import android.annotation.TargetApi;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ecjtu.net.demon.R;
import ecjtu.net.demon.adapter.tushuShowCardAdapter;
import ecjtu.net.demon.utils.HttpAsync;
import ecjtu.net.demon.utils.ToastMsg;

public class Tusho_show_card_activity extends BaseActivity {

    private static final String url = "http://pic.ecjtu.net/api.php/post";
    private static final int duration = 100;
    private Bitmap headImage;
    private RecyclerView recyclerView;
    private tushuShowCardAdapter adapeter;
    private LinearLayoutManager linearLayoutManager;
    private static String pid;
    public static ArrayList<String> urlList = new ArrayList<>();
    public static ArrayList<String> infoList = new ArrayList<>();

    public static void setPid(String pid) {
        Tusho_show_card_activity.pid = pid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentViewLayout(R.layout.activity_tusho_show_card_activity);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tusho_show_card_activity);
        loadData(url);
        initActionBar();
        getSupportActionBar().setTitle("图说");

        recyclerView = (RecyclerView) findViewById(R.id.profile_show_card_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapeter =  new tushuShowCardAdapter(Tusho_show_card_activity.this);
        recyclerView.setAdapter(adapeter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (urlList != null) {
            urlList.clear();
            pid = null;
            infoList.clear();
        }
    }

    private void loadData(String url) {
        urlList.clear();
            url = url + "/" + pid;
            HttpAsync.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        adapeter.headInfo.add(0, response.get("title").toString());
                        adapeter.headInfo.add(1, response.get("author").toString());
                        adapeter.headInfo.add(2, response.get("count").toString());
                        adapeter.headInfo.add(3, response.get("click").toString());
                        adapeter.setHeadText();
                        JSONArray jsonArray = response.getJSONArray("pictures");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ArrayMap<String, Object> item = new ArrayMap<>();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String url = "http://pic.ecjtu.net/" + jsonObject.getString("url");
                            urlList.add(i, url);
                            item.put("url", url);
                            String info = jsonObject.getString("detail");
                            item.put("detail", info);
                            infoList.add(i, info);
                            adapeter.getContent().add(item);
                        }
                        adapeter.notifyDataChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    ToastMsg.builder.display("加载失败，请稍后重试。", duration);
                }

                @Override
                public void onFinish() {
                    super.onFinish();

                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tusho_show_card_activity, menu);
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

        if (id == android.R.id.home) {
            Intent upIntent = NavUtils.getParentActivityIntent(this);
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

    public void setHeadImage(Bitmap bitmap) {
        headImage = bitmap;
    }
}
