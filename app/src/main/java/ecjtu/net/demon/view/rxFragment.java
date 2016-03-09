package ecjtu.net.demon.view;

import android.support.v4.app.Fragment;

/**
 *
 * 提供让主页viewpager更新界面的回调
 * Created by Shakugan on 16/3/9.
 */
public class rxFragment extends Fragment {

    protected OnDataChangeListener listener;

    public void setOnDataChangeListener(OnDataChangeListener listener) {
        this.listener = listener;
    }

    public interface OnDataChangeListener {
        void onChange();
    }

}
