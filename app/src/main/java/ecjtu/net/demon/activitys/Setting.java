package ecjtu.net.demon.activitys;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ecjtu.net.demon.R;
import ecjtu.net.demon.utils.HttpAsync;
import ecjtu.net.demon.utils.SharedPreUtil;
import ecjtu.net.demon.utils.ToastMsg;
import ecjtu.net.demon.utils.UserEntity;
import ecjtu.net.demon.view.CycleImageView;

/**
 * Created by 圣麟 on 2015/3/30.
 */
public class Setting extends BaseActivity {

    private ListView userListView;
    private ListView aboutListView;
    private CycleImageView headImage;
    private UserEntity userEntity;
    private Button exit;
    private String userName;
    private String updateUrl = "http://app.ecjtu.net/";
    private String VersionUrl = "http://app.ecjtu.net/api/v1/version";
    private int duration = 300;
    private String md5 = null;


    private void showNoticeDialog()
    {
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(R.string.soft_update_info);
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                turn2ActivityWithUrl(ContentWebView.class, updateUrl);

            }
        });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog noticeDialog = builder.create();
        noticeDialog.show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        initAcitonBar();
        userEntity = SharedPreUtil.getInstance().getUser();
        userName = userEntity.getUserName();
        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreUtil.getInstance().DeleteUser();
                NewMain.isUserInit = false;
                turn2mianActivity(null);
            }
        });
        userListView = (ListView) findViewById(R.id.userlist);
        SimpleAdapter userAdapter = new SimpleAdapter(this, getUserData(), R.layout.list_item, new String[] { "notes","information" }, new int[] { R.id.notes, R.id.information});
        userListView.setAdapter(userAdapter);
        headImage = (CycleImageView) findViewById(R.id.imageView2);
        File file = new File(getApplicationContext().getExternalFilesDir("headImage") + "/" + getUserId() + ".png");
        if (file.exists()) {
            headImage.setImageDrawable(Drawable.createFromPath(getApplicationContext()
                    .getExternalFilesDir("headImage") + "/" + getUserId() + ".png"));
        }
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Setting.this)
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
                                                        getExternalFilesDir("headImage"), getUserId() + "big" + ".png")));
                                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                                        startActivityForResult(intent, 10);
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
        aboutListView = (ListView) findViewById(R.id.aboutlist);
        SimpleAdapter aboutAdapter = null;
        try {
            aboutAdapter = new SimpleAdapter(this, getAboutData(), R.layout.list_item, new String[] { "notes","information" }, new int[] { R.id.notes, R.id.information});
        } catch (Exception e) {
            e.printStackTrace();
        }
        aboutListView.setAdapter(aboutAdapter);
        aboutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        checkVersionAsync();
                        break;
                    case 2:
                        turn2ActivityWithUrl(AboutActivity.class,null);
                }
            }
        });
    }

    private void turn2ActivityWithUrl(Class activity, String url) {
        Intent intent = new Intent();
        intent.setClass(Setting.this, activity);
        if (url != null) {
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    private void initAcitonBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("设置");
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void checkVersionAsync(){
        HttpAsync.get(VersionUrl, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.i("tag", "it start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int versionCode = response.getInt("version_code");
                    md5 = response.getString("md5");
                    if (versionCode > getVersionCode()) {
                        Log.i("tag", "需要更新");
                        showNoticeDialog();
                    } else {
                        Log.i("tag", "我们不需要更新");
                        ToastMsg.builder.display("已是最新版本，无需更新", duration);
                        //Toast.makeText(Setting.this,"已是最新版本，无需更新",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ToastMsg.builder.display("网络请求失败", duration);
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


    private List<Map<String, Object>> getUserData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("notes", "账号信息");
        map.put("information",getUserId());
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("notes", "昵称");
        map.put("information",getNickname());
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("notes", "密码");
        map.put("information","******");
        list.add(map);
        return list;
    }

    private List<Map<String, Object>> getAboutData() throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("notes", "消息通知");
        map.put("information",">");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("notes", "版本信息");
        map.put("information",getVersionName());
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("notes", "关于我们");
        map.put("information",">");
        list.add(map);
        return list;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(Setting.this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    Log.i("tag", "nihao" + String.valueOf(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getVersionName() throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

    public String getUserId() {

        return userEntity.getStudentID();
    }

    public String getNickname() {
        return userEntity.getUserName();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
//            headImage.setImageDrawable(Drawable.createFromPath(getApplicationContext()
//                    .getExternalFilesDir("headImage") + "/" + studentID + ".png"));
//            headImageUrl = studentID+".png";
//            userEntity.setHeadImage(headImageUrl);
            turn2ActivityWithStringForResult(CutImageActivity.class, getUserId(),null,12);
        }else if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            Log.i("TAG", String.valueOf(data.getData()));
            turn2ActivityWithStringForResult(CutImageActivity.class, getUserId(), String.valueOf(data.getData()), 12);
            Log.i("TAG", "settingHead----->");
        }
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            headImage.setImageDrawable(Drawable.createFromPath(getApplicationContext()
                    .getExternalFilesDir("headImage") + "/" + getUserId() + ".png"));
            NewMain.isUserInit = false;
            Log.i("TAG", "settingHead----->");
            Log.i("tag", "file://" + getApplicationContext()
                    .getExternalFilesDir("headImage") + "/" + getUserId() + ".png");
        }
    }

    private void turn2ActivityWithStringForResult(Class activity, String string,String data,int requestCode) {
        Intent intent = new Intent();
        intent.setClass(Setting.this, activity);
        intent.putExtra("string", string);
        intent.putExtra("data",data);
        startActivityForResult(intent, requestCode);
    }

}
