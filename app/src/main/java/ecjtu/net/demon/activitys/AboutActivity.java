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
public class AboutActivity extends NoGestureBaseActivity {

    private TextView versionCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        setContentViewLayout(R.layout.activity_about);
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

}
