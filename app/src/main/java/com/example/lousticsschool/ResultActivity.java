package com.example.lousticsschool;

import static com.example.lousticsschool.UtilsMethods.calculateFromString;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lousticsschool.db.AppDb;
import com.example.lousticsschool.db.Quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultActivity extends AppCompatActivity  {

    private ResultRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<String> recapList = new ArrayList<>();
    private ArrayList<Boolean> isCorrectArray = new ArrayList<>();

    private TextView tvScore;
    private TextView tvTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        String exerciceType = getIntent().getStringExtra("EXERCICE_TYPE");

        tvScore = findViewById(R.id.tvScore);
        tvTitle = findViewById(R.id.tvTitle);



        // initialize isCorrect array by comparing the user's answer with the correct answer
        switch (exerciceType) {
            case "Math":
                recapList = getIntent().getStringArrayListExtra("RESULT_ARRAY");
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

        if (score < isCorrectArray.size() / 2) {
            tvTitle.setText(R.string.try_again);
        }
        else if (score < isCorrectArray.size() * 3 / 4) {
            tvTitle.setText(R.string.almost_there);
        }
        else if (score > isCorrectArray.size() * 3 / 4 && score < isCorrectArray.size()) {
            tvTitle.setText(R.string.almost_perfect);
        }
        else if (score == isCorrectArray.size()) {
            tvTitle.setText(R.string.perfect);
        }

        recyclerView = findViewById(R.id.rvAnswers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

}
