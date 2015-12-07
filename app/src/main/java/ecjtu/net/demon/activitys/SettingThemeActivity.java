package ecjtu.net.demon.activitys;

import android.os.Bundle;
import android.widget.ListView;

import ecjtu.net.demon.R;
import ecjtu.net.demon.adapter.ThemeListAdapter;

/**
 * Created by Shakugan on 15/12/5.
 */
public class SettingThemeActivity extends BaseActivity {

    private ListView themeList;
    private ThemeListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        themeList = (ListView) findViewById(R.id.themeList);
        adapter = new ThemeListAdapter(this);
        themeList.setAdapter(adapter);
    }
}
