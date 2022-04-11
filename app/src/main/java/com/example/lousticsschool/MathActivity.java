package com.example.lousticsschool;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lousticsschool.db.AppDb;
import com.example.lousticsschool.db.User;

import java.util.ArrayList;

public class MathActivity extends AppCompatActivity {


    private TextView Calc;
    private EditText Answer;
    private Button Next;
    private Button Quit;
    private LinearLayout QALayout;
    private MathModel mathModel;

    private String operator_list;
    private User current_user;


    @Override
    public void onBackPressed() {
        // disable back button to prevent accidental exit or cheating
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        // get the current user
        current_user = (User) getIntent().getSerializableExtra("user");

        operator_list = getIntent().getStringExtra("oplist");

        if (getIntent().getExtras().get("init") != null) {
            mathModel = new MathModel(getIntent().getIntExtra("qnb", 10), operator_list);
        } else {
            mathModel = (MathModel) getIntent().getSerializableExtra("mathModel");
        }


        Calc = findViewById(R.id.tvQuestion);
        Answer = findViewById(R.id.Answer);
        QALayout = findViewById(R.id.QALayout);
        TextView questionNumber = findViewById(R.id.tvQuestionNumber);
        Next = findViewById(R.id.next);
        Quit = findViewById(R.id.Quit);

        // if we are on the last question, change the text of the next button an the color
        if (mathModel.getCurrentQuestionNb() == mathModel.getTotalQuestionsNb()) {
            Next.setText(R.string.finish);
        }


        Calc.setText(mathModel.getCurrentQuestionString() + " = ");

        String QuestionNumberString = "Question " + mathModel.getCurrentQuestionNb() + "/" + mathModel.getTotalQuestionsNb();
        questionNumber.setText(QuestionNumberString);

        Next.setOnClickListener(view -> CheckResult(Answer.getText().toString()));

        Answer.setOnEditorActionListener((textView, i, keyEvent) -> CheckResult(Answer.getText().toString()));
        // ask for confirmation in a dialog when the user clicks on the quit button
        Quit.setOnClickListener(view -> UtilsMethods.goToLoggedMenu(this, current_user, true));

        Answer.requestFocus();


    }

    private boolean CheckResult(String answer) {
        try {
            if (mathModel.IsCorrect(answer)) {
                Answer.setTextColor(Color.GREEN);
                Calc.setTextColor(Color.GREEN);
                // increment user correct answers
                current_user.setTotalQuestionsCorrect(current_user.getTotalQuestionsCorrect() + 1);

            } else {
                // set the text to red if the user has entered the wrong answer and display the correct answer
                Answer.setTextColor(Color.RED);
                Calc.setTextColor(Color.RED);
                QALayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            }
            Next.setEnabled(false);
            Answer.setEnabled(false);
            Quit.setEnabled(false);
            mathModel.setCurrentAnswer(answer);
            // increment user total questions answered
            current_user.setTotalQuestionsAnswered(current_user.getTotalQuestionsAnswered() + 1);
            updateUser();
            // wait for 1 second
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                // if the user has answered all the questions fininsh the activity and clear the activity stack
                if (mathModel.isLastQuestion()) {
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    // concatenate question array with answer array in an other array and send it to the result activity
                    ArrayList<String> result = new ArrayList<>();
                    for (int i = 0; i < mathModel.getTotalQuestionsNb(); i++) {
                        result.add(mathModel.GetCalculArray().get(i) + " = " + mathModel.GetAnswerArray().get(i));
                    }
                    intent.putExtra("RESULT_ARRAY", result);
                    intent.putExtra("EXERCICE_TYPE", "Math");
                    intent.putExtra("user", current_user);
                    intent.putExtra("oplist", operator_list);
                    intent.putExtra("qnb", getIntent().getIntExtra("qnb", 10));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // if the user has not answered all the questions, go to the next question
                    Intent intent = new Intent(getApplicationContext(), MathActivity.class);
                    intent.putExtras(mathModel.getNextBundle());
                    intent.putExtra("user", current_user);
                    intent.putExtra("oplist", operator_list);
                    intent.putExtra("qnb", getIntent().getIntExtra("qnb", 10));
                    // add a slide animation when starting the next activity
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.no_transition);
                    Next.setEnabled(true);
                    Quit.setEnabled(true);
                    finish();
                }

            }, 1000);
            return true;
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Veuillez entrer une réponse", Toast.LENGTH_SHORT).show();
            return false;
        } catch (Exception e) {
            Toast.makeText(this, "Une erreur est survenue", Toast.LENGTH_SHORT).show();
            throw e;
        }
    }

    // Async class update user int the database
    private void updateUser() {

        class UpdateUser extends AsyncTask<Void, Void, Void> {


            @Override
            protected Void doInBackground(Void... voids) {
                AppDb.getInstance(getApplicationContext()).userDao().update(current_user);
                return null;
            }
        }

        UpdateUser uu = new UpdateUser();
        uu.execute();
    }


}