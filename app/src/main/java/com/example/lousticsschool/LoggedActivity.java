package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.lousticsschool.db.AppDb;

public class LoggedActivity extends AppCompatActivity {

    private AppDb db;

    private Button Math;
    private Button CG;

    private Button Logout;
    private Button Quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        db = AppDb.getInstance(this);

        Math = findViewById(R.id.mathsButton);
        CG = findViewById(R.id.CultureGButton);

        Logout = findViewById(R.id.LogoutButton);
        Quit = findViewById(R.id.QuitButton);

        Math.setOnClickListener(v -> {
            Intent intent = new Intent(LoggedActivity.this, MathPickerActivity.class);
            startActivity(intent);
        });


    }
}