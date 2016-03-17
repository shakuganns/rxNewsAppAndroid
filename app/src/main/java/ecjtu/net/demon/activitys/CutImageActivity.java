package ecjtu.net.demon.activitys;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ecjtu.net.demon.R;
import ecjtu.net.demon.utils.ToastMsg;
import ecjtu.net.demon.view.CutImageView;

/**
 * Created by shakugan on 15/9/15.
 * 头像裁剪
 */
public class CutImageActivity extends NoGestureBaseActivity {

    private CutImageView cutImageView;
    private ImageView imageView;
    private Button btnClip;
    private Bitmap bitmap = null;
    private String mUri;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setContentViewLayout(R.layout.activity_cutimage);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutimage);

        initActionBar();
        getSupportActionBar().setTitle("裁剪头像");

        cutImageView = (CutImageView) findViewById(R.id.cutimageview);
        imageView = (ImageView) findViewById(R.id.headimage);
        btnClip = (Button) findViewById(R.id.btn_clip);
        if (getIntent().getStringExtra("data") != null) {
            //通过相册选择图片
            dialog = new ProgressDialog(CutImageActivity.this);
            dialog.setProgressStyle(R.attr.progressBarStyle);
            dialog.setMessage("加载中...");
            dialog.setIndeterminate(true);              //设置进度条是否为不明确
            dialog.setCancelable(false);                //设置进度条是否可以按退回键取消
            dialog.setCanceledOnTouchOutside(false);    //设置点击进度对话框外的区域对话框不消失
            dialog.show();
            LoadImageTask task = new LoadImageTask();
            task.execute();
//            mUri = Uri.parse(getIntent().getStringExtra("data")).toString();
////            ImageLoader.getInstance().displayImage(mUri,cutImageView,options);
//            ImageLoader.getInstance().loadImage(mUri, new ImageLoadingListener() {
//
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//                    dialog = new ProgressDialog(CutImageActivity.this);
//                    dialog.setProgressStyle(R.attr.progressBarStyle);
//                    dialog.setMessage("加载中...");
//                    dialog.setIndeterminate(true);              //设置进度条是否为不明确
//                    dialog.setCancelable(false);                //设置进度条是否可以按退回键取消
//                    dialog.setCanceledOnTouchOutside(false);    //设置点击进度对话框外的区域对话框不消失
//                    dialog.show();
//                }
//
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                    ToastMsg.builder.display("图片加载失败", 500);
//                    finish();
//                }
//
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    try {
//                        saveFileImageLoader(loadedImage);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Log.i("tag", "uri = " + mUri);
//                    cutImageView.setImageBitmap(getDiskBitmap(getRealFilePath(CutImageActivity.this, Uri.parse(getApplicationContext()
//                            .getExternalFilesDir("headImage") + "/" + getIntent().getStringExtra("string") + "2" + ".png"))));
//                    dialog.dismiss();
//                }
//
//                @Override
//                public void onLoadingCancelled(String imageUri, View view) {
//
//                }
//            });
        } else {
            //拍照取得的图片
            cutImageView.setImageBitmap(
                    drawableToBitmap(Drawable.createFromPath(getApplicationContext()
                            .getExternalFilesDir("headImage") + "/" + getIntent().getStringExtra("string")+ "big" + ".png")));
        }
        btnClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveFile(cutImageView.clipBitmap());
                    saveFileJPG(cutImageView.clipBitmap());
                    setResult(Activity.RESULT_OK);
                    onBackPressed();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 保存图片到本地
     */

    public void saveFile(Bitmap bm) throws IOException {
        File myCaptureFile = new File(getApplicationContext()
                .getExternalFilesDir("headImage") + "/" + getIntent().getStringExtra("string") + ".png");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
    }

    /**
     * 使用相册时把头像转存到指定目录
     * @param bm
     * @throws IOException
     */
    public void saveFileImageLoader(Bitmap bm) throws IOException {
        File myCaptureFile = new File(getApplicationContext()
                .getExternalFilesDir("headImage") + "/" + getIntent().getStringExtra("string")+ "2" + ".png");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
    }


    public void saveFileJPG(Bitmap bm) throws IOException {
        File myCaptureFile = new File(getApplicationContext()
                .getExternalFilesDir("headImage") + "/" + getIntent().getStringExtra("string") + ".jpg");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }


    public Bitmap drawableToBitmap(Drawable drawable) {
        bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * 对超出尺寸的图片进行压缩
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            return inSampleSize;
        }
        return inSampleSize;
    }

    private Bitmap getDiskBitmap(String pathString) {
        try
        {
            File file = new File(pathString);
            if(file.exists())
            {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                bitmap = BitmapFactory.decodeFile(pathString, options);
                options.inSampleSize = calculateInSampleSize(options, 500, 500);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFile(pathString, options);
            }
        } catch (Exception e)
        {
            // TODO: handle exception
        }
        return bitmap;
    }

    /**
     * 获取图片在手机里面的真实路径
     * @param context
     * @param uri
     * @return
     */

    public String getRealFilePath(Context context,Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private class LoadImageTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            mUri = Uri.parse(getIntent().getStringExtra("data")).toString();
            Bitmap loadedImage = ImageLoader.getInstance().loadImageSync(mUri);
            try {
                saveFileImageLoader(loadedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            loadedImage.recycle();
            Log.i("tag", "uri = " + mUri);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cutImageView.setImageBitmap(getDiskBitmap(getRealFilePath(CutImageActivity.this, Uri.parse(getApplicationContext()
                    .getExternalFilesDir("headImage") + "/" + getIntent().getStringExtra("string") + "2" + ".png"))));
            dialog.dismiss();
        }
    }
}
