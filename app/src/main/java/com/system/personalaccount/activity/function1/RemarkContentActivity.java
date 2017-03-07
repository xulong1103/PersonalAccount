package com.system.personalaccount.activity.function1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.system.personalaccount.R;
import com.system.personalaccount.db.AccountDB;
import com.system.personalaccount.model.Remark;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 备注详细界面
 */
public class RemarkContentActivity extends Activity {

    /** 返回键 **/
    private Button back;
    /** 修改和保存键 **/
    private Button button;
    /** 备注时间 **/
    private TextView time;
    /** 备注标题 **/
    private EditText title;
    /** 备注内容 **/
    private EditText content;
    /** 传过来的备注对象 **/
    private Remark remarkExtra;
    /** 点击状态 **/
    private int state = 1;
    /** 数据库对象 **/
    private AccountDB accountDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_remark_content);
        /** 初始化时不显示软键盘 **/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initViews();
        accountDB = AccountDB.getInstance(RemarkContentActivity.this);
        /** 取得其他页面传过来的备注对象 **/
        remarkExtra = (Remark) getIntent().getSerializableExtra("remark");
        /** 将对应属性设置为对应值 **/
        time.setText(remarkExtra.getDate());
        title.setText(remarkExtra.getTitle());
        content.setText(remarkExtra.getContent());
        /** 返回键 **/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, null);
                finish();
            }
        });
        /** 此按钮点击一次变为确定，再次点击提交更改 **/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** 点击状态为1时，变成确定，标题和内容此时可编辑 **/
                if (state == 1) {
                    changeState();
                    button.setText("确认");
                    setViewEnable();
                }
                /** 点击2次时，提交修改的数据 **/
                else if (state == 2) {
                    Date date = new Date();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String nowTime = df.format(date);
                    changeState();
                    button.setText("修改");
                    setViewUnEnable();
                    /** 封装成备注对象 **/
                    Remark remark = new Remark();
                    remark.setTitle(title.getText().toString());
                    remark.setContent(content.getText().toString());
                    remark.setDate(nowTime);
                    /** 修改备注 **/
                    int result = accountDB.updateRemark(RemarkContentActivity.this, remarkExtra.getDate(), "change", remark);
                    if (result == 1) {
                        setResult(RESULT_OK, null);
                    } else {
                        setResult(RESULT_CANCELED, null);
                    }
                    finish();
                }
            }
        });
    }

    /**
     * 设置标题内容不可被编辑
     */
    private void setViewUnEnable() {
        title.setEnabled(false);
        content.setEnabled(false);
    }

    /**
     * 设置标题内容可被编辑
     */
    private void setViewEnable() {
        title.setEnabled(true);
        content.setEnabled(true);
    }

    /**
     * 转换点击状态
     */
    private void changeState() {
        if (state % 2 == 1) {
            state = 2;
        } else if (state % 2 == 0) {
            state = 1;
        }
    }

    /**
     * 初始化各个组件
     */
    private void initViews() {
        back = (Button) findViewById(R.id.RemarkContent_btn_back);
        button = (Button) findViewById(R.id.change_button);
        time = (TextView) findViewById(R.id.RemarkContent_tv_time);
        title = (EditText) findViewById(R.id.RemarkContentActivity_title);
        content = (EditText) findViewById(R.id.RemarkContentActivity_content);
        setViewUnEnable();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        super.onBackPressed();
    }
}
