package com.system.personalaccount.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.system.personalaccount.model.Expend;
import com.system.personalaccount.model.Income;
import com.system.personalaccount.model.Record;
import com.system.personalaccount.model.Remark;
import com.system.personalaccount.model.Type;
import com.system.personalaccount.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mine on 2016/7/5.
 * 数据库操作
 */
public class AccountDB {
    /**
     * 数据库操作对象
     **/
    private SQLiteDatabase db;
    /**
     * 数据库名称
     **/
    private String DBName = "account.db";
    /**
     * 数据库版本号
     **/
    private int Version = 1;
    /**
     * 数据库对象
     **/
    private static AccountDB accountDB;

    /**
     * 当获取数据库对象时，先通过AccountDataBaseHelper创建数据库
     *
     * @param context 上下文
     */
    private AccountDB(Context context) {
        AccountDataBaseHelper helper = new AccountDataBaseHelper(context, DBName, null, Version);
        /** getWritableDatabase返回SQLiteDatabase对象，可对数据库进行操作 **/
        db = helper.getWritableDatabase();
    }

    /**
     * 取得数据库对象
     *
     * @param context 上下文
     * @return 实例化的数据库对象
     */
    public synchronized static AccountDB getInstance(Context context) {
        if (accountDB == null) {
            accountDB = new AccountDB(context);
        }
        return accountDB;
    }

    /**
     * 创建记录
     *
     * @param context    上下文
     * @param recordName 新建记录名称
     * @return 处理结果，1成功，0失败
     */
    public int createRecord(Context context, String recordName) {
        int result = 1;
        if (recordName != null) {
            String sql = "select * from Record";
            /** 执行查询，返回结果集 **/
            Cursor cursor = db.rawQuery(sql, null);
            /** 通过循环逐一查找记录判断是否重名 **/
            if (cursor.moveToFirst()) {
                do {
                    /** 如果重名，返回失败 **/
                    if (recordName.equals(cursor.getString(cursor.getColumnIndex("recordName")))) {
                        Toast.makeText(context, "创建失败，该记录名称已存在", Toast.LENGTH_LONG).show();
                        return 0;
                    }
                } while (cursor.moveToNext());
            }
            /** 不重名，新建记录 **/
            ContentValues values = new ContentValues();
            values.put("recordName", recordName);
            db.insert("Record", null, values);
        } else {
            result = 0;
            Toast.makeText(context, "创建失败，记录名不能为空", Toast.LENGTH_LONG).show();
        }
        return result;
    }

    /**
     * 通过记录名称查询记录ID
     *
     * @param recordName 记录名称
     * @return 记录ID
     */
    public int loadRecordId(String recordName) {
        int recordId = 0;
        Cursor cursor = db.query("Record", null, "recordName=?", new String[]{recordName}, null, null, null);
        if (cursor.moveToFirst()) {
            recordId = cursor.getInt(cursor.getColumnIndex("recordId"));
        }
        return recordId;
    }

