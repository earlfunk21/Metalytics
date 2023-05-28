package com.morax.metalytics.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.morax.metalytics.database.entity.Post;

import java.util.List;


@Dao
public interface PostDao {
    @Insert
    void insert(Post post);

    @Delete
    void delete(Post post);

    @Update
    void update(Post post);

    @Query("SELECT * FROM Post ORDER BY id DESC")
    List<Post> getPosts();

    @Query("DELETE FROM Post")
    void deleteAll();
}
