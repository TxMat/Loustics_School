package com.example.lousticsschool;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizModel implements Serializable {
    private ArrayList<String> question_array;
    private ArrayList<Integer> user_answers;
    private int total_questions;
    private int question_nb;

    public QuizModel(int total_questions) {
        this.total_questions = total_questions;
        question_array = new ArrayList<>();
        user_answers = new ArrayList<>();
        question_nb = 1;
        // generate questions from the database
        for (int i = 0; i < total_questions; i++) {
            question_array.add("Question " + (i + 1));
        }
    }
}
