package ecjtu.net.demon.activitys;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ecjtu.net.demon.R;
import ecjtu.net.demon.view.CutImageView;

/**
 * Created by shakugan on 15/9/15.
 */
public class CutImageActivity extends AppCompatActivity {

    private CutImageView cutImageView;
    private ImageView imageView;
    private Button btnClip;
    private Toolbar toolbar;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutimage);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("裁剪头像");
        cutImageView = (CutImageView) findViewById(R.id.cutimageview);
        imageView = (ImageView) findViewById(R.id.headimage);
        btnClip = (Button) findViewById(R.id.btn_clip);
        if (getIntent().getStringExtra("data") != null) {
            Uri mUri = Uri.parse(getIntent().getStringExtra("data"));
            cutImageView.setImageBitmap(getDiskBitmap(getRealFilePath(this, mUri)));
        } else {
            cutImageView.setImageBitmap(
                    drawableToBitmap(Drawable.createFromPath(getApplicationContext()
                            .getExternalFilesDir("headImage") + "/" + getIntent().getStringExtra("string")+ "big" + ".png")));
        }
        btnClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveFile(cutImageView.clipBitmap());
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
        bitmap.recycle();
        bitmap = null;
    }

    public void saveFile(Bitmap bm) throws IOException {
        File myCaptureFile = new File(getApplicationContext()
                .getExternalFilesDir("headImage") + "/" + getIntent().getStringExtra("string") + ".png");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
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

    private Bitmap getDiskBitmap(String pathString)
    {
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
}
