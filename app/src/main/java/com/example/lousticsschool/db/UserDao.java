package com.example.lousticsschool.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user_table")
    List<User> getAll();

    @Query("SELECT * FROM user_table WHERE id = :id")
    User getUserById(int id);

    @Delete
    void delete(User user);

    @Insert
    void insert(User user);



}
