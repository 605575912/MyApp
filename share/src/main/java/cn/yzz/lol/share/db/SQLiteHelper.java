package cn.yzz.lol.share.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.support.loader.utils.DB_Model;

import cn.yzz.lol.share.bean.CodeItem;

/**
 */
public class SQLiteHelper {
    @SuppressWarnings("unused")
    public static SQLiteHelper instance;
    static int db_version = 3; //2015-7-06
//    static int db_version = 1; //2015-7-06
    /**
     * 库名
     */

//    public static String APP_DATABASE_NAME = ".db";
//    public static String APP_DATABASE_NAME = "xsw_city.db";

    /**
     * 本地Context对象
     */
    private Context mContext = null;

    /**
     * 执行open（）打开数据库时，保存返回的数据库对象
     */
    private SQLiteDatabase mSQLiteDatabase = null;

    /**
     * 由SQLiteOpenHelper继承过来
     */
    private DatabaseHelper mDatabaseHelper = null;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        /**
         * 构造函数-创建一个数据库
         *
         * @param context
         */
        DatabaseHelper(Context context) {
            super(context, context.getPackageName() + ".db", null, db_version); // 数据库版本
            // DB_VERSION
            // = 1;
        }

        @Override
        public SQLiteDatabase getReadableDatabase() {
            return super.getReadableDatabase();
        }

        @Override
        public SQLiteDatabase getWritableDatabase() {
            return super.getWritableDatabase();
        }

        /**
         * 创建一个表
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            DB_Model db_Model = new DB_Model();
            CreateMeassageDb(db, db_Model);

            db_Model = null;
        }

        /**
         * @param db
         */
        private void CreateMeassageDb(SQLiteDatabase db, DB_Model db_Model) {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE " + CodeItemHelper.TABLE + " (");
            sb.append(CodeItemHelper.ID + "  TEXT PRIMARY KEY ");
            db_Model.CreateMeassageDb(sb, CodeItem.class);
            db.execSQL(sb.toString());
        }


        /**
         * 升级数据库
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (oldVersion) {
                case 1: {
                    db.execSQL("DROP TABLE IF EXISTS " + CodeItemHelper.TABLE);
                    onCreate(db);
                }
                break;
                case 2: {
//                    DB_Model db_Model = new DB_Model();
//                    CreateDataDownDb(db, db_Model);
//                    CreateCityIDDb(db, db_Model);
                }
                break;
                case 3: {

                }
                break;
            }


        }
    }

    /**
     * 构造函数-取得Context
     *
     * @param context
     */
    private SQLiteHelper(Context context) {
        mContext = context;
    }

    public static SQLiteHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (SQLiteHelper.class) {
                if (instance == null) {
                    instance = new SQLiteHelper(context);
                }
                instance.open();
            }
        }
        return instance;
    }


    /**
     * 打开数据库
     *
     * @throws SQLException
     */
    private SQLiteDatabase open() throws SQLException {
        if (mSQLiteDatabase != null) {
            if (mSQLiteDatabase.isOpen())
                return mSQLiteDatabase;
        } else {
            mDatabaseHelper = new DatabaseHelper(mContext);
            mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
            return mSQLiteDatabase;
        }
        return mSQLiteDatabase;
    }

    public SQLiteDatabase getmSQLiteDatabase() {
        return instance.open();
    }

    /**
     * 关闭数据库
     */
    public void close() {
        if (mDatabaseHelper != null) {
            mDatabaseHelper.close();
            instance = null;
        }
    }

    public long insertData(String table, ContentValues initialValues,
                           String keyid) {
        long rowId = -1;
        try {
            rowId = mSQLiteDatabase.insert(table, keyid, initialValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowId;
    }

    // public boolean Exist(String table, String id, String value) {
    // String where = id + "=? and accountid = ?";
    // String[] args = { value, "" + StartLBS.mySelf.getBid() };
    // Cursor cursor = mSQLiteDatabase.query(table, null, where, args, null,
    // null, null);
    // boolean isExist = cursor != null && cursor.moveToNext();
    // cursor.close();
    // return isExist;
    // }

    public boolean deleteData(String table, String key, String value) {
        boolean flag = false;
        try {
            flag = mSQLiteDatabase
                    .delete(table, key + "='" + value + "'", null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;

    }

    public Cursor selectDataCursor(String table, String[] columns,
                                   String selection, String[] selectionArgs, String groupBy,
                                   String having, String orderBy, String limit) {
        Cursor cursor = mSQLiteDatabase.query(table, columns, selection,
                selectionArgs, groupBy, having, orderBy, limit);
        return cursor;
    }

    public boolean deleteAllData(String table) {
        boolean flag = false;
        try {
            flag = mSQLiteDatabase.delete(table, null, null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean updateData(String table, String key_id, String rowId,
                              ContentValues args) {
        boolean flag = false;
        try {
            flag = mSQLiteDatabase.update(table, args, key_id + "=" + rowId,
                    null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    // public Cursor findDataByKey(String table, String[] keyString, String key,
    // String value) {
    // Cursor cursor = null;
    // try {
    // cursor = mSQLiteDatabase.query(table, keyString, key + "=" + '"'
    // + value + '"', null, null, null, null);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return cursor;
    // }

    // public Cursor getAllDatas(String table, String[] keyString, String
    // selection) {
    // Cursor cursor = null;
    // try {
    // cursor = mSQLiteDatabase.query(table, keyString, selection, null,
    // null, null, null);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return cursor;
    // }

    // public Cursor getAllVoiceDatas(String[] keyString) {
    // Cursor cursor = null;
    // try {
    // cursor = mSQLiteDatabase.query(DB_TABLE_VOICE, keyString, null,
    // null, null, null, "voice_date asc");
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return cursor;
    // }

    /**
     * 查询记录的总数
     *
     * @return
     */
    public long getCount(String table) {
        long length = 0;
        try {
            String sql = "select count(*) from " + table;
            Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
            cursor.moveToFirst();
            length = cursor.getLong(0);
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return length;
    }

    public void executeBySQL(String sql, String[] args) {
        if (args == null) {
            mSQLiteDatabase.execSQL(sql);
        } else {
            mSQLiteDatabase.execSQL(sql, args);
        }

    }

    /**
     * 带返回值的查询
     *
     * @param sql
     * @param args
     * @return
     */
    public Cursor rawQuery(String sql, String[] args) {
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, args);
        return cursor;
    }

    // /**
    // * 分页
    // *
    // * @param offsetResult
    // * @param maxResult
    // * @return
    // */
    // public Cursor getOffsetItems(int offsetResult, int maxResult) {
    // String sql = "select * from " + DB_TABLE_VOICE
    // + " order by voice_date asc limit ?,?";
    // Cursor cursor = null;
    // try {
    // cursor = mSQLiteDatabase.rawQuery(
    // sql,
    // new String[] { String.valueOf(offsetResult),
    // String.valueOf(maxResult) });
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return cursor;
    // }

}
