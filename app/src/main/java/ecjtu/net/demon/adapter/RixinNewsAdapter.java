package ecjtu.net.demon.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.webview;

/**
 * Created by 圣麟 on 2015/8/11.
 */
public class RixinNewsAdapter extends RecyclerView.Adapter {

    private static final int TYPE_HEAD = 2;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
//    private ArrayMap<String,Object> content = new ArrayMap<>();
    private Context context;
    private ArrayList<ArrayMap<String,Object>> listItem = new ArrayList<>();// 列表正文的的arraylist
    private ArrayList<ArrayMap<String,Object>> slide_articles = new ArrayList<>();// 轮转图的arralist
    private LayoutInflater listContainer;
    private newsImageAdapter newsImageAdapter;
    private boolean isInited = false;
    private ArrayList<ArrayMap<String,String>> myTopViewS;
    private ArrayList<ImageView> points;//标识点的list
    private DisplayImageOptions options;
    private boolean isRefresh = false;
    private boolean isRefreshFoot = false;

    public RixinNewsAdapter(Context context) {
        this.context = context;
        listContainer = LayoutInflater.from(context);
        myTopViewS = new ArrayList<>();
//        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
//                .createDefault(context);
//
//        //Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(configuration);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.thumb_default)
                .showImageOnFail(R.drawable.thumb_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

    }

    public void updateInfo(boolean isRefresh) {
        this.isRefresh = isRefresh;
        isRefreshFoot = isRefresh;
        notifyDataSetChanged();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public TextView info;
        public TextView click;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.news_image);
            title = (TextView) itemView.findViewById(R.id.news_title);
            info = (TextView) itemView.findViewById(R.id.news_info);
            click = (TextView) itemView.findViewById(R.id.click);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String articleID = (String) itemView.getTag();
                    String articleUrl = "http://app.ecjtu.net/api/v1/article/" + articleID + "/view";
                    turn2Activity(webview.class, articleUrl,articleID);
                }
            });
        }
    }

    private void turn2Activity(Class activity, String url,String articleId) {
        Intent intent = new Intent();
        intent.setClass(context, activity);
        intent.putExtra("sid",articleId);
        if (url != null) {
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            intent.putExtras(bundle);
        }
        context.startActivity(intent);

    }

    public ArrayList<ArrayMap<String,Object>> getListItem() {
        return listItem;
    }
    public void setListItem(ArrayList<ArrayMap<String,Object>> listItem) {
        this.listItem = listItem;
    }

    public ArrayList<ArrayMap<String,Object>> getSlide_articles() {
        return slide_articles;
    }
    public void setSlide_articles(ArrayList<ArrayMap<String,Object>> slide_articles) {
        this.slide_articles = slide_articles;
    }
