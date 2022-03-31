package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MathActivity extends AppCompatActivity {


    public static final String FIRST_NUMBER = "N1";
    public static final String OPERATOR = "DEFAULT_OPERATOR";
    public static final String SECOND_NUMBER = "N2";
    public static final String QUESTION_NUMBER = "QUESTION_NUMBER";
    public static final String TOTAL_QUESTIONS = "TOTAL_QUESTIONS";
    public static final String STATUS_HASHMAP = "STATUS_HASHMAP";
    public static int RESULT = -1;
    private TextView Calc;
    private EditText Answer;
    private Button Next;
    private Button Previous;
    private Button Quit;
    private LinearLayout QALayout;
    private TextView QuestionNumber;
    private int question_nb;
    private HashMap<String, Boolean> status_hashmap;
    private int total_questions;



    @Override
    public void onBackPressed() {
        if (question_nb != 1) {
            super.onBackPressed();
            overridePendingTransition(R.anim.no_transition, R.anim.slide_out_right);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        Calc = findViewById(R.id.MathTextView);
        Answer = findViewById(R.id.Answer);
        Previous = findViewById(R.id.Previous);
        QALayout = findViewById(R.id.QALayout);
        QuestionNumber = findViewById(R.id.QuestionNumber);
        Next = findViewById(R.id.next);
        Previous = findViewById(R.id.Previous);
        Quit = findViewById(R.id.Quit);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            int firstNumber = Integer.parseInt(bundle.getString(FIRST_NUMBER));
            int secondNumber = Integer.parseInt(bundle.getString(SECOND_NUMBER));
            question_nb = Integer.parseInt(bundle.getString(QUESTION_NUMBER));
            total_questions = Integer.parseInt(bundle.getString(TOTAL_QUESTIONS));
            status_hashmap = (HashMap<String, Boolean>) bundle.getSerializable(STATUS_HASHMAP);
            String operator = bundle.getString(OPERATOR);
            switch (operator) {
                case "+":
                    RESULT = firstNumber + secondNumber;
                    break;
                case "-":
                    RESULT = firstNumber - secondNumber;
                    break;
                case "x":
                    RESULT = firstNumber * secondNumber;
                    break;
                case "/":
                    RESULT = firstNumber / secondNumber;
                    break;
            }
            Calc.setText(firstNumber + " " + operator + " " + secondNumber + " = ");
            QuestionNumber.setText("Question " + question_nb + "/" + bundle.getString(TOTAL_QUESTIONS));


            Next.setOnClickListener(view -> {
                if (Answer.getText().toString().trim().length() != 0)
                CheckResult();
            });

            Previous.setOnClickListener(view -> onBackPressed());

            Answer.setOnEditorActionListener((textView, i, keyEvent) -> {
                if (i == EditorInfo.IME_ACTION_DONE && Answer.getText().toString().trim().length() != 0) {
                    CheckResult();
                    return true;
                }
                return false;
            });
            // ask for confirmation in a dialog when the user clicks on the quit button
            Quit.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(MathActivity.this);
                builder.setTitle("Abandonner");
                builder.setMessage("Voulez-vous vraiment abandonner ?\nVous perdrez toutes vos réponses.");
                builder.setPositiveButton("Oui, je veux quitter l'exercice", (dialog, which) -> {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                );
                builder.setNegativeButton("Non, je veux continuer", (dialog, which) -> {
                    dialog.dismiss();
                });
                builder.show();
            });
        }
    }

    public void CheckResult() {
        // set the text to green if the user has entered the correct answer
        Answer.setEnabled(false);
        Next.setEnabled(false);
        Previous.setEnabled(false);
        Quit.setEnabled(false);
        boolean isCorrect = Integer.parseInt(Answer.getText().toString()) == RESULT;
        if (isCorrect) {
            Answer.setTextColor(Color.GREEN);
            Calc.setTextColor(Color.GREEN);
        } else {
            // set the text to red if the user has entered the wrong answer and display the correct answer
            Answer.setTextColor(Color.RED);
            Calc.setTextColor(Color.RED);
            QALayout.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
        }
        status_hashmap.put(Calc.getText().toString(), isCorrect);
        // wait for 1 seconds
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Next.setEnabled(true);
            Previous.setEnabled(true);
            Intent intent = new Intent(getApplicationContext(), MathActivity.class);
            intent.putExtras(getNextBundle());
            // add a slide animation when starting the next activity
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.no_transition);

        }, 1000);
    }


    public static int getRandomInt(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }

    public ArrayList<String> RandomizeCalc(int min, int max){
        ArrayList<String> result = RandomizeCalcNoCheck(min, max);
        // check that the result is not in the hashmap already if not randomize again
        while (status_hashmap.containsKey(result.get(0) + result.get(1) + result.get(2))) {
            result = RandomizeCalcNoCheck(min, max);
        }
        return result;
    }

    public static ArrayList<String> RandomizeCalcNoCheck(int min, int max){
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
        ArrayList<String> result = new ArrayList<>();
        result.add(randomFirstNumber + "");
        result.add(operator);
        result.add(randomSecondNumber + "");
        return result;
    }

    // a method that randomises the OPERATOR, FIRST_NUMBER and SECOND_NUMBER
    public static Bundle getRandomBundle(int min, int max) {

        ArrayList<String> result = RandomizeCalcNoCheck(min, max);
        Bundle bundle = new Bundle();
        bundle.putString("FIRST_NUMBER", result.get(0));
        bundle.putString("OPERATOR", result.get(1));
        bundle.putString("SECOND_NUMBER", result.get(2));
        return bundle;
    }

    public static Bundle getFirstBundle(int questionNumber) {
        Bundle bundle = new Bundle();
        bundle.putAll(getRandomBundle(2,100)); // operations with 0 and 1 are too easy
        bundle.putString(QUESTION_NUMBER, "1");
        bundle.putString(TOTAL_QUESTIONS, String.valueOf(questionNumber));
        HashMap<String, Boolean> status_hashmap = new HashMap<>();
        bundle.putSerializable(STATUS_HASHMAP, status_hashmap);

        return bundle;
    }

    public Bundle getNextBundle(){
        Bundle bundle = new Bundle();
        bundle.putAll(getRandomBundle(2,100)); // operations with 0 and 1 are too easy
        bundle.putString(QUESTION_NUMBER, String.valueOf(question_nb + 1));
        bundle.putString(TOTAL_QUESTIONS, String.valueOf(total_questions));
        bundle.putSerializable(STATUS_HASHMAP, status_hashmap);
        return bundle;
    }

/*    public Bundle getPreviousBundle(){
        Bundle bundle = new Bundle();
        bundle.putAll(getRandomBundle(2,100)); // operations with 0 and 1 are too easy
        bundle.putString(QUESTION_NUMBER, String.valueOf(question_nb - 1));
        bundle.putString(TOTAL_QUESTIONS, String.valueOf(total_questions));
        bundle.putSerializable(STATUS_HASHMAP, status_hashmap);
        return bundle;
    }*/



}