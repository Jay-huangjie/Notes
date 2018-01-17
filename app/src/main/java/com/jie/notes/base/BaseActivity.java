package com.jie.notes.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.jie.notes.util.StatusBarUtil;

/**
 * Created by huangjie on 2017/11/3.
 * 类名：
 * 说明：Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    public int mScreenW;
    public int mScreenH;
    private SparseArray<View> mView = new SparseArray<>();
    public BaseApp baseApp;
    private boolean isBack;
    private long downTime;
    public Context context;

    protected abstract int getLayoutID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        int contentView = getLayoutID();
        if (contentView==0){
            throw new NullPointerException(this.getClass().getSimpleName()+"未设置布局文件");
        }else{
            setContentView(contentView);
        }
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mScreenH = metrics.heightPixels;
        mScreenW = metrics.widthPixels;
        baseApp = BaseApp.getInstence();
        initView();
        initEvent();
    }

    protected abstract void initView();

    protected abstract void initEvent();

    public <V extends View> V getViewID(int id){
        V view = (V) mView.get(id);
        if (view==null){
           view = (V) findViewById(id);
           mView.put(id,view);
        }
        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            if (getRunningActivityName().startsWith("Main")){
                if (!isBack){
                    showShortToast("再按一次退出");
                    isBack = true;
                    downTime = event.getDownTime();
                    return true;
                }else{
                    if (event.getDownTime()-downTime <=2000){ //快速点击退出
                        finish();
                    }else{
                        showShortToast("再按一次退出");
                        downTime = event.getDownTime();
                        return true;
                    }
                }
            }else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void showShortToast(String string){
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show();
    }

    public void back(View v){
        finish();
    }

    public void setOnClickListence(View.OnClickListener l,View...views){
        if (l==null){
            throw new NullPointerException("View.OnClickListtener为null==>点击事件都不给,你也太抠了吧");
        }
        if (views==null){
            throw new NullPointerException("views为null==>view都空了，你想给null设置点击事件吗?");
        }
        for (View view:views){
            view.setOnClickListener(l);
        }
    }

    public String getRunningActivityName() {
        String contextString = this.toString();
        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
    }
}
