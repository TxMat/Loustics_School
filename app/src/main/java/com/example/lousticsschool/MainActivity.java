package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btn1);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MathActivity.class);
            intent.putExtras(MathActivity.getFirstBundle(10));
            startActivity(intent);
        });
    }
}