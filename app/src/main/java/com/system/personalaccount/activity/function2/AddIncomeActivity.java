package com.system.personalaccount.activity.function2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.system.personalaccount.R;
import com.system.personalaccount.db.AccountDB;
import com.system.personalaccount.model.Income;
import com.system.personalaccount.model.Type;
import com.system.personalaccount.util.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lenovo on 2016/7/6.
 * 新增收入
 */
public class AddIncomeActivity extends Activity{

    /** 返回键、保存键 **/
    private Button back, save;
    /** 收入名称，金钱，备注 **/
    private EditText name, money, remark;
    /** 收入类型 **/
    private Spinner type;
    /** 收入日期 **/
    private TextView date;
    /** 收入类型的适配器 **/
    private ArrayAdapter<String> adapter;
    /** 收入类型的数据 **/
    private List<Type> list;
    /** 记录收入类型的选中下标 **/
    private int typePosition;
    /** 一年的第几周 **/
    private int weekOfYear;

    /** 日期格式化 **/
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    /** 通过日期格式化获得当前日期 **/
    private String currentDate = format.format(new java.util.Date());
    /** 日历中日期变化监听 **/
    private DatePickerDialog.OnDateSetListener OnDateSetListener;
    /** 数据库对象 **/
    private AccountDB accountDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 不显示标题栏 **/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_income);
        /** 初始化时不显示软键盘 **/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        accountDB = AccountDB.getInstance(this);
        initViews();
        /** 返回键，关闭本页面 **/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /** 保存键 **/
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** 判断名称是否为空 **/
                if (name.getText().toString().equals("")) {
                    Toast.makeText(AddIncomeActivity.this, "名称不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                /** 判断金钱是否为空 **/
                if (money.getText().toString().equals("")) {
                    Toast.makeText(AddIncomeActivity.this, "金额不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                /** 将所有信息封装成一个收入对象 **/
                Income income = new Income();
                income.setRecordId(Constant.getRecordId());
                income.setName(name.getText().toString());
                income.setMoney(Double.parseDouble(money.getText().toString()));
                income.setTypeId(list.get(typePosition).getTypeId());
                income.setDate(date.getText().toString() + " " + weekOfYear);
                income.setRemark(remark.getText().toString());
                /** 新建收入 **/
                boolean flag = accountDB.addIncome(income);
                /** 判断是否成功 **/
                if (flag) {
                    Toast.makeText(AddIncomeActivity.this, "保存成功", Toast.LENGTH_LONG).show();
                    AddIncomeActivity.this.finish();
                } else {
                    Toast.makeText(AddIncomeActivity.this, "保存失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 初始化各个组件
     */
    private void initViews() {
        back = (Button) findViewById(R.id.income_btn_back);
        save = (Button) findViewById(R.id.income_btn_save);
        name = (EditText) findViewById(R.id.income_et_name);
        remark = (EditText) findViewById(R.id.income_et_remark);

        initMoney();
        initType();
        initDate();
    }

    /**
     * 初始化金钱文本框
     */
    private void initMoney() {
        money = (EditText) findViewById(R.id.income_et_money);
        /** 为此文本框添加文字变化监听 **/
        money.addTextChangedListener(new TextWatcher() {
            /** 当文字更改时 **/
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                /** 判断是否包含小数点，当有小数点时，只能输入小数点的后两位 **/
                if (s.toString().contains(".")) {
                    /** 如果输入的最后一位大于小数点后两位，则禁止输入 **/
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        money.setText(s);
                        /** 光标设置在末尾 **/
                        money.setSelection(s.length());
                    }
                }
                /** 判断该字符串中是否第一个位置就是小数点，如果是则在小数点前面添加一个0 **/
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    money.setText(s);
                    /** 光标设置在末尾 **/
                    money.setSelection(2);
                }
                /** 如果该字符串以0开头，后面若不跟小数点，则禁止输入其他数字 **/
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        money.setText(s.subSequence(0, 1));
                        /** 光标设置在末尾 **/
                        money.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

        });
    }

    /**
     * 初始化收入类型
     */
    private void initType() {
        type = (Spinner) findViewById(R.id.income_sp_type);
        /** 遍历收入类型 **/
        list = accountDB.queryType(1);
        List<String> data = new ArrayList<>();
        for (Type type : list) {
            /** 得到收入类型的名称 **/
            data.add(type.getTypeName());
        }
        /** 初始化适配器 **/
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        /** 设置下拉样式 **/
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /** 指定适配器 **/
        type.setAdapter(adapter);
        /** 设置选中事件 **/
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /** 记录点击的位置 **/
                typePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 初始化日期
     */
    private void initDate() {
        date = (TextView) findViewById(R.id.income_tv_date);
        /** 获得日历对象 **/
        Calendar calendar = Calendar.getInstance();
        try {
            /** 为日历设置当前日期 **/
            calendar.setTime(format.parse(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /** 获得当前日期在本年的周数 **/
        weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        String week = "星期";
        /** 获得当前日期在本周的第几日 **/
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                week += "日";
                break;
            case 2:
                week += "一";
                break;
            case 3:
                week += "二";
                break;
            case 4:
                week += "三";
                break;
            case 5:
                week += "四";
                break;
            case 6:
                week += "五";
                break;
            case 7:
                week += "六";
                break;
        }
        /** 设置日期文本 **/
        date.setText(currentDate + " " + week);
        /** 日期点击事件 **/
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** 显示日历对话框 **/
                showDateDialog();
            }
        });
        /** 日历的监听事件 **/
        OnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            /**
             * 在选中一个日期后调用此方法
             * @param view 对应组件
             * @param year 年
             * @param monthOfYear 月
             * @param dayOfMonth 日
             */
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                /** 取得年月日 **/
                String month = "" + (monthOfYear + 1);
                String day =  "" + dayOfMonth;
                if (monthOfYear + 1 < 10) {
                    month = "0" + month;
                }
                if (dayOfMonth < 10) {
                    day = "0" + day;
                }
                /** 拼接年月日 **/
                String time = year + "-" + month + "-" + day;
                /** 取得日历对象 **/
                Calendar calendar = Calendar.getInstance();
                try {
                    /** 设置该对象的日期为选中的日期 **/
                    calendar.setTime(format.parse(time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                /** 取得选中日期在本年的第几周数 **/
                weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                String week = "星期";
                /** 取得选中日期在本周的第几天数 **/
                switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                    case 1:
                        week += "日";
                        break;
                    case 2:
                        week += "一";
                        break;
                    case 3:
                        week += "二";
                        break;
                    case 4:
                        week += "三";
                        break;
                    case 5:
                        week += "四";
                        break;
                    case 6:
                        week += "五";
                        break;
                    case 7:
                        week += "六";
                        break;
                }
                /** 日期文本框设置选中日期 **/
                date.setText(time + " " + week);
            }
        };

    }

    /**
     * 显示日历对话框
     */
    private void showDateDialog() {
        /** 取得日历对象 **/
        Calendar calendar = Calendar.getInstance();
        /** 取得日历中对应的年月日 **/
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        /** 显示日历对话框 **/
        new DatePickerDialog(this, OnDateSetListener, year, month, day).show();
    }

}
