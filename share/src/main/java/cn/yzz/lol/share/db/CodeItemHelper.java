package cn.yzz.lol.share.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.support.loader.utils.DB_Model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import cn.yzz.lol.share.bean.CodeItem;

public class CodeItemHelper {
    public final static byte[] _writeLock = new byte[1];
    public static String TABLE = "CodeTable";
    public static String ID = "id";
    Context context;
    private SQLiteDatabase db = null;
    DB_Model db_model;

    public CodeItemHelper(Context context) {
        try {
            db_model = new DB_Model();
            db = SQLiteHelper.getInstance(context).getmSQLiteDatabase();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }


    public void delete() {
        db.delete(TABLE, null, null);
    }


    private boolean Exist(String value, String id, String table) {
        String where = id + " =?";
        String[] args = {value};
        Cursor cursor = db.query(table, null, where, args, null, null, null);
        boolean isExist = cursor != null && cursor.moveToNext();
        cursor.close();
        return isExist;
    }


    void update(CodeItem messageItem) {
        if (messageItem == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        ArrayList<Object> args = db_model.getUpdateString(sb, messageItem);
        String[] string = new String[args.size() + 1];
        args.toArray(string);
        string[args.size()] = messageItem.getCode();
        sb.append(" where " + "code" + " = ?");
        db.execSQL("update " + TABLE + " set " + sb.toString(), string);
    }


    public void DeleteWheretime(String Recordtime) {
        String where = "Recordtime<?";
        String[] args = {Recordtime};
        db.delete("RecordTDlist", where, args);
    }


    public void AddMessage(CodeItem messageItem) {
        synchronized (_writeLock) {
            db.beginTransaction();
            try {

                boolean isExist = Exist(messageItem.getCode(), "code", TABLE);
                if (!isExist) {
                    insert(messageItem);
                } else {//
                    update(messageItem);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }


    /**
     * 查询 2015-4-16 @author lzx
     */
    public ArrayList<CodeItem> SelectMessage(String limit) {
        ArrayList<CodeItem> arrayList = new ArrayList<CodeItem>();
        String sql = "select * from " + TABLE + " limit " + limit;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getColumnCount() > 0) {
            while (cursor.moveToNext()) {
//                int MessageType = cursor.getInt(cursor
//                        .getColumnIndex("MessageType"));
                CodeItem messageItem = new CodeItem();
                try {
                    db_model.SelectMeassageDb(messageItem, cursor);
                    arrayList.add(messageItem);
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }

        return arrayList;
    }

    /**
     * 根据用户表 标识查出未读数量。
     *
     * @return
     */
    public int getMessageCount(String uid) {
        Cursor cursor = null;
        int count = 0;
        try {
            String sql = "select count(*)  from " + TABLE + " where uid=?";
            cursor = db.rawQuery(sql, new String[]{uid});

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != cursor && !cursor.isClosed())
                cursor.close();
        }
        return count;
    }

    void insert(CodeItem messageItem) {
        ContentValues contentValues = new ContentValues();
        try {
            db_model.InserMeassageDb(messageItem, contentValues);
            long id = db.insert(TABLE, null, contentValues);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
