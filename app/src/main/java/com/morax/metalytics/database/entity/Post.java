package com.morax.metalytics.database.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Post {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;
    public String content;
    public long userId;

    public Post(String title, String content, long userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }
}
