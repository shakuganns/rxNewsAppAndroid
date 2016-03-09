package ecjtu.net.demon.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import org.json.JSONObject;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.NewMain;
import ecjtu.net.demon.activitys.rxCommentsActivity;
import ecjtu.net.demon.activitys.webview;
import ecjtu.net.demon.utils.HttpAsync;
import ecjtu.net.demon.utils.ToastMsg;


public class comment_text extends Fragment {

    private View view;
    private Context context;
    public EditText commentText;
    public InputMethodManager imm;
    public FragmentManager fragmentManager;
    private String url;
    private comment_btn commentBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        webview.isComment = true;
        context = getActivity();
        view = inflater.inflate(R.layout.comment_text, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button submitBtn = (Button) view.findViewById(R.id.submitBtn);
        commentText = (EditText) view.findViewById(R.id.commentText);
        fragmentManager = getFragmentManager();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentText.getText().toString().equals("")) {
                    url = "http://app.ecjtu.net/api/v1/article/";
                    url = url + getActivity().getIntent().getStringExtra("sid") + "/comment";
                    Log.i("tag", url);
                    RequestParams params = new RequestParams();
                    params.put("sid", NewMain.userEntity.getStudentID());
                    params.put("content", commentText.getText());
                    Log.i("tag", String.valueOf(params));
                    HttpAsync.post(url, params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            commentText.setText("");
                            // 构造对话框
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("要去看看评论吗？");
                            builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(context, rxCommentsActivity.class);
                                    String url = comment_text.this.url + "s";
                                    intent.putExtra("url", url);
                                    context.startActivity(intent);
                                }
                            });
                            builder.setNegativeButton("不用了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog noticeDialog = builder.create();
                            noticeDialog.show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            ToastMsg.builder.display("提交失败,请重试" + statusCode, 300);
                        }
                    });
                }
                Log.i("tag", "---------submit!!!----------");
                imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0);
                fragmentManager.beginTransaction().replace(R.id.comment_layout, commentBtn).commit();
            }
        });
        commentText.setFocusable(true);
        commentText.setFocusableInTouchMode(true);
        commentText.requestFocus();
        imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
    }

    public boolean onKeyDown(int keyCode) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            imm.hideSoftInputFromWindow(commentText.getWindowToken(), 0);
            fragmentManager.beginTransaction().replace(R.id.comment_layout, commentBtn).commit();
        }
        return true;
    }

    public void setReplaceView(comment_btn view) {
        commentBtn = view;
    }

}
