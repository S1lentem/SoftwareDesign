package com.example.artem.softwaredesign.data.storages;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME  = "User.db";
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_NAME = "users";
    private final String DEFAULT_INSERT_FOR_USER_INFO = "('NoName', 'NoName', 'NoEmail', 'NoPhone')";

    public UserDbHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "("
                + "id integer primary key autoincrement,"
                + UserColNames.FIRST_NAME + " text,"
                + UserColNames.LAST_NAME + " text,"
                + UserColNames.PHONE + " text,"
                + UserColNames.EMAIL + " text" + ");");

        db.execSQL("INSERT INTO " + TABLE_NAME
                + " ("
                + UserColNames.FIRST_NAME + ", "
                + UserColNames.LAST_NAME + ", "
                + UserColNames.PHONE + ", "
                + UserColNames.EMAIL + ") VALUES "
                + DEFAULT_INSERT_FOR_USER_INFO + ";");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String getTableName(){
        return this.TABLE_NAME;
    }
}
