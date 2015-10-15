package ecjtu.net.demon.view;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ecjtu.net.demon.R;

import static android.os.Build.VERSION_CODES.JELLY_BEAN;

/**
 * Created by homker on 2015/1/15.
 */
public class topBar extends RelativeLayout {

    private ImageButton leftButton, rightButton;
    private TextView tvtitle;


    private Drawable leftButtonBackground, rightButtonBackground;

    private int leftButtonImage, rightButtonImage;


    private float titleTextSize;
    private int titleTextColor;
    private String titleText;

    private LayoutParams leftParams, rightParams, titleParams;

    private topBarClickListener listener;

    public topBar(Context context) {
        this(context, null);
    }


    @TargetApi(JELLY_BEAN)
    public topBar(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.topBar);
        leftButtonBackground = ta.getDrawable(R.styleable.topBar_leftButtonBackground);
        rightButtonBackground = ta.getDrawable(R.styleable.topBar_rightButtonBackground);
        titleText = ta.getString(R.styleable.topBar_titleText);
        titleTextSize = ta.getDimension(R.styleable.topBar_titleTextSize, 0);
        titleTextColor = ta.getColor(R.styleable.topBar_titleTextColor, 0);

        ta.recycle();

        leftButton = new ImageButton(context);
        rightButton = new ImageButton(context);
        tvtitle = new TextView(context);

        if (Build.VERSION.SDK_INT > 15) {
            //使用新的API

            leftButton.setBackground(leftButtonBackground);
            rightButton.setBackground(rightButtonBackground);
        } else {
            //使用旧的API

            leftButton.setBackgroundDrawable(leftButtonBackground);
            rightButton.setBackgroundDrawable(rightButtonBackground);
        }


        tvtitle.setText(titleText);
        tvtitle.setTextColor(titleTextColor);
        tvtitle.setTextSize(titleTextSize);
        tvtitle.setGravity(Gravity.CENTER);


        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        leftParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);

        addView(leftButton, leftParams);

        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        rightParams.addRule(RelativeLayout.CENTER_VERTICAL);

        addView(rightButton, rightParams);

        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        titleParams.addRule(RelativeLayout.CENTER_VERTICAL);

        addView(tvtitle, titleParams);

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tag", String.valueOf(topBar.this.listener));
                topBar.this.listener.leftClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topBar.this.listener.rightClick();
            }
        });


    }

    public void setOntopBarClickListener(topBarClickListener lister) {
        Log.i("tag", String.valueOf(lister));
        this.listener = lister;
        Log.i("tag", "setOn" + String.valueOf(listener));
    }

    public interface topBarClickListener {
        public void leftClick();

        public void rightClick();
    }
}
