package ecjtu.net.demon.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.IntentCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import ecjtu.net.demon.R;
import ecjtu.net.demon.adapter.ThemeListAdapter;

/**
 * Created by Shakugan on 15/12/5.
 */
public class SettingThemeActivity extends BaseActivity {

    private ListView themeList;
    private ThemeListAdapter adapter;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        themeList = (ListView) findViewById(R.id.themeList);
        adapter = new ThemeListAdapter(this);
        themeList.setAdapter(adapter);
        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
        initActionBar();
        getSupportActionBar().setTitle("主题设置");
        themeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (themeID == position) {
                    return;
                }
                themeID = position;
                NewMain.themeIsChange = true;
                SettingActivity.themeIsChange = true;
                switch (position) {
                    case DEFAULT_THEME: {
                        editor = preferences.edit();
                        editor.putInt("theme", 0);
                        editor.apply();
                        setTheme(R.style.AppTheme);
                        themeColor = Color.parseColor(DEFUALT_COLOR);
                        themeColorDark = Color.parseColor(DEFUALT_COLOR_DARK);

                        toolbar.setBackgroundColor(themeColor);
                        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.main_container);
                        layout.setStatusBarBackgroundColor(themeColorDark);

                        view.findViewById(R.id.mark).setVisibility(View.VISIBLE);
                        themeList.getChildAt(adapter.getVisMarkPos()).findViewById(R.id.mark).setVisibility(View.GONE);
                        adapter.setVisMarkPos(0);
                        break;
                    }
                    case DARK_THEME: {
                        editor = preferences.edit();
                        editor.putInt("theme", 1);
                        editor.apply();
                        setTheme(R.style.AppThemeDark);
                        themeColor = Color.parseColor(BLACK_COLOR);
                        themeColorDark = Color.parseColor(BLACK_COLOR_DARK);

                        toolbar.setBackgroundColor(themeColor);
                        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.main_container);
                        layout.setStatusBarBackgroundColor(themeColorDark);

                        view.findViewById(R.id.mark).setVisibility(View.VISIBLE);
                        themeList.getChildAt(adapter.getVisMarkPos()).findViewById(R.id.mark).setVisibility(View.GONE);
                        adapter.setVisMarkPos(1);
                        break;
                    }
                    case RED_THEME: {
                        editor = preferences.edit();
                        editor.putInt("theme", 2);
                        editor.apply();
                        setTheme(R.style.AppThemeRed);
                        themeColor = Color.parseColor(RED_COLOR);
                        themeColorDark = Color.parseColor(RED_COLOR_DARK);

                        toolbar.setBackgroundColor(themeColor);
                        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.main_container);
                        layout.setStatusBarBackgroundColor(themeColorDark);

                        view.findViewById(R.id.mark).setVisibility(View.VISIBLE);
                        themeList.getChildAt(adapter.getVisMarkPos()).findViewById(R.id.mark).setVisibility(View.GONE);
                        adapter.setVisMarkPos(2);
                        break;
                    }
                }
            }
        });
    }

}
