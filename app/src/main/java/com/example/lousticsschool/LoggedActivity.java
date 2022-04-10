package com.example.lousticsschool;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.lousticsschool.db.AppDb;
import com.example.lousticsschool.db.User;

import java.time.LocalDate;
import java.time.LocalTime;

public class LoggedActivity extends UtilsMethods {

    private AppDb db;

    private long id;

    private User current_user;

    // date and time
    private LocalDate date;
    private LocalTime time;

    private TextView Greetings;
    private TextView DateTime;
    private TextView QANumber;
    private TextView PercentageCorrect;



    private Button Math;
    private Button CG;
    private Button Logout;

    @Override
    public void onBackPressed(){
        UtilsMethods.Logout(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        db = AppDb.getInstance(this);

        current_user = (User) getIntent().getSerializableExtra("user");
        // print user attributes for debugging

        date = LocalDate.now();
        time = LocalTime.now();

        Greetings = findViewById(R.id.Greetings);
        DateTime = findViewById(R.id.DateTime);
        QANumber = findViewById(R.id.QANumber);
        PercentageCorrect = findViewById(R.id.PercentageCorrect);

        Greetings.setText("Bonjour " + current_user.getName());
        // with the total number of questions and the number of correct answers calculate the percentage of correct answers
        String percentage;
        if (current_user.getTotalQuestionsAnswered() != 0) {
            percentage = String.valueOf(current_user.getTotalQuestionsCorrect() * 100 / current_user.getTotalQuestionsAnswered());
        } else {
            percentage = "--";
        }


        DateTime.setText("Nous sommes le " + date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + date.getYear()
                + ", il est " + time.getHour() + "h " + time.getMinute());

        QANumber.setText("Nombre de questions répondues : " + current_user.getTotalQuestionsAnswered());
        PercentageCorrect.setText("Pourcentage de réponses correctes : " + percentage + "%");

        Math = findViewById(R.id.mathsButton);
        CG = findViewById(R.id.CultureGButton);
        Logout = findViewById(R.id.LogoutButton);

        Logout.setOnClickListener(v -> UtilsMethods.Logout(this));

        Math.setOnClickListener(v -> {
            Intent intent = new Intent(this, MathPickerActivity.class);
            intent.putExtra("user", current_user);
            startActivity(intent);
        });

        CG.setOnClickListener(v -> { UtilsMethods.startCGActivity(this, current_user); });

    }


}