package com.video.ui.view.subview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.R;
import com.video.ui.view.LayoutConstant;
import com.video.ui.view.LinearFrame;

/**
 * Created by liuhuadong on 11/27/14.
 */
public class GridMediaView extends LinearBaseCardView implements DimensHelper{
    public GridMediaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GridMediaView(Context context, Block<DisplayItem> block, Object tag){
        super(context, null, 0);
        setTag(R.integer.picasso_tag, tag);
        initUI(context, block, tag);
    }

    private int row_count = 0;
    private int padding;
    private int item_padding;
    private int width;
    private int height;
    private int res_id;

    private void initDimens(Block<DisplayItem> block){
        item_padding = getResources().getDimensionPixelSize(R.dimen.ITEM_DIVIDE_SIZE);
        if(block.ui_type.id == LayoutConstant.grid_media_port) {
            row_count = block.ui_type.row_count;
            if(row_count == 0) row_count = 3;

            res_id = R.layout.tab_media_port;
            width  = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_width);
            height = getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_height);
            padding = (getDimens().width - row_count*getResources().getDimensionPixelSize(R.dimen.channel_media_view_port_width))/(row_count+1);
        }else if(block.ui_type.id == LayoutConstant.grid_media_land) {
            row_count = block.ui_type.row_count;
            if (row_count == 0)row_count = 2;

            res_id = R.layout.tab_media_hor;
            width = getResources().getDimensionPixelSize(R.dimen.channel_media_view_width);
            height = getResources().getDimensionPixelSize(R.dimen.channel_media_view_height);
            padding = (getDimens().width - row_count * getResources().getDimensionPixelSize(R.dimen.channel_media_view_width)) / (row_count + 1);
        }
    }

    private void initUI(Context context, Block<DisplayItem> block, Object tag){
        initDimens(block);

        View v = View.inflate(getContext(), R.layout.quick_navigation, null);
        LayoutParams flp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(v, flp);
        LinearFrame mMetroLayout = (LinearFrame)v.findViewById(R.id.metrolayout);
        for(int step=0;step<block.items.size();step++) {
            final DisplayItem item = block.items.get(step);

            ViewGroup meida = (ViewGroup) LayoutInflater.from(getContext()).inflate(res_id, null);
            ImageView image = (ImageView)meida.findViewById(R.id.poster);
            Picasso.with(getContext()).load(item.images.get("poster").url).tag(tag).placeholder(R.drawable.default_poster_pic).error(R.drawable.default_poster_pic).fit().into(image);

            if(block.ui_type.id == LayoutConstant.grid_media_port) {
                TextView title = (TextView) meida.findViewById(R.id.media_title);
                title.setText(item.title);

                TextView desc = (TextView) meida.findViewById(R.id.descrip);
                desc.setText(item.sub_title);
            }else {
                TextView descrip = (TextView)meida.findViewById(R.id.descrip);
                descrip.setText(item.title + " " +item.sub_title);
            }

            meida.findViewById(R.id.tab_media_click).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    launcherAction(getContext(), item);
                }
            });

            FrameLayout.LayoutParams itemflp = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            itemflp.leftMargin = getPaddingLeft() + (width*(step%row_count) ) + padding*(step%row_count + 1);
            itemflp.topMargin  = getPaddingTop()  + (height*(step/row_count)) + item_padding*(step/row_count + 1);

            mMetroLayout.addView(meida,itemflp);
        }

        int lines = (block.items.size()+1)/row_count;
        getDimens().height += (height + item_padding)*lines;
    }

    private Dimens mDimens;
    @Override
    public Dimens getDimens() {
        if(mDimens == null){
            mDimens = new Dimens();
            mDimens.width  = getResources().getDimensionPixelSize(R.dimen.rank_banner_width);
            mDimens.height = 0;
        }
        return mDimens;
    }
}
