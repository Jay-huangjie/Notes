package com.jie.notes.main.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;
import io.objectbox.relation.ToOne;

/**
 * Created by huangjie on 2017/11/17.
 * 类名：
 * 说明：明细数据库实体类
 */
@Entity
public class DetailEntity implements Cloneable{
    @Id
    public long id;

    //备注
    public String remark;

    //收入(true)&支出(false)
    public boolean isIncome;

    //记录金额
    public double money;

    //日期
    public String date;


    //ToOne表示关联一张TimeEntty表
    public ToOne<TimeEntity> entityToOne;

    //icon 图标信息
    public int icon_img;
    //icon 名称
    public String name;
    //icon所在的页码
    public int index;
    //当前Icon的position
    public int position;
    //当日总收入、
//    @Transient  //不需要保存到数据库中字段
//    public String totalIncome;

    //当日总支出
    @Transient
    public String totalExpenditure;

    //item类型
    @Transient
    public int itemType;

    @Override
    public Object clone(){
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
