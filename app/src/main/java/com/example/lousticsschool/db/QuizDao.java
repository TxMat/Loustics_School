package com.example.lousticsschool.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QuizDao {

    @Query("SELECT * FROM quiz")
    List<Quiz> getAll();

    @Query("SELECT * FROM quiz WHERE id = :id")
    Quiz getQuiz(long id);

    @Delete
    void delete(Quiz quiz);

    @Insert
    void insert(Quiz quiz);

    @Update
    void update(Quiz quiz);
}
