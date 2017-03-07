package com.system.personalaccount.activity.function2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.system.personalaccount.R;
import com.system.personalaccount.db.AccountDB;
import com.system.personalaccount.model.Income;
import com.system.personalaccount.adapter.IncomeAdapter;
import com.system.personalaccount.util.ChangeDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2016/7/7.
 * 查询收入记录
 */
public class SearchIncomeActivity extends Activity{

    /** 返回键 **/
    private Button back;
    /** 收入列表 **/
    private ListView income;
    /** 收入列表的适配器 **/
    private ArrayAdapter<Income> adapter;
    /** 收入列表的数据 **/
    private List<Income> data;
    /** 数据库对象 **/
    private AccountDB accountDB;
    /** 上一个，下一个按钮 **/
    private ImageButton pre, nxt;
    /** 日期显示 **/
    private TextView date;
    /** 日期类型下拉列表 **/
    private Spinner spinner;
    /** 日期格式化 **/
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    /** 通过日期格式化获得当前日期 **/
    private final String nowDate = format.format(new Date());
    /** 当前日期拆分后的数组 **/
    private String[] myDate;
    /** 日期状态 **/
    private int dateState;
    /** 一年的第几周 **/
    private int week_in_year;
    /** 固定值，用于还原week_in_year **/
    private int nowWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_income);

        initViews();
        initDates();
        accountDB = AccountDB.getInstance(this);
        /** 初始化收入列表的数据 **/
        data = accountDB.queryIncomeByDays(myDate);
        /** 初始化收入列表的适配器 **/
        adapter = new IncomeAdapter(this, R.layout.show_income_item, data);
        /** 配置适配器 **/
        income.setAdapter(adapter);
        /** 设置收入列表的空布局 **/
        income.setEmptyView(findViewById(R.id.search_income_empty));
        /** 收入列表的点击事件 **/
        income.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /** 跳转到修改支出界面，并将选中的收入记录传递过去 **/
                Intent intent = new Intent(SearchIncomeActivity.this, UpdateIncomeActivity.class);
                intent.putExtra("income", data.get(position));
                startActivityForResult(intent, 0);
            }
        });
        /** 收入列表的长按点击事件 **/
        income.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                /** 设置对话框标题和选项 **/
                menu.setHeaderTitle("执行操作");
                menu.add(0, 0, 0, "删除");
            }
        });
        /** 返回键，关闭当前页面 **/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /** 日期类型数据 **/
        String[] spinnerData = new String[]{"日", "周", "月", "年"};
        /** 日期类型下拉列表的适配器 **/
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, spinnerData);
        /** 设置日期类型下拉列表的下拉样式 **/
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /** 下拉列表配置适配器 **/
        spinner.setAdapter(spinnerAdapter);
        /** 下拉列表的点击事件 **/
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /** 保存选中的位置 **/
                dateState = position;
                switch (dateState) {
                    /** 选中日 **/
                    case 0:
                        /** 初始化日期 **/
                        myDate = nowDate.split("-");
                        date.setText("今天");
                        break;
                    /** 选中周 **/
                    case 1:
                        /** 初始化日期 **/
                        week_in_year = nowWeek;
                        date.setText("本周");
                        break;
                    /** 选中月 **/
                    case 2:
                        /** 初始化日期 **/
                        myDate = nowDate.split("-");
                        date.setText("本月");
                        break;
                    /** 选中年 **/
                    case 3:
                        /** 初始化日期 **/
                        myDate = nowDate.split("-");
                        date.setText("本年");
                        break;
                }
                /** 刷新列表 **/
                notifyListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    /**
     * 初始化各个组件
     */
    private void initViews() {
        income = (ListView) findViewById(R.id.search_income_lv);
        back = (Button) findViewById(R.id.search_income_btn_back);
        pre = (ImageButton) findViewById(R.id.search_income_ib_pre);
        nxt = (ImageButton) findViewById(R.id.search_income_ib_nxt);
        date = (TextView) findViewById(R.id.search_income_tv_date);
        spinner = (Spinner) findViewById(R.id.search_income_spinner);
    }

    /**
     * 初始化日期
     */
    private void initDates() {
        myDate = nowDate.split("-");
        /** 日历对象 **/
        Calendar calendar = Calendar.getInstance();
        try {
            /** 为日历设置当前日期 **/
            calendar.setTime(format.parse(nowDate));
            /** 获取当前日期所在周 **/
            nowWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            week_in_year = nowWeek;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /** 上一个按钮点击事件 **/
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (dateState) {
                    /** 日 **/
                    case 0:
                        /** 日期显示是否为“今天” **/
                        if (date.getText().equals("今天")) {
                            /** 转换日期为上一天 **/
                            ChangeDay.changeDays(myDate, "now", "yesterday");
                            /** 日期显示年-月-日 **/
                            date.setText(myDate[0] + "-" + myDate[1] + "-" + myDate[2]);
                        } else {
                            /** 转换日期为上一天 **/
                            ChangeDay.changeDays(myDate, "", "yesterday");
                            /** 判断转换后的日期与当前日期是否相同 **/
                            if ((myDate[0] + "-" + myDate[1] + "-" + myDate[2]).equals(nowDate)) {
                                date.setText("今天");
                            } else {
                                date.setText(myDate[0] + "-" + myDate[1] + "-" + myDate[2]);
                            }
                        }
                        break;
                    /** 周 **/
                    case 1:
                        /** 日期显示是否为“本周” **/
                        if (date.getText().equals("本周")) {
                            /** 转换日期为上一周 **/
                            week_in_year = ChangeDay.changeWeekOrMonthYear(week_in_year, myDate, "now", "lastWeek");
                            /** 日期显示为年-*周 **/
                            date.setText(myDate[0] + "年 第" + week_in_year + "周");
                        } else {
                            /** 转换日期为上一周 **/
                            week_in_year = ChangeDay.changeWeekOrMonthYear(week_in_year, myDate, "now", "lastWeek");
                            /** 判断转换后的周与当前周是否相同 **/
                            if (week_in_year == nowWeek) {
                                date.setText("本周");
                            } else {
                                date.setText(myDate[0] + "年 第" + week_in_year + "周");
                            }
                        }
                        break;
                    /** 月 **/
                    case 2:
                        /** 日期显示是否为“本月” **/
                        if(date.getText().equals("本月")){
                            /** 日期转换为上一月 **/
                            ChangeDay.changeMonth(myDate,"now","lastMonth");
                            /** 日期显示为年-*月 **/
                            date.setText(myDate[0]+"-"+myDate[1]+"月");
                        }else{
                            /** 日期转换为上一月 **/
                            ChangeDay.changeMonth(myDate,"","lastMonth");
                            /** 判断转换后的月与当前日期是否相同 **/
                            if ((myDate[0] + "-" + myDate[1] + "-" + myDate[2]).equals(nowDate)) {
                                date.setText("本月");
                            } else {
                                date.setText(myDate[0] + "-" + myDate[1] + "月");
                            }
                        }
                        break;
                    /** 年 **/
                    case 3:
                        /** 日期显示是否为“本年” **/
                        if(date.getText().equals("本年")){
                            /** 日期转换为上一年 **/
                            ChangeDay.changeYear(myDate,"now","lastYear");
                            /** 日期显示年 **/
                            date.setText(myDate[0]);
                        }else{
                            /** 日期转换为上一年 **/
                            ChangeDay.changeYear(myDate,"","lastYear");
                            /** 判断转换后的年与当前日期是否相同 **/
                            if ((myDate[0] + "-" + myDate[1] + "-" + myDate[2]).equals(nowDate)) {
                                date.setText("本年");
                            } else {
                                date.setText(myDate[0]);
                            }
                        }
                        break;
                }
                /** 刷新列表 **/
                notifyListView();
            }
        });
        /** 下一个按钮 **/
        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (dateState) {
                    /** 日 **/
                    case 0:
                        /** 日期显示为“今天” **/
                        if (date.getText().equals("今天")) {
                            /** 日期转换为下一天 **/
                            ChangeDay.changeDays(myDate, "now", "morning");
                            /** 日期显示为年-月-日 **/
                            date.setText(myDate[0] + "-" + myDate[1] + "-" + myDate[2]);
                        } else {
                            /** 日期转换为下一天 **/
                            ChangeDay.changeDays(myDate, "", "morning");
                            /** 判断转换后的日期与当前日期是否吸纳同 **/
                            if ((myDate[0] + "-" + myDate[1] + "-" + myDate[2]).equals(nowDate)) {
                                date.setText("今天");
                            } else {
                                date.setText(myDate[0] + "-" + myDate[1] + "-" + myDate[2]);
                            }
                        }
                        break;
                    /** 周 **/
                    case 1:
                        /** 日期显示是否为“本周” **/
                        if (date.getText().equals("本周")) {
                            /** 日期转换为下一周 **/
                            week_in_year = ChangeDay.changeWeekOrMonthYear(week_in_year, myDate, "now", "nextWeek");
                            /** 日期显示为年 第*周 **/
                            date.setText(myDate[0] + "年 第" + week_in_year + "周");
                        } else {
                            /** 日期转换为下一周 **/
                            week_in_year = ChangeDay.changeWeekOrMonthYear(week_in_year, myDate, "now", "nextWeek");
                            /** 判断转换后的周与当前周是否相同 **/
                            if (week_in_year == nowWeek) {
                                date.setText("本周");
                            } else {
                                date.setText(myDate[0] + "年 第" + week_in_year + "周");
                            }
                        }
                        break;
                    /** 月 **/
                    case 2:
                        /** 日期显示是否为“本月” **/
                        if(date.getText().equals("本月")){
                            /** 日期转换为下一月 **/
                            ChangeDay.changeMonth(myDate,"now","nextMonth");
                            /** 日期显示为年-*月 **/
                            date.setText(myDate[0]+"-"+myDate[1]+"月");
                        }else{
                            /** 日期转换为下一月 **/
                            ChangeDay.changeMonth(myDate,"","nextMonth");
                            /** 判断转换后的月与当前月份是否相同 **/
                            if ((myDate[0] + "-" + myDate[1] + "-" + myDate[2]).equals(nowDate)) {
                                date.setText("本月");
                            } else {
                                date.setText(myDate[0] + "-" + myDate[1] + "月");
                            }
                        }
                        break;
                    /** 年 **/
                    case 3:
                        /** 日期显示是否为“本年” **/
                        if(date.getText().equals("本年")){
                            /** 日期转换为下一年 **/
                            ChangeDay.changeYear(myDate,"now","nextYear");
                            /** 日期显示为年 **/
                            date.setText(myDate[0]);
                        }else{
                            /** 日期转换为下一年 **/
                            ChangeDay.changeYear(myDate,"","nextYear");
                            /** 判断转换后的年与当前年份是否相同 **/
                            if ((myDate[0] + "-" + myDate[1] + "-" + myDate[2]).equals(nowDate)) {
                                date.setText("本年");
                            } else {
                                date.setText(myDate[0]);
                            }
                        }
                        break;
                }
                /** 刷新列表 **/
                notifyListView();
            }
        });

        date.setText("今天");
    }

    /**
     * 列表长按回调方法
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        /** 获取长按位置 **/
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = info.position;
        switch (item.getItemId()) {
            /** 删除 **/
            case 0:
                /** 删除选中项 **/
                boolean flag = accountDB.deleteIncomeItem(data.get(position));
                if (flag) {
                    Toast.makeText(this, "删除成功", Toast.LENGTH_LONG).show();
                    notifyListView();
                } else {
                    Toast.makeText(this, "删除失败", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 当关闭其他页面返回此页面时回调此方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            switch (resultCode) {
                /** 返回结果码为成功，刷新列表 **/
                case RESULT_OK:
                    notifyListView();
                    break;
            }
        }
    }

    /**
     * 刷新列表
     */
    private void notifyListView() {
        List<Income> temp = null;
        switch (dateState) {
            /** 日 **/
            case 0:
                temp = accountDB.queryIncomeByDays(myDate);
                break;
            /** 周 **/
            case 1:
                temp = accountDB.queryIncomeByWeeks(myDate, week_in_year);
                break;
            /** 月 **/
            case 2:
                temp = accountDB.queryIncomeByMonths(myDate);
                break;
            /** 年 **/
            case 3:
                temp = accountDB.queryIncomeByYears(myDate);
                break;
        }
        data.clear();
        for (Income i : temp) {
            data.add(i);
        }
        adapter.notifyDataSetChanged();
    }
}
