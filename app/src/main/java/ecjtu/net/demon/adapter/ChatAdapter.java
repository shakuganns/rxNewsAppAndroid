package ecjtu.net.demon.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ecjtu.net.demon.R;

/**
 * Created by Shakugan on 15/11/29.
 */
public class ChatAdapter extends RecyclerView.Adapter {

    private String[] title = {"test"};

    class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public ChatViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("tag","-----onCreateViewHolder----");
        return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rxchat_item,parent));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ChatViewHolder)holder).title.setText(title[position]);
    }

    @Override
    public int getItemCount() {
        return title.length;
    }
}
