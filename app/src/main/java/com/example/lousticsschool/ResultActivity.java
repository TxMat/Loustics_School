package com.example.lousticsschool;

import static com.example.lousticsschool.UtilsMethods.calculateFromString;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lousticsschool.db.User;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ResultRecyclerViewAdapter adapter;
    private ArrayList<Boolean> isCorrectArray = new ArrayList<>();
    private User current_user;

    @Override
    public void onBackPressed() {
        UtilsMethods.goToLoggedMenu(this, current_user, false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        String exerciceType = getIntent().getStringExtra("EXERCICE_TYPE");
        current_user = (User) getIntent().getSerializableExtra("user");

        TextView tvScore = findViewById(R.id.tvScore);
        TextView tvTitle = findViewById(R.id.tvTitle);
        Button btnRestart = findViewById(R.id.RetryButton);
        Button btnBack = findViewById(R.id.ChangeButton);

        btnBack.setOnClickListener(v -> onBackPressed());
        btnRestart.setOnClickListener(v -> goToExercice(this, exerciceType));


        // initialize isCorrect array by comparing the user's answer with the correct answer
        switch (exerciceType) {
            case "Math":
                ArrayList<String> recapList = getIntent().getStringArrayListExtra("RESULT_ARRAY");
                for (int i = 0; i < recapList.size(); i++) {
                    // split the string by "="
                    String[] split = recapList.get(i).split("=");
                    // trim the string
                    split[0] = split[0].trim();
                    split[1] = split[1].trim();
                    // compare the two strings with calculateFromString()
                    if (calculateFromString(split[0]) == Integer.parseInt(split[1])) {
                        isCorrectArray.add(true);
                    } else {
                        isCorrectArray.add(false);
                    }
                }
                adapter = new ResultRecyclerViewAdapter(this, recapList, isCorrectArray, exerciceType);
                break;
            case "Quiz":
                isCorrectArray = (ArrayList<Boolean>) getIntent().getSerializableExtra("IS_CORRECT_ARRAY");
                recapList = getIntent().getStringArrayListExtra("RESULT_ARRAY");
                ArrayList<Long> id = (ArrayList<Long>) getIntent().getSerializableExtra("ID_LIST");
                adapter = new ResultRecyclerViewAdapter(this, recapList, isCorrectArray, exerciceType, id);

        }

        // tv score is the number of true values in isCorrectArray
        int score = 0;
        for (int i = 0; i < isCorrectArray.size(); i++) {
            if (isCorrectArray.get(i)) {
                score++;
            }
        }

        tvScore.setText("[ " + score + " / " + isCorrectArray.size() + " ]");

        if (score < isCorrectArray.size() / 2 || score == 0) {
            tvTitle.setText(R.string.try_again);
        } else if (score <= isCorrectArray.size() * 3 / 4) {
            tvTitle.setText(R.string.almost_there);
        } else if (score >= isCorrectArray.size() * 3 / 4 && score < isCorrectArray.size()) {
            tvTitle.setText(R.string.almost_perfect);
        } else if (score == isCorrectArray.size()) {
            tvTitle.setText(R.string.perfect);
        }

        RecyclerView recyclerView = findViewById(R.id.rvAnswers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


    }

    private void goToExercice(ResultActivity resultActivity, String exerciceType) {
        if (exerciceType.equals("Math")) {
            UtilsMethods.startMathActivity(resultActivity, current_user, getIntent().getStringExtra("oplist"), getIntent().getIntExtra("qnb", 10));
        } else if (exerciceType.equals("Quiz")) {
            UtilsMethods.startCGActivity(resultActivity, current_user);
        }


    }

}
