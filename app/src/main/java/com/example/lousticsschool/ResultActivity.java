package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ResultRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<String> recapList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        recapList = getIntent().getStringArrayListExtra("RESULT_ARRAY");

        recyclerView = findViewById(R.id.rvAnswers);
        adapter = new ResultRecyclerViewAdapter(this, recapList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);





    }
}