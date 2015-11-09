package ecjtu.net.demon.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devspark.progressfragment.ProgressFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ecjtu.net.demon.R;
import ecjtu.net.demon.adapter.TushuoAdapter;
import ecjtu.net.demon.utils.ACache;
import ecjtu.net.demon.utils.HttpAsync;
import ecjtu.net.demon.utils.ToastMsg;

/**
 * Created by homker on 2015/5/5.
 * 日新网新闻客户端
 */
public class TushuoFragment extends ProgressFragment {

    private static final int duration = 100;
    private ArrayList<HashMap<String, Object>> content = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final static String url = "http://pic.ecjtu.net/api.php/list";
    private static String lastId;
    private TushuoAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;
    private View mContentView;
    private ACache tushuoListCache;
    private JSONObject cache;
    private MyTask myTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContentView = inflater.inflate(R.layout.tushuo, container, false);
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(mContentView);
        setEmptyText(R.string.empty);
        setContentShown(false);
        initThread();
        recyclerView = (RecyclerView) getView().findViewById(R.id.tushuo);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new TushuoAdapter(getActivity(), content);

        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.tushuo_fresh);
        swipeRefreshLayout.setColorSchemeColors(R.color.link_text_material_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getcontent(url, null, false, true);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == adapter.getItemCount() - 1) {
                    getcontent(url, lastId, false, false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

    }

    public void initData() {
        getcontent(url, null, true, false);
    }

    private void initThread() {
        myTask = new MyTask();
    }

    private ArrayList<HashMap<String, Object>> getcontent(String url, final String lastId, boolean isInit, final boolean isRefresh) {

        if (lastId != null) {
            url = url + "?before=" + lastId;
        }
        tushuoListCache = ACache.get(getActivity());
        if (isInit) {
            cache = tushuoListCache.getAsJSONObject("tushuoList");
            if (cache != null) {//判断缓存是否为空
                myTask.execute("");
            } else {
                Log.i("tag", "初始化tushuo");
                HttpAsync.get(url, new JsonHttpResponseHandler() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (lastId == null) {//只缓存最新的内容列表
                            tushuoListCache.remove("tushuoList");
                            tushuoListCache.put("tushuoList", response, 7 * ACache.TIME_DAY);
                        }
                        try {
                            JSONArray list = response.getJSONArray("list");
                            content = jsonArray2Arraylist(list);
                            adapter.getContent().addAll(content);
                            adapter.notifyDataSetChanged();
                            setContentShown(true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        ToastMsg.builder.display("网络环境好像不是很好呀！", duration);
                    }

                    @Override
                    public void onFinish() {

                    }
                });
            }
        } else {
            HttpAsync.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (lastId == null) {//只缓存最新的内容列表
                        tushuoListCache.remove("tushuoList");
                        tushuoListCache.put("tushuoList", response, 7 * ACache.TIME_DAY);
                    }
                    try {
                        JSONArray list = response.getJSONArray("list");
                        content = jsonArray2Arraylist(list);
                        if (isRefresh) {
                            swipeRefreshLayout.setRefreshing(false);
                            adapter.getContent().clear();
                        }
                        adapter.getContent().addAll(content);
                        adapter.notifyDataSetChanged();
                        setContentShown(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    ToastMsg.builder.display("网络环境好像不是很好呀！", duration);
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFinish() {

                }
            });
        }


        Log.i("tag", "初始化wancengtushuo");
        return content;
    }

    /**
     * 将json数组变成arraylist
     *
     * @param jsonArray 输入你转换的jsonArray
     * @return 返回arraylist
     */
    private ArrayList<HashMap<String, Object>> jsonArray2Arraylist(JSONArray jsonArray) {
        ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                HashMap<String, Object> item = new HashMap<>();
                String imageUrl = "http://" + jsonObject.getString("thumb");
                item.put("image", imageUrl);
                item.put("title", jsonObject.getString("title"));
                item.put("info", jsonObject.getString("count"));
                item.put("click", jsonObject.getString("click"));
                item.put("time", TimeStamp2Date(jsonObject.getString("pubdate"), "yyyy-MM-dd"));
                lastId = jsonObject.getString("pubdate");
                item.put("pid", jsonObject.getString("pid"));
                arrayList.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    /**
     * Unix时间戳转换
     */

    public String TimeStamp2Date(String timestampString, String formats) {
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));
        return date;
    }

    private class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i("tag", "我们使用了缓存~！tushuo");
            try {
                JSONArray array = cache.getJSONArray("list");
                content = jsonArray2Arraylist(array);
                adapter.getContent().addAll(content);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String result) {
            setContentShown(true);
            getcontent(url,null,false,true);
        }
    }
}
