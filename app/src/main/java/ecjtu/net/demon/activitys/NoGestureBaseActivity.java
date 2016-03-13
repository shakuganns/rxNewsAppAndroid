package ecjtu.net.demon.activitys;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.ImageLoader;

import ecjtu.net.demon.R;

/**
 * Created by Shakugan on 15/12/8.
 */
public class NoGestureBaseActivity extends AppCompatActivity {

    public static int themeID = -1;
    public static final int DEFAULT_THEME = 0;
    public static final int DARK_THEME = 1;
    public static final int RED_THEME = 2;
    public static final String DEFUALT_COLOR = "#009688";
    public static final String DEFUALT_COLOR_DARK = "#00796b";
    public static final String BLACK_COLOR = "#424242";
    public static final String BLACK_COLOR_DARK = "#212121";
    public static final String RED_COLOR = "#c41411";
    public static final String RED_COLOR_DARK = "#b0120a";
    public static int themeColor;
    public static int themeColorDark;

    public SharedPreferences preferences;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    public NavigationView drawer;
    public DrawerLayout drawerLayout;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (themeID == -1) {
            preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
            themeID = preferences.getInt("theme", 0);
        }
        switch (themeID) {
            case DEFAULT_THEME: {
                themeColor = Color.parseColor(DEFUALT_COLOR);
                themeColorDark = Color.parseColor(DEFUALT_COLOR_DARK);
                setTheme(R.style.AppTheme);
                break;
            }
            case DARK_THEME: {
                themeColor = Color.parseColor(BLACK_COLOR);
                themeColorDark = Color.parseColor(BLACK_COLOR_DARK);
                setTheme(R.style.AppThemeDark);
                break;
            }
            case RED_THEME: {
                themeColor = Color.parseColor(RED_COLOR);
                themeColorDark = Color.parseColor(RED_COLOR_DARK);
                setTheme(R.style.AppThemeRed);
                break;
            }
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
//
//        }
//        setContentView(layoutId);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

//    public void setContentViewLayout(@LayoutRes int layoutId) {
//        this.layoutId = layoutId;
//    }


//    @Override
//    protected void onStart() {
//        super.onStart();
////        toolbar.setBackgroundColor(themeColor);
////        getWindow().getDecorView().setBackgroundColor(themeColorDark);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            Window window = getWindow();
////            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////            window.setStatusBarColor(themeColorDark);
////
////            Log.i("tag",String.valueOf(R.attr.statusBarBackground)+"-----id-----");
////            statusBar.setBackgroundColor(Color.parseColor("#00000000"));
//            toolbar.setBackgroundColor(themeColor);
//            CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.main_container);
//            if (layout != null) {
//                layout.setStatusBarBackgroundColor(themeColorDark);
//
//            }
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setBackgroundColor(themeColor);
        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.main_container);
        if (layout != null) {
            layout.setStatusBarBackgroundColor(themeColorDark);
        }
    }

    protected void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void initActionBarTushuo() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void initActionBarAbout() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle("关于我们");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void initActionBarNewMain() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        drawerLayout = (DrawerLayout) findViewById(R.id.DrawLayout);
        drawer = (NavigationView) findViewById(R.id.drawer);
        toolbar.setTitle("首页");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.tool_bar_open, R.string.tool_bar_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        actionBarDrawerToggle.syncState();

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                slidingMenuClickListen(menuItem.getItemId());
                return false;
            }
        });
    }

    /**
     * NewMain.java中重写的方法
     * @param id view的id
     */
    public void slidingMenuClickListen(int id) {}

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.i("lowmemory", "clearMemoryCache0-----------");
        ImageLoader.getInstance().clearMemoryCache();
    }
}
