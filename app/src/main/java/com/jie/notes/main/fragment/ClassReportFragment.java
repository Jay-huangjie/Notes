package com.jie.notes.main.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jie.notes.R;
import com.jie.notes.base.BaseFragment;
import com.jie.notes.constant.BaseConstant;
import com.jie.notes.db.DBManager;
import com.jie.notes.main.MainActivity;
import com.jie.notes.main.Presenter.ClassReportBiz;
import com.jie.notes.main.adapter.RankingAdapter;
import com.jie.notes.main.model.DetailEntity;
import com.jie.notes.main.model.TransmitEntity;
import com.jie.notes.module.Details.DetailsActivity;
import com.jie.notes.module.takeapen.manager.SelectIconManager;
import com.jie.notes.util.BaseUtil;
import com.jie.notes.util.DateUtil;
import com.jie.notes.util.RxBus;
import com.jie.notes.widge.IconImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huangjie on 2017/11/6.
 * <p>
 * 类名：类别报表Fragment
 * 说明：
 * <p>框架bug记录：当数据量过大，旋转颜色会闪动（版本：MPAndroidChart:v3.0.3）</p>
 * <p>算法Bug:当饼图较小，点击后会旋转一圈后再回到底部，待修复</p>
 */

public class ClassReportFragment extends BaseFragment {
    public static final String TAG = "ClassReportFragment";
    private PieChart mChart;
    private String year_month;
    private final ClassReportBiz mBiz = new ClassReportBiz();
    private List<DetailEntity> filtes;
    private LinearLayout ll_bottom_detail;
    private IconImageView class_report_icon;
    private TextView tv_class_report_name;
    private TextView tv_class_report_persent;
    private ImageView iv_class_report_back;
    private TextView tv_class_report_money;
    private TextView tv_class_report_rankname;
    private RecyclerView recycler_ranking;
    private DetailEntity selectEntity;
    private View view_triangle;
    private boolean isLoad;
    private RankingAdapter rankingAdapter;
    private boolean dataChange;
    private Observable<TransmitEntity> observable;
    private List<DetailEntity> rankList;

    public static ClassReportFragment getInstance() {
        return new ClassReportFragment();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.class_report_fragment_layout;
    }

    @Override
    protected void initView(View view) {
        mChart = getViewByID(R.id.pie_charts);
        ll_bottom_detail = getViewByID(R.id.ll_bottom_detail);
        class_report_icon = getViewByID(R.id.class_report_icon);
        tv_class_report_name = getViewByID(R.id.tv_class_report_name);
        tv_class_report_persent = getViewByID(R.id.tv_class_report_persent);
        iv_class_report_back = getViewByID(R.id.iv_class_report_back);
        tv_class_report_money = getViewByID(R.id.tv_class_report_money);
        tv_class_report_rankname = getViewByID(R.id.tv_class_report_rankname);
        recycler_ranking = getViewByID(R.id.recycler_ranking);
        view_triangle = getViewByID(R.id.view_triangle);
    }

