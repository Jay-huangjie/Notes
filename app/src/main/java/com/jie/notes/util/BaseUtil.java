package com.jie.notes.util;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jie.notes.base.BaseApp;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by huangjie on 2017/11/7.
 * 类名：
 * 说明：
 */

public class BaseUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = BaseApp.getInstence().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = BaseApp.getInstence().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static void hideKeyboard(Activity activity){
        /**隐藏软键盘**/
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputmanger!=null)
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    /**
     * 关闭软键盘
     * @param activity
     * @param editText
     */
    public static void closeKeyboard(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null)
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    /**
     * 打开软键盘
     * @param activity
     * @param editText
     */
    public static void openKeyboard(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm!=null){
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    /*
    * 关闭软键盘显示游标
    * */
    public static void showCursor(Activity activity, EditText editText){
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            activity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod(
                        "setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //保留两位小数 float
    public static String FloatFormat(Float number){
        NumberFormat nf = new DecimalFormat("#.##");
        return nf.format(number);
    }

    //保留两位小数 double
    public static String DoubleFormat(Double number){
        NumberFormat nf = new DecimalFormat("#.##");
        return nf.format(number);
    }
}
