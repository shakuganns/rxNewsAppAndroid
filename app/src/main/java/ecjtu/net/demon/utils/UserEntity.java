package ecjtu.net.demon.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.Serializable;

import ecjtu.net.demon.activitys.LoginActivity;


/**
 * Created by homker on 2015/1/19.
 */
public class UserEntity implements Serializable {
    private static final long serialVersionUID = -5683263669918171030L;

    private String userName;  //用户名
    private String studentID; //学号
    private String password;  //密码
    private String headImagePath; //头像图片
    private String sex;
    private String token ; //token

    public UserEntity() {
    }

    public void updataToken() {
        Log.i("tag",studentID+"&"+password);
        HttpHelper.password = password;
        UserLoginTask mAuthTask = new UserLoginTask(studentID,password);
        mAuthTask.execute();
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String student_id) {
        this.studentID = student_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadImagePath() {
        return headImagePath;
    }

    public void setHeadImagePath(String headImagePath) {
        this.headImagePath = headImagePath;
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i("TAG","updata token!");
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

        @Override
        protected void onPostExecute(final Boolean success) {


            if (success) {

            } else {

            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}

