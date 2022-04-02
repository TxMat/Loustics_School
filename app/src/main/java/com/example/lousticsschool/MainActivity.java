package com.example.lousticsschool;

import androidx.appcompat.app.AlertDialog;
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
            intent.putExtras(MathModel.getFirstBundle(10));
            startActivity(intent);
        });
    }

    //ask for a confirmation when the user wants to exit the app using the back button
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Lousics School")
                .setMessage("Veux-tu vraiment quitter l'application?")
                .setPositiveButton("Oui", (dialog, which) -> finish())
                .setNegativeButton("Non", null)
                .show();
    }
}