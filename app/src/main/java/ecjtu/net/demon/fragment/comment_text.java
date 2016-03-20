package ecjtu.net.demon.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.io.IOException;
import java.lang.ref.WeakReference;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.NewMain;
import ecjtu.net.demon.activitys.rxCommentsActivity;
import ecjtu.net.demon.activitys.webview;
import ecjtu.net.demon.utils.OkHttp;
import ecjtu.net.demon.utils.ToastMsg;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class comment_text extends Fragment {

    private View view;
    public EditText commentText;
    public InputMethodManager imm;
    public FragmentManager fragmentManager;
    private String url;
    private comment_btn commentBtn;
    private RxHandler handler;

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
        handler = new RxHandler((webview) getActivity());
        Button submitBtn = (Button) view.findViewById(R.id.submitBtn);
        commentText = (EditText) view.findViewById(R.id.commentText);
        fragmentManager = getFragmentManager();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentText.getText().toString().equals("")) {
                    url = "http://app.ecjtu.net/api/v1/article/";
                    url = url + getActivity().getIntent().getStringExtra("sid") + "/comment";
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("sid", NewMain.userEntity.getStudentID())
                            .addFormDataPart("content", String.valueOf(commentText.getText()))
                            .build();
                    OkHttp.post(url, requestBody, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            ToastMsg.builder.display("提交失败,请重试", 300);
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            handler.sendEmptyMessage(0);
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

    private static class RxHandler extends Handler {

        WeakReference thisActivity;

        public RxHandler(webview activity) {
            thisActivity = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final webview activity = (webview) thisActivity.get();
            activity.commentText.commentText.setText("");
            // 构造对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("要去看看评论吗？");
            builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(activity, rxCommentsActivity.class);
                    String url = activity.commentText.url + "s";
                    intent.putExtra("url", url);
                    activity.startActivity(intent);
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
    }

}
