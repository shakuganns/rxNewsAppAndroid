package ecjtu.net.demon.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.webview;


/**
 * Created by homker on 2015/5/4.
 * 日新网新闻客户端
 */
public class CollageNificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private ArrayList<ArrayMap<String, Object>> content = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private boolean isRefreshFoot = false;

    public CollageNificationAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
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
        if (viewType == TYPE_ITEM) {
            return new NormalTextViewHolder(layoutInflater.inflate(R.layout.collage_item, viewGroup, false));
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalTextViewHolder) {
            ((NormalTextViewHolder) holder).title.setText((String) content.get(position).get("title"));
            ((NormalTextViewHolder) holder).info.setText((String) content.get(position).get("info"));
            ((NormalTextViewHolder) holder).click.setText((String) content.get(position).get("click"));
            ((NormalTextViewHolder) holder).time.setText((String) content.get(position).get("time"));
            ((NormalTextViewHolder) holder).id = (String) content.get(position).get("id");
        } else {
            if (isRefreshFoot) {
                ((FooterViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
                ((FooterViewHolder) holder).textView.setText("加载中……");
            }
        }
    }


    @Override
    public int getItemCount() {
        return content == null ? 0 : content.size() + 1;
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
        private String id;

        public NormalTextViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            info = (TextView) itemView.findViewById(R.id.info);
            click = (TextView) itemView.findViewById(R.id.click);
            time = (TextView) itemView.findViewById(R.id.time);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,webview.class);
                    intent.putExtra("url","http://app.ecjtu.net/api/v1/article/"+id+"/view");
                    intent.putExtra("sid",id);
                    context.startActivity(intent);
                }
            });
        }
    }
}