//    public ArrayMap<String,Object> getContent() {
//        return content;
//    }
//    public void setContent(ArrayMap<String,Object> content) {
//        this.content = content;
//    }

    private class HeadViewHolder extends RecyclerView.ViewHolder {

        private ViewPager myViewPager;
        private LinearLayout myPointView;//pointView 的容器
        private TextView info;

        public HeadViewHolder(View itemView) {
            super(itemView);
            myViewPager = (ViewPager) itemView.findViewById(R.id.news_viewPager);
            myPointView = (LinearLayout) itemView.findViewById(R.id.point_view);
            info = (TextView) itemView.findViewById(R.id.news_info);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;
        private TextView textView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pull_to_refresh_load_progress);
            textView = (TextView) itemView.findViewById(R.id.pull_to_refresh_loadmore_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else if(position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_ITEM;
        }
    }

    public int getCount() {
        return listItem == null ? 0 : listItem.size() + 2;
    }

    public ArrayMap<String,Object> getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("TAG", "生成rixinNews------------");
        if (viewType == TYPE_HEAD) {
            return new HeadViewHolder(listContainer.inflate(R.layout.newsheadimage,parent,false));
        }
        else if(viewType == TYPE_ITEM) {
            return new ItemViewHolder(listContainer.inflate(R.layout.news,parent,false));
        } else {
            return new FooterViewHolder(listContainer.inflate(R.layout.listview_footer,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder) {
            Log.i("TAG", "-----------" + position + "----------");
            String url = (String) listItem.get(position - 1).get("thumb");
            ((ItemViewHolder)holder).image.setImageResource(R.drawable.thumb_default);
            ((ItemViewHolder)holder).title.setText((String) listItem.get(position - 1).get("title"));
            ((ItemViewHolder)holder).info.setText((String) listItem.get(position - 1).get("info"));
            ((ItemViewHolder)holder).click.setText(listItem.get(position - 1).get("click")+"点击");
            ((ItemViewHolder)holder).itemView.setTag(String.valueOf(listItem.get(position - 1).get("id")));
            ImageLoader.getInstance().displayImage(url, ((ItemViewHolder) holder).image, options);
        } else if(holder instanceof HeadViewHolder) {
            if(!isInited) {
                int height = (int) context.getResources().getDimension(R.dimen.news_content_image_height);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
                lp.gravity = Gravity.TOP;
                ((HeadViewHolder) holder).myViewPager.setLayoutParams(lp);
                ArrayMap<String, String> arrayMap = new ArrayMap<>();
                arrayMap.put("url", (String) slide_articles.get(slide_articles.size() - 1).get("thumb"));
                arrayMap.put("id", String.valueOf(slide_articles.get(slide_articles.size() - 1).get("id")));
                Log.i("slide_articles",String.valueOf(arrayMap));
                myTopViewS.add(arrayMap);
                for (int i = 0; i < slide_articles.size(); i++) {
                    arrayMap = new ArrayMap<>();
                    arrayMap.put("url", (String) slide_articles.get(i).get("thumb"));
                    arrayMap.put("id", String.valueOf(slide_articles.get(i).get("id")));
                    myTopViewS.add(arrayMap);
                    Log.i("slide_articles",String.valueOf(arrayMap));
                }
                arrayMap = new ArrayMap<>();
                arrayMap.put("url", (String) slide_articles.get(0).get("thumb"));
                arrayMap.put("id", String.valueOf(slide_articles.get(0).get("id")));
                myTopViewS.add(arrayMap);
                Log.i("slide_articles",String.valueOf(arrayMap));

                Log.i("exNewsA",String.valueOf(myTopViewS)+"-------");

                newsImageAdapter = new newsImageAdapter(myTopViewS, context);
                ((HeadViewHolder) holder).myViewPager.setAdapter(newsImageAdapter);
                ((HeadViewHolder) holder).myViewPager.setCurrentItem(1);
                ((HeadViewHolder) holder).myViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        if(position < 1) {
                            position = newsImageAdapter.getCount() - 3;
                            ((HeadViewHolder) holder).myViewPager.setCurrentItem(newsImageAdapter.getCount() - 2,false);
                        } else if (position == newsImageAdapter.getCount() - 1) {
                            position = 0;
                            ((HeadViewHolder) holder).myViewPager.setCurrentItem(1,false);
                        } else {
                            position--;
                        }
                        Log.i("position",String.valueOf(position)+"~~~~~~~~");
                        draw_point(position, (HeadViewHolder) holder);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                initPoint((HeadViewHolder) holder);
                isInited = true;
            } else if(isRefresh) {
                myTopViewS.clear();

                ArrayMap<String, String> arrayMap = new ArrayMap<>();
                arrayMap.put("url", (String) slide_articles.get(slide_articles.size() - 1).get("thumb"));
                arrayMap.put("id", String.valueOf(slide_articles.get(slide_articles.size() - 1).get("id")));
                myTopViewS.add(arrayMap);
                for (int i = 0; i < slide_articles.size(); i++) {
                    arrayMap = new ArrayMap<>();
                    arrayMap.put("url", (String) slide_articles.get(i).get("thumb"));
                    arrayMap.put("id", String.valueOf(slide_articles.get(i).get("id")));
                    myTopViewS.add(arrayMap);
                }
                arrayMap = new ArrayMap<>();
                arrayMap.put("url", (String) slide_articles.get(0).get("thumb"));
                arrayMap.put("id", String.valueOf(slide_articles.get(0).get("id")));
                myTopViewS.add(arrayMap);

                newsImageAdapter.notifyDataSetChanged();
                ((HeadViewHolder) holder).myViewPager.setCurrentItem(1);
                draw_point(0, (HeadViewHolder) holder);
                isRefresh = false;
            }
        } else if (holder instanceof FooterViewHolder) {
            if (isRefreshFoot) {
                ((FooterViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                ((FooterViewHolder) holder).textView.setText("加载中……");
                isRefreshFoot = false;
            }
        }
    }

    @Override
    public int getItemCount() {
        return listItem == null ? 0 : listItem.size() + 1;
    }

    private void initPoint(HeadViewHolder holder){
        points = new ArrayList<>();
        ImageView imageView;
        for (int i = 0 ; i < myTopViewS.size()-2;i++){
            imageView = new ImageView(context);
            imageView.setImageResource(R.drawable.indicator);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            holder.myPointView.addView(imageView,layoutParams);
            points.add(imageView);
        }
        draw_point(0,holder);
    }

    private void draw_point(int position,HeadViewHolder holder){
        for (int i = 0; i<myTopViewS.size()-2;i++){
            points.get(i).setImageResource(R.drawable.indicator);
        }
        points.get(position).setImageResource(R.drawable.indicator_focused);
        ArrayMap<String,Object> ArrayMap = slide_articles.get(position);
        holder.info.setText((String) ArrayMap.get("title"));
    }

}
