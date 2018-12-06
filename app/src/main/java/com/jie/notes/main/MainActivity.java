package com.jie.notes.main;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jie.notes.R;
import com.jie.notes.base.BaseActivity;
import com.jie.notes.db.DBManager;
import com.jie.notes.main.fragment.AccountFragment;
import com.jie.notes.main.fragment.ClassReportFragment;
import com.jie.notes.main.fragment.DetailedFragment;
import com.jie.notes.main.model.TransmitEntity;
import com.jie.notes.util.DateUtil;
import com.jie.notes.util.ResourceUtil;
import com.jie.notes.util.RxBus;
import com.jie.notes.util.StatusBarUtil;
import com.jie.notes.widge.TopBar;

import java.util.Calendar;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/*
* 记账主界面
* Object-box数据库框架练手项目，图方便去掉了收入功能
* 数据库中保留了查询收入的方法
* 欢迎大牛来完善收入功能
* */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    public static final String TAG = "MainActivity";
    private TextView tv_detailed;
    private TextView tv_class_report;
    private TextView tv_account;
    private DetailedFragment detailedFragment;
    private ClassReportFragment classReportFragment;
    private AccountFragment accountFragment;
    private FragmentManager fm;
    private TextView tv_main_year;
    private TextView tv_main_month;
    private Calendar calendar;
    private TextView tv_main_expenditure;
    private LinearLayout ll_choose_date;
    private TopBar top_bar;
    private String date;
    private boolean dataChange;
    private Observable<String> observable;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tv_detailed = (TextView) getViewID(R.id.tv_detailed);
        tv_class_report = (TextView) getViewID(R.id.tv_class_report);
        tv_account = (TextView) getViewID(R.id.tv_account);
        tv_main_year = (TextView) getViewID(R.id.tv_main_year);
        tv_main_month = (TextView) getViewID(R.id.tv_main_month);
        tv_main_expenditure = (TextView) getViewID(R.id.tv_main_expenditure);
        ll_choose_date = (LinearLayout) getViewID(R.id.ll_choose_date);
        top_bar = getViewID(R.id.top_bar);
        StatusBarUtil.setColor(this,Color.parseColor("#263238"));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initEvent() {
        initTitle();
        calendar = Calendar.getInstance();
        fm = getSupportFragmentManager();
        clickTabFragment(0);
        observable = RxBus.getInstance().register(TAG);
        observable.subscribe(new Consumer<String>() { //如果进行了数据的改动会回调这个方法

            @Override
            public void accept(String date) throws Exception {
                dataChange = true;
                MainActivity.this.date = date;
                getAllUserMoney(date);
            }
        });
        setOnClickListence(this, tv_detailed, tv_class_report, tv_account, tv_main_month,ll_choose_date);
    }

    private void initTitle() {
        top_bar.setTitle("记账本")
                .setTitleColor(ResourceUtil.getColor(this,R.color.white))
                .setBackImage(R.drawable.back_icon_white)
                .setRootsBackgroundColor(Color.parseColor("#263238"))
                .setBottomLineVisivity(false);
        setTopDateViewText(DateUtil.getYear(),DateUtil.getMonth());
        getAllUserMoney(DateUtil.getYearAndMonth());
    }

    private void setTopDateViewText(String year,String month){
        tv_main_year.setText(new StringBuilder(year).append("年"));

        SpannableString spannableString = new SpannableString(String.format(Locale.ENGLISH,"%02d",Integer.valueOf(month))+"月");
        spannableString.setSpan(new AbsoluteSizeSpan(30,true),0,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_main_month.setText(spannableString);
    }


    /**
     * 获取总支出
     * @param date
     */
    private void getAllUserMoney(String date){
        tv_main_expenditure.setText(DBManager.getExpenditureTotal(date));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_detailed:
                clickTabFragment(0);
                break;
            case R.id.tv_class_report:
                clickTabFragment(1);
                break;
            case R.id.tv_account:
                clickTabFragment(2);
                break;
            case R.id.ll_choose_date:  //选择日期
            case R.id.tv_main_month:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                        setTopDateViewText(String.valueOf(year),String.valueOf(month+1));
                        TransmitEntity entity = new TransmitEntity(MainActivity.TAG);
                        entity.setDate(DateUtil.getNumFormat(year, month+1, day_of_month));
                        RxBus.getInstance().post(DetailedFragment.TAG,entity);
                        RxBus.getInstance().post(ClassReportFragment.TAG,entity);
                        getAllUserMoney(DateUtil.getNumFormat_YearMonth(year,month+1));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }



    private void clickTabFragment(int tabIndex) {
        FragmentTransaction ft = fm.beginTransaction();
        hideFragment(ft);
        switch (tabIndex) {
            case 0:
                tv_detailed.setTextColor(getResources().getColor(R.color.color_333333));
                tv_class_report.setTextColor(getResources().getColor(R.color.white));
                tv_account.setTextColor(getResources().getColor(R.color.white));
                tv_detailed.setBackgroundResource(R.color.white);
                tv_class_report.setBackgroundResource(R.color.color_5A5C68);
                tv_account.setBackgroundResource(R.color.color_5A5C68);
                if (detailedFragment == null) {
                    detailedFragment = DetailedFragment.getInstance();
                    ft.add(R.id.fl_fragment, detailedFragment);
                } else {
                    ft.show(detailedFragment);
                }
                break;
            case 1:
                tv_detailed.setTextColor(getResources().getColor(R.color.white));
                tv_class_report.setTextColor(getResources().getColor(R.color.color_333333));
                tv_account.setTextColor(getResources().getColor(R.color.white));
                tv_detailed.setBackgroundResource(R.color.color_5A5C68);
                tv_class_report.setBackgroundResource(R.color.white);
                tv_account.setBackgroundResource(R.color.color_5A5C68);
                if (classReportFragment == null) {
                    classReportFragment = ClassReportFragment.getInstance();
                    ft.add(R.id.fl_fragment, classReportFragment);
                } else {
                    if (dataChange){
                        RxBus.getInstance().post(new TransmitEntity(date));
                        dataChange = false;
                    }
                    ft.show(classReportFragment);
                }
                break;
            case 2:
                tv_detailed.setTextColor(getResources().getColor(R.color.white));
                tv_class_report.setTextColor(getResources().getColor(R.color.white));
                tv_account.setTextColor(getResources().getColor(R.color.color_333333));
                tv_detailed.setBackgroundResource(R.color.color_5A5C68);
                tv_class_report.setBackgroundResource(R.color.color_5A5C68);
                tv_account.setBackgroundResource(R.color.white);
                if (accountFragment == null) {
                    accountFragment = AccountFragment.getInstance();
                    ft.add(R.id.fl_fragment, accountFragment);
                } else {
                    ft.show(accountFragment);
                }
                break;
        }
        ft.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction ft) {
        if (detailedFragment != null) {
            ft.hide(detailedFragment);
        }
        if (accountFragment != null) {
            ft.hide(accountFragment);
        }
        if (classReportFragment != null) {
            ft.hide(classReportFragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unregister(TAG,observable);
    }
}
