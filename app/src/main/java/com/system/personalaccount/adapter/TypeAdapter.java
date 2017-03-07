package com.system.personalaccount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.system.personalaccount.R;
import com.system.personalaccount.model.Type;

import java.util.List;

/**
 * Created by lenovo on 2016/7/5.
 * 管理收入支出界面中listView的自定义适配器
 */
public class TypeAdapter extends ArrayAdapter<Type> {

    /** 列表项显示布局 **/
    private int resource;

    public TypeAdapter(Context context, int resource, List<Type> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            /** 加载布局 **/
            convertView = LayoutInflater.from(getContext()).inflate(resource, null);
            /** 初始化各个组件 **/
            viewHolder.name = (TextView) convertView.findViewById(R.id.show_tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /** 为各个组件设置对应值 **/
        Type type = getItem(position);
        viewHolder.name.setText(type.getTypeName());
        return convertView;
    }
    /** 使用此类优化列表加载速度 **/
    private final class ViewHolder{
        public TextView name;
    }

}
