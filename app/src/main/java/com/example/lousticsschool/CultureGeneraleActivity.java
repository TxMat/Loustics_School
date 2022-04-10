package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.lousticsschool.db.AppDb;
import com.example.lousticsschool.db.Quiz;
import com.example.lousticsschool.db.User;

import java.util.ArrayList;
import java.util.List;

public class CultureGeneraleActivity extends AppCompatActivity {

    private TextView Question;
    private Button Button1;
    private Button Button2;
    private Button Button3;
    private Button Button4;
    private TextView QuestionNumber;
    private QuizModel quizModel;

    private ArrayList<Quiz> questionList;

    private User current_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culture_generale);

        Question = findViewById(R.id.tvQuestion);
        Button1 = findViewById(R.id.A1Button);
        Button2 = findViewById(R.id.A2Button);
        Button3 = findViewById(R.id.A3Button);
        Button4 = findViewById(R.id.A4Button);
        QuestionNumber = findViewById(R.id.tvQuestionNumber);

        current_user = (User) getIntent().getSerializableExtra("user");

        if (getIntent().getExtras().get("init") != null) {
            quizModel = new QuizModel(10, questionList);
        } else {
            quizModel = (QuizModel) getIntent().getSerializableExtra("quizModel");
        }



    }

    @Override
    protected void onStart() {
        super.onStart();
        getQuestions();
    }

    private void getQuestions() {
        ///////////////////////
        // Classe asynchrone permettant de récupérer des taches et de mettre à jour le listView de l'activité
        class GetQuestions extends AsyncTask<Void, Void, List<Quiz>> {

            @Override
            protected List<Quiz> doInBackground(Void... voids) {
                return AppDb.getInstance(getApplicationContext()).quizDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Quiz> qz) {
                super.onPostExecute(qz);
                questionList = (ArrayList<Quiz>) qz;
            }
        }

        //////////////////////////
        // IMPORTANT bien penser à executer la demande asynchrone
        // Création d'un objet de type GetTasks et execution de la demande asynchrone
        GetQuestions gu = new GetQuestions();
        gu.execute();
    }
}