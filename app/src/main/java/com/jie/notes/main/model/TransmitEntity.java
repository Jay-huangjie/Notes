package com.jie.notes.main.model;

import com.jie.notes.base.BaseRxBusEntity;

/**
 * Created by huangjie on 2018/1/7.
 * 说明：Rxbus传递数据的类
 */

public class TransmitEntity extends BaseRxBusEntity{

    public TransmitEntity(String className) {
        super(className);
    }

    public TransmitEntity(String className,String date){
        super(className);
        this.date = date;
    }

    /*时间*/
    private String date;
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    /*备注*/
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
