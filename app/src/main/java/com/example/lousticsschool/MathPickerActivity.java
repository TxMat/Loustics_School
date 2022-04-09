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
            // show a toast
            Toast.makeText(this, "Advanced options coming soon!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ResultActivity.class);
            ArrayList<String> result = new ArrayList<>();
            // filling the array for testing
            for (int i = 0; i < 20; i++) {
                result.add("10 + 65 = " + i);
            }
            intent.putExtra("RESULT_ARRAY", result);
            intent.putExtra("EXERCICE_TYPE", "Math");
            startActivity(intent);
        });

        LogoutButton.setOnClickListener(v -> UtilsMethods.Logout(this));


    }

    private void StartExercise(String operator) {
        Intent intent = new Intent(this, MathActivity.class);
        intent.putExtra("operator_list", operator);
        intent.putExtra("user", current_user);
        intent.putExtra("init", "true");
        startActivity(intent);
        finish();
    }

}