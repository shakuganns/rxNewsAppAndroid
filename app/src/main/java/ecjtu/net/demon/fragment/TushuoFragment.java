package ecjtu.net.demon.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.NewMain;
import ecjtu.net.demon.adapter.TushuoAdapter;
import ecjtu.net.demon.utils.ACache;
import ecjtu.net.demon.utils.OkHttp;
import ecjtu.net.demon.utils.RxHandler;
import ecjtu.net.demon.utils.ToastMsg;
import ecjtu.net.demon.view.rxRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by homker on 2015/5/5.
 * 日新网新闻客户端
 */
public class TushuoFragment extends Fragment {

    private static final int duration = 100;
//    private ArrayList<ArrayMap<String, Object>> content = new ArrayList<>();
    private RecyclerView recyclerView;
    private rxRefreshLayout swipeRefreshLayout;
    private final static String url = "http://pic.ecjtu.net/api.php/list";
    private static String lastId;
    private TushuoAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;
    private View mContentView;
    private ACache tushuoListCache;
    private RxHandler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContentView = inflater.inflate(R.layout.tushuo, container, false);
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handler = new RxHandler();
        handler.setOnHandleMessageListener(new RxHandler.OnHandleMessageListener() {
            @Override
            public void onHanleMessage(Message msg) {
                if (msg.what == 0) {
                    adapter.updateInfo(false);
                    setContentShown(true);
                } else if (msg.what == 1){
                    adapter.updateInfo(true);
                    swipeRefreshLayout.setRefreshing(false);
                    setContentShown(true);
                } else {
                    TextView bottom = (TextView) mContentView.findViewById(R.id.pull_to_refresh_loadmore_text);
                    ProgressBar bottomProgressBar = (ProgressBar) mContentView.findViewById(R.id.pull_to_refresh_load_progress);
                    bottomProgressBar.setVisibility(View.GONE);
                    bottom.setText("已经没有更多啦");
                }
            }
        });
        recyclerView = (RecyclerView) mContentView.findViewById(R.id.tushuo);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TushuoAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = (rxRefreshLayout) mContentView.findViewById(R.id.tushuo_fresh);
//        swipeRefreshLayout.setColorSchemeColors(R.color.link_text_material_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(url, null, false, true);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == adapter.getItemCount() - 1) {
                    loadData(url, lastId, false, false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        loadData(url, null, true, false);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (!isVisible()) {
            mContentView.findViewById(R.id.tushuo_fresh).setVisibility(View.GONE);
            mContentView.findViewById(R.id.loading_container).setVisibility(View.VISIBLE);
        }
    }

    public void updateData() {
        loadData(url, null, false, true);
        swipeRefreshLayout.setRefreshing(true);
    }


    private void loadData(String url, final String lastId, boolean isInit, final boolean isRefresh) {
        if (lastId != null) {
            url = url + "?before=" + lastId;
        }
        tushuoListCache = ACache.get(getActivity());
        if (isInit) {
            JSONObject cache = tushuoListCache.getAsJSONObject("tushuoList");
            if (cache != null) {//判断缓存是否为空
                Log.i("tag", "我们使用了缓存~！tushuo");
                try {
                    JSONArray array = cache.getJSONArray("list");
                    adapter.getContent().addAll(jsonArray2Arraylist(array));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                setContentShown(true);
            } else {
                Log.i("tag", "初始化tushuo");
                OkHttp.get(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastMsg.builder.display("请求超时,请重新刷新！", duration);
                    }

                    @Override
                    public void onResponse(Call call, Response res) throws IOException {
                        try {
                            JSONObject response = new JSONObject(res.body().string());
                            if (lastId == null) {//只缓存最新的内容列表
                                tushuoListCache.remove("tushuoList");
                                tushuoListCache.put("tushuoList", response, 7 * ACache.TIME_DAY);
                            }
                            JSONArray list = response.getJSONArray("list");
                            adapter.getContent().addAll(jsonArray2Arraylist(list));
                            handler.sendEmptyMessage(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } else {
            OkHttp.get(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastMsg.builder.display("请求超时,网络环境好像不是很好呀！", duration);
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onResponse(Call call, Response res) throws IOException {
                    try {
                        JSONObject response = new JSONObject(res.body().string());
                        if (lastId == null) {//只缓存最新的内容列表
                            tushuoListCache.remove("tushuoList");
                            tushuoListCache.put("tushuoList", response, 7 * ACache.TIME_DAY);
                        }
                        JSONArray list = response.getJSONArray("list");
                        if (isRefresh) {
                            adapter.getContent().clear();
                        }
                        adapter.getContent().addAll(jsonArray2Arraylist(list));
                        if (isRefresh) {
                            handler.sendEmptyMessage(1);
                        } else {
                            handler.sendEmptyMessage(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        Log.i("tag", "初始化wancengtushuo");
    }

    public void setContentShown(boolean shown) {
        if (shown) {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 将json数组变成arraylist
     *
     * @param jsonArray 输入你转换的jsonArray
     * @return 返回arraylist
     */
    private ArrayList<ArrayMap<String, Object>> jsonArray2Arraylist(JSONArray jsonArray) {
        ArrayList<ArrayMap<String, Object>> arrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ArrayMap<String, Object> item = new ArrayMap<>();
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
        @SuppressLint("SimpleDateFormat") String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));
        return date;
    }

}