    /*
    * 逻辑说明：当数据更新了，点击"类别报表"才会触发更新逻辑,所以在onHiddenChanged中处理
    * */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if (isAdded() && dataChange) {
                refresh();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded() && dataChange && !isHidden()) {
            refresh();
        }
    }

    @Override
    protected void initEvent() {
        year_month = DateUtil.getYearAndMonth();
        initChart();
        observable = RxBus.getInstance().register(ClassReportFragment.TAG);
        observable.subscribe(new Consumer<TransmitEntity>() { //当数据更新时触发
            @Override
            public void accept(TransmitEntity entity) throws Exception {
                if (entity != null) {
                    switch (entity.getClassName()){
                        case DetailsActivity.TAG:
                            if (!isHidden()) {
                                Observable.timer(1, TimeUnit.SECONDS)
                                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<Long>() {
                                            @Override
                                            public void accept(Long aLong) throws Exception {
                                                refresh();
                                            }
                                        });
                            } else {
                                dataChange = true;
                            }
                            break;
                        case MainActivity.TAG:
                            year_month = DateUtil.getNumFormat_YearMonth(entity.getDate());
                            if (!isHidden()) {
                                Observable.timer(1000, TimeUnit.MICROSECONDS)
                                        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<Long>() {
                                            @Override
                                            public void accept(Long aLong) throws Exception {
                                                refresh();
                                            }
                                        });
                            } else {
                                dataChange = true;
                            }
                            break;
                            default:
                                year_month = DateUtil.getNumFormat_YearMonth(entity.getDate());
                                dataChange = true;
                                break;
                    }
                }
            }
        });
    }

    private void refresh(){
        dataChange = false;
        ll_bottom_detail.setVisibility(View.GONE);
        view_triangle.setVisibility(View.GONE);
        mChart.setCenterText(generateCenterText()); //设置中心文本
        mChart.setData(generatePieData()); //设置数据源
        mChart.postInvalidate();
        isLoad = false;
        starAnim();
    }

    /*
    * 初始化Chart
    *
    * */
    private void initChart() {
        //mChart的半径是根据整体的控件大小来动态计算的，设置外边距等都会影响到圆的半径
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);  //禁止显示图例
        mChart.setCenterText(generateCenterText()); //设置中心文本
        mChart.setCenterTextSize(14f); //设置文本字号
        mChart.setHoleRadius(60f); //设置中心孔半径占总圆的百分比
        mChart.setRotationAngle(90); //初始旋转角度
        mChart.setData(generatePieData()); //设置数据源
        mChart.setRotationEnabled(false);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {  //饼状图点击后调用
                float[] mDrawAngles = mChart.getDrawAngles();
                float[] mAbsoluteAngles = mChart.getAbsoluteAngles();
                float start = mChart.getRotationAngle();
                int i = (int) h.getX();
                float offset = mDrawAngles[i] / 2;
                float end = 90 - (mAbsoluteAngles[i] - offset);
                //90 - 360 + 18
                spin(start, end, i);
            }

            @Override
            public void onNothingSelected() {  //点击饼状图之外的地方后调用
            }
        });
        starAnim();
    }

    /*
    * 开始饼图动画
    * */
    public void starAnim() {
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mChart.getAnimator(), "phaseY", 0f, 1f);
        animatorY.setInterpolator(Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutQuad));
        animatorY.setDuration(1000);
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mChart.postInvalidate();
            }
        });
        animatorY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //先前设置了mChart.setRotationAngle(90); 所以初始角度是90
                if (filtes.size() > 0) {
                    float[] mDrawAngles = mChart.getDrawAngles();
                    float[] mAbsoluteAngles = mChart.getAbsoluteAngles();
//                    float toangle = (float) (filtes.get(0).money * 360 / total / 2);//计算最大的饼块的角度比例
                    float start = mChart.getRotationAngle();
                    float offset = mDrawAngles[0] / 2;
                    float end = 90 - (mAbsoluteAngles[0] - offset);
                    spin(start, end, 0);
                }
            }
        });
        animatorY.start();
    }


    private void spin(float fromangle, float toangle, final int i) {
        mChart.setRotationAngle(fromangle);
        ObjectAnimator spinAnimator = ObjectAnimator.ofFloat(mChart, "rotationAngle", fromangle,
                toangle);
        spinAnimator.setDuration(1500);
        spinAnimator.setInterpolator(Easing.getEasingFunctionFromOption(Easing.EasingOption.EaseInOutQuad));

        spinAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mChart.postInvalidate();
            }
        });
        spinAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                initRankText(i);
                initRecycleView(i);
                //是否加载排行榜动画
                if (!isLoad) {
                    showIndicatorAnim();
                }
                isLoad = true;
            }
        });
        spinAnimator.start();
    }

    private void showIndicatorAnim() {
        ValueAnimator animator = ObjectAnimator.ofFloat(ll_bottom_detail, "alpha", 0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                view_triangle.setAlpha(alpha);
            }
        });
        animator.setDuration(1000);
        animator.start();
    }

    private void initRankText(int index) {
        if (filtes.size() > index) {
            ll_bottom_detail.setVisibility(View.VISIBLE);
            view_triangle.setVisibility(View.VISIBLE);
            selectEntity = filtes.get(index);
            class_report_icon.setResId(selectEntity.icon_img);
            class_report_icon.originSet();
            tv_class_report_money.setText("-" + String.valueOf(selectEntity.money));
            tv_class_report_name.setText(selectEntity.name);
            tv_class_report_rankname.setText(selectEntity.name + "消费排行榜");
            double mainExpend = Double.valueOf(DBManager.getExpenditureTotal(year_month));
            String persent = BaseUtil.DoubleFormat((selectEntity.money / mainExpend) * 100);
            tv_class_report_persent.setText(persent + "%");
        } else {
            ll_bottom_detail.setVisibility(View.GONE);
            view_triangle.setVisibility(View.GONE);
        }
    }

    private SpannableString generateCenterText() {
        String source = "总支出\n" + DBManager.getExpenditureTotal(year_month) + "元\ns";
        SpannableString s = new SpannableString(source);
        s.setSpan(new ImageSpan(context, R.drawable.ic_zhuanzhang), s.length() - 1, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    /*
    * 生成数据,将数据有大到小排列
    * */
    protected PieData generatePieData() {
        filtes = mBiz.getFilteDetailList(year_month);
        int count = filtes.size();
        ArrayList<PieEntry> entries1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            DetailEntity value = filtes.get(i);
            Drawable drawable = SelectIconManager.getIconDrawable(context, R.color.white, value.index, value.position);
            entries1.add(new PieEntry((float) value.money, "", drawable));
        }
        PieDataSet ds1 = new PieDataSet(entries1, "");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS); //添加默认的颜色组
        ds1.setSliceSpace(2f); //饼块之间的间隙
        ds1.setDrawValues(false);  //将饼状图上的默认百分比子去掉
        ds1.setHighlightEnabled(true); // allow highlighting for DataSet
        return new PieData(ds1);
    }

    private void initRecycleView(int index) {
        if (filtes.size() > index) {
            rankList = mBiz.getRankListData(filtes.get(index).name);
            rankingAdapter = new RankingAdapter(rankList);
            recycler_ranking.setLayoutManager(new LinearLayoutManager(context));
            recycler_ranking.setAdapter(rankingAdapter);
            rankingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    intent.putExtra(BaseConstant.ID, rankList.get(position).id);
                    intent.putExtra(BaseConstant.FROMWHERE, DetailsActivity.FROM_MAIN_PAGE);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unregister(TAG, observable);
    }
}
