package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.rxjava3.RxDataStore;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

public class MathActivity extends AppCompatActivity {


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
    private boolean is_answered;
    private RxDataStore<Preference> CalcDataStore;



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
        // init CalcDataStore
        //CalcDataStore =


        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            int firstNumber = bundle.getInt("FIRST_NUMBER");
            int secondNumber = bundle.getInt("SECOND_NUMBER");
            question_nb = bundle.getInt("QUESTION_NUMBER");
            total_questions = bundle.getInt("TOTAL_QUESTIONS");
            status_hashmap = (HashMap<String, Boolean>) bundle.getSerializable("STATUS_HASHMAP");
            String operator = bundle.getString("OPERATOR");
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
            QuestionNumber.setText("Question " + question_nb + "/" + bundle.getInt("TOTAL_QUESTIONS"));
            if (question_nb < 1) { // < instead of == for security reasons
                Previous.setEnabled(false);
            }


            Next.setOnClickListener(view -> CheckResult());

            Previous.setOnClickListener(view -> onBackPressed());

            Answer.setOnEditorActionListener((textView, i, keyEvent) -> CheckResult());
            // ask for confirmation in a dialog when the user clicks on the quit button
            Quit.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(MathActivity.this);
                builder.setTitle("Abandonner");
                builder.setMessage("Voulez-vous vraiment abandonner ?\nVous perdrez toutes vos réponses.");
                builder.setPositiveButton("Oui, je veux quitter l'exercice", (dialog, which) -> {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    //clear all the activity stack
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    public boolean CheckResult() {
        // set the text to green if the user has entered the correct answer
        if (Answer.getText().toString().trim().length() == 0 || Answer.getText().toString().equals("-")) {
            return false;
        }
        Answer.setEnabled(false);
        Next.setEnabled(false);
        Previous.setEnabled(false);
        Quit.setEnabled(false);
        if (!is_answered){
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
                // if the user has answered all the questions fininsh the activity and clear the activity stack
                if (question_nb == total_questions) {
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("STATUS_HASHMAP", status_hashmap);
                    intent.putExtra("TOTAL_QUESTIONS", total_questions);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // if the user has not answered all the questions, go to the next question
                    Next.setEnabled(true);
                    Previous.setEnabled(true);
                    is_answered = true;
                    Intent intent = new Intent(getApplicationContext(), MathActivity.class);
                    intent.putExtras(getNextBundle());
                    // add a slide animation when starting the next activity
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.no_transition);
                }

            }, 1000);
            return true;
        } else {
            Next.setEnabled(true);
            Previous.setEnabled(true);
            is_answered = true;
            // go to perviouly exited activity
            Intent intent = new Intent(getApplicationContext(), MathActivity.class);
            intent.putExtras(getNextBundle());


            // add a slide animation when starting the next activity
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.no_transition);
            return true;
        }
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

/*    public Bundle getPreviousBundle(){
        Bundle bundle = new Bundle();
        bundle.putAll(getRandomBundle(2,100)); // operations with 0 and 1 are too easy
        bundle.putString(QUESTION_NUMBER, String.valueOf(question_nb - 1));
        bundle.putString(TOTAL_QUESTIONS, String.valueOf(total_questions));
        bundle.putSerializable(STATUS_HASHMAP, status_hashmap);
        return bundle;
    }*/



}