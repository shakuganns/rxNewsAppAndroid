package ecjtu.net.demon.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Created by homker on 2015/4/11.
 */
public enum  ToastMsg {
    builder;
    private View view;
    private Toast toast;
    public void init(Context context){
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = Toast.makeText(context,"",Toast.LENGTH_SHORT).getView();
        toast = new Toast(context);
        toast.setView(view);
    }

    public void display(CharSequence str, int duration){
        toast.setText(str);
        toast.setDuration(duration);
        toast.show();
    }

    public void display(int ResourceId , int duration){
        toast.setText(ResourceId);
        toast.setDuration(duration);
        toast.show();
    }
}
