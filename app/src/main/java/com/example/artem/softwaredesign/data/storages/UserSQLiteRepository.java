package com.example.artem.softwaredesign.data.storages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.artem.softwaredesign.data.models.User;
import com.example.artem.softwaredesign.interfaces.UserRepository;

public class UserSQLiteRepository implements UserRepository {

    private final UserDbHelper dbHelper;
    private int userId;

    public UserSQLiteRepository(Context context){
        dbHelper = new UserDbHelper(context);
    }

    @Override
    public User getUser(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            String request = "SELECT * FROM " + dbHelper.getTableName();
            Cursor cursor = db.rawQuery(request, null);
            if (cursor.moveToNext()){
                userId = cursor.getInt(ColumnsTable.id.ordinal());
                String firstName = cursor.getString(ColumnsTable.first_name.ordinal());
                String lastName = cursor.getString(ColumnsTable.last_name.ordinal());
                String phone = cursor.getString(ColumnsTable.phone.ordinal());
                String email = cursor.getString(ColumnsTable.email.ordinal());
                return new User(firstName, lastName, phone, email);
            }
            return null;
        }
        finally {
            db.close();
        }
    }

    @Override
    public void savedUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put(ColumnsTable.first_name.toString(), user.getFirstName());
        cv.put(ColumnsTable.last_name.toString(), user.getLastName());
        cv.put(ColumnsTable.phone.toString(), user.getPhone());
        cv.put(ColumnsTable.email.toString(), user.getEmail());

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            db.update(dbHelper.getTableName(), cv,
                    ColumnsTable.id.toString() + "=" + String.valueOf(userId), null);
        }
        finally {
            db.close();
        }
    }
}
