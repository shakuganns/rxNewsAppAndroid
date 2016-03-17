package ecjtu.net.demon.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.webview;

/**
 * Created by homker on 2015/3/19.
 */
public class newsImageAdapter extends PagerAdapter {

    private ArrayList<ImageView> newsHeadImageViewList;
    private ArrayList<ArrayMap<String,String>> newsHeadImageViewListS;
    private Context context;
    private DisplayImageOptions options;
    private boolean[] isComplete = {false,false,false,false,false};

    public ArrayList<ArrayMap<String,String>> getNewsHeadImageViewListS() {
        return newsHeadImageViewListS;
    }

    public newsImageAdapter(ArrayList<ImageView> newsHeadImageViewList){
        this.newsHeadImageViewList = newsHeadImageViewList;
    }
    public newsImageAdapter(ArrayList<ArrayMap<String,String>> newsHeadImageViewListS,Context context){

        this.newsHeadImageViewListS = newsHeadImageViewListS;
        this.context = context;

        newsHeadImageViewList = new ArrayList<>();
        //防止下面从position=1开始生成视图时产生数组越界错误
        for (int i = 0;i<getCount();i++) {
            newsHeadImageViewList.add(new ImageView(context));
        }

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.thumb_default)
                .showImageOnFail(R.drawable.thumb_default)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        Log.i("tag",String.valueOf(position)+"~~~~~~~");
        String url = newsHeadImageViewListS.get(position).get("url");
        String id = newsHeadImageViewListS.get(position).get("id");
        Log.i("url",url+"~~~~~~");
        Log.i("id",id+"~~~~~~");
        ImageView imageView = null;
        if (position<newsHeadImageViewList.size()){
            Log.i("tag","---a---");
            if (newsHeadImageViewList.get(position) != null) {
                Log.i("tag","---b---");
                imageView = newsHeadImageViewList.get(position);
            }
        }
        imageView.setImageResource(R.drawable.thumb_default);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(new slidePageClickerListener(id));
//        newsHeadImageViewList.add(position,imageView);
        container.addView(imageView);
        ImageLoader.getInstance().displayImage(url, imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                isComplete[position] = true;
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        return newsHeadImageViewList.get(position);
    }

    private void turn2Activity(String ArticleID) {
        String articleUrl = "http://app.ecjtu.net/api/v1/article/"+ArticleID+"/view";
        Intent intent = new Intent();
        intent.setClass(context,webview.class);
        Bundle bundle = new Bundle();

        bundle.putString("url", articleUrl);
        intent.putExtras(bundle);
        intent.putExtra("sid",ArticleID);
        context.startActivity(intent);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i("position","dele"+position);
        container.removeView(newsHeadImageViewList.get(position));
        Drawable drawable = newsHeadImageViewList.get(position).getDrawable();
        if(drawable != null&&isComplete[position]) {
            if(drawable instanceof BitmapDrawable){
                BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if(bitmap != null) {
                    Log.i("position","recycle"+position);
                    bitmap.recycle();
                }
            }
        }
    }

    @Override
    public int getCount() {
        return newsHeadImageViewListS.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private class slidePageClickerListener implements View.OnClickListener {

        private String articleId;

        public slidePageClickerListener(String articleId) {
            this.articleId = articleId;
        }

        @Override
        public void onClick(View v) {
            turn2Activity(articleId);
        }
    }
}
