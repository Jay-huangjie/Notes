package com.jie.notes.module.Details;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jie.notes.R;
import com.jie.notes.base.BaseActivity;
import com.jie.notes.constant.BaseConstant;
import com.jie.notes.db.DBManager;
import com.jie.notes.main.fragment.ClassReportFragment;
import com.jie.notes.main.fragment.DetailedFragment;
import com.jie.notes.main.model.DetailEntity;
import com.jie.notes.main.model.TransmitEntity;
import com.jie.notes.module.takeapen.TakePenActivity;
import com.jie.notes.util.DateUtil;
import com.jie.notes.util.RxBus;
import com.jie.notes.util.StatusBarUtil;
import com.jie.notes.widge.IconImageView;
import com.jie.notes.widge.TopBar;

import java.util.Date;

/**
 * Created by huangjie on 2017/11/18.
 * <p>
 * 类名：详情页面
 * 说明：产品逻辑：
 * <p> 跳转逻辑
 * 从记一笔页面点击备注可以跳转到此页面
 * 从明细页面点击item可以跳转到此页面
 * </p>
 * <p>
 * <h1> 页面逻辑
 * <p>
 * <h2>明细页面</h2>
 * 点击icon的详情部分可以跳转到记一笔页面修改该笔记录
 * <p>
 * <h2>记一笔页面</h2>
 * 只可以修改备注,其余无事件
 * </h1>
 */

public class DetailsActivity extends BaseActivity {
    public static final String TAG = "DetailsActivity";

    // 从首页点击item进入
    public static final int FROM_MAIN_PAGE = 0;
    //从记一笔页面的备注页进入
    public static final int FROM_ICON_PAGE = 1;

    private TextView tv_date;
    private LinearLayout ll_detail_main;
    private IconImageView iiv_detail_img;
    private TextView tv_detail_name;
    private TextView tv_detail_money;
    private EditText et_detail_remark;
    private DetailEntity entity;
    private TextView tv_empty;
    private String remark;
    private int from;
    private long id;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_detail_layout;
    }

    @Override
    protected void initView() {
        ((TopBar) getViewID(R.id.detail_topbar)).setTitle("详情");
        tv_date = (TextView) getViewID(R.id.tv_date);
        tv_detail_name = (TextView) getViewID(R.id.tv_detail_name);
        tv_detail_money = (TextView) getViewID(R.id.tv_detail_money);
        ll_detail_main = (LinearLayout) getViewID(R.id.ll_detail_main);
        iiv_detail_img = (IconImageView) getViewID(R.id.iiv_detail_img);
        et_detail_remark = (EditText) getViewID(R.id.et_detail_remark);
        tv_empty = (TextView) getViewID(R.id.tv_empty);
        StatusBarUtil.setColor(this, Color.parseColor("#FFFFFF"));
    }

    @Override
    protected void initEvent() {
        from = getIntent().getIntExtra(BaseConstant.FROMWHERE, FROM_MAIN_PAGE);
        if (from == FROM_MAIN_PAGE) {
            id = getIntent().getLongExtra(BaseConstant.ID, 0);
            ll_detail_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailsActivity.this, TakePenActivity.class);
                    intent.putExtra(BaseConstant.FROMWHERE, TAG);
                    intent.putExtra(BaseConstant.ID, id);
                    startActivity(intent);
                }
            });
        } else if (from == FROM_ICON_PAGE) {
            Intent i = getIntent();
            String icon_name = i.getStringExtra(BaseConstant.ICON_NAME);
            int icon_img = i.getIntExtra(BaseConstant.ICON_IMG, 0);
            String money = i.getStringExtra(BaseConstant.MONEY);
            remark = i.getStringExtra(BaseConstant.REMARK);
            Date date = new Date();
            tv_date.setText(new StringBuilder().append(DateUtil.getMonthAndDayToChinese(date)).append("\t").append(DateUtil.getWeek(date)));
            tv_detail_name.setText(icon_name);
            iiv_detail_img.setResId(icon_img);
            iiv_detail_img.originSet();
            if (TextUtils.isEmpty(money))
                money = "0";
            tv_detail_money.setText(new StringBuilder("-").append(money));
            et_detail_remark.setText(remark);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (from == FROM_MAIN_PAGE) {
            entity = DBManager.getDetailEntity(id);
            if (entity != null) {
                Date date = DateUtil.StrToDate(entity.date);
                tv_date.setText(new StringBuilder().append(DateUtil.getMonthAndDayToChinese(date)).append("\t").append(DateUtil.getWeek(date)));
                iiv_detail_img.setResId(entity.icon_img);
                iiv_detail_img.originSet();
                tv_detail_money.setText(new StringBuilder("-").append(entity.money));
                tv_detail_name.setText(entity.name);
                et_detail_remark.setText(entity.remark);
            } else {
                tv_empty.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void finish() {
        String remark = et_detail_remark.getText().toString();
        if (from == FROM_ICON_PAGE) {
            RxBus.getInstance().post(TakePenActivity.TAG, remark);
        } else if (from == FROM_MAIN_PAGE) {
            if (entity != null) {
                if (!remark.equals(entity.remark)){
                    entity.remark = remark;
                    DBManager.getDetailedBox().put(entity);  //更新备注
                    TransmitEntity rxbusEntity = new TransmitEntity(DetailsActivity.TAG);
                    rxbusEntity.setRemark(remark);
                    RxBus.getInstance().post(DetailedFragment.TAG, rxbusEntity);
                    RxBus.getInstance().post(ClassReportFragment.TAG, rxbusEntity);
                }
            }
        }
        super.finish();
    }
}
