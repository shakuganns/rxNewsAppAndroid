package ecjtu.net.demon.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ecjtu.net.demon.BuildConfig;
import ecjtu.net.demon.R;

/**
 * Created by Shakugan on 15/8/19.
 */
public class AboutActivity extends NoGestureBaseActivity {

    private TextView versionCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        versionCode = (TextView) findViewById(R.id.tv_version);

        try {
            versionCode.setText(String.valueOf("Version " + BuildConfig.VERSION_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setBackgroundColor(Color.parseColor("#00000000"));
    }

    @Override
    protected void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle("关于我们");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