    /**
     * 查询所有记录
     *
     * @return 所有记录集合
     */
    public List<Record> loadRecord() {
        List<Record> list = new ArrayList<>();
        Cursor cursor = db.query("Record", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            /** 一次循环就是一条记录，封装后放入集合类中 **/
            do {
                Record rd = new Record();
                rd.setRecordId(cursor.getInt(cursor.getColumnIndex("recordId")));
                rd.setRecordName(cursor.getString(cursor.getColumnIndex("recordName")));
                list.add(rd);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 删除记录
     *
     * @param Record 记录
     * @return 操作结果
     */
    public boolean deleteRecordItem(Record Record) {
        String sql = "delete from Record where recordId = ?";
        try {
            /** 执行操作 **/
            db.execSQL(sql, new String[]{String.valueOf(Record.getRecordId())});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建备注
     *
     * @param context 上下文
     * @param remark  备注
     * @return 处理结果
     */
    public int createRemark(Context context, Remark remark) {
        int result = 1;
        if (remark != null) {
            /** 将备注信息通过ContentValues存入表中 **/
            ContentValues values = new ContentValues();
            values.put("name", remark.getTitle());
            values.put("content", remark.getContent());
            values.put("date", remark.getDate());
            db.insert("remark", null, values);
        } else {
            result = 0;
            Toast.makeText(context, "创建失败", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    /**
     * 查询所有备注
     *
     * @param list 所有备注集合
     */
    public void loadRemark(List<Remark> list) {
        Cursor cursor = db.query("remark", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            /** 通过循环将备注信息逐一封装并添加至备注集合 **/
            do {
                Remark remark = new Remark();
                remark.setRemarkId(cursor.getInt(cursor.getColumnIndex("remarkId")));
                remark.setTitle(cursor.getString(cursor.getColumnIndex("name")));
                remark.setContent(cursor.getString(cursor.getColumnIndex("content")));
                remark.setDate(cursor.getString(cursor.getColumnIndex("date")));
                list.add(remark);
            } while (cursor.moveToNext());
        }
    }

    /**
     * 修改备注信息
     *
     * @param context 上下文
     * @param date    备注日期
     * @param type    操作类型
     * @param remark  一条备注
     * @return 操作结果
     */
    public int updateRemark(Context context, String date, String type, Remark remark) {
        int result = 0;
        /** 查询所有备注 **/
        Cursor cursor = db.query("remark", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                /** 通过备注日期找到对应备注 **/
                if (cursor.getString(cursor.getColumnIndex("date")).equals(date)) {
                    /** 如果操作类型为null，删除该备注 **/
                    if (type == null) {
                        db.delete("remark", "remarkId=?", new String[]{cursor.getString(cursor.getColumnIndex(String.valueOf("remarkId")))});
                    }
                    /** 否则修改备注信息 **/
                    else {
                        if (remark != null) {
                            ContentValues values = new ContentValues();
                            values.put("content", remark.getContent());
                            values.put("name", remark.getTitle());
                            values.put("date", remark.getDate());
                            db.update("remark", values, "remarkId=?", new String[]{cursor.getString(cursor.getColumnIndex(("remarkId")))});
                            Toast.makeText(context, "更新成功", Toast.LENGTH_SHORT).show();
                            result = 1;
                        } else {
                            Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
                            result = 0;
                        }
                    }
                }
            } while (cursor.moveToNext());
        }
        return result;
    }

    /**
     * 查询支出收入类型
     *
     * @param type 操作类型，0为查询支出类型，1为查询收入类型
     * @return 查询结果集
     */
    public List<Type> queryType(int type) {
        List<Type> list = new ArrayList<>();
        String sql;
        Cursor cursor;
        try {
            switch (type) {
                /** 查询支出类型 **/
                case 0:
                    sql = "select * from expendType";
                    cursor = db.rawQuery(sql, null);
                    if (cursor.moveToFirst()) {
                        /** 通过循环逐一封装并放入结果集 **/
                        do {
                            Type temp = new Type();
                            int id = cursor.getInt(cursor.getColumnIndex("expendId"));
                            String name = cursor.getString(cursor.getColumnIndex("typeName"));
                            temp.setTypeId(id);
                            temp.setTypeName(name);
                            list.add(temp);
                        } while (cursor.moveToNext());
                    }
                    break;
                /** 查询收入类型 **/
                case 1:
                    sql = "select * from incomeType";
                    cursor = db.rawQuery(sql, null);
                    if (cursor.moveToFirst()) {
                        do {
                            Type temp = new Type();
                            int id = cursor.getInt(cursor.getColumnIndex("incomeId"));
                            String name = cursor.getString(cursor.getColumnIndex("typeName"));
                            temp.setTypeId(id);
                            temp.setTypeName(name);
                            list.add(temp);
                        } while (cursor.moveToNext());
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 删除选中项
     *
     * @param typeId 选中项的Id
     * @param type   操作类型，0为支出类型，1为收入类型
     * @return
     */
    public boolean deleteTypeItem(int typeId, int type) {
        String sql = "";
        switch (type) {
            case 0:
                sql = "delete from expendType where expendId = " + typeId;
                break;
            case 1:
                sql = "delete from incomeType where incomeId = " + typeId;
                break;
        }
        try {
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改支出收入类型的名称
     *
     * @param typeId  选中项的ID
     * @param newName 新的名称
     * @param type    操作类型
     * @return 操作结果
     */
    public boolean updateTypeItem(int typeId, String newName, int type) {
        String sql = "";
        switch (type) {
            case 0:
                sql = "update expendType set typeName = ? where expendId = " + typeId;
                break;
            case 1:
                sql = "update incomeType set typeName = ? where incomeId = " + typeId;
                break;
        }
        try {
            db.execSQL(sql, new String[]{newName});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 添加支出收入类型
     *
     * @param newName 名称
     * @param type    操作类型
     * @return 操作结果
     */
    public boolean addTypeItem(String newName, int type) {
        String sql = "";
        switch (type) {
            case 0:
                sql = "insert into expendType (typeName) values (?)";
                break;
            case 1:
                sql = "insert into incomeType (typeName) values (?)";
                break;
        }
        try {
            db.execSQL(sql, new String[]{newName});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断该支出收入类型是否已经存在
     *
     * @param newName 名称
     * @param type    操作类型，0为查询支出类型，1为查询收入类型
     * @return true：存在；false：不存在
     */
    public boolean isTypeItemExist(String newName, int type) {
        List<Type> list = queryType(type);
        for (Type t : list) {
            /** 判断支出收入类型中是否已经存在newName名称 **/
            if (t.getTypeName().equals(newName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加支出
     *
     * @param expend 支出
     * @return 操作结果
     */
    public boolean addExpend(Expend expend) {
        try {
            /** 通过ContentValues将信息存入表中 **/
            ContentValues values = new ContentValues();
            values.put("recordId", expend.getRecordId());
            values.put("name", expend.getName());
            values.put("money", expend.getMoney());
            values.put("typeId", expend.getTypeId());
            values.put("date", expend.getDate());
            values.put("remark", expend.getRemark());
            db.insert("expend", null, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 按照天的方式查询支出记录
     *
     * @param date 当前日期，date[0]:年，date[1]:月，date[2]:日
     * @return 结果集
     */
    public List<Expend> queryExpendByDays(String[] date) {
        /** 将date中的年月日拼成一个字符串 **/
        String dateString = date[0] + "-" + date[1] + "-" + date[2];
        List<Expend> data = new ArrayList<>();
        String sql = "select * from expend where recordId = ?";
        try {
            /** 查询当前记录中的所有支出记录 **/
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Constant.getRecordId())});
            if (cursor.moveToFirst()) {
                /** 在循环中通过对日期的匹配筛选支出记录，并将选中的记录封装放入结果集中 **/
                do {
                    /** 取得当前支出记录的日期，与当前日期进行匹配 **/
                    String day = cursor.getString(cursor.getColumnIndex("date")).split(" ")[0];
                    if (dateString.equals(day)) {
                        Expend expend = new Expend();
                        expend.setExpendId(cursor.getInt(cursor.getColumnIndex("expendId")));
                        expend.setRecordId(cursor.getInt(cursor.getColumnIndex("recordId")));
                        expend.setName(cursor.getString(cursor.getColumnIndex("name")));
                        expend.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                        expend.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                        expend.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        expend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                        data.add(expend);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 按照周的方式查询支出记录
     *
     * @param date        当前日期
     * @param currentWeek 当前日期所在本年的周
     * @return 结果集
     */
    public List<Expend> queryExpendByWeeks(String[] date, int currentWeek) {
        List<Expend> data = new ArrayList<>();
        String sql = "select * from expend where recordId = ?";
        try {
            /** 查询当前记录中的所有支出记录 **/
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Constant.getRecordId())});
            if (cursor.moveToFirst()) {
                /** 在循环中通过对周的匹配筛选支出记录 **/
                do {
                    /** 当前支出记录的年 **/
                    String year = cursor.getString(cursor.getColumnIndex("date")).split(" ")[0].split("-")[0];
                    /** 当前支出记录的周 **/
                    int week = Integer.parseInt(cursor.getString(cursor.getColumnIndex("date")).split(" ")[2]);
                    /** 当年和周同时匹配时，将该支出记录封装并存入结果集中 **/
                    if (date[0].equals(year) && currentWeek == week) {
                        Expend expend = new Expend();
                        expend.setExpendId(cursor.getInt(cursor.getColumnIndex("expendId")));
                        expend.setRecordId(cursor.getInt(cursor.getColumnIndex("recordId")));
                        expend.setName(cursor.getString(cursor.getColumnIndex("name")));
                        expend.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                        expend.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                        expend.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        expend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                        data.add(expend);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 按照月的方式查询支出记录
     *
     * @param date 当前日期
     * @return 结果集
     */
    public List<Expend> queryExpendByMonths(String[] date) {
        List<Expend> data = new ArrayList<>();
        String sql = "select * from expend where recordId = ?";
        try {
            /** 查询当前记录中所有的支出记录 **/
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Constant.getRecordId())});
            if (cursor.moveToFirst()) {
                /** 在循环中对年和月进行匹配 **/
                do {
                    /** 当前支出记录的年 **/
                    String year = cursor.getString(cursor.getColumnIndex("date")).split(" ")[0].split("-")[0];
                    /** 当前支出记录的月 **/
                    String month = cursor.getString(cursor.getColumnIndex("date")).split(" ")[0].split("-")[1];
                    /** 判断年和月是否同时匹配，是则封装并存入结果集中 **/
                    if (date[0].equals(year) && date[1].equals(month)) {
                        Expend expend = new Expend();
                        expend.setExpendId(cursor.getInt(cursor.getColumnIndex("expendId")));
                        expend.setRecordId(cursor.getInt(cursor.getColumnIndex("recordId")));
                        expend.setName(cursor.getString(cursor.getColumnIndex("name")));
                        expend.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                        expend.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                        expend.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        expend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                        data.add(expend);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 按照年的方式查询支出记录
     *
     * @param date 当前日期
     * @return 结果集
     */
    public List<Expend> queryExpendByYears(String[] date) {
        List<Expend> data = new ArrayList<>();
        String sql = "select * from expend where recordId = ?";
        try {
            /** 查询当前记录中的所有支出记录 **/
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Constant.getRecordId())});
            if (cursor.moveToFirst()) {
                /** 在循环中对年进行匹配 **/
                do {
                    /** 当前支出记录的年 **/
                    String year = cursor.getString(cursor.getColumnIndex("date")).split(" ")[0].split("-")[0];
                    /** 判断年是否匹配 **/
                    if (date[0].equals(year)) {
                        Expend expend = new Expend();
                        expend.setExpendId(cursor.getInt(cursor.getColumnIndex("expendId")));
                        expend.setRecordId(cursor.getInt(cursor.getColumnIndex("recordId")));
                        expend.setName(cursor.getString(cursor.getColumnIndex("name")));
                        expend.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                        expend.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                        expend.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        expend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                        data.add(expend);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 通过支出类型的ID查询支出类型的名称
     *
     * @param typeId 支出类型的ID
     * @return 支出类型的名称
     */
    public String queryExpendTypeName(int typeId) {
        String sql = "select typeName from expendType where expendId = ?";
        try {
            /** 执行查询 **/
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(typeId)});
            if (cursor.moveToFirst()) {
                /** 返回名称 **/
                return cursor.getString(cursor.getColumnIndex("typeName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改支出记录
     *
     * @param expend 支出
     * @return 操作结果
     */
    public boolean updateExpend(Expend expend) {
        try {
            /** 通过ContentValues将支出记录修改 **/
            ContentValues values = new ContentValues();
            values.put("name", expend.getName());
            values.put("money", expend.getMoney());
            values.put("typeId", expend.getTypeId());
            values.put("date", expend.getDate());
            values.put("remark", expend.getRemark());
            /** 执行修改 **/
            db.update("expend", values, "expendId = ?", new String[]{String.valueOf(expend.getExpendId())});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过支出的ID删除支出记录
     *
     * @param expend 支出
     * @return 操作结果
     */
    public boolean deleteExpendItem(Expend expend) {
        String sql = "delete from expend where expendId = ?";
        try {
            /** 执行删除操作 **/
            db.execSQL(sql, new String[]{String.valueOf(expend.getExpendId())});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 添加收入记录
     *
     * @param income 收入
     * @return 操作结果
     */
    public boolean addIncome(Income income) {
        try {
            /** 通过ContentValues将数据存入表中 **/
            ContentValues values = new ContentValues();
            values.put("recordId", income.getRecordId());
            values.put("name", income.getName());
            values.put("money", income.getMoney());
            values.put("typeId", income.getTypeId());
            values.put("date", income.getDate());
            values.put("remark", income.getRemark());
            /** 执行插入 **/
            db.insert("income", null, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 按照天的方式查询收入记录
     *
     * @param date 当前日期
     * @return 结果集
     */
    public List<Income> queryIncomeByDays(String[] date) {
        /** 将当前日期拼成一个字符串 **/
        String dateString = date[0] + "-" + date[1] + "-" + date[2];
        List<Income> data = new ArrayList<>();
        String sql = "select * from income where recordId = ?";
        try {
            /** 查询当前记录中的所有收入记录 **/
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Constant.getRecordId())});
            if (cursor.moveToFirst()) {
                do {
                    /** 当前收入记录的日期 **/
                    String day = cursor.getString(cursor.getColumnIndex("date")).split(" ")[0];
                    /** 判断当前日期是否与当前收入记录的日期匹配，是则封装并添加至结果集中 **/
                    if (dateString.equals(day)) {
                        Income income = new Income();
                        income.setIncomeId(cursor.getInt(cursor.getColumnIndex("incomeId")));
                        income.setRecordId(cursor.getInt(cursor.getColumnIndex("recordId")));
                        income.setName(cursor.getString(cursor.getColumnIndex("name")));
                        income.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                        income.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                        income.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        income.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                        data.add(income);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 按照周的方式查询收入记录
     *
     * @param date        当前日期
     * @param currentWeek 当前周
     * @return 结果集
     */
    public List<Income> queryIncomeByWeeks(String[] date, int currentWeek) {
        List<Income> data = new ArrayList<>();
        String sql = "select * from income where recordId = ?";
        try {
            /** 查询当前记录中的所有收入记录 **/
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Constant.getRecordId())});
            if (cursor.moveToFirst()) {
                do {
                    /** 当前收入记录的年 **/
                    String year = cursor.getString(cursor.getColumnIndex("date")).split(" ")[0].split("-")[0];
                    /** 当前收入记录的周 **/
                    int week = Integer.parseInt(cursor.getString(cursor.getColumnIndex("date")).split(" ")[2]);
                    /** 判断年和周是否同时匹配 **/
                    if (date[0].equals(year) && currentWeek == week) {
                        Income income = new Income();
                        income.setIncomeId(cursor.getInt(cursor.getColumnIndex("incomeId")));
                        income.setRecordId(cursor.getInt(cursor.getColumnIndex("recordId")));
                        income.setName(cursor.getString(cursor.getColumnIndex("name")));
                        income.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                        income.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                        income.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        income.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                        data.add(income);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 按照月的方式查询收入记录
     *
     * @param date 当前日期
     * @return 结果集
     */
    public List<Income> queryIncomeByMonths(String[] date) {
        List<Income> data = new ArrayList<>();
        String sql = "select * from income where recordId = ?";
        try {
            /** 查询当前记录中的所有收入记录 **/
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Constant.getRecordId())});
            if (cursor.moveToFirst()) {
                do {
                    /** 当前收入记录的年 **/
                    String year = cursor.getString(cursor.getColumnIndex("date")).split(" ")[0].split("-")[0];
                    /** 当前收入记录的月 **/
                    String month = cursor.getString(cursor.getColumnIndex("date")).split(" ")[0].split("-")[1];
                    /** 判断年和月是否同时匹配 **/
                    if (date[0].equals(year) && date[1].equals(month)) {
                        Income income = new Income();
                        income.setIncomeId(cursor.getInt(cursor.getColumnIndex("incomeId")));
                        income.setRecordId(cursor.getInt(cursor.getColumnIndex("recordId")));
                        income.setName(cursor.getString(cursor.getColumnIndex("name")));
                        income.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                        income.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                        income.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        income.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                        data.add(income);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 按照年的方式查询收入记录
     *
     * @param date 当前日期
     * @return 结果集
     */
    public List<Income> queryIncomeByYears(String[] date) {
        List<Income> data = new ArrayList<>();
        String sql = "select * from income where recordId = ?";
        try {
            /** 查询当前记录中的所有收入记录 **/
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(Constant.getRecordId())});
            if (cursor.moveToFirst()) {
                do {
                    /** 当前收入记录的年 **/
                    String year = cursor.getString(cursor.getColumnIndex("date")).split(" ")[0].split("-")[0];
                    /** 判断年是否匹配 **/
                    if (date[0].equals(year)) {
                        Income income = new Income();
                        income.setIncomeId(cursor.getInt(cursor.getColumnIndex("incomeId")));
                        income.setRecordId(cursor.getInt(cursor.getColumnIndex("recordId")));
                        income.setName(cursor.getString(cursor.getColumnIndex("name")));
                        income.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                        income.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                        income.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        income.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                        data.add(income);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 通过收入类型ID查询收入类型的名称
     *
     * @param typeId 收入类型的ID
     * @return 收入类型的名称
     */
    public String queryIncomeTypeName(int typeId) {
        String sql = "select typeName from incomeType where incomeId = ?";
        try {
            /** 执行查询 **/
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(typeId)});
            if (cursor.moveToFirst()) {
                /** 返回名称 **/
                return cursor.getString(cursor.getColumnIndex("typeName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改收入记录
     *
     * @param income 收入
     * @return 操作结果
     */
    public boolean updateIncome(Income income) {
        try {
            /** 使用ContentValues修改收入记录 **/
            ContentValues values = new ContentValues();
            values.put("name", income.getName());
            values.put("money", income.getMoney());
            values.put("typeId", income.getTypeId());
            values.put("date", income.getDate());
            values.put("remark", income.getRemark());
            /** 执行修改 **/
            db.update("income", values, "incomeId = ?", new String[]{String.valueOf(income.getIncomeId())});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除收入记录
     *
     * @param income 收入
     * @return 操作结果
     */
    public boolean deleteIncomeItem(Income income) {
        String sql = "delete from income where incomeId = ?";
        try {
            /** 按照收入记录ID删除对应收入记录 **/
            db.execSQL(sql, new String[]{String.valueOf(income.getIncomeId())});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 按照天的方式统计支出或收入
     * @param date  当前日期
     * @param state 操作类型，0为统计支出，1为统计收入
     * @return 结果集
     */
    public List<Expend> SearchExpendOfDay(String[] date, int state) {
        List<Expend> list = new ArrayList<>();
        Cursor cursor = null;
        /** 根据操作类型，查询当前记录中的所有支出或收入记录 **/
        if (state == 0) {
            cursor = db.query("expend", null, "recordId=?", new String[]{String.valueOf(Constant.getRecordId())}, null, null, null);
        } else if (state == 1) {
            cursor = db.query("income", null, "recordId=?", new String[]{String.valueOf(Constant.getRecordId())}, null, null, null);
        }
        if (cursor.moveToFirst()) {
            do {
                /** 取得当前支出或收入的日期  日期格式为 "yyyy-MM-dd 星期* 周" **/
                String str = cursor.getString(cursor.getColumnIndex("date"));
                /** 将日期按照空格拆分 **/
                String[] findDate = str.split(" ");
                /** 判断当前支出或收入的日期是否与当前日期匹配 **/
                if (findDate[0].equals(date[0] + "-" + date[1] + "-" + date[2])) {
                    /** 判断结果集的数量是否为0 **/
                    if (list.size() == 0) {
                        Expend expend1 = new Expend();
                        expend1.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                        expend1.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                        list.add(expend1);
                    } else {
                        /** 遍历结果集，判断是否出现过当前类型的记录，例如餐饮已经出现且钱为10，当前记录也是餐饮且钱为20，便将两条记录的钱合并为30，如未出现则新添加 **/
                        for (int i = 0; i < list.size(); i++) {
                            /** 判断类型是否一样 **/
                            if (list.get(i).getTypeId() == cursor.getInt(cursor.getColumnIndex("typeId"))) {
                                list.get(i).setMoney(list.get(i).getMoney() + cursor.getDouble(cursor.getColumnIndex("money")));
                                Log.i("mine", "------------------------->" + "相等");
                                break;
                            }
                            /** 当前类型未出现，在末尾添加 **/
                            if (i == list.size() - 1) {
                                Expend expend = new Expend();
                                expend.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                                expend.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                                list.add(expend);
                                break;
                            }
                        }
                    }
                }
            } while (cursor.moveToNext());
            for (int i = 0; i < list.size(); i++) {
                Log.i("mine", "-------------所有Id------------>" + list.get(i).getTypeId());
            }
        }

        return list;
    }

    /**
     * 按照周的方式统计支出或收入
     * @param week 当前周
     * @param state 操作类型
     * @return 结果集
     */
    public List<Expend> SearchExpendOfWeek(String[] date, String week, int state) {
        Log.i("mine", "-------------week------------>" + week);
        List<Expend> list = new ArrayList<>();
        Cursor cursor = null;
        /** 根据操作类型查询当前记录的所有支出或收入 **/
        if (state == 0) {
            cursor = db.query("expend", null, "recordId=?", new String[]{String.valueOf(Constant.getRecordId())}, null, null, null);
        } else if (state == 1) {
            cursor = db.query("income", null, "recordId=?", new String[]{String.valueOf(Constant.getRecordId())}, null, null, null);
        }
        if (cursor.moveToFirst()) {
            do {
                /** 取得当前支出或收入的日期 **/
                String str = cursor.getString(cursor.getColumnIndex("date"));
                String[] findDate = str.split(" ");
                /** 判断周是否匹配 **/
                if (date[0].equals(findDate[0].split("-")[0]) && findDate[2].equals(week)) {
                    Log.i("mine", "------------------------->" + "相等");
                    /** 判断结果集的数量是否为0 **/
                    if (list.size() == 0) {
                        Expend expend1 = new Expend();
                        expend1.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                        expend1.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                        list.add(expend1);
                    } else {
                        /** 遍历结果集，判断是否出现过当前类型的记录，例如餐饮已经出现且钱为10，当前记录也是餐饮且钱为20，便将两条记录的钱合并为30，如未出现则新添加 **/
                        for (int i = 0; i < list.size(); i++) {
                            /** 判断类型是否一样 **/
                            if (list.get(i).getTypeId() == cursor.getInt(cursor.getColumnIndex("typeId"))) {
                                list.get(i).setMoney(list.get(i).getMoney() + cursor.getDouble(cursor.getColumnIndex("money")));
                                Log.i("mine", "------------------------->" + "相等");
                                break;
                            }
                            /** 当前类型未出现，在末尾添加 **/
                            if (i == list.size() - 1) {
                                Expend expend = new Expend();
                                expend.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                                expend.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                                list.add(expend);
                                break;
                            }
                        }
                    }
                }
            } while (cursor.moveToNext());
            for (int i = 0; i < list.size(); i++) {
                Log.i("mine", "-------------所有周Id------------>" + list.get(i).getTypeId());
            }
        }
        return list;
    }

    /**
     * 按照月的方式查询支出或收入
     * @param date 当前日期
     * @param state 操作类型
     * @return 结果集
     */
    public List<Expend> searchExpendOfMonth(String[] date, int state) {
        List<Expend> list = new ArrayList<>();
        Cursor cursor = null;
        /** 根据操作类型查询当前记录的所有支出或收入 **/
        if (state == 0) {
            cursor = db.query("expend", null, "recordId=?", new String[]{String.valueOf(Constant.getRecordId())}, null, null, null);
        } else if (state == 1) {
            cursor = db.query("income", null, "recordId=?", new String[]{String.valueOf(Constant.getRecordId())}, null, null, null);
        }
        if (cursor.moveToFirst()) {
            do {
                /** 取得当前支出或收入的日期 日期格式为 "yyyy-MM-dd 星期* 周"**/
                String str = cursor.getString(cursor.getColumnIndex("date"));
                /** 通过空格拆分取得yyyy-MM-dd **/
                String[] findDate = str.split(" ");
                /** 通过-拆分取得年月日 **/
                String[] sqlDate = findDate[0].split("-");
                /** 判断年和月是否同时匹配 **/
                if (sqlDate[0].equals(date[0]) && sqlDate[1].equals(date[1])) {
                    Log.i("mine", "------------------------->" + "相等");
                    /** 判断结果集的数量是否为0 **/
                    if (list.size() == 0) {
                        Expend expend1 = new Expend();
                        expend1.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                        expend1.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                        list.add(expend1);
                    } else {
                        /** 遍历结果集，判断是否出现过当前类型的记录，例如餐饮已经出现且钱为10，当前记录也是餐饮且钱为20，便将两条记录的钱合并为30，如未出现则新添加 **/
                        for (int i = 0; i < list.size(); i++) {
                            /** 判断类型是否一样 **/
                            if (list.get(i).getTypeId() == cursor.getInt(cursor.getColumnIndex("typeId"))) {
                                list.get(i).setMoney(list.get(i).getMoney() + cursor.getDouble(cursor.getColumnIndex("money")));
                                Log.i("mine", "------------------------->" + "相等");
                                break;
                            }
                            /** 当前类型未出现，在末尾添加 **/
                            if (i == list.size() - 1) {
                                Expend expend = new Expend();
                                expend.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                                expend.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                                list.add(expend);
                                break;
                            }
                        }
                    }
                }
            } while (cursor.moveToNext());
            for (int i = 0; i < list.size(); i++) {
                Log.i("mine", "-------------所有周Id------------>" + list.get(i).getTypeId());
            }
        }
        return list;

    }

    /**
     * 按照年的方式统计支出或收入
     * @param date 当前日期
     * @param state 操作类型
     * @return 结果集
     */
    public List<Expend> searchExpendOfYear(String[] date, int state) {
        List<Expend> list = new ArrayList<>();
        Cursor cursor = null;
        /** 根据操作类型查询当前记录的所有支出或收入 **/
        if (state == 0) {
            cursor = db.query("expend", null, "recordId=?", new String[]{String.valueOf(Constant.getRecordId())}, null, null, null);
        } else if (state == 1) {
            cursor = db.query("income", null, "recordId=?", new String[]{String.valueOf(Constant.getRecordId())}, null, null, null);
        }
        if (cursor.moveToFirst()) {
            do {
                /** 取得当前支出或收入的日期 日期格式为 "yyyy-MM-dd 星期* 周"**/
                String str = cursor.getString(cursor.getColumnIndex("date"));
                /** 通过空格拆分取得yyyy-MM-dd **/
                String[] findDate = str.split(" ");
                /** 通过-拆分取得年月日 **/
                String[] sqlDate = findDate[0].split("-");
                /** 判断年是否匹配 **/
                if (sqlDate[0].equals(date[0])) {
                    Log.i("mine", "------------------------->" + "相等");
                    /** 判断结果集的数量是否为0 **/
                    if (list.size() == 0) {
                        Expend expend1 = new Expend();
                        expend1.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                        expend1.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                        list.add(expend1);
                    } else {
                        /** 遍历结果集，判断是否出现过当前类型的记录，例如餐饮已经出现且钱为10，当前记录也是餐饮且钱为20，便将两条记录的钱合并为30，如未出现则新添加 **/
                        for (int i = 0; i < list.size(); i++) {
                            /** 判断类型是否一样 **/
                            if (list.get(i).getTypeId() == cursor.getInt(cursor.getColumnIndex("typeId"))) {
                                list.get(i).setMoney(list.get(i).getMoney() + cursor.getDouble(cursor.getColumnIndex("money")));
                                Log.i("mine", "------------------------->" + "相等");
                                break;
                            }
                            /** 当前类型未出现，在末尾添加 **/
                            if (i == list.size() - 1) {
                                Expend expend = new Expend();
                                expend.setMoney(cursor.getDouble(cursor.getColumnIndex("money")));
                                expend.setTypeId(cursor.getInt(cursor.getColumnIndex("typeId")));
                                list.add(expend);
                                break;
                            }
                        }
                    }
                }
            } while (cursor.moveToNext());
            for (int i = 0; i < list.size(); i++) {
                Log.i("mine", "-------------所有周Id------------>" + list.get(i).getTypeId());
            }
        }
        return list;

    }


}
