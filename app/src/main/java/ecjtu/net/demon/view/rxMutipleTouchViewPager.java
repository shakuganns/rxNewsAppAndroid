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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean bl = false;
        try {
            bl = super.onInterceptTouchEvent(ev);
        } catch(IllegalArgumentException ex) {
        }
        return bl;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean bl = false;
        try{
            bl = super.onTouchEvent(ev);
        } catch(IllegalArgumentException ex) {
        }
        return bl;
    }
}
