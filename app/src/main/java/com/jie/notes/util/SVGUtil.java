package com.jie.notes.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.widget.ImageView;

import com.jie.notes.base.BaseApp;

/**
 * Created by huangjie on 2017/11/7.
 * 类名：
 * 说明：svg图片工具类
 */

public class SVGUtil {

    /*
    * 设置指定SVG图片颜色到控件上
    * */
    public static void setSvgImageTint(@DrawableRes  int resId, @ColorRes int color, ImageView imageView){
        if (resId==0){
            return;
        }
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(BaseApp.getInstence().getResources(), resId, BaseApp.getInstence().getTheme());
        if (vectorDrawableCompat!=null){
            vectorDrawableCompat.setTint(BaseApp.getInstence().getResources().getColor(color));
            imageView.setImageDrawable(vectorDrawableCompat);
        }else{
            throw new NullPointerException("VectorDrawableCompat类未获取到资源");
        }
    }

    /*
    * 获取特定颜色的Svg图片
    * */
    public static Drawable getSvgColorTint(@DrawableRes  int resId, @ColorRes int color){
        if (resId==0) return null;
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(BaseApp.getInstence().getResources(), resId, BaseApp.getInstence().getTheme());
        if (vectorDrawableCompat!=null){
            vectorDrawableCompat.setTint(BaseApp.getInstence().getResources().getColor(color));
            return vectorDrawableCompat;
        }else{
            throw new NullPointerException("VectorDrawableCompat类未获取到资源");
        }
    }
}
