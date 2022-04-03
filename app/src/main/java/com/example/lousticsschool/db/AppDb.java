package com.example.lousticsschool.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDb extends RoomDatabase {

    public abstract UserDao userDao();

    private static AppDb INSTANCE;

    public static synchronized AppDb getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDb.class, "main_db").build();
        }
        return INSTANCE;
    }



}
