package ecjtu.net.demon.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;


import ecjtu.net.demon.R;
import ecjtu.net.demon.fragment.Show_image_ActivityFragment;
import ecjtu.net.demon.view.rxMutipleTouchViewPager;


public class Show_image_Activity extends BaseActivity {

    private CoordinatorLayout layout;
    private File file;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_);
        layout = (CoordinatorLayout) findViewById(R.id.main_container);
        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(getApplicationContext().getExternalFilesDir("share") + "/" + "share.jpg")));
        initActionBar();
        getSupportActionBar().setTitle("图说");
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setBackgroundColor(Color.parseColor("#000000"));
        layout.setStatusBarBackgroundColor(Color.parseColor("#000000"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_image_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_share){
            Log.i("tag","share_____");
            fetchImage();
            startActivity(Intent.createChooser(intent, "请选择"));
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchImage() {
        new Thread() {
            public void run() {
                try {
                    file = new File(Show_image_ActivityFragment.uri[((rxMutipleTouchViewPager)findViewById(R.id.tushuo_viewpager)).getCurrentItem()]);
                    InputStream in = new FileInputStream(file);
                    readAsFile(in, new File(getApplicationContext().getExternalFilesDir("share") + "/" + "share.jpg"));
                    Log.i("tag", getApplicationContext().getExternalFilesDir("share") + "/" + "share.jpg");
                    Log.i("tag", "获取成功！！！！！！！！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void readAsFile(InputStream inSream, File file) throws Exception{
        FileOutputStream outStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len = -1;
        while( (len = inSream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inSream.close();
    }
}

