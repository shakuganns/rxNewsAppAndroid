package ecjtu.net.demon.activitys;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import ecjtu.net.demon.R;

/**
 * Created by Shakugan on 15/12/8.
 */
public class NoGestureBaseActivity extends AppCompatActivity {

    public static int themeID;
    public static final int DEFAULT_THEME = 0;
    public static final int DARK_THEME = 1;
    public static final int RED_THEME = 2;


    @LayoutRes int layoutId;
    public SharedPreferences preferences;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    public NavigationView drawer;
    public DrawerLayout drawerLayout;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
        themeID = preferences.getInt("theme",0);
        switch (themeID) {
            case DEFAULT_THEME: {
                setTheme(R.style.AppTheme);
                break;
            }
            case DARK_THEME: {
                setTheme(R.style.AppThemeDark);
                break;
            }
            case RED_THEME: {
                setTheme(R.style.AppThemeRed);
                break;
            }
        }
        super.onCreate(savedInstanceState);
//        setContentView(layoutId);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

//    public void setContentViewLayout(@LayoutRes int layoutId) {
//        this.layoutId = layoutId;
//    }

    protected void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
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
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
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

}
