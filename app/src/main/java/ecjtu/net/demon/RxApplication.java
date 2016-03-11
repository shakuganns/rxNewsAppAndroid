package ecjtu.net.demon;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

import cn.jpush.android.api.JPushInterface;
import ecjtu.net.demon.utils.ToastMsg;

/**
 * Created by homker on 2015/4/11.
 */
public class RxApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastMsg.builder.init(getApplicationContext());
        LeakCanary.install(this);
//        JPushInterface.init(this);
//        CrashReport.initCrashReport(getApplicationContext(), "900011553", false);
    }
}
