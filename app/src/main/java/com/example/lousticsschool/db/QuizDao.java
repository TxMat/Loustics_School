package com.example.lousticsschool.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuizDao {

    @Query("SELECT * FROM quiz")
    List<Quiz> getAll();

    @Query("SELECT * FROM quiz WHERE id = :id")
    Quiz getQuiz(long id);

    @Insert
    void insert(Quiz quiz);

}
