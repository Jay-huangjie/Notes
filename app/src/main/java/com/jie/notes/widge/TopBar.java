package com.jie.notes.widge;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jie.notes.R;

/**
 * Created by huangjie on 2017/12/28.
 * 类名：
 * 说明：公共头部
 */

public class TopBar extends FrameLayout {
    private View v_bottom_line;
    private TextView titleView;
    private ImageView backView;
    private ImageView right_imgView;
    private RelativeLayout roots;
    public TopBar(Context context) {
        this(context,null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public interface RightClickListence{
        void click();
    }
    private RightClickListence rightClickListence;

    public TopBar(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (isInEditMode()){
            return;
        }
        int topbarLayoutId = R.layout.common_title;
        String title = null;
        int back_img = R.drawable.back_icon;
        int right_img = 0;
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.Toolbar);
        if (a!=null){
            topbarLayoutId = a.getResourceId(R.styleable.TopBar_t_LayoutId,topbarLayoutId);
            title = a.getString(R.styleable.TopBar_t_title);
            back_img = a.getResourceId(R.styleable.TopBar_t_backImage,back_img);
            right_img = a.getResourceId(R.styleable.TopBar_t_rightImage,right_img);
            a.recycle();
        }
        LayoutInflater.from(context).inflate(topbarLayoutId,this);
        titleView = findViewById(R.id.tv_comm_title);
        if (titleView!=null){
            if (TextUtils.isEmpty(title)){
                title = "记账本";
            }
            titleView.setText(title);
        }
        backView = findViewById(R.id.iv_comm_back);
        if (backView!=null){
            backView.setImageResource(back_img);
            if (context instanceof Activity){
                backView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity a = (Activity) context;
                        //get Activity name
                        String activity = a.toString();
                        activity = activity.substring(activity.lastIndexOf(".") + 1, activity.indexOf("@"));
                        if ("MainActivity".equals(activity)){
                            Toast.makeText(context,"已经是首页啦",Toast.LENGTH_SHORT).show();
                        }else{
                            a.finish();
                        }
                    }
                });
            }
        }
        right_imgView = findViewById(R.id.iv_right_comments);
        if (right_imgView!=null){
            if (right_img!=0){
                right_imgView.setImageResource(right_img);
            }
        }

        roots = findViewById(R.id.rl_roots);
        v_bottom_line = findViewById(R.id.v_bottom_line);
    }

    public void setRightClickListence(RightClickListence listence){
        this.rightClickListence = listence;
        if (rightClickListence!=null){
            if (right_imgView!=null){
                right_imgView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rightClickListence.click();
                    }
                });
            }
        }
    }

    public  TopBar setTitle(String title){
        if (titleView!=null){
            titleView.setText(title);
        }
        return this;
    }

    public TopBar setTitleColor(@ColorInt int color){
        if (titleView!=null){
            titleView.setTextColor(color);
        }
        return this;
    }

    public TopBar setBackImage(@DrawableRes int drawable){
        if (backView!=null){
            backView.setImageResource(drawable);
        }
        return this;
    }

    public TopBar setRightImage(@DrawableRes int drawable){
        if (right_imgView!=null){
            right_imgView.setImageResource(drawable);
        }
        return this;
    }

    public TopBar setRootsBackgroundColor(@ColorInt int color){
        if (roots!=null){
            roots.setBackgroundColor(color);
        }
        return this;
    }

    public TopBar setBottomLineColor(@ColorInt int color){
        if (v_bottom_line!=null){
            v_bottom_line.setBackgroundColor(color);
        }
        return this;
    }

    public TopBar setBottomLineVisivity(boolean isVisibity){
        if (v_bottom_line!=null){
            if (isVisibity){
                v_bottom_line.setVisibility(VISIBLE);
            }else{
                v_bottom_line.setVisibility(GONE);
            }
        }
        return this;
    }

    public ImageView getBackView(){
        return backView;
    }
}
