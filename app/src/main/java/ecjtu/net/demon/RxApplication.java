package ecjtu.net.demon;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;
import ecjtu.net.demon.utils.ToastMsg;

/**
 * Created by homker on 2015/4/11.
 */
public class RxApplication extends Application {

//    public static final int DEFAULT_THEME = 0;
//    public static final int DARK_THEME = 1;
//    public static final int RED_THEME = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        ToastMsg.builder.init(getApplicationContext());
        LeakCanary.install(this);
//        SharedPreferences preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
//        switch (preferences.getInt("theme",0)) {
//            case DEFAULT_THEME: {
//                setTheme(R.style.AppTheme);
//                break;
//            }
//            case DARK_THEME: {
//                setTheme(R.style.AppThemeDark);
//                break;
//            }
//            case RED_THEME: {
//                setTheme(R.style.AppThemeRed);
//                break;
//            }
//        }
//        JPushInterface.init(this);
//        CrashReport.initCrashReport(getApplicationContext(), "900011553", false);
    }
}
