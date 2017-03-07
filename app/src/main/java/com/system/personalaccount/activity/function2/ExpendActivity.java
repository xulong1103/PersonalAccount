package com.system.personalaccount.activity.function2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.system.personalaccount.R;
import com.system.personalaccount.db.AccountDB;
import com.system.personalaccount.model.Expend;
import com.system.personalaccount.util.ChangeDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 统计支出或收入界面
 */
public class ExpendActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    /** 上一个 **/
    private ImageButton before;
    /** 下一个 **/
    private ImageButton next;
    /** 返回 **/
    private Button back;
    /** 日期 **/
    private TextView dateShow;
    /** 支出收入转换按钮 **/
    private Switch switchButton;
    /** 文本框 **/
    private TextView tv;
    /** 现在时间 **/
    private String nowDate;
    /** 转换的日期 **/
    private String[] changeDate = new String[3];
    /** 日期 **/
    private String[] date = new String[3];
    /** 日周月年下拉列表 **/
    private Spinner spinner;
    /** 饼状图 **/
    private PieChart mChart;
    /** 统计状态，0表示统计支出，1表示统计收入 **/
    private int expendState;
    /** 金钱总和 **/
    private double sumMoney;
    /** 日周月年选中状态，默认为日 **/
    private int state = 1;
    /** 日历对象 **/
    private Calendar calendar;
    /** 一年中的第几周 **/
    private int week_In_Year;
    /** 现在的周数 **/
    private int nowWeek;
    /** 现在的月数 **/
    private String[] nowMonth;
    /** 下拉列表的适配器 **/
    private ArrayAdapter<String> adapter;
    /** 下拉列表中的数据 **/
    private String[] list = {"日", "周", "月", "年"};
    /** 数据库对象 **/
    private AccountDB accountDB;
    /** 统计的数据 **/
    private List<Expend> expendList = new ArrayList<Expend>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expend);
        accountDB = AccountDB.getInstance(this);
        /** 初始化适配器 **/
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        /** 为下拉列表设置下拉样式 **/
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /** 取得现在日期 **/
        nowDate = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        /** 将现在日期分割成3部分 **/
        changeDate = nowDate.split("-");
        /** 实例化日历对象 **/
        calendar = Calendar.getInstance();
        initViews();
        /** 下拉列表配置适配器 **/
        spinner.setAdapter(adapter);
        /** 初始化饼状图 **/
        mChart = (PieChart) findViewById(R.id.spread_pie_chart);
        /** 支出收入转换按钮点击事件 **/
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /** 是否点击 **/
                if (isChecked) {
                    /** 状态转换为1，统计收入 **/
                    expendState = 1;
                    tv.setText("收入");
                    /** 根据当前查看类型进行统计 **/
                    if (state == 1) {
                        /** 按照天的方式统计数据 **/
                        expendList = accountDB.SearchExpendOfDay(date, expendState);
                        /** 判断当天是否存在数据 **/
                        if (expendList.size() == 0) {
                            show(expendList, "没有记录");
                        } else {
                            show(expendList, "当前记录");
                        }
                    } else if (state == 2) {
                        /** 按照周的方式统计数据 **/
                        List<Expend> list = accountDB.SearchExpendOfWeek(date, String.valueOf(nowWeek), expendState);
                        /** 判断当前周是否存在数据 **/
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                    } else if (state == 3) {
                        /** 按照月的方式统计数据 **/
                        List<Expend> list = accountDB.searchExpendOfMonth(date, expendState);
                        /** 判断当前月是否存在数据 **/
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                    } else if (state == 4) {
                        /** 按照年的方式统计数据 **/
                        List<Expend> list = accountDB.searchExpendOfYear(date, expendState);
                        /** 判断当前年是否存在数据 **/
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }

                    }
                } else {
                    tv.setText("支出");
                    /** 状态转换为0，统计支出 **/
                    expendState = 0;
                    /** 根据当前查看类型进行统计 **/
                    if (state == 1) {
                        /** 按照天的方式统计数据 **/
                        expendList = accountDB.SearchExpendOfDay(date, expendState);
                        /** 判断当前天是否存在数据 **/
                        if (expendList.size() == 0) {
                            show(expendList, "没有记录");
                        } else {
                            show(expendList, "当前记录");
                        }
                    } else if (state == 2) {
                        /** 按照周的方式统计数据 **/
                        List<Expend> list = accountDB.SearchExpendOfWeek(date, String.valueOf(nowWeek), expendState);
                        /** 判断当前周是否存在数据 **/
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                    } else if (state == 3) {
                        /** 按照月的方式统计数据 **/
                        List<Expend> list = accountDB.searchExpendOfMonth(date, expendState);
                        /** 判断当前月是否存在数据 **/
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                    } else if (state == 4) {
                        /** 按照年的方式统计数据 **/
                        List<Expend> list = accountDB.searchExpendOfYear(date, expendState);
                        /** 判断当前年是否存在数据 **/
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }

                    }
                }
            }
        });
    }

    /**
     * 展示饼状图
     * @param pieChart
     * @param pieData
     * @param centerText
     */
    private void showChart(PieChart pieChart, PieData pieData, String centerText) {
        pieChart.setHoleColorTransparent(true);
        /** 半径 **/
        pieChart.setHoleRadius(60f);
        /** 半透明圆 **/
        pieChart.setTransparentCircleRadius(64f);
        /** 描述 **/
        pieChart.setDescription("");
        /** 饼状图中间可以添加文字 **/
        pieChart.setDrawCenterText(true);
        pieChart.setDrawHoleEnabled(true);
        /** 初始旋转角度 **/
        pieChart.setRotationAngle(90);
        /** 可以手动旋转 **/
        pieChart.setRotationEnabled(true);
        /** 显示成百分比 **/
        pieChart.setUsePercentValues(true);
        /** 判断中心文字 **/
        if (centerText.equals("没有记录")) {
            /** 设置中心文字 **/
            pieChart.setCenterText(centerText);
            sumMoney = 0;
        } else {
            /** 设置中心文字 **/
            pieChart.setCenterText(centerText + "总计" + String.valueOf(sumMoney) + "元");
            sumMoney = 0;
        }
        /** 中心文字的大小 **/
        pieChart.setCenterTextSize(15);

        /** 设置数据 **/
        pieChart.setData(pieData);
        /** 设置比例图 **/
        Legend mLegend = pieChart.getLegend();
        /** 右侧显示 **/
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        /** 设置动画 **/
        pieChart.animateXY(1000, 1000);
    }

    /**
     * 显示
     * @param list
     * @param centerText
     */
    private void show(List<Expend> list, String centerText) {
        /** 通过支出或收入数据初始化饼状图 **/
        PieData mPieData = getPieData(list, expendState);
        /** 展示饼状图 **/
        showChart(mChart, mPieData, centerText);
    }

    /**
     * 取得饼状图数据
     * @param list
     * @param range
     * @return
     */
    private PieData getPieData(List<Expend> list, float range) {

        /** xVals用来表示每个饼块上的内容 **/
        ArrayList<String> xValues = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            if (range == 0) {
                /** 饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4 **/
                xValues.add(accountDB.queryExpendTypeName(list.get(i).getTypeId()) + String.valueOf(list.get(i).getMoney()) + "元");
            } else if (range == 1) {
                /** 饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4 **/
                xValues.add(accountDB.queryIncomeTypeName(list.get(i).getTypeId()) + String.valueOf(list.get(i).getMoney()) + "元");

            }
        }
        /** yVals用来表示封装每个饼块的实际数据 **/
        ArrayList<Entry> yValues = new ArrayList<Entry>();

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
        for (int i = 0; i < list.size(); i++) {
            double money = list.get(i).getMoney();
            /** 添加数据 **/
            yValues.add(new Entry((float) money, i));
            sumMoney += money;
        }
        /** y轴的集合 **/
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);
        /** 设置个饼状图之间的距离 **/
        pieDataSet.setSliceSpace(0f);

        /** 饼图颜色 **/
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));
        colors.add(Color.rgb(110, 129, 129));
        colors.add(Color.rgb(177, 148, 34));
        colors.add(Color.rgb(5, 3, 35));
        /** 配置颜色 **/
        pieDataSet.setColors(colors);
        /** 取得系统屏幕参数 **/
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        /** 选中态多出的长度 **/
        pieDataSet.setSelectionShift(px);

        /** 实例化饼状图 **/
        PieData pieData = new PieData(xValues, pieDataSet);
        return pieData;
    }

    /**
     * 初始化各个组件，并设置监听
     */
    private void initViews() {
        tv = (TextView) findViewById(R.id.expendActivity_type);
        before = (ImageButton) findViewById(R.id.expend_bt_before);
        next = (ImageButton) findViewById(R.id.expend_bt_next);
        back = (Button) findViewById(R.id.expendActivity_back);
        dateShow = (TextView) findViewById(R.id.expend_tv_date);
        spinner = (Spinner) findViewById(R.id.spinner);
        switchButton = (Switch) findViewById(R.id.expendActivity_switch);
        back.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
        before.setOnClickListener(this);
        next.setOnClickListener(this);
        dateShow.setText("今天");
    }

    /**
     * 点击事件
     * @param v 点击的组件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回键 **/
            case R.id.expendActivity_back:
                finish();
                break;
            /** 上一个按钮 **/
            case R.id.expend_bt_before:
                if (state == 1) {
                    /** 判断日期显示是否为 "今天" **/
                    if (dateShow.getText().equals("今天")) {
                        /** 转换上一天 **/
                        ChangeDay.changeDays(date, "now", "yesterday");
                        /** 设置转换后的日期 **/
                        dateShow.setText(date[1] + "-" + date[2]);
                        /** 取得转换后日期对应的支出收入数据 **/
                        expendList = accountDB.SearchExpendOfDay(date, expendState);
                        if (expendList == null) {
                            show(expendList, "没有记录");
                        } else {
                            show(expendList, "当前记录");
                        }
                    } else {
                        /** 转换上一天 **/
                        ChangeDay.changeDays(date, "", "yesterday");
                        /** 判断转换后的日期与当前日期是否相同 **/
                        if (Arrays.equals(date, changeDate)) {
                            /** 若相同，则日期显示为 "今天" **/
                            dateShow.setText("今天");
                            /** 取得转换后日期对应的支出收入数据 **/
                            expendList = accountDB.SearchExpendOfDay(date, expendState);
                            if (expendList == null) {
                                show(expendList, "没有记录");
                            } else {
                                show(expendList, "当前记录");
                            }
                        } else {
                            /** 若不相同，则日期显示为月-日 **/
                            dateShow.setText(date[1] + "-" + date[2]);
                            expendList = accountDB.SearchExpendOfDay(date, expendState);
                            if (expendList == null) {
                                show(expendList, "没有记录");
                            } else {
                                show(expendList, "当前记录");
                            }
                        }
                    }
                } else if (state == 2) {
                    /** 判断日期框显示的是否为 "本周" **/
                    if (dateShow.getText().toString().equals("本周")) {
                        /** 将当前日期转换成上一周 **/
                        int week = ChangeDay.changeWeekOrMonthYear(week_In_Year, date, "now", "lastWeek");
                        /** 查询转换后周的统计数据 **/
                        List<Expend> list = accountDB.SearchExpendOfWeek(date, String.valueOf(week), expendState);
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                        week_In_Year = week;
                        /** 显示年-*周 **/
                        dateShow.setText(date[0] + "-" + String.valueOf(week) + "周");
                    } else {
                        /** 将当前日期转换成上一周 **/
                        int week = ChangeDay.changeWeekOrMonthYear(week_In_Year, date, "", "lastWeek");
                        /** 查询转换后周的统计数据 **/
                        List<Expend> list = accountDB.SearchExpendOfWeek(date, String.valueOf(week), expendState);
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                        /** 如果年和周同时相同，则日期显示为 “本周” **/
                        if (date[0].equals(changeDate[0]) && String.valueOf(week).equals(String.valueOf(nowWeek))) {
                            dateShow.setText("本周");
                            week_In_Year = week;
                        } else {
                            dateShow.setText(date[0] + "-" + String.valueOf(week) + "周");
                            week_In_Year = week;
                        }
                    }
                } else if (state == 3) {
                    /** 判断日期显示是否为 "本月" **/
                    if (dateShow.getText().equals("本月")) {
                        /** 将日期转换成上一个月 **/
                        ChangeDay.changeMonth(date, "now", "lastMonth");
                        /** 按照转换后月的日期查询数据 **/
                        List<Expend> list = accountDB.searchExpendOfMonth(date, expendState);
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                        /** 日期显示为年-*月 **/
                        dateShow.setText(date[0] + "-" + date[1] + "月");
                    } else {
                        /** 将日期转换成上一个月 **/
                        ChangeDay.changeMonth(date, "", "lastMonth");
                        /** 判断当前日期与转换后的日期是否相同，若相同则日期显示为“本月”，不同则显示年-*月 **/
                        if (Arrays.equals(date, nowMonth)) {
                            dateShow.setText("本月");
                        } else {
                            dateShow.setText(date[0] + "-" + date[1] + "月");
                        }
                        /** 按照转换后的月查询数据 **/
                        List<Expend> list = accountDB.searchExpendOfMonth(date, expendState);
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                    }
                } else if (state == 4) {
                    /** 判断日期显示是否为“本年” **/
                    if (dateShow.getText().toString().equals("本年")) {
                        /** 将日期转换成上一年 **/
                        ChangeDay.changeYear(date, "now", "lastYear");
                        /** 日期显示转换后的年份 **/
                        dateShow.setText(date[0]);
                        /** 按照转换后的年查询数据 **/
                        List<Expend> list = accountDB.searchExpendOfYear(date, expendState);
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                    } else {
                        /** 将日期转换成上一年 **/
                        ChangeDay.changeYear(date, "", "lastYear");
                        /** 判断当前日期与转换后的日期是否相同，是则日期显示为“本年”，否则显示年 **/
                        if (Arrays.equals(date, changeDate)) {
                            dateShow.setText("本年");
                        } else {
                            dateShow.setText(date[0]);
                        }
                        /** 按照转换后的年查询数据 **/
                        List<Expend> list = accountDB.searchExpendOfYear(date, expendState);
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                    }
                }

                break;
            /** 下一个按钮 **/
            case R.id.expend_bt_next:
                if (state == 1) {
                    /** 判断日期显示是否为“今天” **/
                    if (dateShow.getText().equals("今天")) {
                        /** 将日期转换为下一天 **/
                        ChangeDay.changeDays(date, "now", "morning");
                        /** 日期显示转换后的月-日 **/
                        dateShow.setText(date[1] + "-" + date[2]);
                        /** 按照转换后的天查询数据 **/
                        expendList = accountDB.SearchExpendOfDay(date, expendState);
                        if (expendList.size() == 0) {
                            show(expendList, "没有记录");
                        } else {
                            show(expendList, "当前记录");
                        }
                    } else {
                        /** 将日期转换为下一天 **/
                        ChangeDay.changeDays(date, "", "morning");
                        /** 判断当前日期与转换后的日期是否相同，是则显示“今天” **/
                        if (Arrays.equals(date, changeDate)) {
                            dateShow.setText("今天");
                            /** 按照转换后的天查询数据 **/
                            expendList = accountDB.SearchExpendOfDay(date, expendState);
                            if (expendList.size() == 0) {
                                Log.i("mine", "-------------ssssssssssssssssss---->" + "没有数据");
                                show(expendList, "没有记录");
                            } else {
                                show(expendList, "当前记录");
                            }
                        } else {
                            /** 将日期显示为月-日 **/
                            dateShow.setText(date[1] + "-" + date[2]);
                            /** 按照转换后的天查询数据 **/
                            expendList = accountDB.SearchExpendOfDay(date, expendState);
                            if (expendList.size() == 0) {
                                Log.i("mine", "-------------ssssssssssssssssss---->" + "没有数据");
                                show(expendList, "没有记录");
                            } else {
                                show(expendList, "当前记录");
                            }
                        }
                    }
                } else if (state == 2) {
                    /** 判断日期显示的周是否为“本周” **/
                    if (dateShow.getText().toString().equals("本周")) {
                        /** 将日期转换为下一周 **/
                        int week = ChangeDay.changeWeekOrMonthYear(week_In_Year, date, "now", "nextWeek");
                        /** 按照转换后的周查询数据 **/
                        List<Expend> list = accountDB.SearchExpendOfWeek(date, String.valueOf(week), expendState);
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                        week_In_Year = week;
                        /** 日期显示为年-*周 **/
                        dateShow.setText(date[0] + "-" + String.valueOf(week) + "周");
                    } else {
                        /** 将日期转换为下一周 **/
                        int week = ChangeDay.changeWeekOrMonthYear(week_In_Year, date, "", "nextWeek");
                        /** 按照转换后的周查询数据 **/
                        List<Expend> list = accountDB.SearchExpendOfWeek(date, String.valueOf(week), expendState);
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                        /** 判断当前日期与转换后的日期是否相同，是则显示“本周”，否则显示年-*周 **/
                        if (date[0].equals(changeDate[0]) && String.valueOf(week).equals(String.valueOf(nowWeek))) {
                            dateShow.setText("本周");
                            week_In_Year = week;
                        } else {
                            dateShow.setText(date[0] + "-" + String.valueOf(week) + "周");
                            week_In_Year = week;
                        }
                    }
                } else if (state == 3) {
                    /** 判断日期显示是否为“本月” **/
                    if (dateShow.getText().equals("本月")) {
                        /** 将日期转换为下一月 **/
                        ChangeDay.changeMonth(date, "now", "nextMonth");
                        /** 按照转换后的月查询数据 **/
                        List<Expend> list = accountDB.searchExpendOfMonth(date, expendState);
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                        /** 日期显示为年-*月 **/
                        dateShow.setText(date[0] + "-" + date[1] + "月");
                    } else {
                        /** 将日期转换为下一月 **/
                        ChangeDay.changeMonth(date, "", "nextMonth");
                        /** 判断当前日期与转换后的日期是否相同，是则显示“本月”，否则显示年-*月 **/
                        if (Arrays.equals(date, nowMonth)) {
                            dateShow.setText("本月");
                        } else {
                            dateShow.setText(date[0] + "-" + date[1] + "月");
                        }
                        /** 按照转换后的月查询数据 **/
                        List<Expend> list = accountDB.searchExpendOfMonth(date, expendState);
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                    }
                } else if (state == 4) {
                    /** 判断日期显示是否为“本年” **/
                    if (dateShow.getText().toString().equals("本年")) {
                        /** 将日期转换为下一年 **/
                        ChangeDay.changeYear(date, "now", "nextYear");
                        /** 日期显示为转换后的年 **/
                        dateShow.setText(date[0]);
                        /** 按照转换后的年查询数据 **/
                        List<Expend> list = accountDB.searchExpendOfYear(date, expendState);
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                    } else {
                        /** 将日期转换为下一年 **/
                        ChangeDay.changeYear(date, "", "nextYear");
                        /** 判断当前日期与转换后的日期是否相同 **/
                        if (Arrays.equals(date, changeDate)) {
                            dateShow.setText("本年");
                        } else {
                            dateShow.setText(date[0]);
                        }
                        /** 按照转换后的年查询数据 **/
                        List<Expend> list = accountDB.searchExpendOfYear(date, expendState);
                        if (list.size() == 0) {
                            show(list, "没有记录");
                        } else {
                            show(list, "当前记录");
                        }
                    }
                }
                break;
        }
    }

    /**
     * 日期类型下拉列表项被选中时回调此方法
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        /** 当选中类型为日 **/
        if (list[position].equals("日")) {
            /** 选中状态设置为1 **/
            state = 1;
            /** 当前时间 **/
            date = nowDate.split("-");
            dateShow.setText("今天");
            /** 按照当前的天查询数据 **/
            expendList = accountDB.SearchExpendOfDay(date, expendState);
            if (expendList.size() == 0) {
                show(expendList, "没有记录");
            } else {
                show(expendList, "当前记录");
            }
        }
        /** 当前选中类型为周 **/
        else if (list[position].equals("周")) {
            /** 选中状态设置为2 **/
            state = 2;
            dateShow.setText("本周");
            /** 当前时间 **/
            date = nowDate.split("-");
            /** 取得当前时间所在年份的周 **/
            week_In_Year = calendar.get(Calendar.WEEK_OF_YEAR);
            nowWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            /** 按照当前的周查询数据 **/
            List<Expend> list = accountDB.SearchExpendOfWeek(date, String.valueOf(nowWeek), expendState);
            if (list.size() == 0) {
                show(list, "没有记录");
            } else {
                show(list, "当前记录");
            }
        }
        /** 当前日期类型为月 **/
        else if (list[position].equals("月")) {
            dateShow.setText("本月");
            /** 当前时间 **/
            date = nowDate.split("-");
            nowMonth = nowDate.split("-");
            /** 选中状态设置为3 **/
            state = 3;
            /** 按照当前的月查询数据 **/
            List<Expend> list = accountDB.searchExpendOfMonth(date, expendState);
            if (list.size() == 0) {
                show(list, "没有记录");
            } else {
                show(list, "当前记录");
            }
        }
        /** 当前选中类型为年 **/
        else if (list[position].equals("年")) {
            dateShow.setText("本年");
            /** 当前时间 **/
            date = nowDate.split("-");
            /** 选中状态设置为4 **/
            state = 4;
            /** 按照当前的年查询数据 **/
            List<Expend> list = accountDB.searchExpendOfYear(date, expendState);
            if (list.size() == 0) {
                show(list, "没有记录");
            } else {
                show(list, "当前记录");
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
