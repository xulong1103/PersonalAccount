package com.system.personalaccount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.system.personalaccount.R;
import com.system.personalaccount.model.Income;

import java.util.List;

/**
 * Created by lenovo on 2016/7/7.
 * 查看收入listView的适配器
 */
public class IncomeAdapter extends ArrayAdapter<Income> {

    /** 列表项显示布局 **/
    private int resource;

    public IncomeAdapter(Context context, int resource, List<Income> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            /** 加载布局 **/
            convertView = LayoutInflater.from(getContext()).inflate(resource, null);
            viewHolder = new ViewHolder();
            /** 初始化各个组件 **/
            viewHolder.name = (TextView) convertView.findViewById(R.id.show_income_tv_name);
            viewHolder.money = (TextView) convertView.findViewById(R.id.show_income_tv_money);
            viewHolder.date = (TextView) convertView.findViewById(R.id.show_income_tv_date);
            viewHolder.day = (TextView) convertView.findViewById(R.id.show_income_tv_day);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /** 为各个组件设置对应值 **/
        Income income = getItem(position);
        viewHolder.name.setText(income.getName());
        viewHolder.money.setText(String.valueOf(income.getMoney()));
        viewHolder.date.setText(income.getDate().split(" ")[0]);
        viewHolder.day.setText(income.getDate().split(" ")[1]);
        return convertView;
    }
    /** 使用此类优化列表加载速度 **/
    private class ViewHolder {
        public TextView name;
        public TextView money;
        public TextView date;
        public TextView day;
    }

}
