package ecjtu.net.demon.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import ecjtu.net.demon.R;
import ecjtu.net.demon.activitys.Show_image_Activity;
import ecjtu.net.demon.view.CycleImageView;

/**
 * Created by homker on 2015/5/21.
 * 日新网新闻客户端
 */
public class tushuShowCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEAD = 1;
    public static Drawable headImage;
    private ArrayList<ArrayMap<String, Object>> content = new ArrayList<>();
    public ArrayList<String> headInfo;
    private Context context;
    private RecyclerView.ViewHolder holder;
    private boolean isHeadSet = false;
    private LayoutInflater layoutInflater;
    private DisplayImageOptions options;

    public static int position = 0;

    public tushuShowCardAdapter(Context context) {
        this.context = context;
        headInfo = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
//        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
//                .createDefault(context);
//        //Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(configuration);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.thumb_default)
                .showImageOnFail(R.drawable.thumb_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }

    public void notifyDataChanged() {
        content.remove(0);
        super.notifyDataSetChanged();
    }

    public ArrayList<ArrayMap<String, Object>> getContent() {
        return content;
    }
    
    public void setContent(ArrayList<ArrayMap<String, Object>> content) {
        this.content = content;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == TYPE_HEAD) {
            holder = new HeadViewHolder(layoutInflater.inflate(R.layout.profile_head_view, viewGroup, false));
            return holder;
        }
        return new NormalTextViewHolder(layoutInflater.inflate(R.layout.show_card_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalTextViewHolder) {
            ((NormalTextViewHolder) holder).position = position;
            ((NormalTextViewHolder) holder).image.setImageResource(R.drawable.thumb_default);
            ImageLoader.getInstance().displayImage((String) content.get(position - 1).get("url"), ((NormalTextViewHolder) holder).image, options);
        } else {
            if (!isHeadSet) {
                isHeadSet = true;
                if (Build.VERSION.SDK_INT > 16) {
                    BitmapDrawable bd = (BitmapDrawable) headImage;
                    Bitmap bitmap = bd.getBitmap();
                    bitmap = blur(bitmap,((HeadViewHolder) holder).bg,20);
                    Drawable drawable = new BitmapDrawable(bitmap);
                    ((HeadViewHolder) holder).bg.setImageDrawable(drawable);
                }
                else ((HeadViewHolder) holder).bg.setImageDrawable(headImage);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Bitmap blur(Bitmap bkg, View view, float radius) {
        Bitmap overlay = Bitmap.createBitmap(bkg.getWidth(), bkg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.drawBitmap(bkg, -view.getLeft(), -view.getTop(), null);
        RenderScript rs = RenderScript.create(context);
        Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, overlayAlloc.getElement());
        blur.setInput(overlayAlloc);
        blur.setRadius(radius);
        blur.forEach(overlayAlloc);
        overlayAlloc.copyTo(overlay);
        rs.destroy();
        return overlay;
    }

    @Override
    public int getItemCount() {
        return content == null ? 0 : content.size() + 1;
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        private TextView author;
        private CycleImageView userImage;
        private ImageView bg;
        private TextView click;
        private TextView count;
        private TextView title;

        public HeadViewHolder(View view) {
            super(view);
            author = (TextView) itemView.findViewById(R.id.author);
            click = (TextView) itemView.findViewById(R.id.click);
            count = (TextView) itemView.findViewById(R.id.count);
            title = (TextView) itemView.findViewById(R.id.title);
            bg = (ImageView) itemView.findViewById(R.id.show_card_headbg);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tushuShowCardAdapter.position = 0;
                    Intent intent = new Intent();
                    intent.setClass(itemView.getContext(), Show_image_Activity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    public void setHeadText() {
        ((HeadViewHolder)holder).title.setText(headInfo.get(0));
        ((HeadViewHolder)holder).author.setText(headInfo.get(1));
        ((HeadViewHolder)holder).count.setText(headInfo.get(2));
        ((HeadViewHolder)holder).click.setText(headInfo.get(3));
    }


    public class NormalTextViewHolder extends RecyclerView.ViewHolder {


        private ImageView image;
        private int position;

        public NormalTextViewHolder(final View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.tushuo_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tushuShowCardAdapter.position = position;
                    Intent intent = new Intent();
                    intent.setClass(itemView.getContext(), Show_image_Activity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
