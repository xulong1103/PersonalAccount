package com.system.personalaccount.activity.function2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.system.personalaccount.R;

/**
 * 第二个功能页
 */
public class SecondActivity extends Activity implements View.OnClickListener{

    /** 返回 **/
    private Button back;
    /** 新增支出 **/
    private Button addExpend;
    /** 新增收入 **/
    private Button addIncome;
    /** 查询支出 **/
    private Button searchExpend;
    /** 查询收入 **/
    private Button searchIncome;
    /** 统计 **/
    private Button statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_seond);

        /** 初始化各个组件 **/
        back = (Button) findViewById(R.id.second_btn_back);
        addExpend = (Button) findViewById(R.id.second_btn_add_expend);
        addIncome = (Button) findViewById(R.id.second_btn_add_income);
        searchExpend = (Button) findViewById(R.id.second_btn_search_expend);
        searchIncome = (Button) findViewById(R.id.second_btn_search_income);
        statistics = (Button) findViewById(R.id.second_btn_statistics);
        /** 为组件设置监听 **/
        back.setOnClickListener(this);
        addExpend.setOnClickListener(this);
        addIncome.setOnClickListener(this);
        searchExpend.setOnClickListener(this);
        searchIncome.setOnClickListener(this);
        statistics.setOnClickListener(this);
    }

    /**
     * 点击某一组件后回调此方法
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回，关闭本页面 **/
            case R.id.second_btn_back:
                finish();
                break;
            /** 新增支出 **/
            case R.id.second_btn_add_expend:
                /** 跳转到新增支出页面 **/
                Intent intent1 = new Intent(SecondActivity.this, AddExpendActivity.class);
                startActivity(intent1);
                break;
            /** 新增收入 **/
            case R.id.second_btn_add_income:
                /** 跳转到新增收入页面 **/
                Intent intent2 = new Intent(SecondActivity.this, AddIncomeActivity.class);
                startActivity(intent2);
                break;
            /** 查询支出 **/
            case R.id.second_btn_search_expend:
                /** 跳转到查询支出界面 **/
                Intent intent3 = new Intent(SecondActivity.this, SearchExpendActivity.class);
                startActivity(intent3);
                break;
            /** 查询收入 **/
            case R.id.second_btn_search_income:
                /** 跳转到查询收入界面 **/
                Intent intent4 = new Intent(SecondActivity.this, SearchIncomeActivity.class);
                startActivity(intent4);
                break;
            /** 统计 **/
            case R.id.second_btn_statistics:
                /** 跳转到统计页面 **/
                Intent intent5=new Intent(SecondActivity.this,ExpendActivity.class);
                startActivity(intent5);
                break;
        }
    }
}
