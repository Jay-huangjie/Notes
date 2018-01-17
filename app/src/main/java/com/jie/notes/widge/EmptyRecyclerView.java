package com.jie.notes.widge;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.jie.notes.widge.oubowu.stickyitemdecoration.StickyHeadContainer;

/**
 * Created by huangjie on 2017/11/18.
 * 类名：
 * 说明：自动检测展示空试图的Recyclerview
 */

public class EmptyRecyclerView extends RecyclerView {

    private View emptyView;

    private StickyHeadContainer headContainer;

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

   final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            check();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            check();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            check();
        }
    };

    private void check() {
        if (emptyView!=null&&getAdapter()!=null){
            boolean emptyVisibility = getAdapter().getItemCount()==0?true:false;
            emptyView.setVisibility(emptyVisibility?VISIBLE:GONE);
            setVisibility(emptyVisibility?GONE:VISIBLE);
            if (headContainer!=null){
                headContainer.setVisibility(emptyVisibility?GONE:VISIBLE);
            }
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter old = getAdapter();
        if (old!=null){
            old.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter!=null){
            adapter.registerAdapterDataObserver(observer);
        }
    }

    public void setEmptyView(View emptyView){
        this.emptyView = emptyView;
        check();
    }
    public void setHeadContainer(StickyHeadContainer headContainer){
        this.headContainer = headContainer;
        check();
    }
}
