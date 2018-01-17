package com.jie.notes.base;

import android.support.multidex.MultiDexApplication;

import com.jie.notes.MyObjectBox;

import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;
import io.objectbox.android.BuildConfig;


/**
 * Created by huangjie on 2017/11/3.
 * 类名：
 * 说明：Application基类
 */

public class BaseApp extends MultiDexApplication {
    private static BaseApp baseApp;
    private BoxStore boxStore; //数据库表的管理者
    @Override
    public void onCreate() {
        super.onCreate();
        baseApp = this;
        boxStore = MyObjectBox.builder().androidContext(this).build();
        if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(boxStore).start(this);
        }
    }

    public BoxStore getBoxStore(){
        return boxStore;
    }

    public static BaseApp getInstence(){
        return baseApp;
    }
}
