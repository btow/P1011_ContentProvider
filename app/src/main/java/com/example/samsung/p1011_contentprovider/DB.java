package com.example.samsung.p1011_contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by samsung on 24.04.2017.
 */

public class DB {

    private static final String
            DB_NAME = "address_book",
            TABLE_CONTACT = "contacts",
            TABLE_ID = "_id",
            TABLE_NAME = "name",
            TABLE_EMAIL = "email",
            DB_CREATE = "create table " + TABLE_CONTACT + "("
                    + TABLE_ID + " integer primary key autoincrement, "
                    + TABLE_NAME + " text, " + TABLE_EMAIL + " text" + ");";
    private final int DB_VERSION = 1;
    private Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    DB(Context context) {
        mCtx = context;
    }

    static String getTableId() {
        return TABLE_ID;
    }

    static String getTableName() {
        return TABLE_NAME;
    }

    public void openDB() {
        mDBHelper = new DBHelper(mCtx);
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public long insert(ContentValues values) {
        return mDatabase.insert(TABLE_CONTACT, null, values);
    }

    public void closeDb() {
        if (mDBHelper != null) mDBHelper.close();
    }

    public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return mDatabase.query(TABLE_CONTACT, projection, selection,
                selectionArgs, null, null, sortOrder);
    }

    public int update(ContentValues values, String selection, String[] selectionArgs) {
        return mDatabase.update(TABLE_CONTACT, values, selection, selectionArgs);
    }

    public int delete(String selection, String[] selectionArgs) {
        return mDatabase.delete(TABLE_CONTACT, selection, selectionArgs);
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                db.execSQL(DB_CREATE);
                ContentValues contentValues = new ContentValues();
                for (int i = 1; i < 5; i++) {
                    contentValues.put(TABLE_NAME, "name " + i);
                    contentValues.put(TABLE_EMAIL, "email" + i + "@example.com");
                    db.insert(TABLE_CONTACT, null, contentValues);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
