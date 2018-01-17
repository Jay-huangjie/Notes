package com.jie.notes.main.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.notes.R;
import com.jie.notes.main.model.DetailEntity;

import java.util.List;

/**
 * Created by huangjie on 2018/1/9.
 * 类名：
 * 说明：
 */

public class RankingAdapter extends BaseQuickAdapter<DetailEntity,BaseViewHolder> {

    public RankingAdapter(@Nullable List<DetailEntity> data) {
        super(R.layout.ranking_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DetailEntity item) {
        TextView tv_rank_position = helper.getView(R.id.tv_rank_position);
        TextView tv_rank_name = helper.getView(R.id.tv_rank_name);
        TextView tv_rank_money = helper.getView(R.id.tv_rank_money);
        int rank = helper.getAdapterPosition()+1;
        if (rank<4){
            tv_rank_position.setTextColor(Color.parseColor("#FF6633"));
        }else {
            tv_rank_position.setTextColor(Color.parseColor("#bbbbbb"));
        }
        tv_rank_position.setText(String.valueOf(rank));
        if (TextUtils.isEmpty(item.remark)){
            tv_rank_name.setText(item.name);
        }else {
            tv_rank_name.setText(item.remark);
        }
        tv_rank_money.setText("-"+item.money);
    }
}
