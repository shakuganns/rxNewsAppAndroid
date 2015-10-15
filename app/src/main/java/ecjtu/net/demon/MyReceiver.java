package ecjtu.net.demon;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.jpush.android.api.JPushInterface;
import ecjtu.net.demon.activitys.ContentWebView;

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "tag";
    private String url;

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
//            receivingNotification(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

            System.out.println("用户点击打开了通知");
            Log.i("tag","==================="+bundle.getString(JPushInterface.EXTRA_ALERT));
            String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
            JSONTokener jsonTokener = new JSONTokener(json);
            try {
                JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                String articleID = jsonObject.getString("id");
                url = "http://app.ecjtu.net/api/v1/article/" + articleID + "/view";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("tag",json);
            // 在这里可以自己写代码去定义用户点击后的行为
            Intent i = new Intent(context, ContentWebView.class);  //自定义打开的界面
            i.putExtra("url", url);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtras(bundle);
            context.startActivity(i);

        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        // 使用notification
        // 使用广播或者通知进行内容的显示
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        builder.setContentText(message).setSmallIcon(R.drawable.notify_icon).setContentTitle(JPushInterface.EXTRA_TITLE);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        manager.notify(1, builder.build());
    }


}
