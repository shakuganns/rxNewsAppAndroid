package ecjtu.net.demon.utils;

import android.view.View;

/**
 * Created by shakugan on 15/11/3.
 */
public abstract class rxOnClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        upload(v);
        rxClick(v);
    }

    public void upload(View v) {
        v.getId();
    }

    public abstract void rxClick(View v);
}
