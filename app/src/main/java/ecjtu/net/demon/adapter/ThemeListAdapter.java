package ecjtu.net.demon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.BaseActivity;

/**
 * Created by Shakugan on 15/12/5.
 */
public class ThemeListAdapter extends BaseAdapter {

    private String[] themeList = {"早苗绿","可乐黑","姨妈红"};
    private int[] colorDrawble = {R.drawable.circle_green,R.drawable.circle_dark,R.drawable.circle_red};
    private int[] colors = {R.color.md_teal_500,R.color.md_grey_800,R.color.md_red_700};
    private LayoutInflater inflater;
    private int visibilityPosition;

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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_theme_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.themeText);
        if (BaseActivity.themeID == position) {
            TextView mark = (TextView) convertView.findViewById(R.id.mark);
            mark.setVisibility(View.VISIBLE);
            visibilityPosition = position;
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.themeColor);
        imageView.setBackgroundResource(colorDrawble[position]);
        textView.setText(themeList[position]);
        textView.setTextColor(convertView.getResources().getColor(colors[position],null));
        return convertView;
    }


    public int getVisMarkPos() {
        return visibilityPosition;
    }

    public void setVisMarkPos(int visibilityPosition) {
        this.visibilityPosition = visibilityPosition;
    }


}
