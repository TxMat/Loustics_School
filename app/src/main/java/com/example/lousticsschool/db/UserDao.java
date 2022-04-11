package com.example.lousticsschool.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Insert
    long insert(User user);


}
