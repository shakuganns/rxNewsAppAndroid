package ecjtu.net.demon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.LoginActivity;
import ecjtu.net.demon.activitys.NewMain;
import ecjtu.net.demon.activitys.webview;
import ecjtu.net.demon.utils.ToastMsg;


public class comment_btn extends Fragment {

    private View view;
    private Button commentBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        webview.isComment = false;
        view = inflater.inflate(R.layout.comment_btn, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        commentBtn = (Button) view.findViewById(R.id.comment_btn);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NewMain.userEntity.getStudentID() == null) {
                    ToastMsg.builder.display("请先登录", 300);
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
//                    commentBtn.setText("逗你的啦，功能还没做好 (/= _ =)/~┴┴ ");
                    getFragmentManager().beginTransaction().replace(R.id.comment_layout, webview.commentText).commit();
                }
            }
        });
    }
}
