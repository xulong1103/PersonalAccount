package com.system.personalaccount.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.system.personalaccount.R;

/**
 * Created by lenovo on 2016/7/5.
 * 添加修改支出收入类型对话框
 */
public class AddTypeDialog extends Dialog{

    /** 操作类型 **/
    private String type;
    /** 对话框监听 **/
    private TypeOnClickListener listener;
    /** 对话框名称 **/
    private String name;

    public AddTypeDialog(Context context) {
        super(context);
    }

    public AddTypeDialog(Context context, TypeOnClickListener listener, String name, String type) {
        super(context);
        this.listener = listener;
        this.name = name;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_type_dialog);

        final EditText newName = (EditText) findViewById(R.id.type_et_name);
        TextView title = (TextView) findViewById(R.id.type_tv_title);
        /** 为对话框设置名称 **/
        if (type==null) {
            if (name != null) {
                title.setText("修改类型");
                newName.setText(name);
            } else {
                title.setText("添加类型");
            }
        }else{
            title.setText("创建记录");
        }
        Button positive = (Button) findViewById(R.id.type_btn_positive);
        Button negative = (Button) findViewById(R.id.type_btn_negative);
        /** 确定点击 **/
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPositiveButton(newName.getText().toString());
                dismiss();
            }
        });
        /** 取消点击 **/
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNegativeButton();
                dismiss();
            }
        });
    }

    /** 对话框监听接口 **/
    public interface TypeOnClickListener {
        void onPositiveButton(String name);
        void onNegativeButton();
    }

}
