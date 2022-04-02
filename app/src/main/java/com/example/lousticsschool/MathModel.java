package com.example.lousticsschool;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

import java.util.HashMap;

public class MathModel {
    private int question_nb;
    private HashMap<String, Boolean> status_hashmap;
    private int total_questions;
    private boolean is_answered;
    private int result;
    private String CalcString;

    public MathModel(Bundle bundle) {
        if (bundle != null) {
            int firstNumber = bundle.getInt("FIRST_NUMBER");
            int secondNumber = bundle.getInt("SECOND_NUMBER");
            question_nb = bundle.getInt("QUESTION_NUMBER");
            total_questions = bundle.getInt("TOTAL_QUESTIONS");
            status_hashmap = (HashMap<String, Boolean>) bundle.getSerializable("STATUS_HASHMAP");
            String operator = bundle.getString("OPERATOR");
            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "x":
                    result = firstNumber * secondNumber;
                    break;
                case "/":
                    result = firstNumber / secondNumber;
                    break;
            }
            CalcString = firstNumber + " " + operator + " " + secondNumber + " = ";
        }
    }

    public int getQuestion_nb() {
        return question_nb;
    }

    public String getCalcString() {
        return CalcString;
    }



    public void setQuestion_nb(int question_nb) {
        this.question_nb = question_nb;
    }

    public HashMap<String, Boolean> getStatus_hashmap() {
        return status_hashmap;
    }

    public void setStatus_hashmap(HashMap<String, Boolean> status_hashmap) {
        this.status_hashmap = status_hashmap;
    }

    public int getTotal_questions() {
        return total_questions;
    }

    public void setTotal_questions(int total_questions) {
        this.total_questions = total_questions;
    }

    public boolean isAnswered() {
        return is_answered;
    }

    public void setIs_answered(boolean is_answered) {
        this.is_answered = is_answered;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Boolean IsCorrect(String answer) {
        // set the text to green if the user has entered the correct answer
        if (answer.length() == 0 || answer.equals("-")) {
            throw new IllegalArgumentException("Answer cannot be empty");
        }
        boolean isCorrect = Integer.parseInt(answer) == result;
        status_hashmap.put(answer, isCorrect);
        return isCorrect;
    }

    public boolean isLastQuestion() {
        return question_nb == total_questions;
    }




    public static int getRandomInt(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }


    public static Bundle RandomizeCalcNoCheck(int min, int max){
        Bundle bundle = new Bundle();
        String operator = "+-x/".charAt((int) (Math.random() * 4)) + "";
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

    public Bundle RandomizeCalc(int min, int max){
        // Get the bundle from RandomizeCalcNoCheck and check if the result is not already in the hashmap (avoid duplicates)
        // if so get another random bundle
        Bundle bundle = RandomizeCalcNoCheck(min, max);
        String result = bundle.getInt("FIRST_NUMBER") + " " + bundle.getString("OPERATOR") + " " + bundle.getInt("SECOND_NUMBER");
        while (status_hashmap.containsKey(result)) {
            bundle = RandomizeCalcNoCheck(min, max);
            result = bundle.getInt("FIRST_NUMBER") + " " + bundle.getString("OPERATOR") + " " + bundle.getInt("SECOND_NUMBER");
        }
        return bundle;
    }

    public static Bundle getFirstBundle(int questionNumber) {
        Bundle bundle = new Bundle();
        bundle.putAll(RandomizeCalcNoCheck(2,100)); // operations with 0 and 1 are too easy
        bundle.putInt("QUESTION_NUMBER", 1);
        bundle.putInt("TOTAL_QUESTIONS", questionNumber);
        HashMap<String, Boolean> status_hashmap = new HashMap<>();
        bundle.putSerializable("STATUS_HASHMAP", status_hashmap);
        return bundle;
    }

    public Bundle getNextBundle(){
        Bundle bundle = new Bundle();
        bundle.putAll(RandomizeCalc(2,100)); // operations with 0 and 1 are too easy
        bundle.putInt("QUESTION_NUMBER", question_nb + 1);
        bundle.putInt("TOTAL_QUESTIONS", total_questions);
        bundle.putSerializable("STATUS_HASHMAP", status_hashmap);
        return bundle;
    }

    public Bundle getNextBundle(String next_calc){
        Bundle bundle = new Bundle();
        // extract the operator and the numbers from the string
        String[] numbers = next_calc.split(" ");
        bundle.putInt("FIRST_NUMBER", Integer.parseInt(numbers[0]));
        bundle.putInt("SECOND_NUMBER", Integer.parseInt(numbers[2]));
        bundle.putString("OPERATOR", numbers[1]);
        bundle.putInt("QUESTION_NUMBER", question_nb + 1);
        bundle.putInt("TOTAL_QUESTIONS", total_questions);
        bundle.putSerializable("STATUS_HASHMAP", status_hashmap);
        return bundle;
    }







}
