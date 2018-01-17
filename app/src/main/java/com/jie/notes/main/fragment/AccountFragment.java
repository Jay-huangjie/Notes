package com.jie.notes.main.fragment;

import android.view.View;

import com.jie.notes.R;
import com.jie.notes.base.BaseFragment;

/**
 * Created by huangjie on 2017/11/6.
 * 类名：
 * 说明：账户Fragment
 */

public class AccountFragment extends BaseFragment {

    public static AccountFragment getInstance(){
        return new AccountFragment();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.account_fragment_layout;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initEvent() {

    }
}
