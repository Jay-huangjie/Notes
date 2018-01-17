package com.jie.notes.module.takeapen;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jie.notes.R;
import com.jie.notes.base.BaseActivity;
import com.jie.notes.constant.BaseConstant;
import com.jie.notes.db.DBManager;
import com.jie.notes.main.MainActivity;
import com.jie.notes.main.fragment.ClassReportFragment;
import com.jie.notes.main.fragment.DetailedFragment;
import com.jie.notes.main.model.DetailEntity;
import com.jie.notes.main.model.TimeEntity;
import com.jie.notes.main.model.TransmitEntity;
import com.jie.notes.module.Details.DetailsActivity;
import com.jie.notes.module.takeapen.Presenter.IconPresenter;
import com.jie.notes.module.takeapen.View.OnIconClickListence;
import com.jie.notes.module.takeapen.adapter.IconAdapter;
import com.jie.notes.module.takeapen.adapter.ViewPagerAdapter;
import com.jie.notes.module.takeapen.manager.SelectIconManager;
import com.jie.notes.module.takeapen.model.Icon;
import com.jie.notes.util.BaseUtil;
import com.jie.notes.util.DateUtil;
import com.jie.notes.util.RxBus;
import com.jie.notes.util.RxJavaUtil;
import com.jie.notes.util.StatusBarUtil;
import com.jie.notes.widge.KeyBoard.CustomKeyBoard;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * 记一笔页面
 * 有两个地方进入，首页和详情页面
 * 详情页面进入需要展示页面数据并定位到具体的icon,无备注按钮
 * 产品逻辑：
 * 新用户进入显示4个图标展示页面，最后一个页面为用户自定义界面
 * 当用户记录了数据后，会新开一个界面在第一的位置，并把记录后的图标加入到该页面
 * 图标加入逻辑：总共展示10个图标，当新加入一个图标，没在这10个中，会将最后一个也就是最先加入的图标删除，把新加入的放到第一个
 * 当新图标在这10个图标中已存在，那就把已存在的删除，把新的放在首位，其余的往后排序
 * <p>
 * 自定义图标逻辑：新加入的图标从第4页往后排。最多添加一页，也就是9个。
 * <p>
 * 从详情页进入其实就是修改数据，如果修改了图标，就将修改的图标加入我喜欢的图标库
 * 通过名称对比判断是否修改了图标
 */
