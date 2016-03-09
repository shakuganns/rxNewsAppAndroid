package ecjtu.net.demon.utils;

import android.os.AsyncTask;
import android.util.Log;

import ecjtu.net.demon.activitys.LoginActivity;

/**
 * Created by Shakugan on 16/3/9.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mEmail;
    private final String mPassword;

    UserLoginTask(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Log.i("TAG", "updata token!");
        HttpHelper httpHelper = new HttpHelper();
        String token = httpHelper.passwordcheck(mEmail, mPassword, LoginActivity.url);
        if (token != null) {
            Log.i("TAG","updata token successed!");
            UserEntity userEntity = httpHelper.getUserContent(mEmail, token, LoginActivity.url);
            SharedPreUtil.getInstance().putUser(userEntity);
            return true;
        } else {
            Log.i("TAG","updata token failed!");
            return false;
        }
    }

}