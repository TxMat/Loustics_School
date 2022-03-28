package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MathActivity extends AppCompatActivity {

    public static final String CALCUL = "NAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);
        String calc = getIntent().getStringExtra(CALCUL);
        TextView calcView = findViewById(R.id.MathTextView);
        calcView.setText(calc);
    }


}