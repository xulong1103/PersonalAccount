package com.system.personalaccount.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.system.personalaccount.R;

/**
 * Created by lenovo on 2016/7/5.
 * 管理支出收入类型对话框
 */
public class ManageDialog extends Dialog {

    /** 对话框监听 **/
    private DialogOnClickListener listener;

    public ManageDialog(Context context) {
        super(context);
    }

    public ManageDialog(Context context, DialogOnClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.manage_dialog);

        TextView expend = (TextView) findViewById(R.id.dialog_tv_expend);
        TextView income = (TextView) findViewById(R.id.dialog_tv_income);
        /** 支出监听 **/
        expend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getType(0);
                dismiss();
            }
        });
        /** 收入监听 **/
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getType(1);
                dismiss();
            }
        });
    }

    /**
     * 回调接口
     */
    public interface DialogOnClickListener {
        void getType(int type);
    }

}
