package com.jie.notes.widge.oubowu.stickyitemdecoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by huangjie on 2017/11/17.
 * 类名：
 * 说明：分割线
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int[] ATTR = new int[]{android.R.attr.listDivider};
    private Drawable mdrawable;

    public SpaceItemDecoration(Context context) {
        TypedArray a = context.obtainStyledAttributes(ATTR);
        mdrawable = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            DividerHelper.drawBottomAlignItem(c, mdrawable, view, params);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int pos = parent.getChildAdapterPosition(view);
        if (pos>=0){
            int type = parent.getAdapter().getItemViewType(pos);
            if (type!=RecyclerViewAdapter.TYPE_DATA&&type!=RecyclerViewAdapter.TYPE_SMALL_STICKY_HEAD_WITH_DATA){
                outRect.set(0,0,0,0);
            }else{
                outRect.set(0,0,0,mdrawable.getIntrinsicHeight());
            }
        }else{
            outRect.set(0,0,0,0);
        }
    }
}
