package ecjtu.net.demon.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.NewMain;
import ecjtu.net.demon.activitys.webview;
import ecjtu.net.demon.utils.HttpAsync;
import ecjtu.net.demon.utils.ToastMsg;
import ecjtu.net.demon.utils.UserEntity;


public class comment_text extends Fragment {

    private View view;
    private Button submitBtn;
    public static EditText commentText;
    private static InputMethodManager imm;
    private static FragmentManager fragmentManager;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        webview.isComment = true;
        view = inflater.inflate(R.layout.comment_text, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        submitBtn = (Button) view.findViewById(R.id.submitBtn);
        commentText = (EditText) view.findViewById(R.id.commentText);
        fragmentManager = getFragmentManager();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!commentText.getText().toString().equals("")) {
                    url = "http://app.ecjtu.net/api/v1/article/";
                    url = url + getActivity().getIntent().getStringExtra("sid") + "/comment";
                    Log.i("tag", url);
                    RequestParams params = new RequestParams();
                    params.put("sid", NewMain.userEntity.getStudentID());
                    params.put("token", NewMain.userEntity.getToken());
                    params.put("content", commentText.getText());
                    Log.i("tag",String.valueOf(params));
                    HttpAsync.post(url, params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            commentText.setText("");
                            ToastMsg.builder.display("提交成功～～～", 300);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            ToastMsg.builder.display("提交失败～～～" + statusCode, 300);
                        }
                    });
                }
                Log.i("tag","---------submit!!!----------");
                imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0);
                fragmentManager.beginTransaction().replace(R.id.comment_layout, webview.commentBtn).commit();
            }
        });
        commentText.setFocusable(true);
        commentText.setFocusableInTouchMode(true);
        commentText.requestFocus();
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == event.KEYCODE_BACK) {
            imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0);
            fragmentManager.beginTransaction().replace(R.id.comment_layout, webview.commentBtn).commit();
        }
        return true;
    }

}
