package ecjtu.net.demon.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by shakugan on 15/10/24.
 */
public class rxMutipleTouchViewPager extends ViewPager {

    public rxMutipleTouchViewPager(Context context) {
        super(context);
    }

    public rxMutipleTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    private boolean mIsDisallowIntercept = false;
//    @Override
//    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//        // keep the info about if the innerViews do
//        // requestDisallowInterceptTouchEvent
//        mIsDisallowIntercept = disallowIntercept;
//        super.requestDisallowInterceptTouchEvent(disallowIntercept);
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        // the incorrect array size will only happen in the multi-touch
//        // scenario.
//        if (ev.getPointerCount() > 1 && mIsDisallowIntercept) {
//            requestDisallowInterceptTouchEvent(false);
//            boolean handled = super.dispatchTouchEvent(ev);
//            requestDisallowInterceptTouchEvent(true);
//            return handled;
//        } else {
//            return super.dispatchTouchEvent(ev);
//        }
//    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            super.onInterceptTouchEvent(ev);
        } catch(IllegalArgumentException ex) {
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try{
            super.onTouchEvent(ev);
        } catch(IllegalArgumentException ex) {
        }
        return false;
    }
}
