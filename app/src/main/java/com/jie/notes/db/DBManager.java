package com.jie.notes.db;

import android.text.TextUtils;

import com.jie.notes.base.BaseApp;
import com.jie.notes.main.model.DetailEntity;
import com.jie.notes.main.model.DetailEntity_;
import com.jie.notes.main.model.TimeEntity;
import com.jie.notes.main.model.TimeEntity_;
import com.jie.notes.module.takeapen.manager.SelectIconManager;
import com.jie.notes.module.takeapen.model.Icon;
import com.jie.notes.module.takeapen.model.Icon_;
import com.jie.notes.util.BaseUtil;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;
import io.objectbox.query.QueryBuilder;

/**
 * Created by huangjie on 2017/11/9.
 * 类名：
 * 说明：数据库管理类
 */

public class DBManager {
    private final static Box<DetailEntity> detailedBox = BaseApp.getInstence().getBoxStore().boxFor(DetailEntity.class);
    private final static Box<TimeEntity> timeEntityBox = BaseApp.getInstence().getBoxStore().boxFor(TimeEntity.class);
    private final static Box<Icon> iconBox = BaseApp.getInstence().getBoxStore().boxFor(Icon.class);

    public static Box<DetailEntity> getDetailedBox() {
        return detailedBox;
    }

    public static QueryBuilder<DetailEntity> getDetailedQueryBuilder() {
        return detailedBox.query();
    }

    public static Query<DetailEntity> getDetailedQuery() {
        return getDetailedQueryBuilder().build();
    }
    //获取Box实例

    /*
    * 获取Box实例
    *
    * Box可以进行数据的增(put)，删(remove)，以及获取实例(get)的一些操作
    * */
    public static Box<TimeEntity> getTimeEntityBox() {
        return timeEntityBox;
    }

    /*
    * 获取Query实例
    *
    * Query可以求和(sum)，执行Rxjava方法进行异步查(scribe)询，精确查找（setParameter）,延迟加载数据(findLazy)等
    * */
    public static Query<TimeEntity> getTimeEntityQuery() {
        return getTimeEntityQueryBuild().build();
    }


    /*
    * 获取QueryBuilder实例
    *
    * QueryBuilder可以进行各种条件查询,例如between，contains等等
    * */
    public static QueryBuilder<TimeEntity> getTimeEntityQueryBuild() {
        return timeEntityBox.query();
    }

    public static Box<Icon> getIconBox() {
        return iconBox;
    }

    //-----------------超纯洁分割线-------------------------------------//
    //查询当月记录的所有日期  date:yyyy-mm
    public static List<TimeEntity> getTimeEntityToMonth(String date) {
        return timeEntityBox.query().contains(TimeEntity_.date, date).build().find();
    }

    //

//    //查询某个时间段的收入或支出总额  isIncome?true（收入）:false(支出)
//    public static String getForTimeTotal(String date,boolean isIncome){
//        double result = detailedBox.query()
//                .contains(DetailEntity_.date, date)
//                .and()
//                .equal(DetailEntity_.isIncome,isIncome)
//                .build()
//                .sumDouble(DetailEntity_.money);
//        return BaseUtil.DoubleFormat(result);
//    }

    /**
     * @param date 查询的时间 yyyy-mm
     * @return 当前月份下的支出总和, 模糊查询
     */
    public static String getExpenditureTotal(String date) {
        double result = detailedBox
                .query()
                .contains(DetailEntity_.date, date)
                .build()
                .sumDouble(DetailEntity_.money);
        return BaseUtil.DoubleFormat(result);
    }

    /**
     * @param date yyyy-mm-dd
     * @return当前日期下的支出总和 精确查询
     */
    public static String getExpenditureTotalForDay(String date) {
        double result = detailedBox
                .query()
                .equal(DetailEntity_.date, date)
                .build()
                .sumDouble(DetailEntity_.money);
        return BaseUtil.DoubleFormat(result);
    }


    //查询当天是否存在数据 date:yyyy-mm-dd
    public static boolean TaDayisExist(String date) {
        /*
        * 查询第一个对象，如果第一个对象没有值则证明该值在数据库中不存在,也可以使用count()>0方法判断是否有值
        * */
        TimeEntity entity = timeEntityBox.query().equal(TimeEntity_.date, date).build().findFirst();
        return entity == null ? false : true;
    }

