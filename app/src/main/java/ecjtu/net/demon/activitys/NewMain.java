package ecjtu.net.demon.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


import cz.msebera.android.httpclient.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import ecjtu.net.demon.R;
import ecjtu.net.demon.adapter.MainAdapter;
import ecjtu.net.demon.fragment.CollageNificationFragment;
import ecjtu.net.demon.fragment.MainFragment;
import ecjtu.net.demon.fragment.TushuoFragment;
import ecjtu.net.demon.utils.HttpAsync;
import ecjtu.net.demon.utils.SharedPreUtil;
import ecjtu.net.demon.utils.ToastMsg;
import ecjtu.net.demon.utils.UserEntity;
import ecjtu.net.demon.view.CycleImageView;
import ecjtu.net.demon.view.SlidingTabLayout;


public class NewMain extends AppCompatActivity {

    private boolean isExit = false;
    public static UserEntity userEntity;
    public static boolean isUserInited = false;
    private String studentID;
    private String userName;
    private CycleImageView headImage;
    private SlidingTabLayout tab;
    private ViewPager pager;
    private DrawerLayout drawerLayout;
    private NavigationView drawer;
    private String md5 = null;
    private String updateUrl = "http://app.ecjtu.net/";
    private String VersionUrl = "http://app.ecjtu.net/api/v1/version";
    private int duration = 300;
    public static MainAdapter mainAdapter;
    private MainFragment mainFragment;
    private CollageNificationFragment collageNificationFragment;
    private TushuoFragment tushoFragment;
    private boolean[] isInit = {true,true,true};
    private Toolbar toolbar;
    private DisplayImageOptions options;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public void initFragment() {
        mainFragment = new MainFragment();
        collageNificationFragment = new CollageNificationFragment();
        tushoFragment = new TushuoFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        View main_view = LayoutInflater.from(this).inflate(R.layout.drawlayout, null);
//        main_view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.drawlayout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //使用toolbar代替actionbar
        drawerLayout = (DrawerLayout) findViewById(R.id.DrawLayout);
        initFragment();
        initActionBar();
        initViewPager();
        checkVersionAsync();
        headImage = (CycleImageView) findViewById(R.id.UserImage);
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG", "TouchImage-----");
                slidingMenuClickListen(R.id.UserImage);
            }
        });
        userEntity = SharedPreUtil.getInstance().getUser();
        if (!TextUtils.isEmpty(userEntity.getStudentID())) {
            userEntity.updataToken();
        }

        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);

        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.userimage)
                .showImageOnFail(R.drawable.userimage)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isUserInited) {
            initUserInfo();
            TextView userNameView = (TextView) findViewById(R.id.UserName);
            TextView studentIdView = (TextView) findViewById(R.id.studentId);
            if (userName != null) {
                userNameView.setText(userName);
                studentIdView.setText(studentID);
                File file = new File(getApplicationContext().getExternalFilesDir("headImage") + "/" + studentID + ".png");
                if (file.exists()) {
                    headImage = (CycleImageView) findViewById(R.id.UserImage);
                    headImage.setImageDrawable(Drawable.createFromPath(getApplicationContext()
                            .getExternalFilesDir("headImage") + "/" + studentID + ".png"));
                } else if(userEntity.getHeadImagePath() == "") {

                } else {
                    ImageLoader.getInstance().displayImage("http://"+userEntity.getHeadImagePath(),headImage,options);
                }
            } else {
                userNameView.setText(R.string.UserName);
                studentIdView.setText("");
                headImage = (CycleImageView) findViewById(R.id.UserImage);
                headImage.setImageResource(R.drawable.userimage);
                userNameView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(NewMain.this, LoginActivity.class);
                        NewMain.this.startActivity(intent);
                    }
                });
            }
            isUserInited = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreUtil.getInstance().putUser(userEntity);
    }

    private void findView() {
        tab = (SlidingTabLayout) findViewById(R.id.tab);
        tab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("TAG", "---------" + position + "------------");
                switch (position) {
                    case 1:
                        if (isInit[1]) {
                            Log.i("TAG", "---------setfragment2------------");
                            collageNificationFragment.initData();
                            isInit[1] = false;
                        }
                        break;
                    case 2:
                        if (isInit[2]) {
                            Log.i("TAG", "---------setfragment3------------");
                            tushoFragment.initData();
                            isInit[2] = false;
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pager = (ViewPager) findViewById(R.id.pager);
    }

    private void initViewPager() {
        findView();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(mainFragment);
        fragments.add(collageNificationFragment);
        fragments.add(tushoFragment);
        mainAdapter = new MainAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(mainAdapter);
        pager.setOffscreenPageLimit(fragments.size());
        tab.setCustomTabView(R.layout.tab_style, 0);
       /* tab.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
            }

            @Override
            public int getDividerColor(int position) {
                return 0;
            }
        });*/
        tab.setViewPager(pager);
    }


    private void initUserInfo() {
        userEntity = SharedPreUtil.getInstance().getUser();
        if (!TextUtils.isEmpty(userEntity.getStudentID())) {
            studentID = userEntity.getStudentID();
            userName = userEntity.getUserName();
        }
        else {
            studentID = null;
            userName = null;
            headImage = null;
        }
    }


    public void slidingMenuClickListen(int id) {
        String url = null;
        if (!TextUtils.isEmpty(studentID)) {
            switch (id) {
                case R.id.UserImage:
                    new AlertDialog.Builder(this)
                            .setTitle("上传头像")
                            .setItems(new String[]{"在相册中选择", "拍照"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent;
                                    switch (which) {
                                        case 0:
                                            intent = new Intent(Intent.ACTION_GET_CONTENT);
                                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                                            intent.setType("image/*");
                                            startActivityForResult(intent, 11);
                                            break;
                                        case 1:
                                            intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                                    Uri.fromFile(new File(getApplicationContext().
                                                            getExternalFilesDir("headImage"), studentID+ "big" + ".png")));
                                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                                            startActivityForResult(intent, 10);
                                            break;
                                    }
                                }
                            })
                            .show();
                    break;
                case R.id.scran:
                    turn2ActivityWithUrl(CaptureActivity.class, null);
                    break;
                case R.id.score:
                    url = "file:///android_asset/RixinScoreQuery/scoreQuery.html";
                    break;
                case R.id.classquery:
                    url = "file:///android_asset/RixinClassQuery/classQuery.html";
                    break;
                case R.id.yktquery:
                    url = "file:///android_asset/RixinCardQuery/cardQuery.html";
                    break;
                case R.id.setting:
                    turn2ActivityWithUrl(Setting.class, null);
                    break;
                case R.id.bookquery:
                    ToastMsg.builder.display("开发中...", duration);
                    break;
                default:
                    //ToastMsg.builder.display("开发中...", duration);
                    //Toast.makeText(main.this, "开发中。。。", Toast.LENGTH_SHORT).show();
                /*case R.id.bookquery:;break;
                case R.id.moonModel:;break;
                */
            }
            if (url != null) {
                turn2ActivityWithUrl(ContentWebView.class, url);
            }
        } else {
            switch (id) {
                case R.id.scran:
                    turn2ActivityWithUrl(CaptureActivity.class, null);
                    break;
                default: {
                    turn2ActivityWithUrl(LoginActivity.class, null);
                    ToastMsg.builder.display("请先行登入", duration);
                }
            }
            //Toast.makeText(main.this, "请先行登入", Toast.LENGTH_SHORT).show();
        }
    }

    private void turn2ActivityWithUrl(Class activity, String url) {
        Intent intent = new Intent();
        intent.setClass(NewMain.this, activity);
        if (url != null) {
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    private void turn2ActivityWithStringForResult(Class activity, String string,String data,int requestCode) {
        Intent intent = new Intent();
        intent.setClass(NewMain.this, activity);
        intent.putExtra("string", string);
        intent.putExtra("data", data);
        startActivityForResult(intent, requestCode);
    }

    private void initActionBar() {
        setContentView(R.layout.activity_new_main);
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
                toolbar.setTitle("小新助手");
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                toolbar.setTitle("首页");
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        boolean isDrawerOpen = drawerLayout.isDrawerOpen(drawer);
//        menu.findItem(R.id.searchView).setVisible(!isDrawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.searchView) {
            ToastMsg.builder.display("这个还不知道要用来干什么", duration);
        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private void showNoticeDialog() {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(R.string.soft_update_info);
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                // 显示下载对话框
                turn2ActivityWithUrl(ContentWebView.class,updateUrl);
            }
        });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.putBoolean("update", false);
                editor.commit();
                dialog.dismiss();
                ToastMsg.builder.display("需要升级请在设置界面选择升级哟～", duration);
            }
        });
        AlertDialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void checkVersionAsync(){
        HttpAsync.get(VersionUrl, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                Log.i("tag", "it start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);
                if (preferences.getBoolean("update", true)) {
                    try {
                        int versionCode = response.getInt("version_code");
                        md5 = response.getString("md5");
                        if (versionCode > getVersionCode()) {
                            Log.i("tag", "需要更新");
                            ToastMsg.builder.display("有新版本了，快更新吧!", duration);
                            showNoticeDialog();
                        } else {
                            Log.i("tag", "我们不需要更新");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
//                ToastMsg.builder.display("更新请求失败", duration);
                //Toast.makeText(Setting.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private int getVersionCode() throws Exception{
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionCode;
    }

    private void exitInBack2() {
        Timer tExit;
        if (!isExit) {
            isExit = true;
            ToastMsg.builder.display("再按一次退出程序", duration);
            //Toast.makeText(main.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == event.KEYCODE_BACK)&&drawerLayout.isDrawerOpen(drawer)) {
            drawerLayout.closeDrawer(drawer);
        }
        else if (keyCode == event.KEYCODE_BACK){
            exitInBack2();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
//            headImage.setImageDrawable(Drawable.createFromPath(getApplicationContext()
//                    .getExternalFilesDir("headImage") + "/" + studentID + ".png"));
//            headImageUrl = studentID+".png";
//            userEntity.setHeadImage(headImageUrl);
            turn2ActivityWithStringForResult(CutImageActivity.class, studentID,null,12);
        }else if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            Log.i("TAG", String.valueOf(data.getData()));
            turn2ActivityWithStringForResult(CutImageActivity.class, studentID, String.valueOf(data.getData()), 12);
            Log.i("TAG", "settingHead----->");
        }
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            headImage.setImageDrawable(Drawable.createFromPath(getApplicationContext()
                      .getExternalFilesDir("headImage") + "/" + studentID + ".png"));
            uplaodHeadImage();
            Log.i("TAG", "settingHead----->");
            Log.i("tag", "file://" + getApplicationContext()
                    .getExternalFilesDir("headImage") + "/" + studentID + ".png");
        }
    }

    public void uplaodHeadImage() {
        File file = new File(getApplicationContext().getExternalFilesDir("headImage") + "/" + studentID + ".png");
        RequestParams params = new RequestParams();
        params.put("token",userEntity.getToken());
        try {
            params.put("avatar",file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.i("TAG", String.valueOf(file.exists()));

        HttpAsync.post("http://user.ecjtu.net/api/user/"+studentID+"/avatar",params,new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(response.getBoolean("result") == false) {
                        userEntity.updataToken();
                        ToastMsg.builder.display("上传头像失败，请重试～", duration);
                    } else {
                        userEntity.setHeadImagePath(response.getString("avatar"));
                        SharedPreUtil.getInstance().putUser(userEntity);
                        ToastMsg.builder.display("上传头像成功", duration);
                        Log.i("TAG",String.valueOf(response));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ToastMsg.builder.display("头像上传失败，网络有点不给力呀～", duration);
                Log.i("TAG", String.valueOf(responseString));
                Log.i("TAG", String.valueOf(statusCode));
            }
        });
    }

}
