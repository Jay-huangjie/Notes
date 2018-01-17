package com.jie.notes.widge.oubowu.stickyitemdecoration;

import android.view.View;

/**
 * Created by huangjie on 2017/11/18.
 * 类名：
 * 说明：长按事件监听
 */

public interface OnItemLongClickListener<T> {
    void onItemLongClick(View view, T data, int position);
}
