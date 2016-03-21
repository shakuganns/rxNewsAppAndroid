package ecjtu.net.demon.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by homker on 2015/1/24.
 */
public class HttpHelper {

    private String result = null;
    private URL url1 = null;
    private HttpURLConnection connection = null;
    private InputStreamReader in = null;
    public static String password;
    private HttpURLConnection conn;

    public HttpHelper() {

    }

    /**
     * 系统级别的get调用 不建议使用
     *
     * @return String result
     */
    public String get(String url) {
        try {
            url1 = new URL(url);
            connection = (HttpURLConnection) url1.openConnection();
            in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            result = stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * 系统级别的post调用 不建议使用
     *
     * @param datas
     * @return
     */
    public String Android_post(ArrayList<HashMap<String, String>> datas,String url) {
        try {
            url1 = new URL(url);
            connection = (HttpURLConnection) url1.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Charset", "utf-8");
            connection.setRequestProperty("Connection", "Keep-Alive");
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            for (HashMap<String, String> hashMap : datas) {
                String key = hashMap.get("key");
                String value = hashMap.get("value");
                dataOutputStream.writeBytes(key + "=" + value);
            }
            dataOutputStream.writeBytes("token=rx");
            dataOutputStream.flush();
            dataOutputStream.close();
            in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            result = stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public String HttpPost(String username,String password,String s_url) {
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(s_url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            conn.setDoOutput(true);// 是否输入参数
            conn.setDoInput(true);

            StringBuffer params = new StringBuffer();
            // 表单参数与get形式一样
            params.append("username").append("=").append(username).append("&")
                    .append("password").append("=").append(password).append("&")
                    .append("token").append("=").append("homker");
            byte[] bypes = params.toString().getBytes();
            conn.getOutputStream().write(bypes);// 输入参数
            InputStream inStream=conn.getInputStream();
//            System.out.println(new String(StreamTool.readInputStream(inStream), "gbk"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    inStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (MalformedURLException e) {

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return result.toString();
    }

    /**
     * 账户密码检查
     *
     * @param userName
     * @param passWord
     * @return
     */
    public String passwordcheck(String userName, String passWord,String url) {
        String token = null;
        boolean flag = false;
        url = url+"login";
        String result = HttpPost(userName,passWord,url);
        try {
            JSONObject jsonObject = new JSONObject(result);
            token = jsonObject.getString("token");
            flag = jsonObject.getBoolean("result");

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (flag) {
            return token;
        } else {
            return null;
        }
    }

    /**
     * 获取用户信息
     *
     * @param studentID
     * @return
     */
    public UserEntity getUserContent(String studentID,String token,String url) {
        Log.i("tag", "getUserContent works");
        JSONObject person = null;
        boolean status = false;
        UserEntity userEntity = new UserEntity();
        url = url + "user/" + studentID + "?token="+token;
        String result = null;
        try {
            result = OkHttp.getSync(url).body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result != null){
            try {
                JSONTokener jsonTokener = new JSONTokener(result);
                JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                Log.i("TAG",jsonObject.toString());
                status = jsonObject.getBoolean("result");
                person = jsonObject.getJSONObject("user");
                userEntity.setStudentID(person.getString("student_id"));
                userEntity.setToken(token);
                userEntity.setPassword(password);
                userEntity.setHeadImagePath("avatar");
                userEntity.setUserName(person.getString("Name"));
                userEntity.setSex(person.getString("Sex"));
                userEntity.setHeadImagePath(person.getString("avatar"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return userEntity;
        }else{
            return null;
        }
    }
}
