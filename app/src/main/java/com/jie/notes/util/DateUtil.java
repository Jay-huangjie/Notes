package com.jie.notes.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by huangjie on 2017/11/17.
 * 类名：
 * 说明：时间转换工具类
 */

public class DateUtil {

    //获取年月日
    public static String getDate(){
        return new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(new Date());
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //获取当天的月份和日期 MM-dd
    public static String getMonthAndDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd", Locale.ENGLISH);
        return date==null?"":sdf.format(date);
    }

    //获取当天的年和月 yyyy-MM
    public static String getYearAndMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    public static String getYearAndMonth(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.ENGLISH);
        return date==null?"":sdf.format(date);
    }

    //当前年
    public static String getYear(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    //根据时间戳获取年
    public static String getYear(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        Date date = new Date(time);
        return sdf.format(date);
    }

    //当前月
    public static String getMonth(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    //根据时间戳获取月
    public static String getMonth(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("MM", Locale.ENGLISH);
        Date date = new Date(time);
        return sdf.format(date);
    }

    //根据时间戳获日期
    public static String getDay(long time){
        SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.ENGLISH);
        Date date = new Date(time);
        return sdf.format(date);
    }

    //返回dd-星期几的格式
    public static String getWeekAndDay(String data) {
        long time = StrToDate(data).getTime();
        StringBuilder buffer = new StringBuilder();
        buffer.append(getDay(time)).append("日").append("-").append(getWeek(new Date(time)));
        return buffer.toString();
    }

    //获取当前时间是周几 1-7
    public static int getWeekToNum(Date dt) {
        if (dt != null) {
            int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
            Calendar cal = Calendar.getInstance();
            cal.setTime(dt);
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0)
                w = 0;
            return weekDays[w];
        } else {
            return 1;
        }
    }


    //获取当前时间是周几 星期
    public static String getWeek(Date date) {
        int week = getWeekToNum(date);
        String wee = null;
        switch (week) {
            case 1:
                wee = "星期一";
                break;
            case 2:
                wee = "星期二";
                break;
            case 3:
                wee = "星期三";
                break;
            case 4:
                wee = "星期四";
                break;
            case 5:
                wee = "星期五";
                break;
            case 6:
                wee = "星期六";
                break;
            case 7:
                wee = "星期日";
                break;
        }
        return wee;
    }


    //获取当前时间是多少月多少日
    public static String getMonthAndDayToChinese(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日", Locale.CHINESE);
        return sdf.format(date);
    }

    //数字转化为字符串 year_month_day
    public static String getNumFormat(int year,int month,int day){
        return year+"-"+String.format(Locale.ENGLISH,"%02d",month)+"-"+String.format(Locale.ENGLISH,"%02d",day);
    }

    //get year_month
    public static String getNumFormat_YearMonth(int year,int month){
        return year + "-" + String.format(Locale.ENGLISH, "%02d", month);  //当月份为一位时补0
    }

    //get year_month
    public static String getNumFormat_YearMonth(String date){
       return getYearAndMonth(StrToDate(date));
    }
}
