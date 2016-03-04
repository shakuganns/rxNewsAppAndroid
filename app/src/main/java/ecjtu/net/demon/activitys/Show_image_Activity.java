package ecjtu.net.demon.activitys;

import android.annotation.TargetApi;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;


import ecjtu.net.demon.R;
import ecjtu.net.demon.fragment.Show_image_ActivityFragment;


public class Show_image_Activity extends BaseActivity {

    private ShareActionProvider mShareActionProvider;
    private File file;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentViewLayout(R.layout.activity_show_image_);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_);
        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(getApplicationContext().getExternalFilesDir("share") + "/" + "share.jpg")));
        initActionBar();
        getSupportActionBar().setTitle("图说");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_image_, menu);
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(shareItem);
        mShareActionProvider.setShareIntent(intent);
        mShareActionProvider.setOnShareTargetSelectedListener(new ShareActionProvider.OnShareTargetSelectedListener() {
            @Override
            public boolean onShareTargetSelected(ShareActionProvider shareActionProvider, Intent i) {
                fetchImage();
                Log.i("tag", "file://" + String.valueOf(Show_image_ActivityFragment.uri[Show_image_ActivityFragment.viewPager.getCurrentItem()]));
                return false;
            }
        });
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_item_share){
        }

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(upIntent)
                        .startActivities();
            } else {
                Log.i("tag", "nihao" + String.valueOf(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, upIntent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchImage() {
        new Thread() {
            public void run() {
                try {
                    file = new File(Show_image_ActivityFragment.uri[Show_image_ActivityFragment.viewPager.getCurrentItem()]);
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

    public static void readAsFile(InputStream inSream, File file) throws Exception{
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

