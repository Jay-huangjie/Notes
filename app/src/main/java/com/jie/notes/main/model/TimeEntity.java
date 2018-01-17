package com.jie.notes.main.model;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

/**
 * Created by huangjie on 2017/11/17.
 * 类名：
 * 说明：记录具体时间的数据库,当天有且只有1条数据
 *       一张表下有多个明细数据(DetailEntity)
 *
 *                    对应关系：
 *      1张 TimeEntity表 ---> 对应多条DetailEntity详情数据
 */

@Entity
public class TimeEntity {
    @Id
    long id;

    public String day;
    public String month;
    public String year;
    public String date;

    public void set(String date){
        this.date =date;
        String[] value = date.split("-");
        year = value[0];
        month = value[1];
        day = value[2];
    }

    /*
    反向关联一对多
     */
    @Backlink(to = "entityToOne")
    public ToMany<DetailEntity> detailed;
    //item类型
    public int itemType;
    //当日总收入、
    public float totalIncome;
    //当日总支出
    public float totalExpenditure;
}
