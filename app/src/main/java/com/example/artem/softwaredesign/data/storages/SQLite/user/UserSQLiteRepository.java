package com.example.artem.softwaredesign.data.storages.SQLite.user;

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
    public void savedUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put(ColumnsTable.first_name.toString(), user.getFirstName());
        cv.put(ColumnsTable.last_name.toString(), user.getLastName());
        cv.put(ColumnsTable.phone.toString(), user.getPhone());
        cv.put(ColumnsTable.email.toString(), user.getEmail());

        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            db.update(dbHelper.getTableName(), cv,
                    ColumnsTable.id.toString() + "=" + String.valueOf(userId), null);
        }
    }

    @Override
    public void updateUserSettings(String feedResource, int countFeedForCache, int userId){
        ContentValues cv =  new ContentValues();
        cv.put(ColumnsTable.news_source.toString(), feedResource);
        cv.put(ColumnsTable.count_for_cache.toString(), countFeedForCache);

        try(SQLiteDatabase database = dbHelper.getReadableDatabase()){
            database.update(dbHelper.getTableName(), cv,
                    ColumnsTable.id.toString() + "=" + String.valueOf(userId), null);
        }
    }

    @Override
    public void addUser(User user){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColumnsTable.first_name.toString(), user.getFirstName());
        contentValues.put(ColumnsTable.last_name.toString(), user.getLastName());
        contentValues.put(ColumnsTable.phone.toString(), user.getPhone());
        contentValues.put(ColumnsTable.email.toString(), user.getEmail());
        contentValues.put(ColumnsTable.password.toString(), user.getPassword());
        contentValues.put(ColumnsTable.news_source.toString(), user.getNewsSource());
        contentValues.put(ColumnsTable.count_for_cache.toString(), user.getCountRssForCached());

        try (SQLiteDatabase database = dbHelper.getReadableDatabase()) {
            long e = database.insert(dbHelper.getTableName(), null, contentValues);
            long d  = e;
        }
    }

    @Override
    public User getUserByEmail(String email){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            String request = "SELECT * FROM " + dbHelper.getTableName()
                    + " WHERE " + ColumnsTable.email.toString() + " = '" + email + "';";
            Cursor cursor = db.rawQuery(request, null);
            if (cursor.moveToFirst()){
                userId = cursor.getInt(ColumnsTable.id.ordinal());
                String firstName = cursor.getString(ColumnsTable.first_name.ordinal());
                String lastName = cursor.getString(ColumnsTable.last_name.ordinal());
                String phone = cursor.getString(ColumnsTable.phone.ordinal());
                String password = cursor.getString(ColumnsTable.password.ordinal());
                String newSource = cursor.getString(ColumnsTable.news_source.ordinal());
                int countForCache = cursor.getInt(ColumnsTable.count_for_cache.ordinal());

                return new User(userId, firstName, lastName, email, phone, password, newSource, countForCache);
            }
            return null;
        }
        finally {
            db.close();
        }
    }

    @Override
    public User getUserById(int id){
        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            String request = "SELECT * FROM " + dbHelper.getTableName()
                    + " WHERE " + ColumnsTable.id.toString() + " = " + String.valueOf(id) + ";";
            Cursor cursor = db.rawQuery(request, null);
            if (cursor.moveToFirst()) {
                userId = cursor.getInt(ColumnsTable.id.ordinal());
                String firstName = cursor.getString(ColumnsTable.first_name.ordinal());
                String lastName = cursor.getString(ColumnsTable.last_name.ordinal());
                String phone = cursor.getString(ColumnsTable.phone.ordinal());
                String email = cursor.getString(ColumnsTable.email.ordinal());
                String password = cursor.getString(ColumnsTable.password.ordinal());
                String newsResource = cursor.getString(ColumnsTable.news_source.ordinal());
                int countFotCache = cursor.getInt(ColumnsTable.count_for_cache.ordinal());

                return new User(userId, firstName, lastName, email, phone, password, newsResource, countFotCache);
            }
            return null;
        }
    }
}
