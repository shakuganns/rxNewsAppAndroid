package ecjtu.net.demon.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;
import ecjtu.net.demon.R;
import ecjtu.net.demon.adapter.SettingListAdapter;
import ecjtu.net.demon.utils.HttpAsync;
import ecjtu.net.demon.utils.SharedPreUtil;
import ecjtu.net.demon.utils.ToastMsg;
import ecjtu.net.demon.utils.UserEntity;
import ecjtu.net.demon.view.CycleImageView;

/**
 * Created by Shakugan on 15/12/5.
 */
public class SettingActivity extends BaseActivity {

    private ExpandableListView settingList;
    private SettingListAdapter adapter;
    private CycleImageView headImage;
    private UserEntity userEntity;
    private String updateUrl = "http://app.ecjtu.net/";
    private String VersionUrl = "http://app.ecjtu.net/api/v1/version";
    private int duration = 500;
    private String md5 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initAcitonBar();
        userEntity = NewMain.userEntity;

        settingList = (ExpandableListView) findViewById(R.id.setting);
        adapter = new SettingListAdapter(this);
        settingList.setAdapter(adapter);
        for(int i = 0;i < adapter.getGroupCount();i++) {
            settingList.expandGroup(i);
        }
        settingList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        settingList.setGroupIndicator(null);
        settingList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (groupPosition) {
                    case 0: {
                        new AlertDialog.Builder(SettingActivity.this)
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
                                                                getExternalFilesDir("headImage"), userEntity.getStudentID() + "big" + ".png")));
                                                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                                                startActivityForResult(intent, 10);
                                                break;
                                        }
                                    }
                                })
                                .show();
                        break;
                    }
                    case 1: {
                        break;
                    }
                    case 2: {
                        switch (childPosition) {
                            case 0: {
                                turn2ActivityWithUrl(SettingThemeActivity.class,null);
                                break;
                            }
                            case 1: {
                                turn2ActivityWithUrl(SubCommentsActivity.class,null);
                                break;
                            }
                            case 2: {
                                checkVersionAsync();
                                break;
                            }
                            case 3: {
                                turn2ActivityWithUrl(AboutActivity.class,null);
                                break;
                            }
                        }
                        break;
                    }
                    case 3: {
                        SharedPreUtil.getInstance().DeleteUser();
                        NewMain.isUserInited = false;
                        Intent intent = new Intent(SettingActivity.this,NewMain.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
                return false;
            }
        });
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

    private void turn2ActivityWithUrl(Class activity, String url) {
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, activity);
        if (url != null) {
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    private void checkVersionAsync(){
        HttpAsync.get(VersionUrl, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                Log.i("tag", "it start");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("tag",String.valueOf(response));
                try {
                    int versionCode = response.getInt("version_code");
                    if (versionCode > getVersionCode()) {
                        Log.i("tag", "需要更新");
                        showNoticeDialog();
                    } else {
                        Log.i("tag", "我们不需要更新");
                        ToastMsg.builder.display("已是最新版本，无需更新", duration);
                        //Toast.makeText(Setting.this,"已是最新版本，无需更新",Toast.LENGTH_SHORT).show();
                    }
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

    private int getVersionCode() throws Exception{
        //获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packInfo.versionCode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
//            headImage.setImageDrawable(Drawable.createFromPath(getApplicationContext()
//                    .getExternalFilesDir("headImage") + "/" + studentID + ".png"));
//            headImageUrl = studentID+".png";
//            userEntity.setHeadImage(headImageUrl);
            turn2ActivityWithStringForResult(CutImageActivity.class, userEntity.getStudentID(),null,12);
        }else if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
            Log.i("TAG", String.valueOf(data.getData()));
            turn2ActivityWithStringForResult(CutImageActivity.class, userEntity.getStudentID(), String.valueOf(data.getData()), 12);
            Log.i("TAG", "settingHead----->");
        }
        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
            headImage = adapter.getHeadImage();
            headImage.setImageDrawable(Drawable.createFromPath(getApplicationContext()
                    .getExternalFilesDir("headImage") + "/" + userEntity.getStudentID() + ".png"));
            NewMain.isUserInited = false;
            uplaodHeadImage();
            Log.i("TAG", "settingHead----->");
            Log.i("tag", "file://" + getApplicationContext()
                    .getExternalFilesDir("headImage") + "/" + userEntity.getStudentID() + ".png");
        }
    }

    public void uplaodHeadImage() {
        File file = new File(getApplicationContext().getExternalFilesDir("headImage") + "/" + userEntity.getStudentID() + ".png");
        RequestParams params = new RequestParams();
        params.put("token", userEntity.getToken());
        try {
            params.put("avatar",file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.i("TAG", String.valueOf(file.exists()));

        HttpAsync.post("http://user.ecjtu.net/api/user/" + userEntity.getStudentID() + "/avatar", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("result") == false) {
                        userEntity.updataToken();
                        ToastMsg.builder.display("上传头像失败，请重试", duration);
                    } else {
                        userEntity.setHeadImagePath(response.getString("avatar"));
                        SharedPreUtil.getInstance().putUser(userEntity);
                        ToastMsg.builder.display("上传头像成功", duration);
                        Log.i("TAG", String.valueOf(response));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ToastMsg.builder.display("头像上传失败，网络有点不给力呀", duration);
                Log.i("TAG", String.valueOf(responseString));
                Log.i("TAG", String.valueOf(statusCode));
            }
        });
    }

    private void turn2ActivityWithStringForResult(Class activity, String string,String data,int requestCode) {
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, activity);
        intent.putExtra("string", string);
        intent.putExtra("data",data);
        startActivityForResult(intent, requestCode);
    }

}
