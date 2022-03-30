package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

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

public class MathActivity extends AppCompatActivity {


    public static final String FIRST_NUMBER = "N1";
    public static final String OPERATOR = "DEFAULT_OPERATOR";
    public static final String SECOND_NUMBER = "N2";
    public static final String QUESTION_NUMBER = "QUESTION_NUMBER";
    public static final String TOTAL_QUESTIONS = "TOTAL_QUESTIONS";
    public static int RESULT = -1;
    private TextView Calc;
    private EditText Answer;
    private Button Next;
    private Button Previous;
    private LinearLayout QALayout;
    private TextView QuestionNumber;
    private int question_nb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        Calc = findViewById(R.id.MathTextView);
        Answer = findViewById(R.id.Answer);
        Previous = findViewById(R.id.Previous);
        QALayout = findViewById(R.id.QALayout);
        QuestionNumber = findViewById(R.id.QuestionNumber);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            int firstNumber = Integer.parseInt(bundle.getString(FIRST_NUMBER));
            int secondNumber = Integer.parseInt(bundle.getString(SECOND_NUMBER));
            question_nb = Integer.parseInt(bundle.getString(QUESTION_NUMBER));
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

            Next = findViewById(R.id.next);
            Next.setOnClickListener(view -> {
                if (Answer.getText().toString().trim().length() != 0)
                CheckResult();
            });

            Answer.setOnEditorActionListener((textView, i, keyEvent) -> {
                if (i == EditorInfo.IME_ACTION_DONE && Answer.getText().toString().trim().length() != 0) {
                    CheckResult();
                    return true;
                }
                return false;
            });
        }
    }

    public void CheckResult() {
        // set the text to green if the user has entered the correct answer
        Answer.setEnabled(false);
        Next.setEnabled(false);
        if (Integer.parseInt(Answer.getText().toString()) == RESULT) {
            Answer.setTextColor(Color.GREEN);
            Calc.setTextColor(Color.GREEN);
        } else {
            // set the text to red if the user has entered the wrong answer and display the correct answer
            Answer.setTextColor(Color.RED);
            Calc.setTextColor(Color.RED);
            QALayout.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
        }
        // wait for 1 seconds
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(getApplicationContext(), MathActivity.class);
            intent.putExtras(getNextBundle());
            startActivity(intent);
        }, 1000);
    }


    public static int getRandomInt(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }

    // a method that randomises the OPERATOR, FIRST_NUMBER and SECOND_NUMBER
    public static Bundle getRandomBundle(int min, int max) {
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


        Bundle bundle = new Bundle();
        bundle.putString(FIRST_NUMBER, String.valueOf(randomFirstNumber));
        bundle.putString(OPERATOR, operator);
        bundle.putString(SECOND_NUMBER, String.valueOf(randomSecondNumber));
        return bundle;
    }

    public static Bundle getFirstBundle(int questionNumber) {
        Bundle bundle = new Bundle();
        bundle.putAll(getRandomBundle(2,100)); // operations with 0 and 1 are too easy
        bundle.putString(QUESTION_NUMBER, "1");
        bundle.putString(TOTAL_QUESTIONS, String.valueOf(questionNumber));
        return bundle;
    }

    public Bundle getNextBundle(){
        Bundle bundle = new Bundle();
        bundle.putAll(getRandomBundle(2,100)); // operations with 0 and 1 are too easy
        bundle.putString(QUESTION_NUMBER, String.valueOf(question_nb + 1));
        return bundle;
    }



}