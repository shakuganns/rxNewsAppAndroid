package ecjtu.net.demon.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.NewMain;
import ecjtu.net.demon.utils.UserEntity;
import ecjtu.net.demon.view.CycleImageView;

/**
 * Created by Shakugan on 15/12/5.
 */
public class SettingListAdapter extends BaseExpandableListAdapter {

    private String[][] title = {{"头像"},{"昵称","账号","密码"},{"意见反馈","检查更新","关于我们"},{"退出登录"}};
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private UserEntity userEntity;
    private Context context;

    public SettingListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;

        userEntity = NewMain.userEntity;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.userimage)
                .showImageOnFail(R.drawable.userimage)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    @Override
    public int getGroupCount() {
        return title.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return title[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return title[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return title[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.list_setting_group,null);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (groupPosition == 0 && childPosition == 0) {
                convertView = inflater.inflate(R.layout.list_setting_headitem,null);
            } else {
                convertView = inflater.inflate(R.layout.list_setting_item, null);
            }
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(this.title[groupPosition][childPosition]);
        switch (groupPosition) {
            case 0: {
                CycleImageView imageView = (CycleImageView) convertView.findViewById(R.id.head_image);
                ImageLoader.getInstance().displayImage("http://"+userEntity.getHeadImagePath(),imageView,options);
                break;
            }
            case 1: {
                TextView textView = (TextView) convertView.findViewById(R.id.info);
                switch (childPosition) {
                    case 0: {
                        textView.setText(userEntity.getUserName());
                        break;
                    }
                    case 1: {
                        textView.setText(userEntity.getStudentID());
                        break;
                    }
                    case 2: {
                        break;
                    }
                }
                break;
            }
            case 2: {
                TextView textView = (TextView) convertView.findViewById(R.id.info);
                switch (childPosition) {
                    case 0: {
                        break;
                    }
                    case 1: {
                        try {
                            textView.setText(getVersionName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case 2: {
                        break;
                    }
                }
                break;
            }
            case 3: {
                TextView textView = (TextView) convertView.findViewById(R.id.title);
                textView.setText(this.title[groupPosition][childPosition]);
                break;
            }
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }
}
