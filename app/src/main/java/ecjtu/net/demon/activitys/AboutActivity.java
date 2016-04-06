package ecjtu.net.demon.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
        initActionBarAbout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setBackgroundColor(Color.parseColor("#00000000"));
    }
}
