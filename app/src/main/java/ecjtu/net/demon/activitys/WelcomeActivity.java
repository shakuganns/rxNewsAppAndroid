package ecjtu.net.demon.activitys;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import java.util.ArrayList;

import ecjtu.net.demon.R;
import ecjtu.net.demon.fragment.WelcomePage;

/**
 * Created by 圣麟 on 2015/8/15.
 */
public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fragments = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.welcomepage);
        fragments.add(new WelcomePage());
        fragments.add(new WelcomePage());
        fragments.add(new WelcomePage());
        ((WelcomePage)fragments.get(0)).setIndex(0);
        ((WelcomePage)fragments.get(1)).setIndex(1);
        ((WelcomePage)fragments.get(2)).setIndex(2);
        adapter = new WelcomeAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
    }

    public class WelcomeAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<>();

        public WelcomeAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }
}
