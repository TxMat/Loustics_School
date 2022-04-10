package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.lousticsschool.db.AppDb;
import com.example.lousticsschool.db.User;

import java.util.ArrayList;

public class MathPickerActivity extends AppCompatActivity {

    private AppDb db;
    private User current_user;

    private Button AdditionButton;
    private Button SubtractButton;
    private Button MultiplyButton;
    private Button DivideButton;
    private Button RandomButton;
    private Button AdvancedButton;

    private Button LogoutButton;


    @Override
    public void onBackPressed() {
        UtilsMethods.goToLoggedMenu(this, current_user, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_picker);

        db = AppDb.getInstance(this);
        current_user = (User) getIntent().getSerializableExtra("user");

        AdditionButton = findViewById(R.id.AdditionsButton);
        SubtractButton = findViewById(R.id.SoustractionsButton);
        MultiplyButton = findViewById(R.id.MultiplicationsButton);
        DivideButton = findViewById(R.id.DivisionsButton);
        RandomButton = findViewById(R.id.RandomButton);
        AdvancedButton = findViewById(R.id.AdvancedOptionsButton);
        LogoutButton = findViewById(R.id.LogoutButton);

        AdditionButton.setOnClickListener(v -> {
            StartExercise("+", 10);
        });

        SubtractButton.setOnClickListener(v -> {
            StartExercise("-", 10);
        });

        MultiplyButton.setOnClickListener(v -> {
            StartExercise("x", 10);
        });

        DivideButton.setOnClickListener(v -> {
            StartExercise("/", 10);
        });

        RandomButton.setOnClickListener(v -> {
            StartExercise("+-x/", 10);
        });

        AdvancedButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdvancedMathActivityLauncher.class);
            intent.putExtra("user", current_user);
            startActivity(intent);
        });

        LogoutButton.setOnClickListener(v -> UtilsMethods.Logout(this));


    }

    public void StartExercise(String operator, int qnb) {
        Intent intent = new Intent(this, MathActivity.class);
        intent.putExtra("oplist", operator);
        intent.putExtra("user", current_user);
        intent.putExtra("qnb", qnb);
        intent.putExtra("init", "true");
        startActivity(intent);
        finish();
    }

}