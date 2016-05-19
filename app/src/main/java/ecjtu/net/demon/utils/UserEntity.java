package ecjtu.net.demon.utils;

import android.util.Log;

import java.io.Serializable;


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
        AsyncTask<Void,Void,Boolean> task = new AsyncTask<>();
        task.setOnDoInBackgroundListener(new AsyncTask.OnDoInBackgroundListener<Boolean>() {
            @Override
            public Boolean onDoInBackground() {
                return HttpHelper.getInstance().getUserToken(studentID,password);
            }
        });
        task.execute();
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

}

