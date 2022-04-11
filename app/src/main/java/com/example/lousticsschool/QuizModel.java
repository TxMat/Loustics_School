package com.example.lousticsschool;

import android.os.Bundle;

import com.example.lousticsschool.db.Quiz;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizModel implements Serializable {
    private final ArrayList<Quiz> question_array;
    private final ArrayList<String> user_answers;
    private final ArrayList<Boolean> answer_boolean;
    private final ArrayList<Long> idList;
    private final int total_questions;
    private int question_nb;

    public QuizModel(int total_questions, ArrayList<Quiz> quizArray) {
        this.total_questions = total_questions;
        question_array = new ArrayList<>();
        user_answers = new ArrayList<>();
        question_nb = 1;
        answer_boolean = new ArrayList<>();
        idList = new ArrayList<>();
        // choose a random question in quizArray and avoid duplicates
        for (int i = 0; i < total_questions; i++) {
            int random = (int) (Math.random() * quizArray.size());
            Quiz quiz = quizArray.get(random);
            // check if the question is already in the array
            if (!question_array.contains(quiz)) {
                question_array.add(quiz);
                idList.add(quiz.getId());
            } else {
                i--;
            }
        }





    }

    public ArrayList<Long> getIdList() {
        return idList;
    }

    public int getTotalQuestionsNb() {
        return total_questions;
    }

    public int getQuestionNb() {
        return question_nb;
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
