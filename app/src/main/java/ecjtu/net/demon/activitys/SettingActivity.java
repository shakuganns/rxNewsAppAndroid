package ecjtu.net.demon.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;

import cz.msebera.android.httpclient.Header;
import ecjtu.net.demon.R;
import ecjtu.net.demon.adapter.SettingListAdapter;
import ecjtu.net.demon.utils.HttpAsync;
import ecjtu.net.demon.utils.OkHttp;
import ecjtu.net.demon.utils.SharedPreUtil;
import ecjtu.net.demon.utils.ToastMsg;
import ecjtu.net.demon.utils.UserEntity;
import ecjtu.net.demon.view.CycleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Shakugan on 15/12/5.
 */
public class SettingActivity extends BaseActivity {

    private static final int VERSION_DIALOG = 0;
    private static final int UPLOAD_HEADIMAGE = 1;

    public static boolean themeIsChange = false;
    private ExpandableListView settingList;
    private SettingListAdapter adapter;
    private CycleImageView headImage;
    private UserEntity userEntity;
    private String updateUrl = "http://app.ecjtu.net/";
    private String VersionUrl = "http://app.ecjtu.net/api/v1/version";
    private int duration = 500;
    private String md5 = null;
    private RxHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentViewLayout(R.layout.activity_setting);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        handler = new RxHandler(this);
        initActionBar();
        getSupportActionBar().setTitle("设置");

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

    @Override
    protected void onResume() {
        super.onResume();
        if (themeIsChange) {
            setTheme(themeID);
            themeIsChange = false;
        }
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
        OkHttp.get(updateUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastMsg.builder.display("网络好像出了点问题", duration);
            }

            @Override
            public void onResponse(Call call, Response res) throws IOException {
                try {
                    JSONObject response = new JSONObject(res.body().string());
                    int versionCode = response.getInt("version_code");
                    if (versionCode > getVersionCode()) {
                        Log.i("tag", "需要更新");
                        handler.sendEmptyMessage(VERSION_DIALOG);
                    } else {
                        Log.i("tag", "我们不需要更新");
                        ToastMsg.builder.display("已是最新版本，无需更新", duration);
                        //Toast.makeText(Setting.this,"已是最新版本，无需更新",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", userEntity.getToken())
                .addFormDataPart("avatar", file.getName(),
                        RequestBody.create(OkHttp.MEDIA_TYPE_PNG, file))
                .build();

        OkHttp.post("http://user.ecjtu.net/api/user/" + userEntity.getStudentID() + "/avatar", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastMsg.builder.display("头像上传失败，网络有点不给力呀", duration);
            }

            @Override
            public void onResponse(Call call, Response res) throws IOException {
                try {
                    JSONObject response = new JSONObject(res.body().string());
                    if (!response.getBoolean("result")) {
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
        });
    }

    private void turn2ActivityWithStringForResult(Class activity, String string,String data,int requestCode) {
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, activity);
        intent.putExtra("string", string);
        intent.putExtra("data",data);
        startActivityForResult(intent, requestCode);
    }

    private static class RxHandler extends Handler {

        WeakReference thisActivity;

        public RxHandler(SettingActivity activity) {
            thisActivity = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == VERSION_DIALOG) {
                ((SettingActivity)thisActivity.get()).showNoticeDialog();
            }
        }
    }

}
