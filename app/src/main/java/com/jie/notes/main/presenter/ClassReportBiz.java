package com.jie.notes.main.presenter;

import com.jie.notes.db.DBManager;
import com.jie.notes.main.model.DetailEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by huangjie on 2017/11/7.
 * 类名：类别报表p层
 * 说明：
 */

public final class ClassReportBiz {

    private Map<String, List<DetailEntity>> NameMap;

    public ClassReportBiz() {
    }

    /*
    * 获取一个计算好总金额的明细数据分类集合,并按照金额从大到小排列
    * */
    public List<DetailEntity> getFilteDetailList(String year_month) {
        NameMap = generateCategoryNameMap(year_month);
        Iterator iterator = NameMap.entrySet().iterator();
        List<DetailEntity> filteDetailListes = new ArrayList<>();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            List<DetailEntity> valueList = (List<DetailEntity>) entry.getValue();
            if (valueList!=null&&valueList.size()>0){
                DetailEntity filteEntity = (DetailEntity) valueList.get(0).clone(); //获取相同类中的Icon信息等，因为都是一样的，因此取第一个就行了
                filteEntity.money = DBManager.getCategoryNum(year_month, (String) entry.getKey());//计算一下相同名称的金额总额
                filteDetailListes.add(filteEntity);
            }
        }
        Collections.sort(filteDetailListes, new Comparator<DetailEntity>() {
            @Override
            public int compare(DetailEntity o1, DetailEntity o2) {
                return (int) (o2.money-o1.money);
            }
        });
        return filteDetailListes;
    }

    /**
     * 按名称将所有的明细数据分类到HashMap里
     * @param year_month
     * @return
     */
    private Map<String,List<DetailEntity>> generateCategoryNameMap(String year_month){
        List<DetailEntity> allEntities = DBManager.getDetailList(year_month);
        Map<String,List<DetailEntity>> nameMap = new HashMap<>();
        for (DetailEntity entity:allEntities){
            if (nameMap.get(entity.name)==null){
                List<DetailEntity> nameList = new ArrayList<>();
                nameList.add(entity);
                nameMap.put(entity.name,nameList);
            }else {
                List<DetailEntity> nameList = nameMap.get(entity.name);
                nameList.add(entity);
            }
        }
        return nameMap;
    }

    /**
     * @return 获取一个按名称分类的HashMap
     * Notes:一定要调用了getFilteDetailList方法NameMap才有值
     */
    public Map<String,List<DetailEntity>> getNameMap(){
        return NameMap;
    }

    /*
    * get Ranking list data
    * */
    public List<DetailEntity> getRankListData(String name) {
        List<DetailEntity> resultList = getNameMap().get(name);
        Collections.sort(resultList, new Comparator<DetailEntity>() {
            @Override
            public int compare(DetailEntity o1, DetailEntity o2) {
                return (int) (o2.money-o1.money);
            }
        });
        return resultList;
    }
}
