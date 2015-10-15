package ecjtu.net.demon.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by homker on 2015/1/24.
 */
public class httpDownloadThread extends Thread {

    private String myurl;

    public httpDownloadThread(String url) {
        this.myurl = url;
    }


    @Override
    public void run() {
        try {
            URL httpurl = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) httpurl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            InputStream in = connection.getInputStream();
            File downloadfile;
            File savefile;
            FileOutputStream out = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                downloadfile = Environment.getExternalStorageDirectory();
                savefile = new File(downloadfile, "test.apk");
                out = new FileOutputStream(savefile);
            }

            byte[] bytes = new byte[6 * 1024];
            int len;

            while ((len = in.read(bytes)) != -1) {
                if (out != null) {
                    out.write(bytes, 0, len);
                }
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
