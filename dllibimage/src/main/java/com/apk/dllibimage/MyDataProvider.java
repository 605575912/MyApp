//package com.apk.dllibimage;
//
//import android.content.ContentProvider;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.UriMatcher;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteQueryBuilder;
//import android.net.Uri;
//import android.util.Log;
//
///**
// * Created by liangzhenxiong on 15/12/20.
// */
//public class MyDataProvider extends ContentProvider
//{
//
//    // public static final String SCHEME = "test";
//    public static final String SCHEME = "content"; // 源码里面规定这样写，所以这个地方改变不了
//
//    public static final String HOST = "com.zyj";
//    public static final String PORT = "497393102";
//    public static final String PATH = "simple";
//
//    public static final int ALARMS = 1;
//    public static final String SHARE_LIST_TYPE = "com.zyj.test.dir/";
//    public static final int ALARMS_ID = 2;
//    public static final String SHARE_TYPE = "com.zyj.test.item/";
//
//    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//
//    private SQLiteOpenHelper mDB = null;
//
//    // ===content://com.zyj:497393102/simple
//    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + HOST + ":" + PORT + "/" + PATH);
//
//    // 添加Uri的匹配方式，返回的就是上面自定义的整数类型，1代表操作的是一个批量，2操作的是单独的一个对象
//    static
//    {
//        sURIMatcher.addURI(HOST + ":" + PORT, PATH, ALARMS);
//        sURIMatcher.addURI(HOST + ":" + PORT, PATH + "/#", ALARMS_ID);
//    }
//
//    @Override
//    public boolean onCreate()
//    {
//        mDB = new MyDB(getContext()); // 获取数据库的引用
//        return mDB != null;
//    }
//
//    @Override
//    public String getType(Uri uri)
//    {
//        // 得到我们自定义的Uri的类型，看上面你自己的定义
//        int match = sURIMatcher.match(uri);
//        switch (match)
//        {
//            case ALARMS:
//            {
//                return SHARE_LIST_TYPE;
//            }
//            case ALARMS_ID:
//            {
//                return SHARE_TYPE;
//            }
//            default:
//            {
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//            }
//        }
//    }
//
//    @Override
//    public Uri insert(Uri uri, ContentValues values)
//    {
//        // 首先是看Uri和我们自定义的是否匹配，，匹配则将数据属性插入到数据库中并同志更新
//        SQLiteDatabase db = mDB.getWritableDatabase();
//        if (sURIMatcher.match(uri) != ALARMS)
//        {
//            throw new IllegalArgumentException("Unknown/Invalid URI " + uri);
//        }
//
//        ContentValues filteredValues = new ContentValues();
//        filteredValues.put(MyDB.BEAN_ID, values.getAsInteger(MyDB.BEAN_ID));
//        filteredValues.put(MyDB.MESSAGE, values.getAsString(MyDB.MESSAGE));
//        filteredValues.put(MyDB.TASK_PROGRESS, values.getAsFloat(MyDB.TASK_PROGRESS));
//        long rowID = db.insert(MyDB.TABLET, null, filteredValues);
//        if (rowID != -1)
//        {
//            getContext().getContentResolver().notifyChange(uri, null);
//        }
//        return CONTENT_URI;
//    }
//
//    @Override
//    public int delete(Uri uri, String selection, String[] selectionArgs)
//    {
//
//        // 首先是看Uri和我们自定义的是否匹配，，匹配则进行删除
//
//        SQLiteDatabase db = mDB.getWritableDatabase();
//        int count = 0;
//        int match = sURIMatcher.match(uri);
//        switch (match)
//        {
//            case ALARMS:
//            case ALARMS_ID:
//                String where = null;
//                // 这里对selection进行匹配操作，看你传递的是一个批量还是一个单独的文件
//                if (selection != null)
//                {
//                    if (match == ALARMS)
//                    {
//                        where = "( " + selection + " )";
//                    }
//                    else
//                    {
//                        where = "( " + selection + " ) AND ";
//                    }
//                }
//                else
//                {
//                    where = "";
//                }
//                if (match == ALARMS_ID)
//                {
//                    // 如果你传递的是一个单独的文件，也就是Uri后面添加了/item的，那么在这里把该值与数据库中的属性段进行比较，返回sql语句中的where
//                    String segment = uri.getPathSegments().get(1);
//                    long rowId = Long.parseLong(segment);
//                    where += " ( " + MyDB.BEAN_ID + " = " + rowId + " ) ";
//                }
//                count = db.delete(MyDB.TABLET, where, selectionArgs);
//                break;
//            default:
//                throw new UnsupportedOperationException("Cannot delete URI: " + uri);
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
//        return count;
//    }
//
//    @Override
//    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
//    {
//        // 基本同上了
//        SQLiteDatabase db = mDB.getWritableDatabase();
//
//        int count;
//        long rowId = 0;
//
//        int match = sURIMatcher.match(uri);
//        switch (match)
//        {
//            case ALARMS:
//            case ALARMS_ID:
//            {
//                String myWhere;
//                if (selection != null)
//                {
//                    if (match == ALARMS)
//                    {
//                        myWhere = "( " + selection + " )";
//                    }
//                    else
//                    {
//                        myWhere = "( " + selection + " ) AND ";
//                    }
//                }
//                else
//                {
//                    myWhere = "";
//                }
//                if (match == ALARMS_ID)
//                {
//                    String segment = uri.getPathSegments().get(1);
//                    rowId = Long.parseLong(segment);
//                    myWhere += " ( " + MyDB.BEAN_ID + " = " + rowId + " ) ";
//                }
//
//                if (values.size() > 0)
//                {
//                    count = db.update(MyDB.TABLET, values, myWhere, selectionArgs);
//                }
//                else
//                {
//                    count = 0;
//                }
//                break;
//            }
//            default:
//            {
//                throw new UnsupportedOperationException("Cannot update URI: " + uri);
//            }
//        }
//        getContext().getContentResolver().notifyChange(uri, null);
//
//        return count;
//    }
//
//    @Override
//    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
//    {
//        SQLiteDatabase db = mDB.getReadableDatabase();
//        SQLiteQueryBuilder qb = new SQLiteQueryBuilder(); //SQLiteQueryBuilder是一个构造SQL查询语句的辅助类
//
//        int match = sURIMatcher.match(uri);
//        switch (match)
//        {
//            case ALARMS:
//            {
//                qb.setTables(MyDB.TABLET);
//                break;
//            }
//            case ALARMS_ID:
//            {
//                qb.setTables(MyDB.TABLET);
//                qb.appendWhere(MyDB.BEAN_ID + "=");
//                qb.appendWhere(uri.getPathSegments().get(1));
//                break;
//            }
//            default:
//                throw new IllegalArgumentException("Unknown URI: " + uri);
//        }
//        Cursor ret = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
//
//        if (ret != null)
//        {
//            ret.setNotificationUri(getContext().getContentResolver(), uri);
//            Log.d("zyj", "created cursor " + ret + " on behalf of ");
//        }
//        else
//        {
//            Log.d("zyj", "query failed in downloads database");
//        }
//        return ret;
//    }
//
//    private static class MyDB extends SQLiteOpenHelper
//    {
//
//        // 这里就是数据库了，数据库字段、名称、表名等...
//        private static final String DATABASE = "test_database";
//        public static final String TABLET = "test_table";
//        public static String ID = "_id";
//        public static String BEAN_ID = "_bean_id";
//        public static String MESSAGE = "_message";
//        public static String TASK_PROGRESS = "_progress";
//
//        private SQLiteDatabase mDB = null;
//
//        private final String msql = "CREATE TABLE IF NOT EXISTS " + TABLET + "( " + ID
//                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BEAN_ID + " TEXT, " + MESSAGE + " TEXT, " + TASK_PROGRESS
//                + " TEXT )";
//
//        private MyDB(Context context)
//        {
//            super(context, DATABASE, null, 1);
//        }
//
//        @Override
//        public void onCreate(SQLiteDatabase db)
//        {
//            mDB = db;
//            mDB.execSQL(msql);
//        }
//
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
//        {
//            // 升级，自己可以去实现
//        }
//    }
//}