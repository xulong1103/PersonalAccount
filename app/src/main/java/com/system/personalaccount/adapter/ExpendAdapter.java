package com.system.personalaccount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.system.personalaccount.R;
import com.system.personalaccount.model.Expend;

import java.util.List;

/**
 * Created by lenovo on 2016/7/7.
 * 查看支出listView的适配器
 */
public class ExpendAdapter extends ArrayAdapter<Expend> {

    /** 列表项显示布局 **/
    private int resource;

    public ExpendAdapter(Context context, int resource, List<Expend> objects) {
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
            viewHolder.name = (TextView) convertView.findViewById(R.id.show_expend_tv_name);
            viewHolder.money = (TextView) convertView.findViewById(R.id.show_expend_tv_money);
            viewHolder.date = (TextView) convertView.findViewById(R.id.show_expend_tv_date);
            viewHolder.day = (TextView) convertView.findViewById(R.id.show_expend_tv_day);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /** 为各个组件设置对应值 **/
        Expend expend = getItem(position);
        viewHolder.name.setText(expend.getName());
        viewHolder.money.setText(String.valueOf(expend.getMoney()));
        viewHolder.date.setText(expend.getDate().split(" ")[0]);
        viewHolder.day.setText(expend.getDate().split(" ")[1]);
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
