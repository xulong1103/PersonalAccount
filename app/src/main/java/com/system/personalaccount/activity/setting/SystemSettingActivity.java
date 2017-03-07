package com.system.personalaccount.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.system.personalaccount.R;
import com.system.personalaccount.util.ManageDialog;

/**
 * 设置界面
 */
public class SystemSettingActivity extends Activity implements View.OnClickListener{

    /** 返回，管理类型，系统说明书按钮 **/
    private Button back, manage, instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.system_setting);

        /** 初始化各个组件 **/
        back = (Button) findViewById(R.id.setting_btn_back);
        manage = (Button) findViewById(R.id.setting_btn_manage);
        instruction = (Button) findViewById(R.id.setting_btn_instruction);
        /** 添加监听 **/
        back.setOnClickListener(this);
        manage.setOnClickListener(this);
        instruction.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回 **/
            case R.id.setting_btn_back:
                finish();
                break;
            /** 管理类型 **/
            case R.id.setting_btn_manage:
                /** 自定义对话框 **/
                ManageDialog dialog = new ManageDialog(SystemSettingActivity.this, new ManageDialog.DialogOnClickListener() {
                    @Override
                    public void getType(int type) {
                        /** 跳转到管理支出收入类型页面 **/
                        Intent manageIntent = new Intent(SystemSettingActivity.this, ManageTypeActivity.class);
                        /** 传递管理类型 **/
                        manageIntent.putExtra("type", type);
                        startActivity(manageIntent);
                    }
                });
                dialog.show();
                break;
            /** 系统说明书 **/
            case R.id.setting_btn_instruction:
                /** 跳转到系统说明书页面 **/
                Intent instructionIntent = new Intent(SystemSettingActivity.this, InstructionActivity.class);
                startActivity(instructionIntent);
                break;
        }
    }
}
