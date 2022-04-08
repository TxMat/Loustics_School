package com.example.lousticsschool;

import android.os.Bundle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class MathModel implements Serializable {
    private int question_nb;
    private HashMap<Integer, String> calcul_hashmap;
    private HashMap<Integer, Integer> user_answers;
    private int total_questions;
    private boolean is_answered;
    private int result;
    private String CalcString;

    public MathModel(int total_questions, String operator_list) {
        // fill the hashmap with the operations with random operators
        this.total_questions = total_questions;
        this.question_nb = 1;
        this.user_answers = new HashMap<>();
        this.calcul_hashmap = new HashMap<>();
        for (int i = 0; i < total_questions; i++) {
            Bundle bundle = RandomizeCalc(2,100,operator_list);
            // check if the calculation is not already in the hashmap if it is, generate a new one
            while (calcul_hashmap.containsValue(bundle.getString("FIRST_NUMBER") + bundle.getString("OPERATOR") + bundle.getString("SECOND_NUMBER"))) {
                bundle = RandomizeCalc(2,100,operator_list);
            }
            calcul_hashmap.put(i, bundle.getInt("FIRST_NUMBER") + " " + bundle.getString("OPERATOR") + " " + bundle.getInt("SECOND_NUMBER"));
        }
    }

    public int getCurrentQuestionNb() {
        return question_nb;
    }

    public String getCurrentQuestionString() {
        return calcul_hashmap.get(question_nb - 1);
    }

    public void setQuestion_nb(int question_nb) {
        this.question_nb = question_nb;
    }

    public HashMap<Integer, String> getCalcul_hashmap() {
        return calcul_hashmap;
    }

    public void setCalcul_hashmap(HashMap<Integer, String> calcul_hashmap) {
        this.calcul_hashmap = calcul_hashmap;
    }

    public int getTotalQuestionsNb() {
        return total_questions;
    }

    public void setTotal_questions(int total_questions) {
        this.total_questions = total_questions;
    }

    public boolean isAnswered(int currentQuestionNb) {
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
        return Integer.parseInt(answer) == result;
    }

    public boolean isLastQuestion() {
        return question_nb == total_questions;
    }


    public static int getRandomInt(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }


    public static Bundle RandomizeCalc(int min, int max, String operator_list) {
        Bundle bundle = new Bundle();
        String operator = operator_list.charAt((int) (Math.random() * 4)) + "";
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

    public void test() {
        question_nb--;
        for (int i = 0; i < calcul_hashmap.size(); i++) {
            System.out.println(calcul_hashmap.get(i));
        }
    }

    public void setCurrentAnswer(String answer) {
        user_answers.put(question_nb, Integer.valueOf(answer));
    }

    public String getCurrentQuestionAnswer() {
        if (user_answers.containsKey(question_nb)) {
            return String.valueOf(user_answers.get(question_nb));
        }
        return null;
    }
}
