package com.system.personalaccount.activity.function1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.system.personalaccount.R;
import com.system.personalaccount.adapter.RemarkAdapter;
import com.system.personalaccount.db.AccountDB;
import com.system.personalaccount.model.Remark;

import java.util.ArrayList;
import java.util.List;

/**
 * 备注界面
 */
public class RemarkActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    /** 返回键 **/
    private Button back;
    /** 新建备注 **/
    private ImageButton addRemark;
    /** 滑动列表 **/
    private ListView remark;
    /** 滑动列表的数据 **/
    private List<Remark> list = new ArrayList<Remark>();
    /** 滑动列表的适配器 **/
    private ArrayAdapter<Remark> adapter;
    /** 数据库对象 **/
    private AccountDB accountDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_remark);
        accountDB = AccountDB.getInstance(this);
        initViews();
        initData();
        /** 实例化适配器 **/
        adapter = new RemarkAdapter(this, R.layout.remark_item, list);
        /** 设置适配器 **/
        remark.setAdapter(adapter);
        /** 空列表时显示的布局 **/
        remark.setEmptyView(findViewById(R.id.remark_empty));

    }

    /**
     * 初始化列表的数据
     */
    private void initData() {
        accountDB.loadRemark(list);
    }

    /**
     * 初始化各个组件，并设置监听
     */
    private void initViews() {
        back = (Button) findViewById(R.id.remark_btn_back);
        addRemark = (ImageButton) findViewById(R.id.add_remark);
        remark = (ListView) findViewById(R.id.remark_listView);
        back.setOnClickListener(this);
        addRemark.setOnClickListener(this);
        /** 为滑动列表设置长按点击事件 **/
        remark.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                /** 长按显示对话框 **/
                menu.setHeaderTitle("执行操作");
                menu.add(0, 0, 0, "删除");
            }
        });
        remark.setOnItemClickListener(this);
    }

    /**
     * 点击某组件时回调此方法
     * @param v 被点击的组件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remark_btn_back:
                /** 返回键，结束当前页面 **/
                finish();
                break;
            case R.id.add_remark:
                /** 新建备注键，跳转到新建备注界面 **/
                Intent intent = new Intent(RemarkActivity.this, SetRemarkActivity.class);
                /** 当被打开的界面关闭时会返回数据 **/
                startActivityForResult(intent, 1);
                break;
        }
    }

    /**
     * 当从这个界面打开的其他界面关闭时回调此方法
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            switch (resultCode) {
                /** 返回成功时，更新滑动列表的数据 **/
                case RESULT_OK:
                    list.clear();
                    accountDB.loadRemark(list);
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 长按滑动列表后回调此方法
     * @param item 点中的标题
     * @return 操作结果
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        /** 取得滑动列表中被点击的位置 **/
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = info.position;
        switch (item.getItemId()) {
            /** 执行删除操作 **/
            case 0:
                /** 取得date数据集合中下表为position的数据 **/
                String date = list.get(position).getDate();
                /** 执行删除操作 **/
                accountDB.updateRemark(RemarkActivity.this, date, null, null);
                /** 更新集合类中的数据 **/
                list.clear();
                accountDB.loadRemark(list);
                /** 更新列表数据的显示 **/
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 滑动列表被点击时回调的方法
     * @param parent
     * @param view
     * @param position 被点击的位置
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /** 当点击某项时，跳转到查看具体备注信息界面 **/
        Intent intent = new Intent(RemarkActivity.this, RemarkContentActivity.class);
        intent.putExtra("remark", list.get(position));
        startActivityForResult(intent, 1);
    }

}
