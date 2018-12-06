package com.jie.notes.widge.keyBoard;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jie.notes.R;

// 一个简单的自定义数字键盘
public class CustomKeyBoard extends FrameLayout implements
        View.OnClickListener {

    private Button Row00, Row01, Row02;
    private Button Row10, Row11, Row12;
    private Button Row20, Row21, Row22;
    private Button Row30, Row31;
    //    private ImageButton Row32;
    private Button btn_confirm;
    private ImageButton btn_clear;
    private KbOnClickListener kcl;
    private TextView tv;
    private long lastTime;

    public CustomKeyBoard(Context context) {
        this(context, null);
    }

    public CustomKeyBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomKeyBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initKeyBoard(context);
    }

    public void setKbOnClickListener(KbOnClickListener l) {
        this.kcl = l;
    }

    public void setEditText(final TextView tv) {
        this.tv = tv;
        if (tv != null) {
            tv.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (kcl != null) {
                        kcl.inputTextChange(tv.getText().toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    public void setEditText(final TextView tv, final ImageView clear_imgae) {
        this.tv = tv;
        clear_imgae.setVisibility(INVISIBLE);
        if (tv != null) {
            tv.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (kcl != null) {
                        kcl.inputTextChange(tv.getText().toString());
                    }
                    if (s.length() > 0) {
                        clear_imgae.setVisibility(VISIBLE);
                    } else {
                        tv.setText("0");
                        clear_imgae.setVisibility(INVISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void initKeyBoard(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout content =
                (LinearLayout) inflater.inflate(R.layout.custom_layout, this, false);
        content.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        addView(content);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_clear = (ImageButton) findViewById(R.id.btn_clear);
        Row00 = (Button) findViewById(R.id.row_00);
        Row01 = (Button) findViewById(R.id.row_01);
        Row02 = (Button) findViewById(R.id.row_02);

        Row10 = (Button) findViewById(R.id.row_10);
        Row11 = (Button) findViewById(R.id.row_11);
        Row12 = (Button) findViewById(R.id.row_12);

        Row20 = (Button) findViewById(R.id.row_20);
        Row21 = (Button) findViewById(R.id.row_21);
        Row22 = (Button) findViewById(R.id.row_22);

        Row30 = (Button) findViewById(R.id.row_30);
        Row31 = (Button) findViewById(R.id.row_31);
        setOnClickListener(this);
    }


    public void setOnClickListener(OnClickListener lsn) {
        Row00.setOnClickListener(lsn);
        Row01.setOnClickListener(lsn);
        Row02.setOnClickListener(lsn);

        Row10.setOnClickListener(lsn);
        Row11.setOnClickListener(lsn);
        Row12.setOnClickListener(lsn);

        Row20.setOnClickListener(lsn);
        Row21.setOnClickListener(lsn);
        Row22.setOnClickListener(lsn);

        Row30.setOnClickListener(lsn);
        Row31.setOnClickListener(lsn);

        btn_confirm.setOnClickListener(lsn);
        btn_clear.setOnClickListener(lsn);

    }


    public void getClickedText(int id) {
        switch (id) {
            case R.id.row_00:
                sendNum(Row00);
                break;
            case R.id.row_01:
                sendNum(Row01);
                break;
            case R.id.row_02:
                sendNum(Row02);
                break;
            case R.id.row_10:
                sendNum(Row10);
                break;
            case R.id.row_11:
                sendNum(Row11);
                break;
            case R.id.row_12:
                sendNum(Row12);
                break;
            case R.id.row_20:
                sendNum(Row20);
                break;
            case R.id.row_21:
                sendNum(Row21);
                break;
            case R.id.row_22:
                sendNum(Row22);
                break;
            case R.id.row_30:
                sendNum(Row30);
                break;
            case R.id.row_31:
                sendNum(Row31);
                break;
            case R.id.btn_clear:
                Del(tv);
                break;
            case R.id.btn_confirm:
                long currTime = System.currentTimeMillis();
                if (currTime - lastTime > 500) { //防止连续点击
                    if (kcl != null && tv != null) {
                        kcl.confirm(tv.getText().toString());
                    }
                }
                lastTime = currTime;
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        getClickedText(arg0.getId());
    }

    private void sendNum(TextView tvNum) {
        if (kcl != null) {
            String num = tvNum.getText().toString().trim();
            inPut(tv, num);
        }
    }

    private void Del(TextView et) {
        if (et != null) {
            if (et.isFocused()) {
                String str = et.getText().toString();
                if (!TextUtils.isEmpty(str)) {
                    str = str.substring(0, str.length() - 1);
                    et.setText(str);
                }
                if (et instanceof EditText) {
                    ((EditText) et).setSelection(et.getText().length());
                }
            }
        }
    }

    private void inPut(TextView et, String tempTxt) {
        try {
            if (et.isFocused()) {
                String str = et.getText().toString();
                if (str.length() == 1 && str.equals("0")) {
                    str = tempTxt;
                } else {
                    str = str + tempTxt;
                }
                String regex = "^[0-9]+\\.{0,1}[0-9]{0,2}$";
                if (str.matches(regex)) {
                    et.setText(str);
                }
                if (et instanceof EditText) {
                    ((EditText) et).setSelection(et.getText().length());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface KbOnClickListener {
        void confirm(String tempTxt);  //确认

//        void clear();  //清空

        void inputTextChange(String tempTxt);  //正在输入中
    }

}