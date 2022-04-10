package com.example.lousticsschool;

import android.os.Bundle;

import com.example.lousticsschool.db.Quiz;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizModel implements Serializable {
    private ArrayList<Quiz> question_array;
    private ArrayList<String> user_answers;
    private ArrayList<Boolean> answer_boolean;
    private int total_questions;
    private int question_nb;

    public QuizModel(int total_questions, ArrayList<Quiz> quizArray) {
        this.total_questions = total_questions;
        question_array = new ArrayList<>();
        user_answers = new ArrayList<>();
        question_nb = 1;
        answer_boolean = new ArrayList<>();
        // choose random
        for (int i = 0; i < total_questions; i++) {
            int random = (int) (Math.random() * quizArray.size());
            question_array.add(quizArray.get(random));
        }
    }

    public Quiz getQuestion(int index) {
        return question_array.get(index);
    }

    public int getTotalQuestionsNb() {
        return total_questions;
    }

    public int getQuestionNb() {
        return question_nb;
    }

    public void setQuestionNb(int question_nb) {
        this.question_nb = question_nb;
    }

    public ArrayList<String> getUserAnswers() {
        // print the array for debugging

        return user_answers;
    }

    public void addUserAnswer(String answer) {
        user_answers.add(answer);
        // store if the questin is true or false
        if (isCorrect(answer)) {
            answer_boolean.add(true);
        } else {
            answer_boolean.add(false);
        }
    }

    public ArrayList<Boolean> getAnswer_boolean() {
        return answer_boolean;
    }

    public Quiz getCurrentQuestionObject() {
        return question_array.get(question_nb - 1);
    }

    public Bundle getNextBundle(){
        Bundle bundle = new Bundle();
        question_nb++;
        bundle.putSerializable("quizModel", this);
        return bundle;
    }

    public boolean isCorrect(String btntxt) {
        return getCurrentQuestionObject().getCorrectAnswer().equals(btntxt);
    }
}
