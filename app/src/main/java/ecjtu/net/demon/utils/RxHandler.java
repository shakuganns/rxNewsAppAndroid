package ecjtu.net.demon.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Shakugan on 16/5/18.
 */
public class RxHandler extends Handler {

    private OnHandleMessageListener listener;

    public RxHandler() {
    }

    @Override
    public void handleMessage(Message msg) {
        listener.onHanleMessage(msg);
    }

    public void setOnHandleMessageListener(OnHandleMessageListener listener) {
        this.listener = listener;
    }

    public interface OnHandleMessageListener {

        void onHanleMessage(Message msg);

    }

}
