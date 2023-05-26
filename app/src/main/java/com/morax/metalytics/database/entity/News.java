package com.morax.metalytics.database.entity;

public class News {
    public long id;
    public byte[] image;
    public String title;
    public String content;
    public String source;

    public News(byte[] image, String title, String content, String source) {
        this.image = image;
        this.title = title;
        this.content = content;
        this.source = source;
    }
}
