package ecjtu.net.demon.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.Tusho_show_card_activity;
import ecjtu.net.demon.adapter.tushuShowCardAdapter;
import ecjtu.net.demon.utils.ToastMsg;
import uk.co.senab.photoview.PhotoView;

/**
 * A placeholder fragment containing a simple view.
 */
public class Show_image_ActivityFragment extends Fragment {

    private DisplayImageOptions options;
    public ArrayList<String> content;
    public TushuoImageAdapeter tushuoImageAdapeter;
    public static String[] uri = new String[30];
    public static ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_show_image_, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getActivity())
                .diskCacheFileNameGenerator(null)
                .build();

        ImageLoader.getInstance().init(config);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.thumb_default)
                .showImageOnFail(R.drawable.thumb_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        viewPager = (ViewPager) getView().findViewById(R.id.tushuo_viewpager);
        tushuoImageAdapeter = new TushuoImageAdapeter();
        tushuoImageAdapeter.setContent(getcontent());
        viewPager.setAdapter(tushuoImageAdapeter);
        viewPager.setCurrentItem(tushuShowCardAdapter.position); //设置当前图片为点击图片
    }

    private ArrayList<String> getcontent() {
        content = Tusho_show_card_activity.urlList;
        return content;
    }


    public class TushuoImageAdapeter extends PagerAdapter {

        private ArrayList<String> urls;

        public void setContent(ArrayList<String> urls) {
            this.urls = urls;
        }


        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 生成viewpager item视图
         */

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final PhotoView photoView = new PhotoView(container.getContext());
            photoView.setTag(position);
            ImageLoader.getInstance().displayImage(urls.get(position), photoView, options);
            uri[position] = ImageLoader.getInstance().getDiskCache().get(urls.get(position)).getPath();
            container.addView(photoView);
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i("tag","long click~~");
                    new AlertDialog.Builder(getActivity())
                            .setItems(new String[]{"保存到相册"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                                                ImageLoader.getInstance().getDiskCache().get(urls.get(position)).getAbsolutePath()
                                                , "", "");
                                        ToastMsg.builder.display("保存成功～",500);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .show();
                    return false;
                }
            });
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
