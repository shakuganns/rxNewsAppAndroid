package ecjtu.net.demon.activitys;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import ecjtu.net.demon.BuildConfig;
import ecjtu.net.demon.R;

/**
 * Created by Shakugan on 15/8/19.
 */
public class AboutActivity extends AppCompatActivity {

    private TextView versionCode;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        versionCode = (TextView) findViewById(R.id.tv_version);
        try {
            versionCode.setText(String.valueOf("Version " + BuildConfig.VERSION_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle("关于我们");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