public class TakePenActivity extends BaseActivity implements OnIconClickListence {
    public static final String TAG = "TakePenActivity";
    private Button btn_date;
    private CustomKeyBoard keyboard;
    private EditText et_input_num;
    private ImageView iv_close_input;
    private DetailEntity detailed;
    private TextView tv_comm_title;
    private String date;
    private TimeEntity entity;
    private Icon select_icon;
    private ViewPagerAdapter pagerAdapter;
    private ViewPager viewpager;
    private LinearLayout ll_dot_content;
    private IconPresenter presenter = new IconPresenter();
    private List<View> views = new ArrayList<>();
    private int lastNum = 0;
    private Button btn_backup;
    private String remark;
    private String money;
    private String from;
    private boolean hasLove;
    private String oldName;
    private Observable<String> observable;
    private ImageView iv_camera;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_take_pen;
    }

    @Override
    protected void initView() {
        tv_comm_title = ((TextView) getViewID(R.id.tv_comm_title));
        btn_date = getViewID(R.id.btn_date);
        ll_dot_content = (LinearLayout) getViewID(R.id.ll_dot_content);
        keyboard = (CustomKeyBoard) getViewID(R.id.keyboard);
        et_input_num = (EditText) getViewID(R.id.et_input_num);
        iv_close_input = (ImageView) getViewID(R.id.iv_close_input);
        viewpager = (ViewPager) getViewID(R.id.viewpager);
        btn_backup = (Button) getViewID(R.id.btn_backup);
        iv_camera = (ImageView) getViewID(R.id.iv_camera);
        StatusBarUtil.setColor(this, Color.parseColor("#FFFFFF"));
    }

    @Override
    protected void initEvent() {
        from = getIntent().getStringExtra(BaseConstant.FROMWHERE);
        if (DetailedFragment.TAG.equals(from)) {
            detailed = new DetailEntity();
            tv_comm_title.setText("支出");
            date = getIntent().getStringExtra(BaseConstant.DATE);
            btn_date.setText(DateUtil.getMonthAndDay(DateUtil.StrToDate(date)));
            et_input_num.requestFocus();
            et_input_num.setText("0");
        } else if (DetailsActivity.TAG.equals(from)) {
            long id = getIntent().getLongExtra(BaseConstant.ID, 0);
            detailed = DBManager.getDetailEntity(id);
            if (detailed != null) {
                tv_comm_title.setText("支出");
                date = detailed.date;
                oldName = detailed.name;
                btn_date.setText(DateUtil.getMonthAndDay(DateUtil.StrToDate(date)));
                et_input_num.requestFocus();
                et_input_num.setText(String.valueOf(detailed.money));
                remark = detailed.remark;
            } else {
                showShortToast("查询数据失败---->id=0");
            }
            btn_backup.setVisibility(View.INVISIBLE);
        }
        initViewPager();
        initDot();
        et_input_num.setSelection(et_input_num.getText().length());
        BaseUtil.showCursor(this, et_input_num);
        keyboard.setEditText(et_input_num, iv_close_input);
        keyboard.setKbOnClickListener(new CustomKeyBoard.KbOnClickListener() {
            @Override
            public void confirm(final String tempTxt) {  //完成按钮监听
                RxJavaUtil.StarIOSubscribeTransaction(new RxJavaUtil.ToSubscribe() {

                    @Override
                    public TimeEntity subscribe() {
                        //获取金额，日期
                        detailed.money = Float.valueOf(tempTxt);
                        if (DetailedFragment.TAG.equals(from)) {
                            detailed.date = date;
                        }
                        detailed.remark = remark;
                        //判断是否当日是否记录过
                        if (DBManager.TaDayisExist(date)) {
                            entity = DBManager.getTaDayEntity(date);
                        } else {
                            entity = new TimeEntity();
                        }
                        entity.set(date);
                        //添加一条明细。从首页来就添加一条，从详情来就修改一条
                        if (DetailedFragment.TAG.equals(from)) {
                            entity.detailed.add(detailed);
                            if (select_icon != null) {
                                //添加一条记录到喜欢的列表
                                DBManager.insertLikeIcon(select_icon);
                            }
                        } else {
                            //更新detail表数据
                            DBManager.getDetailedBox().put(detailed);
                            if (select_icon != null && !select_icon.icon_name.equals(oldName)) {
                                DBManager.insertLikeIcon(select_icon);
                            }
                        }
                        DBManager.getTimeEntityBox().put(entity);
                        return entity;
                    }
                }, new RxJavaUtil.ToNext() {
                    @Override
                    public void onNext(Object t) {
                        if (!TextUtils.isEmpty(date)) { //发送数据给相应的类,通知他们更新数据
                            RxBus.getInstance().post(MainActivity.TAG, DateUtil.getNumFormat_YearMonth(date));
                            TransmitEntity entity = new TransmitEntity(TakePenActivity.TAG);
                            entity.setDate(date);
                            RxBus.getInstance().post(DetailedFragment.TAG, entity);
                            RxBus.getInstance().post(ClassReportFragment.TAG, entity);
                        }
                        finish();
                    }

                    @Override
                    public void onError(String errorMsg) {
                        showShortToast("数据存储出错:" + errorMsg);
                    }
                });
            }

            @Override
            public void inputTextChange(String tempTxt) {
                money = tempTxt;
            }
        });
        iv_close_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_input_num.setText("0");
            }
        });
        btn_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TakePenActivity.this, DetailsActivity.class);
                intent.putExtra(BaseConstant.FROMWHERE, DetailsActivity.FROM_ICON_PAGE);
                intent.putExtra(BaseConstant.REMARK, remark);
                if (select_icon != null) {
                    intent.putExtra(BaseConstant.ICON_NAME, select_icon.icon_name);
                    intent.putExtra(BaseConstant.ICON_IMG, select_icon.icon_img);
                }
                intent.putExtra(BaseConstant.MONEY, money);
                startActivity(intent);
            }
        });
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShortToast("该功能Jay还在全力开发中");
            }
        });
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShortToast("该功能Jay还在全力开发中");
            }
        });
        observable = RxBus.getInstance().register(TAG);
        observable.subscribe(new Consumer<String>() {  //详情页修改备注回调
            @Override
            public void accept(String remark) throws Exception {
                TakePenActivity.this.remark = remark;
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SelectIconManager.clearAll();
    }


    /**
     * Icon点击选择的回调
     *
     * @param icon 回调的数据
     */
    @Override
    public void iconClick(Icon icon) {
        if (icon.iconType == 3) {
            final FrameLayout roots = new FrameLayout(context);
            final EditText editText = new EditText(context);
            editText.setHint("最多支持四个字");
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
            roots.addView(editText);
            FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) editText.getLayoutParams();
            fl.leftMargin = BaseUtil.dip2px(16);
            fl.rightMargin = BaseUtil.dip2px(16);

            new AlertDialog.Builder(this)
                    .setTitle("请输入类别名称")
                    .setView(roots)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String contentValue = editText.getText().toString();
                            if (TextUtils.isEmpty(contentValue)) {
                                showShortToast("请输入类型名称");
                                return;
                            }
                            DBManager.insertClassIcon(contentValue);
                            initRecycleView(views.get(views.size() - 1), 3);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        } else {
            this.select_icon = icon;
            //复制数据到detail表中
            detailed.index = icon.index;
            detailed.position = icon.position;
            detailed.icon_img = icon.icon_img;
            detailed.name = icon.icon_name;
        }
    }

    private void initViewPager() {
        hasLove = DBManager.hasLove(); //是否有喜欢的图标数据
        if (hasLove) {//有喜欢的图标数据
            View view = createView();
            views.add(view);
            initRecycleView(view, -1);
        }
        for (int i = 0; i < 4; i++) {
            views.add(createView());
            if (hasLove) { //bug 记录，如果有喜欢的图标，后面获取的View应该要+1，不然会获取到喜欢的图标的数据
                initRecycleView(views.get(i + 1), i);
            } else {
                initRecycleView(views.get(i), i);
            }
        }
        pagerAdapter = new ViewPagerAdapter(views);
        viewpager.setAdapter(pagerAdapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ll_dot_content.getChildAt(lastNum).setSelected(false);
                ll_dot_content.getChildAt(position).setSelected(true);
                lastNum = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setOffscreenPageLimit(views.size());
    }

    private void initDot() {
        for (int i = 0; i < views.size(); i++) {
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(BaseUtil.dip2px(7), BaseUtil.dip2px(7));
            if (i == 0) {
                if (hasLove) {
                    imageView.setImageResource(R.drawable.dot_select_love);
                    ll.height = BaseUtil.dip2px(9);
                    ll.width = BaseUtil.dip2px(9);
                } else {
                    imageView.setImageResource(R.drawable.dot_select);
                }
                imageView.setSelected(true);
            } else {
                ll.leftMargin = BaseUtil.dip2px(6);
                imageView.setImageResource(R.drawable.dot_select);
            }
            imageView.setLayoutParams(ll);
            ll_dot_content.addView(imageView);
        }
    }

    private View createView() {
        return LayoutInflater.from(context).inflate(R.layout.pen_icon_fragment_layout, viewpager, false);
    }

    private void initRecycleView(View view, int index) {
        RecyclerView recyclerView = view.findViewById(R.id.rlv_icon_view);
        List<Icon> data = presenter.getData(index);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        IconAdapter mAdapter = new IconAdapter(data, hasLove, DetailsActivity.TAG.equals(from));
        if (DetailsActivity.TAG.equals(from)) { //从详情页过来要定位到图标信息
            if (hasLove) { //有喜爱的图标信息,就根据名称定位到图片
                mAdapter.setIcon_name(detailed.name);
            } else {
                if (detailed.index == index && detailed.position != 0) {
                    data.get(detailed.position).position = detailed.position;
                }
            }
        }
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnIconClickListence(this);
    }

//    private final class AddClassDialog extends AlertDialog
}
