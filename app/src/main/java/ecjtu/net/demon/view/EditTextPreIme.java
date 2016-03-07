package ecjtu.net.demon.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class EditTextPreIme extends EditText {
    public EditTextPreIme(Context context) {
        super(context);
    }

    public EditTextPreIme(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public EditTextPreIme(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            ((Activity)this.getContext()).onKeyDown(KeyEvent.KEYCODE_BACK, event);
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}