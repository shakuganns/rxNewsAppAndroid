package ecjtu.net.demon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import ecjtu.net.demon.R;


public class TushuoShowCardAdapter extends BaseAdapter {
//    private ArrayList<HashMap<String,Object>> content;
      private LayoutInflater layoutInflater;
//    private DisplayImageOptions options;

    public TushuoShowCardAdapter(Context context,ArrayList<HashMap<String,Object>> content) {
//        this.content = content;
        layoutInflater = LayoutInflater.from(context);
//        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
//                .createDefault(context);
//
//        ImageLoader.getInstance().init(configuration);
//
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.thumb_default)
//                .showImageOnFail(R.drawable.thumb_default)
//                .cacheInMemory(false)
//                .cacheOnDisk(true)
//                .build();

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view!=null) {

        }
        return null;
    }


    public class NormalTextViewHolder extends RecyclerView.ViewHolder {

        private TextView detail;
        private ImageView image;

        public NormalTextViewHolder(final View itemView) {
            super(itemView);
            detail = (TextView) itemView.findViewById(R.id.info);
            image = (ImageView) itemView.findViewById(R.id.tushuo_image);


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.setClass(itemView.getContext(), Show_image_Activity.class);
//                    itemView.getContext().startActivity(intent);
//                }
//            });
        }
    }
}
