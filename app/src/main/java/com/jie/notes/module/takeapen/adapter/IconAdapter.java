package com.jie.notes.module.takeapen.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jie.notes.R;
import com.jie.notes.module.takeapen.View.OnIconClickListence;
import com.jie.notes.module.takeapen.manager.SelectIconManager;
import com.jie.notes.module.takeapen.model.Icon;
import com.jie.notes.widge.IconImageView;

import java.util.List;

/**
 * Created by huangjie on 2017/11/6.
 * 类名：
 * 说明：图标适配器
 * 适配器中不应该做数据查询工作，把结果判断好传入进来
 */

public class IconAdapter extends BaseQuickAdapter<Icon, BaseViewHolder> {
    private OnIconClickListence l;
    private IconImageView icon_img;
    private LinearLayout item_root;
    private boolean hasLoveIcon; //是否添加了喜欢的图标
    private boolean fromDetail; //是否来自详情页
    private String name; //详情页传过来的icon名称

    public IconAdapter(@Nullable List<Icon> data, boolean hasLoveIcon, boolean fromDetail) {
        super(R.layout.icon_item, data);
        this.hasLoveIcon = hasLoveIcon;
        this.fromDetail = fromDetail;
    }

    public void setIcon_name(String icon_name) {
        name = icon_name;
    }


    public void setOnIconClickListence(OnIconClickListence L) {
        this.l = L;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Icon item) {
        final IconImageView icon_img = helper.getView(R.id.iv_icon_img);
        final TextView icon_name = helper.getView(R.id.tv_icon_name);
        final LinearLayout item_root = helper.getView(R.id.item_root);
        this.icon_img = icon_img;
        this.item_root = item_root;
        icon_img.setResId(item.icon_img);
        item_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.iconType != 3) {
                    SelectIconManager.ChangesIcomUi(icon_img, item);
                }
                if (l != null) {
                    item.position = helper.getAdapterPosition();
                    l.iconClick(item);
                }
            }
        });
        if (fromDetail) {
            if (hasLoveIcon) {
                if (!TextUtils.isEmpty(name) && item.index == -1) {
                    judge(name.equals(item.icon_name));
                } else {
                    clearSelect();
                }
            } else {
                judge(item.position == helper.getAdapterPosition());
            }
        } else {
            if (hasLoveIcon) {
                judge(item.index == -1 && helper.getAdapterPosition() == 0);
            } else {
                judge(item.index == 0 && helper.getAdapterPosition() == 0);
            }
        }
        icon_name.setText(item.icon_name);
    }


    //选中
    private void select() {
        icon_img.originSet();
        item_root.performClick();
    }

    //未选中
    private void clearSelect() {
        icon_img.ClearSet();
    }

    //判断状态
    private void judge(boolean isSatisfied) {
        if (isSatisfied) {
            select();
        } else {
            clearSelect();
        }
    }

}
