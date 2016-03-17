package ecjtu.net.demon.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import ecjtu.net.demon.activitys.NewMain;


/**
 * Created by 圣麟 on 2015/8/12.
 */
public class rxRefreshLayout extends SwipeRefreshLayout {

    /**
     * @param context
     */
    public rxRefreshLayout(Context context) {
        this(context, null);
    }

    public rxRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * appbar隐藏时不可刷新
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                if (NewMain.appBarVerticalOffset == 0) {
                    setEnabled(true);
                } else {
                    setEnabled(false);
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

}
