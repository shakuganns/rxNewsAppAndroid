package ecjtu.net.demon.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by homker on 2015/1/24.
 */
public class HttpHelper {
    public static HttpClient customerHttpClient;
    private String result = null;
    private URL url1 = null;
    private HttpURLConnection connection = null;
    private InputStreamReader in = null;
    private ACache newsListCache;
    public static String password;

    public HttpHelper() {

    }

    public static synchronized HttpClient getHttpClient() {
        if (null== customerHttpClient) {
            HttpParams params =new BasicHttpParams();
            // 设置一些基本参数
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params,"utf-8");
            HttpProtocolParams.setUseExpectContinue(params, true);
            HttpProtocolParams
                    .setUserAgent(
                            params,
                            "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                                    +"AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
            // 超时设置
/* 从连接池中取连接的超时时间 */
            ConnManagerParams.setTimeout(params, 2000);
            /* 连接超时 */
            HttpConnectionParams.setConnectionTimeout(params, 4000);
            /* 请求超时 */
            HttpConnectionParams.setSoTimeout(params, 6000);

            // 设置我们的HttpClient支持HTTP和HTTPS两种模式
            SchemeRegistry schReg =new SchemeRegistry();
            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            // 使用线程安全的连接管理来创建HttpClient
            ClientConnectionManager conMgr =new ThreadSafeClientConnManager(
                    params, schReg);
            customerHttpClient =new DefaultHttpClient(conMgr, params);
        }
        return customerHttpClient;
    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    public static int getVersionCode(Context context) throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        //是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packInfo.versionCode;
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

    /**
     * Apache 封装的get调用，建议使用
     *
     * @return String result
     */
    public String apacheGet(String url) {
        String result = null;
        BufferedReader reader = null;
        try {
            HttpClient client = getHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));

            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * Apache 封装的post调用，建议调用
     *
     * @param datas
     * @return
     */
    public String apachePost(ArrayList<HashMap<String, String>> datas,String url) {
        String result = null;
        BufferedReader reader = null;
        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            for (HashMap<String, String> hashMap : datas) {
                String key = hashMap.get("key");
                String value = hashMap.get("value");
                postParameters.add(new BasicNameValuePair(key, value));
            }
            postParameters.add(new BasicNameValuePair("token", "homker"));

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                    postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            reader = new BufferedReader(new InputStreamReader(response
                    .getEntity().getContent()));

            StringBuffer strBuffer = new StringBuffer("");
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
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
        ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("key", "username");
        hashMap.put("value", userName);
        HashMap<String, String> hashMap1 = new HashMap<String, String>();
        hashMap1.put("key", "password");
        hashMap1.put("value", passWord);
        datas.add(hashMap);
        datas.add(hashMap1);
        url = url+"login";
        String result = apachePost(datas,url);
        if (result == null){
            result = Android_post(datas,url);
        }
        try {
            JSONTokener jsonTokener = new JSONTokener(result);
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
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
     * 获取最新的版本号
     * @param url
     * @return
     */
    public HashMap<String,Object> getVersion(String url){
        HashMap<String,Object> version = new HashMap<String,Object>();
        String result = apacheGet(url);
        if(result != null){
            try {
                JSONTokener jsonTokener = new JSONTokener(result);
                JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                version.put("versionName",jsonObject.getString("version_name"));
                version.put("versionCode",jsonObject.getInt("version_code"));
                version.put("md5",jsonObject.getString("md5"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return version;
        }else{
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
        String result = apacheGet(url);
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

    /**
     * 获取新闻列表
     *
     * @return
     */
    public HashMap<String, Object> getNewsList(String url,String lastId,Context context,Boolean isInit) {
        HashMap<String, Object> list = new HashMap<String, Object>();
        int status;
        if(lastId != null){
            url = url + "?until=" + lastId;
        }
        newsListCache = ACache.get(context);
        JSONObject cache = newsListCache.getAsJSONObject("newsList");
        if(isInit&&cache != null){//判断是否是初始化
            Log.i("tag","我们使用了缓存~！");
            try {
                JSONObject slide_article = cache.getJSONObject("slide_article");
                JSONArray slide_articles = slide_article.getJSONArray("articles");
                JSONObject normal_article = cache.getJSONObject("normal_article");
                JSONArray normal_articles = normal_article.getJSONArray("articles");
                list.put("slide_articles",jsonArray2Arraylist(slide_articles));
                list.put("normal_articles",jsonArray2Arraylist(normal_articles));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }
        Log.i("tag","我们还是请求了");
        String result = apacheGet(url);
        if(result != null){
            try {
                JSONTokener jsonTokener = new JSONTokener(result);
                JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
                if (lastId == null){//只缓存最新的内容列表
                    newsListCache.remove("newsList");
                    newsListCache.put("newsList",jsonObject,7*ACache.TIME_DAY);
                }
                status = jsonObject.getInt("status");
                JSONObject slide_article = jsonObject.getJSONObject("slide_article");
                JSONArray slide_articles = slide_article.getJSONArray("articles");
                JSONObject normal_article = jsonObject.getJSONObject("normal_article");
                JSONArray normal_articles = normal_article.getJSONArray("articles");
                list.put("slide_articles", jsonArray2Arraylist(slide_articles));
                list.put("normal_articles", jsonArray2Arraylist(normal_articles));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }else{
            return null;
        }
    }

    /**
     * 将json数组变成arraylist
     * @param jsonArray
     * @return
     */
    private ArrayList<HashMap<String,Object>> jsonArray2Arraylist(JSONArray jsonArray){
        ArrayList<HashMap<String,Object>> arrayList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i< jsonArray.length(); i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                HashMap<String,Object> item = new HashMap<String,Object>();
                item.put("id",jsonObject.getInt("id"));
                item.put("title",jsonObject.getString("title"));
                item.put("updated_at",jsonObject.getString("updated_at"));
                item.put("info",jsonObject.getString("info"));
                String imageUrl = "http://app.ecjtu.net"+jsonObject.getString("thumb");
                item.put("thumb",imageUrl);
                arrayList.add(item);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    /**
     * 根据url请求图片并返回drawable
     *
     * @param imageUrl
     * @return
     */
    public Drawable getImage(String imageUrl) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), "image");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public HashMap<String, Object> getNewsContent(String articleID,String url) {
        HashMap<String, Object> reback = new HashMap<String, Object>();
        ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();
        int status;
        String title;
        String author;
        String dataTime;
        String tag;
        url = url + "?articleID=" + articleID;
        String result = apacheGet(url);
        Log.i("tag", "===================================" + url);
        Log.i("tag", "result:" + result);
        JSONTokener jsonTokener = new JSONTokener(result);
        try {
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            status = jsonObject.getInt("status");
            title = jsonObject.getString("title");
            author = jsonObject.getString("author");
            dataTime = jsonObject.getString("dataTime");
            tag = jsonObject.getString("tag");
            JSONArray article = jsonObject.getJSONArray("article");
            for (int i = 0; i < article.length(); i++) {
                JSONObject jsonObject1 = article.getJSONObject(i);
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("type", jsonObject1.getString("type"));
                hashMap.put("value", jsonObject1.getString("value"));
                datas.add(hashMap);
            }
            reback.put("status", status);
            reback.put("title", title);
            reback.put("author", author);
            reback.put("dataTime", dataTime);
            reback.put("tags", tag);
            reback.put("article", datas);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reback;
    }

    public HashMap<String,Object> getNewsListByAsync(){
        HashMap<String,Object> list = new HashMap<String,Object>();

        return list;
    }


}
