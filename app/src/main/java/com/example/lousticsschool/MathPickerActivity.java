package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.lousticsschool.db.AppDb;

public class MathPickerActivity extends AppCompatActivity {

    private AppDb db;

    private Button AdditionButton;
    private Button SubtractButton;
    private Button MultiplyButton;
    private Button DivideButton;
    private Button RandomButton;
    private Button AdvancedButton;

    private Button LogoutButton;
    private Button QuitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_picker);

        db = AppDb.getInstance(this);

        AdditionButton = findViewById(R.id.AdditionsButton);
        SubtractButton = findViewById(R.id.SoustractionsButton);
        MultiplyButton = findViewById(R.id.MultiplicationsButton);
        DivideButton = findViewById(R.id.DivisionsButton);
        RandomButton = findViewById(R.id.RandomButton);
        AdvancedButton = findViewById(R.id.AdvancedOptionsButton);
        LogoutButton = findViewById(R.id.LogoutButton);

        AdditionButton.setOnClickListener(v -> {
            StartExercise("+");
        });

        SubtractButton.setOnClickListener(v -> {
            StartExercise("-");
        });

        MultiplyButton.setOnClickListener(v -> {
            StartExercise("x");
        });

        DivideButton.setOnClickListener(v -> {
            StartExercise("/");
        });

        RandomButton.setOnClickListener(v -> {
            StartExercise("+-x/");
        });

        AdvancedButton.setOnClickListener(v -> {
            // next version
            return;
        });

        LogoutButton.setOnClickListener(v -> UtilsMethods.Logout(this));


    }

    private void StartExercise(String operator) {
        Intent intent = new Intent(this, MathActivity.class);
        intent.putExtra("operator_list", operator);
        intent.putExtra("init", "true");
        startActivity(intent);
    }

}