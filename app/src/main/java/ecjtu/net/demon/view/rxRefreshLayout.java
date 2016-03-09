package ecjtu.net.demon.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


/**
 * Created by 圣麟 on 2015/8/12.
 */
public class rxRefreshLayout extends SwipeRefreshLayout {

//    private static final int DEFAULT_CHILD_GRAVITY = Gravity.TOP | Gravity.START;

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

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutChildren();
    }

    void layoutChildren() {
        final int count = getChildCount();


        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child instanceof ImageView) {
                continue;
            }
            if (child.getVisibility() != GONE) {

                final int width = child.getMeasuredWidth();
                final int height = child.getMeasuredHeight();


                int childLeft = 0;
                int childTop = 0;


                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
    }

//    public class LayoutParams extends FrameLayout.LayoutParams {
//
//        public LayoutParams(Context c, AttributeSet attrs) {
//            super(c, attrs);
//        }
//    }

}
