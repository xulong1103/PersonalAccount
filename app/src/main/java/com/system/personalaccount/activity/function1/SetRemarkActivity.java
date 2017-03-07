package com.system.personalaccount.activity.function1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.system.personalaccount.R;
import com.system.personalaccount.db.AccountDB;
import com.system.personalaccount.model.Remark;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 新建备注界面
 */
public class SetRemarkActivity extends Activity {

    /** 备注时间 **/
    private TextView time;
    /** 备注标题 **/
    private EditText title;
    /** 备注内容 **/
    private EditText content;
    /** 提交 **/
    private Button commit;
    /** 返回 **/
    private Button back;
    /** 数据库对象 **/
    private AccountDB accountDB;
    /** 日期格式化 **/
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /** 使用日期格式化得到当前时间 **/
    private String nowTime = df.format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set_remark);
        /** 初始化界面时不显示软键盘 **/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        accountDB = AccountDB.getInstance(this);
        initViews();
        /** 提交点击事件 **/
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** 判断标题是否为空 **/
                if (title.getText().toString().equals("")) {
                    Toast.makeText(SetRemarkActivity.this, "标题不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                /** 将信息封装成备注对象 **/
                Remark remark = new Remark();
                remark.setTitle(title.getText().toString());
                remark.setContent(content.getText().toString());
                remark.setDate(nowTime);
                /** 创建备注 **/
                int result = accountDB.createRemark(SetRemarkActivity.this, remark);
                /** 判断创建是否成功 **/
                if (result == 1) {
                    Toast.makeText(SetRemarkActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, null);
                    finish();
                } else {
                    Toast.makeText(SetRemarkActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /** 返回键的监听事件 **/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 初始化各个组件
     */
    private void initViews() {
        time = (TextView) findViewById(R.id.setRemark_tv_time);
        title = (EditText) findViewById(R.id.setRemark_tv_title);
        content = (EditText) findViewById(R.id.setRemark_tv_content);
        commit = (Button) findViewById(R.id.setRemark_bt_commit);
        back = (Button) findViewById(R.id.setRemark_btn_back);
        time.setText(nowTime);
    }

    /**
     * 设置结果码为取消，不传送数据，备注界面列表不更新
     */
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        super.onBackPressed();
    }
}
