package com.example.lousticsschool;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.lousticsschool.db.AppDb;
import com.example.lousticsschool.db.User;

import java.time.LocalDate;
import java.time.LocalTime;

public class LoggedActivity extends UtilsMethods {

    private User current_user;


    @Override
    public void onBackPressed(){
        UtilsMethods.Logout(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged);

        AppDb db = AppDb.getInstance(this);

        current_user = (User) getIntent().getSerializableExtra("user");
        // print user attributes for debugging

        // date and time
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        TextView greetings = findViewById(R.id.Greetings);
        TextView dateTime = findViewById(R.id.DateTime);
        TextView QANumber = findViewById(R.id.QANumber);
        TextView percentageCorrect = findViewById(R.id.PercentageCorrect);

        greetings.setText("Bonjour " + current_user.getName());
        // with the total number of questions and the number of correct answers calculate the percentage of correct answers
        String percentage;
        if (current_user.getTotalQuestionsAnswered() != 0) {
            percentage = String.valueOf(current_user.getTotalQuestionsCorrect() * 100 / current_user.getTotalQuestionsAnswered());
        } else {
            percentage = "--";
        }


        dateTime.setText("Nous sommes le " + date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + date.getYear()
                + ", il est " + time.getHour() + "h " + time.getMinute());

        QANumber.setText("Nombre de questions répondues : " + current_user.getTotalQuestionsAnswered());
        percentageCorrect.setText("Pourcentage de réponses correctes : " + percentage + "%");

        Button math = findViewById(R.id.mathsButton);
        Button CG = findViewById(R.id.CultureGButton);
        Button logout = findViewById(R.id.LogoutButton);

        logout.setOnClickListener(v -> UtilsMethods.Logout(this));

        math.setOnClickListener(v -> {
            Intent intent = new Intent(this, MathPickerActivity.class);
            intent.putExtra("user", current_user);
            startActivity(intent);
        });

        CG.setOnClickListener(v -> UtilsMethods.startCGActivity(this, current_user));

    }


}