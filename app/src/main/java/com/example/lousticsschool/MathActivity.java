package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MathActivity extends AppCompatActivity {


    private TextView Calc;
    private EditText Answer;
    private Button Next;
    private Button Previous;
    private Button Quit;
    private LinearLayout QALayout;
    private TextView QuestionNumber;
    private MathModel mathModel;

    private String operator_list;


    @Override
    public void onBackPressed() {
        // disable back button to prevent accidental exit or cheating
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        operator_list = getIntent().getStringExtra("operator_list");

        if (getIntent().getExtras().get("init") != null) {
            mathModel = new MathModel(10, operator_list);
        } else {
            mathModel = (MathModel) getIntent().getSerializableExtra("mathModel");
        }



        Calc = findViewById(R.id.MathTextView);
        Answer = findViewById(R.id.Answer);
        Previous = findViewById(R.id.Previous);
        QALayout = findViewById(R.id.QALayout);
        QuestionNumber = findViewById(R.id.QuestionNumber);
        Next = findViewById(R.id.next);
        Quit = findViewById(R.id.Quit);

        Previous.setEnabled(mathModel.getCurrentQuestionNb() != 1);

        // if we are on the last question, change the text of the next button an the color
        if (mathModel.getCurrentQuestionNb() == mathModel.getTotalQuestionsNb()) {
            Next.setText(R.string.finish);
            // set the background color of the next button to the text color of Previous
            Next.setBackgroundColor(Previous.getCurrentTextColor());
            Next.setTextColor(Color.BLACK);
        }


        Calc.setText(mathModel.getCurrentQuestionString() + " = ");

        String QuestionNumberString = "Question " + mathModel.getCurrentQuestionNb() + "/" + mathModel.getTotalQuestionsNb();
        QuestionNumber.setText(QuestionNumberString);

        Next.setOnClickListener(view -> CheckResult(Answer.getText().toString()));

        Previous.setOnClickListener(view -> onBackPressed());

        Answer.setOnEditorActionListener((textView, i, keyEvent) -> CheckResult(Answer.getText().toString()));
        // ask for confirmation in a dialog when the user clicks on the quit button
        Quit.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MathActivity.this);
            builder.setTitle("Abandonner");
            builder.setMessage("Voulez-vous vraiment abandonner ?\nVous perdrez toutes vos réponses.");
            builder.setPositiveButton("Oui, je veux quitter l'exercice", (dialog, which) -> {
                Intent intent = new Intent(getApplicationContext(), MathPickerActivity.class);
                //clear all the activity stack
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            );
            builder.setNegativeButton("Non, je veux continuer", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.show();
        });


    }

    private boolean CheckResult(String answer) {
        if (Answer.isEnabled()) {
            try {
                if (mathModel.IsCorrect(answer)) {
                    Answer.setTextColor(Color.GREEN);
                    Calc.setTextColor(Color.GREEN);
                } else {
                    // set the text to red if the user has entered the wrong answer and display the correct answer
                    Answer.setTextColor(Color.RED);
                    Calc.setTextColor(Color.RED);
                    QALayout.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                }
                Next.setEnabled(false);
                Previous.setEnabled(false);
                Answer.setEnabled(false);
                Quit.setEnabled(false);
                mathModel.setCurrentAnswer(answer);
                // wait for 1 second
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    // if the user has answered all the questions fininsh the activity and clear the activity stack
                    if (mathModel.isLastQuestion()) {
                        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                        intent.putExtra("CALCUL_ARRAY", mathModel.GetCalculArray());
                        intent.putExtra("ANSWER_ARRAY", mathModel.GetAnswerArray());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // if the user has not answered all the questions, go to the next question
                        Intent intent = new Intent(getApplicationContext(), MathActivity.class);
                        intent.putExtras(mathModel.getNextBundle());
                        // add a slide animation when starting the next activity
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.no_transition);
                        Next.setEnabled(true);
                        Previous.setEnabled(mathModel.getCurrentQuestionNb() != 1);
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
        } else {
            // go to perviouly exited activity
            Intent intent = new Intent(getApplicationContext(), MathActivity.class);
            intent.putExtras(mathModel.getBundle());

            // add a slide animation when starting the next activity
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.no_transition);
            return true;
        }
    }



}