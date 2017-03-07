package com.system.personalaccount.activity.function1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.system.personalaccount.R;
import com.system.personalaccount.activity.function2.SecondActivity;
import com.system.personalaccount.activity.setting.SystemSettingActivity;
import com.system.personalaccount.db.AccountDB;
import com.system.personalaccount.util.AddTypeDialog;
import com.system.personalaccount.util.Constant;

/**
 * 第一功能页
 */
public class FirstActivity extends Activity implements View.OnClickListener {

    /** 四个图片按钮 **/
    private ImageView create;
    private ImageView search;
    private ImageView set;
    private ImageView remark;

    /** 创建记录对话框 **/
    private AddTypeDialog dialog;
    /** 数据库对象 **/
    private AccountDB accountDB;
    /** 退出按钮 **/
    private ImageButton exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 设置系统没有标题 **/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_first);
        /** 取得数据库对象 **/
        accountDB = AccountDB.getInstance(this);
        initViews();
    }

    /**
     * 初始化各个组件，并设置监听
     */
    private void initViews() {
        /** 通过id找到对应组件 **/
        create = (ImageView) findViewById(R.id.first_create);
        search = (ImageView) findViewById(R.id.first_search);
        set = (ImageView) findViewById(R.id.first_set);
        remark = (ImageView) findViewById(R.id.first_remark);
        exit= (ImageButton) findViewById(R.id.exit);
        /** 给各个组件设置监听 **/
        create.setOnClickListener(this);
        search.setOnClickListener(this);
        set.setOnClickListener(this);
        remark.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:
                /** 调用返回键的方法 **/
                onBackPressed();
                break;
            case R.id.first_create:
                /** 自定义对话框中的监听，实现两个按钮的回调方法 **/
                AddTypeDialog.TypeOnClickListener listener = new AddTypeDialog.TypeOnClickListener() {
                    /** 点击确定时，创建记录 **/
                    @Override
                    public void onPositiveButton(String name) {
                        /** 创建记录 **/
                        int result = accountDB.createRecord(FirstActivity.this, name);
                        /** 创建成功 **/
                        if (result == 1) {
                            /** 跳转到第二个功能页 **/
                            Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                            /** 设置记录ID为当前新建的记录ID **/
                            Constant.setRecordId(accountDB.loadRecordId(name));
                            startActivity(intent);
                            Toast.makeText(FirstActivity.this, "创建成功", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onNegativeButton() {

                    }
                };
                /** 创建自定义对话框 **/
                dialog = new AddTypeDialog(this, listener, null, "create");
                /** 显示自定义对话框 **/
                dialog.show();
                break;
            case R.id.first_search:
                /** 跳转到记录页面 **/
                Intent intent = new Intent(FirstActivity.this, RecordActivity.class);
                startActivity(intent);
                break;
            case R.id.first_set:
                /** 跳转到设置界面 **/
                Intent intent1 = new Intent(FirstActivity.this, SystemSettingActivity.class);
                startActivity(intent1);
                break;
            case R.id.first_remark:
                /** 跳转到备注界面 **/
                Intent intent2 = new Intent(FirstActivity.this, RemarkActivity.class);
                startActivity(intent2);
                break;
        }
    }

    /**
     * 点击返回键时，弹出退出提示框
     */
    @Override
    public void onBackPressed() {
        /** 取得Builder对象 **/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        /** 设置对话框的标题 **/
        builder.setTitle("提示");
        /** 对话框的信息 **/
        builder.setMessage("是否要退出?");
        /** 对话框的确定按钮点击事件，点击关闭程序 **/
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                FirstActivity.this.finish();
            }
        });
        /** 取消按钮，关闭对话框 **/
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        /** 显示对话框 **/
        builder.create().show();
    }
}
