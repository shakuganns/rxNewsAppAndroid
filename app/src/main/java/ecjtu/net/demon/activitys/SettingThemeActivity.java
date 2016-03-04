package ecjtu.net.demon.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
                switch (position) {
                    case DEFAULT_THEME: {
                        editor = preferences.edit();
                        editor.putInt("theme", 0);
                        editor.commit();
                        break;
                    }
                    case DARK_THEME: {
                        editor = preferences.edit();
                        editor.putInt("theme", 1);
                        editor.commit();
                        break;
                    }
                    case RED_THEME: {
                        editor = preferences.edit();
                        editor.putInt("theme", 2);
                        editor.commit();
                        break;
                    }
                }
                Intent intent = new Intent(SettingThemeActivity.this,NewMain.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
