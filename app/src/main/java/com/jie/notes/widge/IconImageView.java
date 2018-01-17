package com.jie.notes.widge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jie.notes.R;
import com.jie.notes.util.BaseUtil;
import com.jie.notes.util.SVGUtil;

/**
 * Created by huangjie on 2017/11/7.
 * 类名：
 * 说明：Icon图标控件
 */

public class IconImageView extends FrameLayout {
    private ImageView iv;
    private int resId;

    public IconImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        iv = new ImageView(context);
        addView(iv);
        FrameLayout.LayoutParams fl = (LayoutParams) iv.getLayoutParams();
        fl.gravity = Gravity.CENTER;
        fl.width = BaseUtil.dip2px(22);
        fl.height = BaseUtil.dip2px(22);
        iv.setLayoutParams(fl);
    }

    //橘色
    public void originSet() {
        setBackgroundResource(R.drawable.select_origin);
        SVGUtil.setSvgImageTint(resId, R.color.white, iv);
    }

    //默认
    public void ClearSet(int resId) {
        setBackgroundResource(0);
        SVGUtil.setSvgImageTint(resId, R.color.color_8a8a8a, iv);
    }

    public void ClearSet() {
        setBackgroundResource(0);
        SVGUtil.setSvgImageTint(resId, R.color.color_8a8a8a, iv);
    }

    /*
    * 绿色
    * */
    public void greenSet() {
        setBackgroundResource(R.drawable.select_green);
        SVGUtil.setSvgImageTint(resId, R.color.white, iv);
    }

    public void setResId(int resId){
        this.resId = resId;
    }

//    /*
//* 明细列表支出图标设置
//* */
//    public void expenditureSet(int index, int position) {
//        setBackgroundResource(R.drawable.select_origin);
//        if (position >= 0 && position < SelectIconManager.getIcons(index).length) {
//            SVGUtil.setSvgImageTint(SelectIconManager.getIconDrawable(index, position), R.color.white, iv);
//        }
//    }

}
