package ecjtu.net.demon.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 佳诚 on 2015/3/18.
 */
public class rxViewPager extends ViewPager {
    public rxViewPager(Context context) {
        this(context, null);
    }

    public rxViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        getParent().requestDisallowInterceptTouchEvent(true);// 父级元素不阻拦touch时间
        return super.dispatchTouchEvent(ev);
    }

}
