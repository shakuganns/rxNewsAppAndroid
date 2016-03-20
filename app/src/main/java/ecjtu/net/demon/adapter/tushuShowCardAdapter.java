package ecjtu.net.demon.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.Show_image_Activity;

/**
 * Created by homker on 2015/5/21.
 * 日新网新闻客户端
 */
public class tushuShowCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    public static Drawable headImage;
    private ArrayList<ArrayMap<String, Object>> content = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private DisplayImageOptions options;

    public static int position = 0;

    public tushuShowCardAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.thumb_default)
                .showImageOnFail(R.drawable.thumb_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    public ArrayList<ArrayMap<String, Object>> getContent() {
        return content;
    }
    
    public void setContent(ArrayList<ArrayMap<String, Object>> content) {
        this.content = content;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new NormalTextViewHolder(layoutInflater.inflate(R.layout.show_card_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalTextViewHolder) {
            ((NormalTextViewHolder) holder).position = position;
            ((NormalTextViewHolder) holder).image.setImageResource(R.drawable.thumb_default);
            ImageLoader.getInstance().displayImage((String) content.get(position).get("url"), ((NormalTextViewHolder) holder).image, options);
        }
    }

    @Override
    public int getItemCount() {
        return content == null ? 0 : content.size();
    }


    public class NormalTextViewHolder extends RecyclerView.ViewHolder {


        private ImageView image;
        private int position;

        public NormalTextViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.tushuo_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tushuShowCardAdapter.position = position;
                    Intent intent = new Intent();
                    intent.setClass(itemView.getContext(), Show_image_Activity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

}
