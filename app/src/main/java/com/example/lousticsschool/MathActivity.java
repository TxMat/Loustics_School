package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MathActivity extends AppCompatActivity {


    public static final String FIRST_NUMBER = "N1";
    public static final String OPERATOR = "DEFAULT_OPERATOR";
    public static final String SECOND_NUMBER = "N2";
    public static int RESULT = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        TextView Calc = findViewById(R.id.MathTextView);
        EditText Anwser = findViewById(R.id.Answer);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int firstNumber = Integer.parseInt(bundle.getString(FIRST_NUMBER));
            int secondNumber = Integer.parseInt(bundle.getString(SECOND_NUMBER));
            String operator = bundle.getString(OPERATOR);
            System.out.println(firstNumber);
            System.out.println(FIRST_NUMBER);
            System.out.println(secondNumber);
            System.out.println(operator);
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

            Button next = findViewById(R.id.next);
            next.setOnClickListener(view -> {
                if (Anwser.getText().toString().trim().length() != 0)
                finish();
            });

            Anwser.setOnEditorActionListener((textView, i, keyEvent) -> {
                if (i == EditorInfo.IME_ACTION_DONE && Anwser.getText().toString().trim().length() != 0) {
                    finish();
                    return true;
                }
                return false;
            });
        }
    }
}

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
        Bundle extras = getIntent().getExtras();
        String operator = extras.getString(OPERATOR);
        int firstNumber = extras.getInt(FIRST_NUMBER);
        int secondNumber = extras.getInt(SECOND_NUMBER);
        String calc = getIntent().getStringExtra(firstNumber + " " + operator + " " + secondNumber + " = ");
        TextView calcView = findViewById(R.id.MathTextView);
        calcView.setText(calc);
    }
*/