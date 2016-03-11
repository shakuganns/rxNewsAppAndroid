package ecjtu.net.demon.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
//        setContentViewLayout(R.layout.activity_theme);
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
                themeID = position;
                switch (position) {
                    case DEFAULT_THEME: {
                        editor = preferences.edit();
                        editor.putInt("theme", 0);
                        editor.apply();
                        themeColor = Color.parseColor(DEFUALT_COLOR);
                        themeColorDark = Color.parseColor(DEFUALT_COLOR_DARK);
                        getWindow().getDecorView().setBackgroundColor(themeColorDark);
                        toolbar.setBackgroundColor(themeColor);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(Color.parseColor("#00000000"));
//                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                        }
                        break;
                    }
                    case DARK_THEME: {
                        editor = preferences.edit();
                        editor.putInt("theme", 1);
                        editor.apply();
                        themeColor = Color.parseColor(BLACK_COLOR);
                        themeColorDark = Color.parseColor(BLACK_COLOR_DARK);
                        getWindow().getDecorView().setBackgroundColor(themeColorDark);
                        toolbar.setBackgroundColor(themeColor);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(Color.parseColor("#00000000"));
                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        }
                        break;
                    }
                    case RED_THEME: {
                        editor = preferences.edit();
                        editor.putInt("theme", 2);
                        editor.apply();
                        themeColor = Color.parseColor(RED_COLOR);
                        themeColorDark = Color.parseColor(RED_COLOR_DARK);
                        getWindow().getDecorView().setBackgroundColor(themeColorDark);
                        toolbar.setBackgroundColor(themeColor);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(Color.parseColor("#00000000"));
//                            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        }
                        break;
                    }
                }
//                Intent intent = new Intent(SettingThemeActivity.this,NewMain.class);
//                startActivity(intent);
//                finish();
            }
        });
    }

}
