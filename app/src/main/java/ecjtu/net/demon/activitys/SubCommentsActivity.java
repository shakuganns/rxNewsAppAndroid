package ecjtu.net.demon.activitys;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import ecjtu.net.demon.R;
import ecjtu.net.demon.utils.OkHttp;
import ecjtu.net.demon.utils.ToastMsg;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 意见反馈
 * Created by shakugan on 15/10/18.
 */
public class SubCommentsActivity extends BaseActivity {

    private EditText commentsText;
    private TextView sizeText;
    private Button submitBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcomments);
        initActionBar();
        getSupportActionBar().setTitle("意见反馈");

        sizeText = (TextView) findViewById(R.id.size_text);
        commentsText = (EditText) findViewById(R.id.commentText);
        commentsText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sizeText.setText("还能输入" + (100 - s.length()) + "个字符");
            }
        });
        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("username",NewMain.userEntity.getStudentID())
                        .addFormDataPart("content", String.valueOf(commentsText.getText()))
                        .build();
                OkHttp.post("http://app.ecjtu.net/api/v1/feedback", requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastMsg.builder.display("提交失败", 500);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ToastMsg.builder.display("提交成功,感谢您的反馈", 500);
                        finish();
                    }
                });
            }
        });
    }
}
