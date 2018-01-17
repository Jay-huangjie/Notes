package com.jie.notes.main.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jie.notes.R;
import com.jie.notes.base.BaseFragment;
import com.jie.notes.constant.BaseConstant;
import com.jie.notes.db.DBManager;
import com.jie.notes.main.MainActivity;
import com.jie.notes.main.adapter.DetailedAdapter;
import com.jie.notes.main.model.DetailEntity;
import com.jie.notes.main.model.TransmitEntity;
import com.jie.notes.main.model.TimeEntity;
import com.jie.notes.module.Details.DetailsActivity;
import com.jie.notes.module.takeapen.TakePenActivity;
import com.jie.notes.util.DateUtil;
import com.jie.notes.util.RxBus;
import com.jie.notes.widge.EmptyRecyclerView;
import com.jie.notes.widge.oubowu.stickyitemdecoration.OnItemClickListener;
import com.jie.notes.widge.oubowu.stickyitemdecoration.OnItemLongClickListener;
import com.jie.notes.widge.oubowu.stickyitemdecoration.RecyclerViewAdapter;
import com.jie.notes.widge.oubowu.stickyitemdecoration.SpaceItemDecoration;
import com.jie.notes.widge.oubowu.stickyitemdecoration.StickyHeadContainer;
import com.jie.notes.widge.oubowu.stickyitemdecoration.StickyHeadEntity;
import com.jie.notes.widge.oubowu.stickyitemdecoration.StickyItemDecoration;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by huangjie on 2017/11/6.
 * 类名：
 * 说明：明细Fragmeng
 */

public class DetailedFragment extends BaseFragment {
    public static final String TAG = "DetailedFragment";
    private EmptyRecyclerView rv_detail;
    private TextView tv_start_notes;
    private DetailedAdapter mAdapter;
    private List<StickyHeadEntity<DetailEntity>> mainData = new ArrayList<>();
    private StickyHeadContainer shc;
    private TextView tv_time;
    private TextView tv_expenditure;
    private String date;
    private View tv_empty;
    private String year_month;
    //    private DetailEntity currentEntity; //当前点击进入详情的entity
    private Observable<TransmitEntity> observable;

    public static DetailedFragment getInstance() {
        return new DetailedFragment();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.detail_fragment_layout;
    }


    @Override
    protected void initView(View view) {
        rv_detail = getViewByID(R.id.rv_detail);
        tv_start_notes = getViewByID(R.id.tv_star_notes);
        shc = getViewByID(R.id.shc);
        tv_time = shc.findViewById(R.id.tv_time);
        tv_expenditure = shc.findViewById(R.id.tv_expenditure);
        tv_empty = getViewByID(R.id.tv_empty);
    }

    @Override
    protected void initEvent() {
        date = DateUtil.getDate();
        year_month = DateUtil.getYearAndMonth();
        refreshData();
        rv_detail.setLayoutManager(new LinearLayoutManager(context));
        rv_detail.addItemDecoration(new StickyItemDecoration(shc, RecyclerViewAdapter.TYPE_STICKY_HEAD));
        rv_detail.addItemDecoration(new SpaceItemDecoration(rv_detail.getContext()));
        mAdapter = new DetailedAdapter(mainData);
        rv_detail.setAdapter(mAdapter);
        rv_detail.setEmptyView(tv_empty);
        rv_detail.setHeadContainer(shc);
        tv_start_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TakePenActivity.class);
                intent.putExtra(BaseConstant.FROMWHERE, TAG);
                intent.putExtra(BaseConstant.DATE, date);
                startActivity(intent);
            }
        });
        shc.setDataCallback(new StickyHeadContainer.DataCallback() {
            @Override
            public void onDataChange(int pos) {
                DetailEntity item = mAdapter.getData().get(pos).getData();
                tv_time.setText(DateUtil.getWeekAndDay(item.date));
                tv_expenditure.setText("支出:" + item.totalExpenditure);
            }
        });
        mAdapter.setItemClickListener(new OnItemClickListener<DetailEntity>() {
            @Override
            public void onItemClick(View view, DetailEntity data, int position) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(BaseConstant.ID, data.id);
                intent.putExtra(BaseConstant.FROMWHERE, DetailsActivity.FROM_MAIN_PAGE);
                startActivity(intent);
            }
        });

        mAdapter.setItemLongClickListener(new OnItemLongClickListener<DetailEntity>() {
            @Override
            public void onItemLongClick(View view, final DetailEntity data, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog dialog = builder.create();
                dialog.setCancelable(true);
                dialog.setTitle(data.name);
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBManager.deleteForEntity(data);
                        mAdapter.delete(position);
                        refreshData();
                        mAdapter.notifyDataSetChanged();
                        RxBus.getInstance().post(MainActivity.TAG, year_month);
                        RxBus.getInstance().post(ClassReportFragment.TAG, new TransmitEntity(DetailedFragment.TAG, year_month));
                    }
                });
                dialog.show();
            }
        });
        observable = RxBus.getInstance().register(TAG);
        observable.subscribe(new Consumer<TransmitEntity>() {  //处理数据更新
            @Override
            public void accept(TransmitEntity entity) throws Exception {
                if (entity != null) {
                    String date_val = entity.getDate();
                    if (!TextUtils.isEmpty(date_val)) {
                        date = date_val;
                        year_month = DateUtil.getNumFormat_YearMonth(date_val);
                    }
                    refreshData();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    //更新数据 date:yyyy-mm
    private void refreshData() {
        mainData.clear();
        List<TimeEntity> data = DBManager.getTimeEntityToMonth(year_month);
        for (TimeEntity entity : data) {
            if (entity.detailed.size() > 0) {
                DetailEntity detailed = new DetailEntity();
                detailed.itemType = RecyclerViewAdapter.TYPE_STICKY_HEAD;
                detailed.date = entity.date;
                detailed.totalExpenditure = DBManager.getExpenditureTotalForDay(entity.date);
                mainData.add(new StickyHeadEntity<DetailEntity>(detailed, RecyclerViewAdapter.TYPE_STICKY_HEAD));
                for (DetailEntity item : entity.detailed) {
                    mainData.add(new StickyHeadEntity<DetailEntity>(item, RecyclerViewAdapter.TYPE_DATA));
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().unregister(TransmitEntity.class, observable);
    }
}
