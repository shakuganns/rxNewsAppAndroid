package ecjtu.net.demon.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ecjtu.net.demon.R;
import ecjtu.net.demon.view.CycleImageView;

/**
 * Created by Shakugan on 15/12/5.
 */
public class ThemeListAdapter extends BaseAdapter {

    private String[] themeList = {"早苗绿","少女粉","姨妈红","咸蛋黄","胖次蓝","基佬紫"};
    private LayoutInflater inflater;

    public ThemeListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return themeList.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_theme_item, null);
            textView = (TextView) convertView.findViewById(R.id.themeText);
            CycleImageView imageView = (CycleImageView) convertView.findViewById(R.id.themeColor);
            textView.setText(themeList[position]);
        } else {
            textView = (TextView) convertView.findViewById(R.id.themeText);
            textView.setText(themeList[position]);
        }
        return convertView;
    }

}
