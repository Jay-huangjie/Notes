package com.jie.notes.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.jie.notes.R;
import com.jie.notes.main.model.DetailEntity;
import com.jie.notes.util.DateUtil;
import com.jie.notes.widge.IconImageView;
import com.jie.notes.widge.oubowu.stickyitemdecoration.FullSpanUtil;
import com.jie.notes.widge.oubowu.stickyitemdecoration.RecyclerViewAdapter;
import com.jie.notes.widge.oubowu.stickyitemdecoration.RecyclerViewHolder;
import com.jie.notes.widge.oubowu.stickyitemdecoration.StickyHeadEntity;

import java.util.List;


/**
 * 账单适配器
 */
public class DetailedAdapter extends RecyclerViewAdapter<DetailEntity, StickyHeadEntity<DetailEntity>> {

    public DetailedAdapter(List<StickyHeadEntity<DetailEntity>> data) {
        super(data);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        FullSpanUtil.onAttachedToRecyclerView(recyclerView, this, TYPE_STICKY_HEAD);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        FullSpanUtil.onViewAttachedToWindow(holder, this, TYPE_STICKY_HEAD);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        switch (viewType) {
            case TYPE_STICKY_HEAD:
                return R.layout.item_header;
            case TYPE_DATA:
                return R.layout.item_content;
        }
        return 0;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int viewType, int position, DetailEntity item) {
        int type = holder.getItemViewType();
        switch (type) {
            case TYPE_STICKY_HEAD:
                TextView tv_time = (TextView) holder.getView(R.id.tv_time);
                TextView tv_expenditure = (TextView) holder.getView(R.id.tv_expenditure);
                tv_time.setText(DateUtil.getWeekAndDay(item.date));
                tv_expenditure.setText("支出:" + item.totalExpenditure);
                break;
            case TYPE_DATA:
                TextView tv_name = (TextView) holder.getView(R.id.tv_name);
                TextView tv_money = (TextView) holder.getView(R.id.tv_money);
                if (TextUtils.isEmpty(item.remark)){
                    tv_name.setText(item.name);
                }else {
                    tv_name.setText(item.remark);
                }
                IconImageView icon_Image = (IconImageView) holder.getView(R.id.iiv_type);
                icon_Image.setResId(item.icon_img);
                tv_money.setText("-" + String.valueOf(item.money));
                icon_Image.originSet();
                break;
        }
    }
}
