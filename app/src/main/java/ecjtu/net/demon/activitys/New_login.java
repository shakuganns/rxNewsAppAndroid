package ecjtu.net.demon.activitys;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.InputStream;

import cn.jpush.android.api.JPushInterface;
import ecjtu.net.demon.R;
import ecjtu.net.demon.utils.SharedPreUtil;


public class New_login extends Activity {

    private ImageView site;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    boolean mFirst = false;

    public static Drawable readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(is, null, opt));
        return drawable;
    }

    private void turn2mianActivity(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(this, NewMain.class);
        startActivity(intent);
        finish();
    }

    private void turn2WelcomeActivity(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreUtil.initSharedPreference(getApplicationContext());
        setContentView(R.layout.activity_new_login);
        preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
        //判断是不是首次登录，
        if (preferences.getBoolean("firststart", true)) {
            mFirst = true;
            editor = preferences.edit();
            //将登录标志位设置为false，下次登录时不在显示首次登录界面
            editor.putBoolean("firststart", false);
            editor.commit();
        }
//        RelativeLayout background = (RelativeLayout) findViewById(R.id.background);
//        if (Build.VERSION.SDK_INT < 17) {        //兼容低版本
//            background.setBackgroundDrawable(readBitMap(this, R.drawable.backgroud));
//        }else background.setBackground(readBitMap(this, R.drawable.backgroud));
        site = (ImageView) findViewById(R.id.site);
        propertyValuesHolder(site);
        //checkVersionAsync();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    public void propertyValuesHolder(View view) {
        Log.i("tag","动画已经被执行");
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1f);
        Animator objectAnimator =  ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(3000);
        objectAnimator.start();
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mFirst) {
                    turn2WelcomeActivity(null);
                } else {
                    turn2mianActivity(null);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }


}
