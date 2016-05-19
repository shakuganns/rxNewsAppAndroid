package ecjtu.net.demon.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
import ecjtu.net.demon.adapter.RixinNewsAdapter;
import ecjtu.net.demon.utils.ACache;
import ecjtu.net.demon.utils.OkHttp;
import ecjtu.net.demon.utils.RxHandler;
import ecjtu.net.demon.utils.ToastMsg;
import ecjtu.net.demon.view.rxRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainFragment extends Fragment {

    private View mContentView;   //内容视图
    private static final int duration = 100;
    private final static String url = "http://app.ecjtu.net/api/v1/index";
    private RixinNewsAdapter rixinNewsAdapter;
    private RecyclerView newslist;
    private LinearLayoutManager linearLayoutManager;
    private rxRefreshLayout refreshLayout;
//    private ArrayMap<String, Object> list = new ArrayMap<>();
    private boolean isbottom;  //是否还有更多的数据
    private int lastVisibleItem;
    private RxHandler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContentView = inflater.inflate(R.layout.activity_main, container, false);
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isbottom = false;
        handler = new RxHandler();
        handler.setOnHandleMessageListener(new RxHandler.OnHandleMessageListener() {
            @Override
            public void onHanleMessage(Message msg) {
                if (msg.what == 0) {
                    rixinNewsAdapter.updateInfo(false);
                    setContentShown(true);
                } else if (msg.what == 1){
                    rixinNewsAdapter.updateInfo(true);
                    refreshLayout.setRefreshing(false);
                    setContentShown(true);
                } else {
                    TextView bottom = (TextView) mContentView.findViewById(R.id.pull_to_refresh_loadmore_text);
                    ProgressBar bottomProgressBar = (ProgressBar) mContentView.findViewById(R.id.pull_to_refresh_load_progress);
                    bottomProgressBar.setVisibility(View.GONE);
                    bottom.setText("已经没有更多新闻啦");
                }
            }
        });
        linearLayoutManager = new LinearLayoutManager(getActivity());
        newslist = (RecyclerView) mContentView.findViewById(R.id.newslist);
        newslist.setLayoutManager(linearLayoutManager);
        //初始化ListView
        refreshLayout = (rxRefreshLayout) mContentView.findViewById(R.id.fresh_layout);

        rixinNewsAdapter = new RixinNewsAdapter(getActivity());
        newslist.setAdapter(rixinNewsAdapter);
        initReflash(refreshLayout);
        loadData(url, null, true, false);
    }

    private void initReflash(final SwipeRefreshLayout refreshLayout) {
//        refreshLayout.setColorSchemeColors(R.color.);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(url, null, false, true);
            }
        });
        newslist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == rixinNewsAdapter.getItemCount() - 1) {
                    if (!isbottom) {
                        ArrayMap<String, Object> ArrayMap = rixinNewsAdapter.getItem(rixinNewsAdapter.getCount() - 3);
                        String articleId = String.valueOf(ArrayMap.get("id"));
                        loadData(url, articleId, false, false);
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (!isVisible()) {
            mContentView.findViewById(R.id.fresh_layout).setVisibility(View.GONE);
            mContentView.findViewById(R.id.loading_container).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 数据请求方法
     * @param lastId api请求参数
     * @param isInit app启动第一次加载数据
     *
     */

    public void loadData(String url, final String lastId,final boolean isInit, final boolean isRefresh) {
        isbottom = false;
        if (lastId != null) {
            url = url + "?until=" + lastId;
        }
        Log.i("tag", "请求链接：" + url);
        final ACache newsListCache = ACache.get(getActivity());
        if (isInit) {
            final JSONObject cache = newsListCache.getAsJSONObject("newsList");
            if (cache != null) {//判断缓存是否为空
                Log.i("tag", "我们使用了缓存~！");
                try {
                    JSONObject slide_article = cache.getJSONObject("slide_article");
                    JSONArray slide_articles = slide_article.getJSONArray("articles");
                    JSONObject normal_article = cache.getJSONObject("normal_article");
                    JSONArray normal_articles = normal_article.getJSONArray("articles");
                    rixinNewsAdapter.setSlide_articles(jsonArray2Arraylist(slide_articles));
                    rixinNewsAdapter.setListItem(jsonArray2Arraylist(normal_articles));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rixinNewsAdapter.updateInfo(false);
                setContentShown(true);
            } else {
                OkHttp.get(url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastMsg.builder.display("请求超时,请重新刷新！", duration);
                    }

                    @Override
                    public void onResponse(Call call, Response res) throws IOException {
                        try {
                            JSONObject response = new JSONObject(res.body().string());
                            JSONObject slide_article = response.getJSONObject("slide_article");
                            JSONArray slide_articles = slide_article.getJSONArray("articles");
                            JSONObject normal_article = response.getJSONObject("normal_article");
                            JSONArray normal_articles = normal_article.getJSONArray("articles");
                            rixinNewsAdapter.setSlide_articles(jsonArray2Arraylist(slide_articles));
                            rixinNewsAdapter.setListItem(jsonArray2Arraylist(normal_articles));
                            if (lastId == null) {//只缓存最新的内容列表
                                newsListCache.remove("newsList");
                                newsListCache.put("newsList", response, 7 * ACache.TIME_DAY);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("tag", "更新线程执行成功");
                        handler.sendEmptyMessage(0);
                    }
                });
            }
        } else {
            OkHttp.get(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastMsg.builder.display("请求超时,网络环境好像不是很好呀！", duration);
                    if (isRefresh) {
                        refreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onResponse(Call call, Response res) throws IOException {
                    JSONArray slide_articles;
                    JSONArray normal_articles = null;
                    try {
                        JSONObject response = new JSONObject(res.body().string());
                        JSONObject slide_article = response.getJSONObject("slide_article");
                        slide_articles = slide_article.getJSONArray("articles");
                        JSONObject normal_article = response.getJSONObject("normal_article");
                        normal_articles = normal_article.getJSONArray("articles");
                        if (lastId == null) {//只缓存最新的内容列表
                            newsListCache.remove("newsList");
                            newsListCache.put("newsList", response, 7 * ACache.TIME_DAY);
                        }
                        if (normal_article.getInt("count") == 0) {
                            isbottom = true;
                            handler.sendEmptyMessage(2);
                        } else {
                            if (isRefresh) {
                                if (rixinNewsAdapter.getListItem() != null) {
                                    rixinNewsAdapter.getListItem().clear();
                                    rixinNewsAdapter.getListItem().addAll(jsonArray2Arraylist(normal_articles));
                                }
                                if (rixinNewsAdapter.getSlide_articles() != null) {
                                    rixinNewsAdapter.getSlide_articles().clear();
                                    rixinNewsAdapter.getSlide_articles().addAll(jsonArray2Arraylist(slide_articles));
                                }
                                handler.sendEmptyMessage(1);
//                                rixinNewsAdapter.updateInfo(isRefresh);
//                                refreshLayout.setRefreshing(false);
                            }
//                        list.put("slide_articles", rixinNewsAdapter.getSlide_articles());
//                        list.put("normal_articles", rixinNewsAdapter.getListItem());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("tag", "更新线程执行成功");
                    if (!isRefresh) {
//                    Log.i("tag", "list is " + String.valueOf(list));
                        if (!isbottom) {
//                        rixinNewsAdapter.getContent().putAll(list);
                            rixinNewsAdapter.getListItem().addAll(jsonArray2Arraylist(normal_articles));
//                            rixinNewsAdapter.updateInfo(isRefresh);
                            handler.sendEmptyMessage(0);
                        }
//                    refreshLayout.setLoading(false);
                    }
                }
            });
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
                    item.put("id", jsonObject.getInt("id"));
                    item.put("title", jsonObject.getString("title"));
                    item.put("updated_at", jsonObject.getString("updated_at"));
                    item.put("info", jsonObject.getString("info"));
                    item.put("click", jsonObject.getString("click"));
                    String imageUrl = /*"http://app.ecjtu.net" + */jsonObject.getString("thumb");
                    item.put("thumb", imageUrl);
                    arrayList.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        return arrayList;
    }

    public void setContentShown(boolean shown) {
        if (shown) {
            newslist.setVisibility(View.VISIBLE);
        }
    }
}
