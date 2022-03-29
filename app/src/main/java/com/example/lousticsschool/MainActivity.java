package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MathActivity.class);
                intent.putExtra(MathActivity.FIRST_NUMBER, "10");
                intent.putExtra(MathActivity.OPERATOR, "+");
                intent.putExtra(MathActivity.SECOND_NUMBER, "20");
                startActivity(intent);
            }
        });
    }
}