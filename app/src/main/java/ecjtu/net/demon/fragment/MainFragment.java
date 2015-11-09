package ecjtu.net.demon.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.devspark.progressfragment.ProgressFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import ecjtu.net.demon.R;
import ecjtu.net.demon.adapter.RixinNewsAdapter;
import ecjtu.net.demon.utils.ACache;
import ecjtu.net.demon.utils.HttpAsync;
import ecjtu.net.demon.utils.ToastMsg;
import ecjtu.net.demon.view.rxRefreshLayout;

public class MainFragment extends ProgressFragment {

    private View mContentView;   //内容视图
    private static final int duration = 100;
    private final static String url = "http://app.ecjtu.net/api/v1/index";
    private RixinNewsAdapter rixinNewsAdapter;
    private RecyclerView newslist;
    private LinearLayoutManager linearLayoutManager;
    private rxRefreshLayout refreshLayout = null;
    private HashMap<String, Object> list = new HashMap<>();
    private boolean isbottom;  //是否还有更多的数据
    private int lastVisibleItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContentView = inflater.inflate(R.layout.activity_main, container, false);
        return inflater.inflate(R.layout.fragment_loading, container, false);   //返回loading视图
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(mContentView);
        setEmptyText(R.string.empty);
        setContentShown(false);  //设置内容视图不显示
        isbottom = false;
        linearLayoutManager = new LinearLayoutManager(getActivity());
        newslist = (RecyclerView) getView().findViewById(R.id.newslist);
        newslist.setLayoutManager(linearLayoutManager);
        //初始化ListView
        refreshLayout = (rxRefreshLayout) getView().findViewById(R.id.fresh_layout);

//        upToLoad = new TextView(getActivity());
//        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
//        upToLoad.setLayoutParams(layoutParams);
//        upToLoad.setGravity(Gravity.CENTER);
//        upToLoad.setText("向上滑动加载更多");
//        newslist.addFooterView(upToLoad);
//        newslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView articleIDText = (TextView) view.findViewById(R.id.articleID);
//                String articleID = (String) articleIDText.getText();
//                String articleUrl = "http://app.ecjtu.net/api/v1/article/" + articleID + "/view";
//                turn2Activity(webview.class, articleUrl);
//            }
//        });
        rixinNewsAdapter = new RixinNewsAdapter(getActivity(),list);
        newslist.setAdapter(rixinNewsAdapter);
        initReflash(refreshLayout);
        setNewslist(url, null, true, false);
    }

    private void initReflash(final SwipeRefreshLayout refreshLayout) {
        refreshLayout.setColorSchemeColors(R.color.link_text_material_light);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setNewslist(url, null, false,true);
            }
        });
        newslist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == rixinNewsAdapter.getItemCount() - 1) {
                    if (!isbottom) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) rixinNewsAdapter.getItem(rixinNewsAdapter.getCount() - 3);
                        String articleId = String.valueOf(hashMap.get("id"));
                        setNewslist(url, articleId, false, false);
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

    private void turn2Activity(Class activity, String url) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), activity);
        if (url != null) {
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 数据请求方法
     * @param lastId api请求参数
     * @param isInit app启动第一次加载数据
     *
     */

    public HashMap<String,Object> setNewslist(String url, final String lastId,final boolean isInit, final boolean isRefresh) {
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
                    list.put("slide_articles", jsonArray2Arraylist(slide_articles));
                    list.put("normal_articles", jsonArray2Arraylist(normal_articles));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rixinNewsAdapter.getContent().putAll(list);
                rixinNewsAdapter.updateInfo(isRefresh);
                Log.i("TAG", "---------" + rixinNewsAdapter.getContent().size() + "-------");
                setContentShown(true);
                setNewslist(url,null,false,true);
            }
            if (cache == null) {
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
                            list.put("slide_articles", jsonArray2Arraylist(slide_articles));
                            list.put("normal_articles", jsonArray2Arraylist(normal_articles));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("tag", "更新线程执行成功");
                        rixinNewsAdapter.getContent().putAll(list);
                        rixinNewsAdapter.updateInfo(isRefresh);
                        setContentShown(true);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
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
                try {
                    JSONObject slide_article = response.getJSONObject("slide_article");
                    JSONArray slide_articles = slide_article.getJSONArray("articles");
                    JSONObject normal_article = response.getJSONObject("normal_article");
                    JSONArray normal_articles = normal_article.getJSONArray("articles");
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
                    }
                    else {
                        if(isRefresh) {
                            if (rixinNewsAdapter.getListItem() != null) {
                                rixinNewsAdapter.getListItem().clear();
                            }
                            if (rixinNewsAdapter.getSlide_articles() != null) {
                                rixinNewsAdapter.getSlide_articles().clear();
                                rixinNewsAdapter.getSlide_articles().addAll(jsonArray2Arraylist(slide_articles));
                            } else {
                                rixinNewsAdapter.getSlide_articles().addAll(jsonArray2Arraylist(slide_articles));
                            }
                        }
                        rixinNewsAdapter.getListItem().addAll(jsonArray2Arraylist(normal_articles));
                        list.put("slide_articles", rixinNewsAdapter.getSlide_articles());
                        list.put("normal_articles", rixinNewsAdapter.getListItem());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("tag", "更新线程执行成功");
                if (!isRefresh) {
                    Log.i("tag", "list is " + String.valueOf(list));
                    if(!isbottom) {
                        rixinNewsAdapter.getContent().putAll(list);
                        rixinNewsAdapter.updateInfo(isRefresh);
                    }
//                    refreshLayout.setLoading(false);
                } else {
                    rixinNewsAdapter.getContent().putAll(list);
                    rixinNewsAdapter.updateInfo(isRefresh);
                    refreshLayout.setRefreshing(false);
                }
                setContentShown(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                ToastMsg.builder.display("网络环境好像不是很好呀！", duration);
                if(!isRefresh) {
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
        return list;
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
