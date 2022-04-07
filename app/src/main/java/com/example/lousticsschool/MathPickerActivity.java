package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
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
            return;
        });

        SubtractButton.setOnClickListener(v -> {
            return;
        });

        MultiplyButton.setOnClickListener(v -> {
            return;
        });

        DivideButton.setOnClickListener(v -> {
            return;
        });

        RandomButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MathActivity.class);
            intent.putExtra("operator_list", "+-x/");
            intent.putExtra("init", "true");
            startActivity(intent);
        });

        AdvancedButton.setOnClickListener(v -> {
            return;
        });

        LogoutButton.setOnClickListener(v -> {



        });


    }
}