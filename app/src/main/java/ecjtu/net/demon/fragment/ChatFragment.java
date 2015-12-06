package ecjtu.net.demon.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devspark.progressfragment.ProgressFragment;

import ecjtu.net.demon.R;
import ecjtu.net.demon.adapter.ChatAdapter;

/**
 * Created by Shakugan on 15/11/29.
 */
public class ChatFragment extends ProgressFragment {

    private View mContentView;
    private RecyclerView chatRecycler;
    private ChatAdapter chatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContentView = inflater.inflate(R.layout.chat_fragment, container, false);
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(mContentView);
        setContentShown(true);
        chatRecycler = (RecyclerView) getView().findViewById(R.id.chat_recycler);
        chatAdapter = new ChatAdapter();
        chatRecycler.setAdapter(chatAdapter);
    }
}
