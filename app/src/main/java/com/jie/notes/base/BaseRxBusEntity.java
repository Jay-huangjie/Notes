package com.jie.notes.base;

/**
 * Created by huangjie on 2018/1/11.
 * 类名：
 * 说明：
 */

public class BaseRxBusEntity {
    private String className; //从哪儿来
    private BaseRxBusEntity(){};
    public BaseRxBusEntity(String className){
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
