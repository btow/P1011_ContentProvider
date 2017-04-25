package com.example.samsung.p1011_contentprovider;

import android.content.ContentValues;
import android.content.Context;
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
    DBHelper dbHelper;
    SQLiteDatabase database;

    public DB(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public static String getTableContact() {
        return TABLE_CONTACT;
    }

    public static String getTableId() {
        return TABLE_ID;
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public SQLiteDatabase getDataBase() {
        return database;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
            ContentValues contentValues = new ContentValues();
            for (int i = 1; i < 5; i++) {
                contentValues.put(TABLE_NAME, "name " + i);
                contentValues.put(TABLE_EMAIL, "email" + i + "@example.com");
                db.insert(TABLE_CONTACT, null, contentValues);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
