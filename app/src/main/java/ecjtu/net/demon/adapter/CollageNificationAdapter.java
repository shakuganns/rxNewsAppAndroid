package ecjtu.net.demon.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.webview;
import ecjtu.net.demon.fragment.CollageNificationFragment;
import ecjtu.net.demon.utils.ToastMsg;

/**
 * Created by homker on 2015/5/4.
 * 日新网新闻客户端
 */
public class CollageNificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private ArrayList<HashMap<String, Object>> content = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public CollageNificationAdapter(Context context, ArrayList<HashMap<String, Object>> content) {
        this.content = content;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public ArrayList<HashMap<String, Object>> getContent() {
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new NormalTextViewHolder(layoutInflater.inflate(R.layout.collage_item, viewGroup, false));
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder normalTextViewHolder, int position) {
        if (normalTextViewHolder instanceof NormalTextViewHolder) {
            ((NormalTextViewHolder) normalTextViewHolder).title.setText((String) content.get(position).get("title"));
            ((NormalTextViewHolder) normalTextViewHolder).info.setText((String) content.get(position).get("info"));
            ((NormalTextViewHolder) normalTextViewHolder).click.setText((String) content.get(position).get("click"));
            ((NormalTextViewHolder) normalTextViewHolder).time.setText((String) content.get(position).get("time"));
            ((NormalTextViewHolder) normalTextViewHolder).id = (String) content.get(position).get("id");
        }
    }


    @Override
    public int getItemCount() {
        return content == null ? 0 : content.size() + 1;
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    public class NormalTextViewHolder extends RecyclerView.ViewHolder {

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
