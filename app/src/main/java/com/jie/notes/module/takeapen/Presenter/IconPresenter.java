package com.jie.notes.module.takeapen.Presenter;

import com.jie.notes.db.DBManager;
import com.jie.notes.module.takeapen.manager.SelectIconManager;
import com.jie.notes.module.takeapen.model.Icon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjie on 2017/11/7.
 * 类名：
 * 说明：
 */

public final class IconPresenter {

    private int index;

    /*
    * 加载第一页的数据
    * */
    public List<Icon> getIcon1() {
        List<Icon> iconList = new ArrayList<>();
        iconList.clear();
        iconList.add(new Icon("一般"));
        iconList.add(new Icon("餐饮"));
        iconList.add(new Icon("购物"));
        iconList.add(new Icon("服饰"));
        iconList.add(new Icon("交通"));
        iconList.add(new Icon("娱乐"));
        iconList.add(new Icon("社交"));
        iconList.add(new Icon("居家"));
        iconList.add(new Icon("通讯"));
        iconList.add(new Icon("零食"));
        for (int i = 0; i < iconList.size(); i++) {
            Icon icon = iconList.get(i);
            icon.position = i;
            icon.icon_img = SelectIconManager.getIconRes(0, i);
            icon.index = index;
        }
        return iconList;
    }

    /*
   * 加载第二页的数据
   * */
    public List<Icon> getIcon2() {
        List<Icon> iconList = new ArrayList<>();
        iconList.add(new Icon("美容"));
        iconList.add(new Icon("运动"));
        iconList.add(new Icon("旅行"));
        iconList.add(new Icon("数码"));
        iconList.add(new Icon("学习"));
        iconList.add(new Icon("医疗"));
        iconList.add(new Icon("书籍"));
        iconList.add(new Icon("宠物"));
        iconList.add(new Icon("彩票"));
        iconList.add(new Icon("汽车"));
        for (int i = 0; i < iconList.size(); i++) {
            Icon icon = iconList.get(i);
            icon.position = i;
            icon.icon_img = SelectIconManager.getIconRes(1, i);
            icon.index = index;
            icon.isIncome = false;
        }
        return iconList;
    }

    /*
   * 加载第三页的数据
   * */
    public List<Icon> getIcon3() {
        List<Icon> iconList = new ArrayList<>();
        iconList.add(new Icon("办公"));
        iconList.add(new Icon("住房"));
        iconList.add(new Icon("维修"));
        iconList.add(new Icon("孩子"));
        iconList.add(new Icon("长辈"));
        iconList.add(new Icon("礼物"));
        iconList.add(new Icon("礼金"));
        iconList.add(new Icon("还钱"));
        iconList.add(new Icon("捐增"));
        iconList.add(new Icon("理财"));
        for (int i = 0; i < iconList.size(); i++) {
            Icon icon = iconList.get(i);
            icon.position = i;
            icon.icon_img = SelectIconManager.getIconRes(2, i);
            icon.index = index;
            icon.isIncome = false;
        }
        return iconList;
    }

    /*
  * 加载第四页的数据,默认加载添加类别
  * */
    public List<Icon> getIcon4() {
        List<Icon> iconList = new ArrayList<>();
        List<Icon> classIcon = DBManager.getClassIconList();
        if (classIcon.size()>0){
           iconList.addAll(classIcon);
        }
        Icon icon = new Icon("添加类别");
        icon.isIncome = false;
        icon.index = index;
        icon.icon_img = SelectIconManager.getIconRes(3, 0);
        icon.position = 0;
        icon.iconType = 3;
        iconList.add(icon);
        return iconList;
    }

    /*
    * 收入的数据，暂时不开发
    * */
//    public List<Icon> getIcon5() {
//        iconList.clear();
//        iconList.add(new Icon( "工资"));
//        iconList.add(new Icon( "兼职"));
//        iconList.add(new Icon( "理财收益"));
//        iconList.add(new Icon( "礼金"));
//        iconList.add(new Icon( "其他"));
//        for (int i = 0; i < iconList.size(); i++) {
//            Icon icon = iconList.get(i);
//            icon.position = i;
//            icon.icon_img = SelectIconManager.getIconDrawable(4, i);
//            icon.index = index;
//            icon.isIncome = false;
//        }
//        return iconList;
//    }

    public List<Icon> getData(int index) {
        this.index = index;
        List<Icon> iconList = new ArrayList<>();
        switch (index) {
            case 0:
                iconList = getIcon1();
                break;
            case 1:
                iconList = getIcon2();
                break;
            case 2:
                iconList = getIcon3();
                break;
            case 3:
                iconList = getIcon4();
                break;
            case -1:  //获取我喜欢的图标数据
                iconList = DBManager.getIconList();
                break;
        }
        return iconList;
    }

//    /*
//    * 添加自定义类目
//    * */
//    public List<Icon> insertCustomIcon(String icon_name){
//        iconList.add(new Icon(IconImageView.getIcon()[31],icon_name));
//        iconList.add(new Icon(IconImageView.getIcon()[30],"添加类别"));
//        return iconList;
//    }
}
