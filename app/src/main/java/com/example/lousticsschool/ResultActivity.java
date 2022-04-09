package com.example.lousticsschool;

import static com.example.lousticsschool.UtilsMethods.calculateFromString;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity implements ResultRecyclerViewAdapter.ItemClickListener  {

    private ResultRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<String> recapList = new ArrayList<>();
    private ArrayList<Boolean> isCorrectArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        recapList = getIntent().getStringArrayListExtra("RESULT_ARRAY");

        String exerciceType = getIntent().getStringExtra("EXERCICE_TYPE");

        // initialize isCorrect array by comparing the user's answer with the correct answer
        switch (exerciceType) {
            case "Math":
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
                break;
        }

        recyclerView = findViewById(R.id.rvAnswers);
        adapter = new ResultRecyclerViewAdapter(this, recapList, isCorrectArray, exerciceType);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);





    }

    @Override
    public void onItemClick(View view, int position) {
        // if the calcul is incorrect display a dialog with the correct answer

    }
}
