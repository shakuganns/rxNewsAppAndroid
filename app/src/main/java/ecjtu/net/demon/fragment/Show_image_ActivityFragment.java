package ecjtu.net.demon.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.Show_image_Activity;
import ecjtu.net.demon.activitys.Tusho_show_card_activity;
import ecjtu.net.demon.adapter.tushuShowCardAdapter;
import ecjtu.net.demon.utils.ToastMsg;
import ecjtu.net.demon.view.rxMutipleTouchViewPager;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A placeholder fragment containing a simple view.
 */
public class Show_image_ActivityFragment extends Fragment {

    private DisplayImageOptions options;
    public ArrayList<String> content;
    public ArrayList<String> info;
    public TushuoImageAdapeter tushuoImageAdapeter;
    public static String[] uri = new String[30];
    public static rxMutipleTouchViewPager viewPager;
    private TextView pageText;
    private TextView infoText;

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
        viewPager = (rxMutipleTouchViewPager) getView().findViewById(R.id.tushuo_viewpager);
        tushuoImageAdapeter = new TushuoImageAdapeter();
        tushuoImageAdapeter.setContent(getcontent());
        viewPager.setAdapter(tushuoImageAdapeter);
        viewPager.setCurrentItem(tushuShowCardAdapter.position); //设置当前图片为点击图片
        pageText = (TextView) getActivity().findViewById(R.id.page_text);
        pageText.setText(tushuShowCardAdapter.position+1+"/"+tushuoImageAdapeter.getCount());
        infoText = (TextView) getActivity().findViewById(R.id.info_text);
        infoText.setText("              "+Tusho_show_card_activity.infoList.get(tushuShowCardAdapter.position));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageText.setText(position + 1 + "/" + tushuoImageAdapeter.getCount());
                infoText.setText("              "+Tusho_show_card_activity.infoList.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private ArrayList<String> getcontent() {
        content = Tusho_show_card_activity.urlList;
        return content;
    }


    public class TushuoImageAdapeter extends PagerAdapter {

        private ArrayList<String> urls;
        private ActionBar toolbar;

        public TushuoImageAdapeter() {
            toolbar = ((Show_image_Activity) getActivity()).getSupportActionBar();
            toolbar.setShowHideAnimationEnabled(true);
        }

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
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (toolbar.isShowing()) {
                        Log.i("tag", "isShowing~~");
                        toolbar.hide();
//                        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0);
//                        Animator objectAnimator =  ObjectAnimator.ofPropertyValuesHolder(toolbar, pvhX).setDuration(1000);
//                        objectAnimator.start();
                    } else {
                        Log.i("tag", "isHiding~~");
                        toolbar.show();
//                        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 0, 1f);
//                        Animator objectAnimator =  ObjectAnimator.ofPropertyValuesHolder(toolbar, pvhX).setDuration(1000);
//                        objectAnimator.start();
                    }
                }
            });
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.i("tag", "long click~~");
                    new AlertDialog.Builder(getActivity())
                            .setItems(new String[]{"保存到相册"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                                                ImageLoader.getInstance().getDiskCache().get(urls.get(position)).getAbsolutePath()
                                                , "", "");
                                        ToastMsg.builder.display("保存成功", 500);
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
