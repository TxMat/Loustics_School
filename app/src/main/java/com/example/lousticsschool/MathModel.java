package com.example.lousticsschool;

import android.os.Bundle;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class MathModel implements Serializable {
    private int question_nb;
    private ArrayList<String> calcul_array;
    private ArrayList<Integer> user_answers;
    private int total_questions;
    private String CalcString;

    public MathModel(int total_questions, String operator_list) {
        // fill the hashmap with the operations with random operators
        this.total_questions = total_questions;
        this.question_nb = 1;
        this.user_answers = new ArrayList<>();
        this.calcul_array = new ArrayList<>();
        for (int i = 0; i < total_questions; i++) {
            Bundle bundle = RandomizeCalc(2,100,operator_list);
            // check if the calculation is not already in the arraylist if it is, generate a new one
            while (calcul_array.contains(bundle.getString("FIRST_NUMBER") + bundle.getString("OPERATOR") + bundle.getString("SECOND_NUMBER"))) {
                bundle = RandomizeCalc(2,100,operator_list);
            }
            calcul_array.add(bundle.getInt("FIRST_NUMBER") + " " + bundle.getString("OPERATOR") + " " + bundle.getInt("SECOND_NUMBER"));
        }
    }

    public int getCurrentQuestionNb() {
        return question_nb;
    }

    public String getCurrentQuestionString() {
        return calcul_array.get(question_nb - 1);
    }

    public ArrayList<String> GetCalculArray() {
        return calcul_array;
    }

    public int getTotalQuestionsNb() {
        return total_questions;
    }

    public Boolean IsCorrect(String answer) {
        if (answer.length() == 0 || answer.equals("-")) {
            throw new IllegalArgumentException("Answer cannot be empty");
        }
        return Integer.parseInt(answer) == getCurrentAnswer();
    }

    private int getCurrentAnswer() {
        return UtilsMethods.calculateFromString(calcul_array.get(question_nb - 1));

    }

    public boolean isLastQuestion() {
        return question_nb == total_questions;
    }


    public static int getRandomInt(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }


    @NonNull
    public static Bundle RandomizeCalc(int min, int max, @NonNull String operator_list) {
        Bundle bundle = new Bundle();
        String operator = operator_list.charAt((int) (Math.random() * operator_list.length())) + "";
        int randomSecondNumber;
        int randomFirstNumber;
        if (operator.equals("x")) {
            max = max / 10;
        }
        randomFirstNumber = getRandomInt(min, max);
        if (operator.equals("/")) {
            max = max / 10;
        }
        randomSecondNumber = getRandomInt(min, max);
        while (operator.equals("/") && randomFirstNumber % randomSecondNumber != 0) {
            randomFirstNumber = getRandomInt(min, max);
            randomSecondNumber = getRandomInt(min, max/10);
        }

        bundle.putString("OPERATOR", operator);
        bundle.putInt("FIRST_NUMBER", randomFirstNumber);
        bundle.putInt("SECOND_NUMBER", randomSecondNumber);
        return bundle;
    }

    public Bundle getNextBundle(){
        Bundle bundle = new Bundle();
        question_nb++;
        bundle.putSerializable("mathModel", this);
        return bundle;
    }

    public Bundle getBundle(){
        Bundle bundle = new Bundle();
        bundle.putSerializable("mathModel", this);
        return bundle;
    }

    public void setCurrentAnswer(String answer) {
        user_answers.add(Integer.valueOf(answer));
    }

    public ArrayList<Integer> GetAnswerArray() {
        return user_answers;
    }
}
