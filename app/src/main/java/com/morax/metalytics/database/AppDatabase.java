package com.morax.metalytics.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.morax.metalytics.database.dao.PostDao;
import com.morax.metalytics.database.dao.UserDao;
import com.morax.metalytics.database.entity.Post;
import com.morax.metalytics.database.entity.User;

@Database(entities = {User.class, Post.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract PostDao postDao();
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "test_db3").allowMainThreadQueries().build();
        }
        return instance;
    }


}