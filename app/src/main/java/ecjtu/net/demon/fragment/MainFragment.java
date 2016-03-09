package ecjtu.net.demon.fragment;

import android.os.Bundle;
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

import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ecjtu.net.demon.R;
import ecjtu.net.demon.adapter.RixinNewsAdapter;
import ecjtu.net.demon.utils.ACache;
import ecjtu.net.demon.utils.HttpAsync;
import ecjtu.net.demon.utils.ToastMsg;

public class MainFragment extends Fragment {

    private View mContentView;   //内容视图
    private static final int duration = 100;
    private final static String url = "http://app.ecjtu.net/api/v1/index";
    private RixinNewsAdapter rixinNewsAdapter;
    private RecyclerView newslist;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout refreshLayout;
//    private ArrayMap<String, Object> list = new ArrayMap<>();
    private boolean isbottom;  //是否还有更多的数据
    private int lastVisibleItem;

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
        linearLayoutManager = new LinearLayoutManager(getActivity());
        newslist = (RecyclerView) mContentView.findViewById(R.id.newslist);
        newslist.setLayoutManager(linearLayoutManager);
        //初始化ListView
        refreshLayout = (SwipeRefreshLayout) mContentView.findViewById(R.id.fresh_layout);


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
                rixinNewsAdapter.updateInfo(isRefresh);
                setContentShown(true);
            } else {
                HttpAsync.get(url, new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (lastId == null) {//只缓存最新的内容列表
                            newsListCache.remove("newsList");
                            newsListCache.put("newsList", response, 7 * ACache.TIME_DAY);
                        }
                        try {
                            JSONObject slide_article = response.getJSONObject("slide_article");
                            JSONArray slide_articles = slide_article.getJSONArray("articles");
                            JSONObject normal_article = response.getJSONObject("normal_article");
                            JSONArray normal_articles = normal_article.getJSONArray("articles");
                            rixinNewsAdapter.setSlide_articles(jsonArray2Arraylist(slide_articles));
                            rixinNewsAdapter.setListItem(jsonArray2Arraylist(normal_articles));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("tag", "更新线程执行成功");
                        rixinNewsAdapter.updateInfo(isRefresh);
                        setContentShown(true);
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
                        newsListCache.remove("newsList");
                        newsListCache.put("newsList", response, 7 * ACache.TIME_DAY);
                    }
                    JSONArray slide_articles;
                    JSONArray normal_articles = null;
                    try {
                        JSONObject slide_article = response.getJSONObject("slide_article");
                        slide_articles = slide_article.getJSONArray("articles");
                        JSONObject normal_article = response.getJSONObject("normal_article");
                        normal_articles = normal_article.getJSONArray("articles");
                        if (normal_article.getInt("count") == 0) {
                            isbottom = true;
                            TextView bottom = (TextView) mContentView.findViewById(R.id.pull_to_refresh_loadmore_text);
                            ProgressBar bottomProgressBar = (ProgressBar) mContentView.findViewById(R.id.pull_to_refresh_load_progress);
                            if (bottomProgressBar == null) {
                                isbottom = false;
                            } else {
                                bottomProgressBar.setVisibility(View.GONE);
                                bottom.setText("已经到底啦");
                            }
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
                                rixinNewsAdapter.updateInfo(isRefresh);
                                refreshLayout.setRefreshing(false);
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
                            rixinNewsAdapter.updateInfo(isRefresh);
                        }
//                    refreshLayout.setLoading(false);
                    }
                    setContentShown(true);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    ToastMsg.builder.display("网络环境好像不是很好呀！", duration);
                    if (!isRefresh) {
//                    refreshLayout.setLoading(false);
                    } else {
                        refreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFinish() {

                }

            });
        }
    }

    public void setContentShown(boolean shown) {
        if (shown) {
            newslist.setVisibility(View.VISIBLE);
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

}
