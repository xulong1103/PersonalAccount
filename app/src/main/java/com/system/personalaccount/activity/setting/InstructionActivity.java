package com.system.personalaccount.activity.setting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.system.personalaccount.R;
import com.system.personalaccount.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/7/6.
 * 系统说明书界面
 */
public class InstructionActivity extends Activity implements ViewPager.OnPageChangeListener{

    /** 滑动页面 **/
    private ViewPager viewPager;
    /** 滑动页面的适配器 **/
    private ViewPagerAdapter adapter;
    /** 滑动页面中的视图集 **/
    private List<View> views;
    /** 底部小点图片 **/
    private ImageView[] dots;
    /** 记录当前选中位置 **/
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.instruction);

        initViews();
        initDots();
    }

    /**
     * 初始化页面
     */
    private void initViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        /** 引用布局，添加至视图集中 **/
        views = new ArrayList<>();
        views.add(layoutInflater.inflate(R.layout.instruction_viewpager_one, null));
        views.add(layoutInflater.inflate(R.layout.instruction_viewpager_two, null));
        views.add(layoutInflater.inflate(R.layout.instruction_viewpager_three, null));
        views.add(layoutInflater.inflate(R.layout.instruction_viewpager_four, null));
        /** 初始化适配器 **/
        adapter = new ViewPagerAdapter(this, views);
        /** 初始化滑动页面 **/
        viewPager = (ViewPager) findViewById(R.id.instruction_vp);
        /** 配置适配器 **/
        viewPager.setAdapter(adapter);
        /** 添加滑动监听 **/
        viewPager.addOnPageChangeListener(this);
    }

    /**
     * 初始化底部小点
     */
    private void initDots() {
        /** 找到底部小点的布局 **/
        LinearLayout ll = (LinearLayout) findViewById(R.id.instruction_ll);
        /** 初始化各个小点 **/
        dots = new ImageView[views.size()];
        /** 将所有小点设置为灰色 **/
        for (int i = 0;i < dots.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(false);
        }
        currentIndex = 0;
        /** 将当前页面对应的小点设置为白色 **/
        dots[currentIndex].setEnabled(true);
    }

    /**
     * 设置小点指向当前页面
     * @param position
     */
    private void setCurrentIndex(int position) {
        /** 当转换后的页面下标未出界且不与当前页面下标相同时，变换小点状态 **/
        if (position < 0 || position > views.size() - 1 || currentIndex == position) {
            return;
        }
        /** 设置转换后的页面对应下标为白色 **/
        dots[position].setEnabled(true);
        /** 之前的对应下标变为灰色 **/
        dots[currentIndex].setEnabled(false);
        /** 设置转换后的页面为当前下标 **/
        currentIndex = position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    /**
     * 当新的页面被选中时调用
     * @param position 当前页面的下标
     */
    @Override
    public void onPageSelected(int position) {
        setCurrentIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
