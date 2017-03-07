package com.system.personalaccount.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.system.personalaccount.R;

import java.util.List;

/**
 * Created by lenovo on 2016/7/6.
 * 说明书中的viewPager适配器
 */
public class ViewPagerAdapter extends PagerAdapter{

    /** viewPager中的子组件 **/
    private List<View> views;
    /** 活动对象 **/
    private Activity activity;

    public ViewPagerAdapter(Activity activity, List<View> views) {
        this.activity = activity;
        this.views = views;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position), 0);
        /** 判断是否到达最后一页 **/
        if (position == views.size() - 1) {
            Button button = (Button) container.findViewById(R.id.instruction_btn);
            /** 最后一页的按钮，关闭本页面 **/
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
        return views.get(position);
    }

    @Override
    public int getCount() {
        if (views != null) {
            return views.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
