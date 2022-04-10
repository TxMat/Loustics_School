package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.AnimationUtils;
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
    private Button Quit;
    private boolean isTheFirstAwnser = true;
    private boolean isReady = false;


    private QuizModel quizModel;

    private ArrayList<Quiz> questionList;

    private User current_user;


    @Override
    public void onBackPressed() {
        // disable back button to prevent accidental exit or cheating
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_culture_generale);


        Question = findViewById(R.id.tvQuestion);
        Button1 = findViewById(R.id.A1Button);
        Button2 = findViewById(R.id.A2Button);
        Button3 = findViewById(R.id.A3Button);
        Button4 = findViewById(R.id.A4Button);
        Quit = findViewById(R.id.Quit);
        QuestionNumber = findViewById(R.id.tvQuestionNumber);

        current_user = (User) getIntent().getSerializableExtra("user");

        if (getIntent().getExtras().get("init") != null) {
            getQuestions();
        } else {
            quizModel = (QuizModel) getIntent().getSerializableExtra("quizModel");
            initialize();
        }





    }

    private void initialize(){
        String QuestionNumberString = "Question " + quizModel.getQuestionNb() + "/" + quizModel.getTotalQuestionsNb();
        QuestionNumber.setText(QuestionNumberString);

        Quit.setOnClickListener(view -> { UtilsMethods.goToLoggedMenu(this, current_user, true); });

        Question.setText(quizModel.getCurrentQuestionObject().getQuestion());

        // randomize answers but ensure that we dont have the same answer twice
        String[] answers = quizModel.getCurrentQuestionObject().getAnswers();
        int[] randomIndexes = UtilsMethods.randomizeArray(answers.length);
        Button1.setText(answers[randomIndexes[0]]);
        Button2.setText(answers[randomIndexes[1]]);
        Button3.setText(answers[randomIndexes[2]]);
        Button4.setText(answers[randomIndexes[3]]);

        Button1.setOnClickListener(view -> {
            CheckResult(Button1);
        });

        Button2.setOnClickListener(view -> {
            CheckResult(Button2);
        });

        Button3.setOnClickListener(view -> {
            CheckResult(Button3);
        });

        Button4.setOnClickListener(view -> {
            CheckResult(Button4);
        });
    }

    private void CheckResult(Button btn) {
        // if the button clicked is the correct answer change the text color to green
        // else change the text color to red and disable the button
        Button[] buttons = {Button1, Button2, Button3, Button4};
        String btntxt = btn.getText().toString();
        // disable all buttons
        for (Button button : buttons) {
            button.setEnabled(false);
        }
        Quit.setEnabled(false);

        if (quizModel.isCorrect(btntxt)) {
            btn.setTextColor(Color.GREEN);
            // if isTheFirstAwnser add 1 to the total correct answers of the user
            if (isTheFirstAwnser) {
                current_user.setTotalQuestionsCorrect(current_user.getTotalQuestionsCorrect() + 1);
            }
            isReady = true;
        } else {
            btn.setEnabled(false);
            btn.setTextColor(Color.RED);
            btn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        }
        // wait 1 second
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (isTheFirstAwnser) {
                // add this answer to the list of answers
                quizModel.addUserAnswer(btntxt);
                isTheFirstAwnser = false;
            }
            if (isReady) {
                // check if we are on the last question
                current_user.setTotalQuestionsAnswered(current_user.getTotalQuestionsAnswered() + 1);
                if (quizModel.getQuestionNb() == quizModel.getTotalQuestionsNb()) {
                    // if we are on the last question, go to the result activity
                    Intent intent = new Intent(this, ResultActivity.class);
                    intent.putExtra("RESULT_ARRAY", quizModel.getUserAnswers());
                    intent.putExtra("EXERCICE_TYPE", "Quiz");
                    intent.putExtra("IS_CORRECT_ARRAY", quizModel.getAnswer_boolean());
                    intent.putExtra("ID_LIST", quizModel.getIdList());
                    intent.putExtra("user", current_user);
                    startActivity(intent);
                    finish();
                } else {
                    // if we are not on the last question, go to the next question
                    Intent intent = new Intent(this, CultureGeneraleActivity.class);
                    intent.putExtras(quizModel.getNextBundle());
                    intent.putExtra("user", current_user);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.no_transition);
                    finish();
                }
            } else {
                // if we are not ready, enable all buttons
                for (Button button : buttons) {
                    button.setEnabled(true);
                }
                Quit.setEnabled(true);
            }
        }, 1000);

    }


    @Override
    protected void onStart() {
        super.onStart();
        //getQuestions();
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
                int questionnb = getIntent().getIntExtra("numberOfQuestions", 10);
                quizModel = new QuizModel(questionnb, questionList);
                initialize();
            }
        }

        //////////////////////////
        // IMPORTANT bien penser à executer la demande asynchrone
        // Création d'un objet de type GetTasks et execution de la demande asynchrone
        GetQuestions gu = new GetQuestions();
        gu.execute();
    }
}