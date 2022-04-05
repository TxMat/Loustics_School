package com.example.lousticsschool.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user")
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    private int totalQuestionsAnswered;

    private int TotalQuestionsCorrect;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalQuestionsAnswered() {
        return totalQuestionsAnswered;
    }

    public void setTotalQuestionsAnswered(int totalQuestionsAnswered) {
        this.totalQuestionsAnswered = totalQuestionsAnswered;
    }

    public int getTotalQuestionsCorrect() {
        return TotalQuestionsCorrect;
    }

    public void setTotalQuestionsCorrect(int totalQuestionsCorrect) {
        TotalQuestionsCorrect = totalQuestionsCorrect;
    }
}
