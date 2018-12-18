package com.example.artem.softwaredesign.data.storages.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME  = "User.db";
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_NAME = "users";

    public UserDbHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "("
                + "id integer primary key autoincrement,"
                + ColumnsTable.first_name + " text,"
                + ColumnsTable.last_name + " text,"
                + ColumnsTable.phone + " text,"
                + ColumnsTable.email + " text,"
                + ColumnsTable.password + " text,"
                + ColumnsTable.news_source + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public String getTableName(){
        return this.TABLE_NAME;
    }
}
