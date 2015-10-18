package ecjtu.net.demon.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import ecjtu.net.demon.R;

/**
 * Created by shakugan on 15/10/18.
 */
public class SubCommentsActivity extends AppCompatActivity {

    private EditText commentsText;
    private TextView sizeText;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcomments);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("意见反馈");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                sizeText.setText("还能输入"+(100-s.length())+"个字符");
            }
        });
    }
}
