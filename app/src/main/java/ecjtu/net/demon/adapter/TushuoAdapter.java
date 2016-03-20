package ecjtu.net.demon.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.Tusho_show_card_activity;

/**
 * Created by homker on 2015/5/5.
 * 日新网新闻客户端
 */
public class TushuoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private ArrayList<ArrayMap<String, Object>> content = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private DisplayImageOptions options;
    private Context context;
    private boolean isRefreshFoot = false;

    public TushuoAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
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

    public ArrayList<ArrayMap<String, Object>> getContent() {
        return this.content;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void updateInfo(boolean isRefresh) {
        isRefreshFoot = isRefresh;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.i("TAG","生成图说card------------");
        if (viewType == TYPE_ITEM) {
            return new NormalTextViewHolder(layoutInflater.inflate(R.layout.tushuo_item, viewGroup, false), this.context);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalTextViewHolder) {
            ((NormalTextViewHolder) holder).pid.setText((String) content.get(position).get("pid"));
            ((NormalTextViewHolder) holder).title.setText((String) content.get(position).get("title"));
            ((NormalTextViewHolder) holder).info.setText((String) content.get(position).get("info"));
            ((NormalTextViewHolder) holder).click.setText((String) content.get(position).get("click"));
            ((NormalTextViewHolder) holder).time.setText((String) content.get(position).get("time"));
            ((NormalTextViewHolder) holder).imageView.setImageResource(R.drawable.thumb_default);
            ImageLoader.getInstance().displayImage((String) content.get(position).get("image"), ((NormalTextViewHolder) holder).imageView, options);
        } else {
            if (isRefreshFoot) {
                ((FooterViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                ((FooterViewHolder) holder).textView.setText("加载中……");
            }
        }
    }


    @Override
    public int getItemCount() {
        return content == null ? 0 : content.size() +1;
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        private TextView textView;

        public FooterViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pull_to_refresh_load_progress);
            textView = (TextView) itemView.findViewById(R.id.pull_to_refresh_loadmore_text);
        }
    }

    private class NormalTextViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView info;
        private TextView click;
        private TextView time;
        private ImageView imageView;
        private TextView pid;

        public NormalTextViewHolder(final View itemView, final Context context) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            info = (TextView) itemView.findViewById(R.id.info);
            click = (TextView) itemView.findViewById(R.id.click);
            time = (TextView) itemView.findViewById(R.id.time);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            pid = new TextView(context);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tusho_show_card_activity.setPid(pid.getText().toString());
                    tushuShowCardAdapter.headImage = imageView.getDrawable();
                    Intent intent = new Intent(context, Tusho_show_card_activity.class);
                    context.startActivity(intent);
                }
            });
        }
    }

}
