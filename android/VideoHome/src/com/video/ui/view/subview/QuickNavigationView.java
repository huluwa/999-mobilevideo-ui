package com.video.ui.view.subview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LinearFrame;

import java.util.ArrayList;

/**
 * Created by liuhuadong on 11/17/14.
 */
public class QuickNavigationView extends RelativeLayout implements DimensHelper {
    public QuickNavigationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int []draws = {
        R.drawable.quick_entry_tv_series_bg,
        R.drawable.quick_entry_film_bg,
        R.drawable.quick_entry_variety_bg,
        R.drawable.quick_entry_all_bg
    };
    public QuickNavigationView(Context context, ArrayList<DisplayItem> items) {
        this(context, null, 0);

        View v = View.inflate(getContext(), R.layout.quick_navigation, this);
        LinearFrame mMetroLayout = (LinearFrame)v.findViewById(R.id.metrolayout);

        int width = getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_width);
        int padding = (getDimens().width-4*width)/3;
        int leftPadding = 0;
        for (int i=0;i<items.size();i++) {
            DisplayItem item = items.get(i);
            final TextView tv = (TextView) View.inflate(getContext(), R.layout.qucik_entry_textview, null);
            tv.setText(item.title);
            tv.setBackgroundResource(draws[i%4]);

            Target topDrawable = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    if(bitmap != null){

                        DisplayMetrics metrics = getResources().getDisplayMetrics();
                        Bitmap resizedBitmap = bitmap;
                        if(Math.abs(metrics.scaledDensity-3.0f) > 0.1) {

                            int width  = bitmap.getWidth();
                            int height = bitmap.getHeight();

                            float scaleWidth = metrics.scaledDensity / 3.0f;
                            float scaleHeight = metrics.scaledDensity / 3.0f;

                            // create a matrix for the manipulation
                            Matrix matrix = new Matrix();
                            // resize the bit map
                            matrix.postScale(scaleWidth, scaleHeight);

                            // recreate the new Bitmap
                            resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                        }

                        BitmapDrawable image = new BitmapDrawable(getResources(), resizedBitmap);
                        int h = image.getIntrinsicHeight();
                        int w = image.getIntrinsicWidth();
                        image.setBounds( 0, 0, w, h );
                        tv.setCompoundDrawables(null, image, null, null);
                    }
                }

                @Override public void onBitmapFailed(Drawable drawable) {}
                @Override public void onPrepareLoad(Drawable drawable) {}
            };

            mMetroLayout.addItemView(tv,width , getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_height), leftPadding, padding);
            Picasso.with(getContext()).load(item.images.icon().url).into(topDrawable);
        }
    }

    private static Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.media_banner_width);
            mDimens.height = getResources().getDimensionPixelSize(R.dimen.quick_entry_channel_height);
        }
        return mDimens;
    }
}
