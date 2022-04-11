package com.example.lousticsschool;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lousticsschool.db.AppDb;
import com.example.lousticsschool.db.User;

public class MathPickerActivity extends AppCompatActivity {

    private User current_user;


    @Override
    public void onBackPressed() {
        UtilsMethods.goToLoggedMenu(this, current_user, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_picker);

        AppDb db = AppDb.getInstance(this);
        current_user = (User) getIntent().getSerializableExtra("user");

        Button additionButton = findViewById(R.id.AdditionsButton);
        Button subtractButton = findViewById(R.id.SoustractionsButton);
        Button multiplyButton = findViewById(R.id.MultiplicationsButton);
        Button divideButton = findViewById(R.id.DivisionsButton);
        Button randomButton = findViewById(R.id.RandomButton);
        Button advancedButton = findViewById(R.id.AdvancedOptionsButton);
        Button logoutButton = findViewById(R.id.LogoutButton);

        additionButton.setOnClickListener(v -> StartExercise("+", 10));

        subtractButton.setOnClickListener(v -> StartExercise("-", 10));

        multiplyButton.setOnClickListener(v -> StartExercise("x", 10));

        divideButton.setOnClickListener(v -> StartExercise("/", 10));

        randomButton.setOnClickListener(v -> StartExercise("+-x/", 10));

        advancedButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdvancedMathActivityLauncher.class);
            intent.putExtra("user", current_user);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> UtilsMethods.Logout(this));


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