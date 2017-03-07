package com.system.personalaccount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.system.personalaccount.R;
import com.system.personalaccount.model.Record;

import java.util.List;


/**
 * Created by mine on 2016/7/6.
 * 记录列表的适配器
 */
public class RecordAdapter extends ArrayAdapter<Record> {

    /** 列表项显示布局 **/
    private int resourceId;

    public RecordAdapter(Context context, int resource, List<Record> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Record rd = getItem(position);
        final ViewHolder viewholder;
        final View view;
        if (convertView == null) {
            viewholder = new ViewHolder();
            /** 加载布局 **/
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            /** 初始化各个组件 **/
            viewholder.recordName = (TextView) view.findViewById(R.id.record_name);
            view.setTag(viewholder);
        } else {
            view = convertView;
            viewholder = (ViewHolder) view.getTag();
        }
        /** 为各个组件设置对应值 **/
        viewholder.recordName.setText(rd.getRecordName());
        return view;
    }
    /** 使用此类优化列表加载速度 **/
    class ViewHolder {
        TextView recordName;
    }
}
