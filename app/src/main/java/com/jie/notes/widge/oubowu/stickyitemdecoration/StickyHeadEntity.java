package com.jie.notes.widge.oubowu.stickyitemdecoration;


/**
 * Created by Oubowu on 2016/7/21 17:51.
 * <p>
 * 实体类，可以将自己想要填充的数据包装进去，同时附带这个数据对应的类型
 */
public class StickyHeadEntity<T>{

    private final int itemType;

    private T data;

    public StickyHeadEntity(T data, int itemType) {
        this.data = data;
        this.itemType = itemType;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public int getItemType() {
        return itemType;
    }
}
