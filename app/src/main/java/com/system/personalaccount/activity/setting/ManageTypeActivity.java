package com.system.personalaccount.activity.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.system.personalaccount.R;
import com.system.personalaccount.db.AccountDB;
import com.system.personalaccount.model.Type;
import com.system.personalaccount.util.AddTypeDialog;
import com.system.personalaccount.adapter.TypeAdapter;

import java.util.List;

/**
 * Created by lenovo on 2016/7/5.
 * 管理支出收入类型界面，长按某项可进行编辑和删除
 */
public class ManageTypeActivity extends Activity implements View.OnClickListener{

    /** 操作类型 **/
    private int type;
    /** 数据库对象 **/
    private AccountDB accountDB;
    /** 滑动列表 **/
    private ListView listView;
    /** 支出收入类型项 **/
    private List<Type> list;
    /** 适配器 **/
    private ArrayAdapter<Type> adapter;
    /** 返回键 **/
    private Button back;
    /** 新增键 **/
    private ImageButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_type);

        accountDB = AccountDB.getInstance(this);
        listView = (ListView) findViewById(R.id.manage_lv_type);
        /** 获取其他页面传入的操作类型 **/
        type = getIntent().getIntExtra("type", 0);
        TextView title = (TextView) findViewById(R.id.manage_tv_title);
        /** 设置标题 **/
        if (type == 0) {
            title.setText("管理支出类型");
        } else {
            title.setText("管理收入类型");
        }
        /** 初始化列表数据 **/
        list = accountDB.queryType(type);
        /** 初始化适配器 **/
        adapter = new TypeAdapter(ManageTypeActivity.this, R.layout.show_type_item, list);
        /** 配置适配器 **/
        listView.setAdapter(adapter);
        /** 配置列表空布局 **/
        listView.setEmptyView(findViewById(R.id.manage_lv_empty));
        /** 列表长按点击 **/
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                /** 对话框标题以及操作项目 **/
                menu.setHeaderTitle("执行操作");
                menu.add(0, 0, 0, "编辑");
                menu.add(0, 1, 1, "删除");
            }
        });

        back = (Button) findViewById(R.id.manage_btn_back);
        add = (ImageButton) findViewById(R.id.manage_btn_add);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    /**
     * 长按某项回调
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        /** 找到选中位置 **/
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = info.position;
        switch (item.getItemId()) {
            /** 编辑 **/
            case 0:
                /** 自定义对话框 **/
                final AddTypeDialog dialog = new AddTypeDialog(ManageTypeActivity.this, new AddTypeDialog.TypeOnClickListener() {
                    /** 确定按钮 **/
                    @Override
                    public void onPositiveButton(String name) {
                        /** 判断此类型是否存在 **/
                        boolean flag = accountDB.isTypeItemExist(name, type);
                        if (flag) {
                            Toast.makeText(ManageTypeActivity.this, "该类型名称已经存在，修改失败", Toast.LENGTH_LONG).show();
                            return;
                        }
                        /** 修改名称不能为空，若不为空则执行修改 **/
                        flag = !name.equals("") ? accountDB.updateTypeItem(list.get(position).getTypeId(), name, type) : false;
                        if (flag) {
                            Toast.makeText(ManageTypeActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                            /** 刷新列表 **/
                            list.clear();
                            List<Type> temp = accountDB.queryType(type);
                            for (Type t : temp) {
                                list.add(t);
                            }
                            adapter.notifyDataSetChanged();
                            /** 设置选中位置为修改的位置 **/
                            listView.setSelection(position - 1);
                        } else {
                            Toast.makeText(ManageTypeActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onNegativeButton() {

                    }
                }, list.get(position).getTypeName(), null);
                /** 显示对话框 **/
                dialog.show();
                break;
            /** 删除 **/
            case 1:
                /** 删除选中项 **/
                boolean flag = accountDB.deleteTypeItem(list.get(position).getTypeId(), type);
                if (flag) {
                    Toast.makeText(ManageTypeActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                    /** 刷新列表 **/
                    list.clear();
                    List<Type> temp = accountDB.queryType(type);
                    for (Type t : temp) {
                        list.add(t);
                    }
                    adapter.notifyDataSetChanged();
                    /** 设置选中位置为删除位置 **/
                    listView.setSelection(position - 1);
                } else {
                    Toast.makeText(ManageTypeActivity.this, "删除失败", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 点击某一组件时回调此方法
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /** 返回 **/
            case R.id.manage_btn_back:
                finish();
                break;
            /** 新增按钮 **/
            case R.id.manage_btn_add:
                /** 自定义对话框 **/
                AddTypeDialog dialog = new AddTypeDialog(ManageTypeActivity.this, new AddTypeDialog.TypeOnClickListener() {
                    /** 确定按钮 **/
                    @Override
                    public void onPositiveButton(String name) {
                        /** 新增类型是否存在 **/
                        boolean flag = accountDB.isTypeItemExist(name, type);
                        if (flag) {
                            Toast.makeText(ManageTypeActivity.this, "该类型名称已经存在，添加失败", Toast.LENGTH_LONG).show();
                            return;
                        }
                        /** 在名称不为空的情况下，新增类型 **/
                        flag = !name.equals("") ? accountDB.addTypeItem(name, type) : false;
                        if (flag) {
                            Toast.makeText(ManageTypeActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                            /** 刷新列表 **/
                            list.clear();
                            List<Type> temp = accountDB.queryType(type);
                            for (Type t : temp) {
                                list.add(t);
                            }
                            adapter.notifyDataSetChanged();
                            /** 设置选中位置为第一个 **/
                            listView.setSelection(0);
                        } else {
                            Toast.makeText(ManageTypeActivity.this, "添加失败", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onNegativeButton() {

                    }
                }, null, null);
                /** 显示对话框 **/
                dialog.show();
                break;
        }
    }
}
