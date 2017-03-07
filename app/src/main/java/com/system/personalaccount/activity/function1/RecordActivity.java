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
import android.widget.ListView;
import android.widget.Toast;

import com.system.personalaccount.R;
import com.system.personalaccount.activity.function2.SecondActivity;
import com.system.personalaccount.adapter.RecordAdapter;
import com.system.personalaccount.db.AccountDB;
import com.system.personalaccount.model.Record;
import com.system.personalaccount.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录界面
 */
public class RecordActivity extends Activity {

    /** 滑动列表 **/
    private ListView listView;
    /** 滑动列表中的数据 **/
    private List<Record> list=new ArrayList<Record>();
    /** 滑动列表的适配器 **/
    private ArrayAdapter<Record> adapter;
    /** 数据库对象 **/
    private AccountDB accountDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 不显示标题栏 **/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_record);
        accountDB=AccountDB.getInstance(this);
        initViews();
        initList();
        /** 实例化适配器 **/
        adapter=new RecordAdapter(this, R.layout.record_item, list);
        /** 列表设置适配器 **/
        listView.setAdapter(adapter);
        /** 设置空列表所显示的布局 **/
        listView.setEmptyView(findViewById(R.id.record_empty));
        /** 列表的点击事件 **/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /** 选中某一记录后，跳转到第二个界面 **/
                Intent intent = new Intent(RecordActivity.this, SecondActivity.class);
                /** 设置全局变量，记录当前记录的ID **/
                Constant.setRecordId(list.get(position).getRecordId());
                startActivity(intent);
                /** 关闭本页面 **/
                RecordActivity.this.finish();
            }
        });
        /** 列表的长按点击事件 **/
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                /** 长按后显示对话框 **/
                menu.setHeaderTitle("执行操作");
                menu.add(0, 0, 0, "删除");
            }
        });
    }

    /**
     * 初始化列表数据
     */
    private void initList() {
        list=accountDB.loadRecord();
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        listView= (ListView) findViewById(R.id.record_listView);
    }

    /**
     * 长按列表中的某项会回调此方法
     * @param item 选中的操作
     * @return 操作结果
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        /** 取得当前的点击的列表中的位置 **/
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = info.position;
        switch (item.getItemId()) {
            /** 删除该条目 **/
            case 0:
                /** 执行删除操作 **/
                boolean flag = accountDB.deleteRecordItem(list.get(position));
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
     * 更新列表
     */
    private void notifyListView() {
        List<Record> temp = accountDB.loadRecord();
        list.clear();
        for (Record r : temp) {
            list.add(r);
        }
        adapter.notifyDataSetChanged();
    }
}
