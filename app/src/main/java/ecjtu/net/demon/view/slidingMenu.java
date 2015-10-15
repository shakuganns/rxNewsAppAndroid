package ecjtu.net.demon.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import ecjtu.net.demon.R;

/**
 * Created by homker on 2015/1/14.
 */
public class slidingMenu extends HorizontalScrollView {
    private LinearLayout mWapper;
    private ViewGroup mMenu, mContent;
    private int mScreenWidth, mMenuWidth, mMenuRightPadding = 50;

    private boolean flag, isOpen = false;

    private float scrollX1, scrollX2;

    public slidingMenu(Context context) {
        this(context, null);
    }

    public slidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public slidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.slidingMenu, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.slidingMenu_rightPadding:
                    mMenuRightPadding = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();

        WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!flag) {
            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;
            flag = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(mMenuWidth, 0);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                scrollX1 = ev.getX();
                break;
            case MotionEvent.ACTION_UP:
                scrollX2 = ev.getX();
                int scrollx = getScrollX();
                if (scrollx <= (mMenuWidth * 1 / 3) || scrollx > mMenuWidth) {
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                }
                if (scrollx >= mMenuWidth * 2 / 3 && scrollx < mMenuWidth) {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                }
                if ((mMenuWidth / 3) < scrollx && scrollx < (mMenuWidth * 2 / 3)) {
                    if (scrollX1 < scrollX2) {
                        this.smoothScrollTo(0, 0);
                        isOpen = true;
                    } else {
                        this.smoothScrollTo(mMenuWidth, 0);
                        isOpen = false;
                    }
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    public void openMenu() {
        if (isOpen) return;
        else {
            this.smoothScrollTo(0, 0);
            isOpen = true;
        }
    }

    public void closeMenu() {
        if (!isOpen) return;
        else {
            this.smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        }
    }

    public void toggleMenu() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }


}
