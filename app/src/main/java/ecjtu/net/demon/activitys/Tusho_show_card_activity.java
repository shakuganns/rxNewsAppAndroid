package ecjtu.net.demon.activitys;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ecjtu.net.demon.R;
import ecjtu.net.demon.adapter.tushuShowCardAdapter;
import ecjtu.net.demon.utils.OkHttp;
import ecjtu.net.demon.utils.ToastMsg;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Tusho_show_card_activity extends BaseActivity {

    private static final String url = "http://pic.ecjtu.net/api.php/post";
    private static final int duration = 100;
    private boolean headIsSet = false;
    private Bitmap bitmap;                                              //高斯模糊处理中使用
    private RecyclerView recyclerView;
    private tushuShowCardAdapter adapeter;
    private LinearLayoutManager linearLayoutManager;
    private CollapsingToolbarLayout layout;
    private static String pid;
    public static ArrayList<String> urlList = new ArrayList<>();
    public static ArrayList<String> infoList = new ArrayList<>();

    private TextView author;
    private TextView click;
    private TextView count;
    private TextView title;
    private String authorS;
    private String clickS;
    private String countS;
    private String titleS;
    private ImageView toolbarImage;

    private RxHandler handler;

    public static void setPid(String pid) {
        Tusho_show_card_activity.pid = pid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tusho_show_card_activity);
        handler = new RxHandler(this);
        loadData(url);
        initActionBarTushuo();
        getSupportActionBar().setTitle("图说");

        toolbarImage = (ImageView) findViewById(R.id.toolbar_image);
        layout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        author = (TextView) findViewById(R.id.author);
        click = (TextView) findViewById(R.id.click);
        count = (TextView) findViewById(R.id.count);
        title = (TextView) findViewById(R.id.title);

        recyclerView = (RecyclerView) findViewById(R.id.profile_show_card_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapeter =  new tushuShowCardAdapter(Tusho_show_card_activity.this);
        recyclerView.setAdapter(adapeter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setBackgroundColor(Color.parseColor("#00000000"));
        layout.setStatusBarScrimColor(Color.parseColor("#00000000"));
        if ((Build.VERSION.SDK_INT > 16)&&!headIsSet) {
            BlurTask blurTask = new BlurTask(tushuShowCardAdapter.headImage);
            blurTask.execute();
            headIsSet = true;
//            ((HeadViewHolder) holder).bg.setBackgroundColor(Color.GRAY);
        } else if (!headIsSet) {
            BitmapDrawable bd = (BitmapDrawable) tushuShowCardAdapter.headImage;
            bitmap = bd.getBitmap();
            layout.setContentScrim(new BitmapDrawable(bitmap));
            toolbarImage.setImageDrawable(new BitmapDrawable(bitmap));
            headIsSet = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (urlList != null) {
            urlList.clear();
            pid = null;
            infoList.clear();
            tushuShowCardAdapter.headImage = null;
            bitmap.recycle();
            bitmap = null;
        }
    }

    private void loadData(String url) {
        urlList.clear();
        url = url + "/" + pid;
        OkHttp.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastMsg.builder.display("加载失败，请稍后重试。", duration);
            }

            @Override
            public void onResponse(Call call, Response res) throws IOException {
                try {
                    JSONObject response = new JSONObject(res.body().string());
//                    title.setText(response.get("title").toString());
//                    author.setText(response.get("author").toString());
//                    count.setText(response.get("count").toString());
//                    click.setText(response.get("click").toString());
                    titleS = response.get("title").toString();
                    authorS = response.get("author").toString();
                    countS = response.get("count").toString();
                    clickS = response.get("click").toString();
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
                    handler.sendEmptyMessage(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

//        if (id == android.R.id.home) {
//            Intent upIntent = NavUtils.getParentActivityIntent(this);
//            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
//                TaskStackBuilder.create(this)
//                        .addNextIntentWithParentStack(upIntent)
//                        .startActivities();
//            } else {
//                Log.i("tag", "nihao" + String.valueOf(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                NavUtils.navigateUpTo(this, upIntent);
//            }
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Bitmap blur(Bitmap bkg, View view, float radius) {
        Bitmap overlay = Bitmap.createBitmap(bkg.getWidth(), bkg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(bkg, -view.getLeft(), -view.getTop(), null);
        RenderScript rs = RenderScript.create(this);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        rs.destroy();
        return overlay;
    }

//    public void setHeadImage(Bitmap bitmap) {
//        headImage = bitmap;
////    }

    private class BlurTask extends AsyncTask< Void, Void, Bitmap> {

        private Drawable drawable;

        public BlurTask(Drawable drawable) {
            this.drawable = drawable;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            if (bd != null) {
                int height = bd.getBitmap().getHeight() / 4;
                int width = bd.getBitmap().getWidth() / 4;
                int x = bd.getBitmap().getHeight() / 4;
                int y = bd.getBitmap().getWidth() / 4;

                bitmap = Bitmap.createBitmap(bd.getBitmap(), x, y, width, height);
                bitmap = blur(bitmap, layout, 20);
            }
            return bitmap;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            layout.setContentScrim(new BitmapDrawable(bitmap));
            toolbarImage.setImageDrawable(new BitmapDrawable(bitmap));
        }
    }

    private static class RxHandler extends Handler {

        WeakReference thisActivity;

        public RxHandler(Tusho_show_card_activity activity) {
            thisActivity = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Tusho_show_card_activity activity = (Tusho_show_card_activity) thisActivity.get();
            activity.title.setText(activity.titleS);
            activity.author.setText(activity.authorS);
            activity.count.setText(activity.countS);
            activity.click.setText(activity.clickS);
            activity.adapeter.notifyDataChanged();
        }
    }
}
