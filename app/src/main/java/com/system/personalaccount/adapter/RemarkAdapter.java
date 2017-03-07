package com.system.personalaccount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.system.personalaccount.R;
import com.system.personalaccount.model.Remark;

import java.util.List;


/**
 * Created by mine on 2016/7/6.
 * 备注列表的适配器
 */
public class RemarkAdapter extends ArrayAdapter<Remark> {

    /** 列表项显示布局 **/
    private int resourceId;

    public RemarkAdapter(Context context, int resource, List<Remark> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Remark remark = getItem(position);
        final ViewHolder viewholder;
        final View view;
        if (convertView == null) {
            viewholder = new ViewHolder();
            /** 加载布局 **/
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            /** 初始化各个组件 **/
            viewholder.title = (TextView) view.findViewById(R.id.remark_item_title);
            viewholder.date = (TextView) view.findViewById(R.id.remark_item_date);
            viewholder.time = (TextView) view.findViewById(R.id.remark_item_time);
            view.setTag(viewholder);
        } else {
            view = convertView;
            viewholder = (ViewHolder) view.getTag();
        }
        /** 为各个组件设置对应值 **/
        viewholder.title.setText(remark.getTitle());
        viewholder.date.setText(remark.getDate().split(" ")[0]);
        viewholder.time.setText(remark.getDate().split(" ")[1]);
        return view;
    }
    /** 使用此类优化列表加载速度 **/
    class ViewHolder {
        TextView title;
        TextView date;
        TextView time;
    }
}
