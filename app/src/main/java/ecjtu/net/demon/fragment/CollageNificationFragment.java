package ecjtu.net.demon.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.NewMain;
import ecjtu.net.demon.adapter.CollageNificationAdapter;
import ecjtu.net.demon.utils.ACache;
import ecjtu.net.demon.utils.HttpAsync;
import ecjtu.net.demon.utils.ToastMsg;

/**
 * Created by homker on 2015/5/4.
 * 日新网新闻客户端
 */

public class CollageNificationFragment extends ProgressFragment {

    private final static String url = "http://app.ecjtu.net/api/v1/schoolnews";
    private static final int duration = 100;
    private CollageNificationAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;
    private static String lastId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<HashMap<String, Object>> content = new ArrayList<>();
    private View mContentView;
    private ACache tushuoListCache;
    private JSONObject cache;
    private MyTask myTask;
    private boolean isBottom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContentView = inflater.inflate(R.layout.collage_nification, container, false);
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(mContentView);
        setEmptyText(R.string.empty);
        setContentShown(false);
        initThread();
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.collage_nification);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.collage_nification_fresh);
        swipeRefreshLayout.setColorSchemeColors(R.color.link_text_material_light);
        adapter = new CollageNificationAdapter(getActivity(),content);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isBottom = false;
                getcontent(url, null, false, true);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == adapter.getItemCount() - 1) {
                    if (!isBottom) {
                        getcontent(url, lastId, false, false);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void initThread() {
        myTask = new MyTask();
    }

    public void initData() {
        getcontent(url, null, true, false);
    }

    private ArrayList<HashMap<String,Object>> getcontent(String url , final String lastId , boolean isInit, final boolean isRefresh) {

        if (lastId != null) {
            url = url + "?until=" + lastId;
        }
        tushuoListCache = ACache.get(getActivity());
        if (isInit) {
            cache = tushuoListCache.getAsJSONObject("CNList");
            if (cache != null) {//判断缓存是否为空
                myTask.execute("");
            }
            if(cache == null) {
                HttpAsync.get(url, new JsonHttpResponseHandler() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (lastId == null) {//只缓存最新的内容列表
                            tushuoListCache.remove("CNList");
                            tushuoListCache.put("CNList", response, 7 * ACache.TIME_DAY);
                        }
                        try {
                            JSONArray list = response.getJSONArray("articles");
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
                        ToastMsg.builder.display("网络环境好像不是很好呀~！", duration);
                    }

                    @Override
                    public void onFinish() {

                    }
                });
            }
        }
        else {
            HttpAsync.get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (lastId == null) {//只缓存最新的内容列表
                        tushuoListCache.remove("CNList");
                        tushuoListCache.put("CNList", response, 7 * ACache.TIME_DAY);
                    }
                    try {
                        if (response.getInt("count") == 0) {
                            isBottom = true;
                            TextView bottom = (TextView) mContentView.findViewById(R.id.pull_to_refresh_loadmore_text);
                            ProgressBar bottomProgressBar = (ProgressBar) mContentView.findViewById(R.id.pull_to_refresh_load_progress);
                            if ( bottomProgressBar == null) {
                                isBottom = false;
                            } else {
                                bottomProgressBar.setVisibility(View.GONE);
                                bottom.setText("已经到底啦");
                            }
                        }
                        else {
                            JSONArray list = response.getJSONArray("articles");
                            content = jsonArray2Arraylist(list);
                            if (isRefresh) {
                                adapter.getContent().clear();
                            }
                            adapter.getContent().addAll(content);
                            adapter.notifyDataSetChanged();
                            setContentShown(true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(isRefresh) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    ToastMsg.builder.display("网络环境好像不是很好呀~！", duration);
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFinish() {

                }
            });
        }
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
                item.put("title", jsonObject.getString("title"));
                item.put("info", jsonObject.getString("info"));
                item.put("click", jsonObject.getString("click"));
                item.put("time", jsonObject.getString("created_at"));
                item.put("id",jsonObject.getString("id"));
                lastId = jsonObject.getString("id");
//                item.put("article_id",jsonObject.getString("article_id"));
                arrayList.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    private class MyTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.i("tag", "我们使用了缓存~！collage");
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                JSONArray array = cache.getJSONArray("articles");
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
        }
    }
}
