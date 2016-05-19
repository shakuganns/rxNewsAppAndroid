package ecjtu.net.demon.utils;

/**
 * Created by Shakugan on 16/5/19.
 */
public class AsyncTask<Params, Progress, Result> extends android.os.AsyncTask<Params, Progress, Result> {

    private OnPostExecuteListener<Result> onPostExecuteListener;
    private OnDoInBackgroundListener<Result> onDoInBackgroundListener;
    private OnCancelledListener onCancelledListener;

    @Override
    protected Result doInBackground(Object[] params) {
        return onDoInBackgroundListener.onDoInBackground();
    }

    @Override
    protected void onPostExecute(Result o) {
        onPostExecuteListener.onPostExecute(o);
    }

    @Override
    protected void onCancelled() {
        onCancelledListener.onCancelled();
    }

    public void setOnPostExecuteListener(OnPostExecuteListener<Result> onPostExecuteListener) {
        this.onPostExecuteListener = onPostExecuteListener;
    }

    public void setOnDoInBackgroundListener(OnDoInBackgroundListener<Result> onDoInBackgroundListener) {
        this.onDoInBackgroundListener = onDoInBackgroundListener;
    }

    public void setOnCancelledListener(OnCancelledListener onCancelledListener) {
        this.onCancelledListener = onCancelledListener;
    }

    public interface OnPostExecuteListener<Result> {
        void onPostExecute(Result result);
    }

    public interface OnDoInBackgroundListener<Result> {
        Result onDoInBackground();
    }

    public interface OnCancelledListener {
        void onCancelled();
    }

}