    //获取当天的时间类操作对象
    public static TimeEntity getTaDayEntity(String date) {
        /*
        * findUnique方法只会返回一个特定的对象，如果查询到了多个结果则会抛出一个异常
        * */
        return timeEntityBox.query().equal(TimeEntity_.date, date).build().findUnique();
    }

    //删除某条记录，根据id
    public static void deleteForId(long id) {
        detailedBox.remove(id);
    }

    //删除某条记录，根据实体类
    public static void deleteForEntity(DetailEntity entity) {
        detailedBox.remove(entity);
    }


    //查询某个实例，id
    public static DetailEntity getDetailEntity(long id) {
        if (id == 0) {
            return null;
        }
        return getDetailedBox().get(id);
    }


    /**
     * 获取该名称下的所有金额总和
     * @param date
     * @param name
     * @return
     */
    public static double getCategoryNum(String date,String name){
        if (!TextUtils.isEmpty(name)) {
            return getDetailedQueryBuilder()
                    .contains(DetailEntity_.date,date)
                    .equal(DetailEntity_.name,name)
                    .build()
                    .sumDouble(DetailEntity_.money);
        }
        return 0;
    }

    /**
     * 获取某一时间段内的所有明细数据
     * @param date yyyy-mm
     * @return
     */
    public static List<DetailEntity> getDetailList(String date){
        return getDetailedQueryBuilder().contains(DetailEntity_.date,date).build().find();
    }


    /**
     * 插入逻辑
     * 总共显示10个图标，如果新加入的图标没有在这10个中，就把最后一个删除，把新加入的放在第一个
     * 如果新加入的图标在这10个中，把已存在的删除，把新加入的放在第一个，其余的往后排
     * 思路：按照id排序
     *
     * @return
     */
    public static void insertLikeIcon(Icon icon) {
        icon.index = -1;
        icon.id = 0;  //id置为0表示插入一个新的实体
        icon.iconType = 1;
        iconBox.query().equal(Icon_.icon_name, icon.icon_name).equal(Icon_.iconType, 1).build().remove();
        iconBox.put(icon);
        long count = iconBox.query().equal(Icon_.iconType, 1).build().count();
        if (count > 10) {
            Icon firstInsertIcon = iconBox.query().equal(Icon_.iconType, 1).order(Icon_.id).build().findFirst();
            if (firstInsertIcon != null)
                iconBox.remove(firstInsertIcon);
        }
    }

    /**
     * 获取喜欢的所有图标
     *
     * @return
     */
    public static List<Icon> getIconList() {
        return iconBox.query().equal(Icon_.iconType, 1).orderDesc(Icon_.id).build().find();
    }

    /*
    * 添加一个类目
    * */
    public static void insertClassIcon(String name) {
        Icon icon = new Icon();
        icon.index = 3;
        icon.id = 0;
        icon.iconType = 2;
        icon.icon_name = name;
        icon.icon_img = SelectIconManager.getIconRes(3, 1);
        long count = iconBox.query().equal(Icon_.iconType, 2).build().count();
        icon.position = (int) count;
        iconBox.put(icon);
        if (count > 10) {
            Icon firstInsertIcon = iconBox.query().equal(Icon_.iconType, 2).order(Icon_.id).build().findFirst();
            if (firstInsertIcon != null)
                iconBox.remove(firstInsertIcon);
        }
    }

    /*
    * 查询类目
    * */
    public static List<Icon> getClassIconList(){
        return iconBox.query().equal(Icon_.iconType, 2).orderDesc(Icon_.id).build().find();
    }

    /**
     * 根据收入支出来获取是否有喜欢的图标（暂未加入收入功能）
     * @param isIncome
     * @return
     */
//     public static boolean haveLove(boolean isIncome){
//        return iconBox.query().equal(Icon_.isIncome,isIncome).build().count()==0?false:true;
//     }

    /**
     * 数据库中是否加入了喜爱的图标
     *
     * @return 是否存在
     */
    public static boolean hasLove() {
        return iconBox.query().equal(Icon_.iconType,1).build().count() > 0 ? true : false;
    }
}
