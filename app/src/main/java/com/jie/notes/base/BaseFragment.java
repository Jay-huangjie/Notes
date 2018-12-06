package com.jie.notes.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by huangjie on 2017/11/6.
 * 类名：
 * 说明：fragment基类
 */

public abstract class BaseFragment extends Fragment {

    protected SparseArray<View> mViews = new SparseArray<View>();
    public Context context;
    protected abstract int getLayoutID();
    protected abstract void initView(View view);
    protected abstract void initEvent();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        int layoutID = getLayoutID();
        if (layoutID == 0) {
            throw new NullPointerException(this.getClass().getSimpleName() + "没有设置布局文件");
        } else {
            rootView = inflater.inflate(getLayoutID(), container, false);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initView(view);
        initEvent();
    }

    public void showShortToast(String string){
        Toast.makeText(getActivity(),string,Toast.LENGTH_SHORT).show();
    }


    /**
     * 根据View的ID获取View对象
     *
     * @param viewId View的资源ID
     * @param <V>    确定获取的View的类型
     * @return 返回参数viewId所对应的View对象
     */
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public <V extends View> V getViewByID(@IdRes int viewId) {
        V view = (V) mViews.get(viewId);
        if (view == null) {
            try {
                view = (V) getView().findViewById(viewId);
            } catch (NullPointerException e) {
                throw new NullPointerException("BaseFragment：#.getViewByID()方法请在initView(View view)方法中调用或避免在Fragment.onCreateView()方法中调用");
            }
            mViews.put(viewId, view);
        }
        return view;
    }
}
