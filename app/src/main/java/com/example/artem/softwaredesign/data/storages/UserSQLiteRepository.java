package com.example.artem.softwaredesign.data.storages;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.artem.softwaredesign.data.models.User;

enum TableCol{
    Id,
    FirstName,
    LastName,
    Phone,
    Email
}

public class UserSQLiteRepository {

    private final UserDbHelper dbHelper;

    public UserSQLiteRepository(Context context){
        dbHelper = new UserDbHelper(context);
    }

    public User getUser(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            String request = "SELECT * FROM " + dbHelper.getTableName();
            Cursor cursor = db.rawQuery(request, null);
            if (cursor.moveToNext()){
                String firstName = cursor.getString(TableCol.FirstName.ordinal());
                String lastName = cursor.getString(TableCol.LastName.ordinal());
                String phone = cursor.getString(TableCol.Phone.ordinal());
                String email = cursor.getString(TableCol.Email.ordinal());
                return new User(firstName, lastName, phone, email);
            }
            return null;
        }
        finally {
            db.close();
        }
    }
}
