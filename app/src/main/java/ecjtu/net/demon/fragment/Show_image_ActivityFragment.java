package ecjtu.net.demon.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.Tusho_show_card_activity;
import ecjtu.net.demon.adapter.tushuShowCardAdapter;
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
        viewPager.setCurrentItem(tushuShowCardAdapter.position);
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

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            ImageLoader.getInstance().displayImage(urls.get(position), photoView, options);
            uri[position] = ImageLoader.getInstance().getDiskCache().get(urls.get(position)).getPath();
            container.addView(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
