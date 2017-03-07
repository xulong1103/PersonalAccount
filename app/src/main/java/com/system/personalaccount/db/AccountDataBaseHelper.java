package com.system.personalaccount.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by mine on 2016/7/5.
 */
public class AccountDataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String Record="create table Record("+"recordId integer primary key autoincrement,"+"recordName text)";
    public static final String Expend="create table expend("+"expendId integer primary key autoincrement,"+"recordId integer,"+"name text,"+"money decimal(11,2),"+"typeId integer,"+"date text,"+"remark text)";
    public static final String Income="create table income("+"incomeId integer primary key autoincrement,"+"recordId integer,"+"name text,"+"money decimal(11,2),"+"typeId integer,"+"date text,"+"remark text)";
    public static final String ExpendType="create table expendType("+"expendId integer primary key autoincrement,"+"typeName text)";
    public static final String IncomeType="create table incomeType("+"incomeId integer primary key autoincrement,"+"typeName text)";
    public static final String Remark="create table remark("+"remarkId integer primary key autoincrement,"+"name text,"+"date text,"+"content text)";

    public AccountDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            /** 分别创建每张表 **/
            db.execSQL(Record);
            db.execSQL(Expend);
            db.execSQL(Income);
            db.execSQL(IncomeType);
            db.execSQL(ExpendType);
            db.execSQL(Remark);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initExpendType(db);
        initIncomeType(db);
    }

    /**
     * 初始化支出类型，开始时存入3条数据
     * @param db
     */
    private void initExpendType(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("typeName", "早午晚餐");
        db.insert("expendType", null, values);
        values.clear();
        values.put("typeName", "烟酒茶");
        db.insert("expendType", null, values);
        values.clear();
        values.put("typeName", "水果零食");
        db.insert("expendType", null, values);
    }

    /**
     * 初始化收入类型，开始时存入3条数据
     * @param db
     */
    private void initIncomeType(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("typeName", "工资");
        db.insert("incomeType", null, values);
        values.clear();
        values.put("typeName", "奖金");
        db.insert("incomeType", null, values);
        values.clear();
        values.put("typeName", "投资");
        db.insert("incomeType", null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}