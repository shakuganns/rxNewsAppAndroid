package ecjtu.net.demon.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;


/**
 * Created by 圣麟 on 2015/8/12.
 */
public class rxRefreshLayout extends SwipeRefreshLayout {

    /**
     * 滑动距离及坐标
     */
    private float xDistance, yDistance, xLast, yLast;

    /**
     * @param context
     */
    public rxRefreshLayout(Context context) {
        this(context, null);
    }

    public rxRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                xDistance = yDistance = 0f;
//                xLast = ev.getX();
//                yLast = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float curX = ev.getX();
//                final float curY = ev.getY();
//
//                xDistance += Math.abs(curX - xLast);
//                yDistance += Math.abs(curY - yLast);
////                xLast = curX;
////                yLast = curY;
//
//                if (xDistance > yDistance) {
//                    return false;   //表示向下传递事件
//                }
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
}
