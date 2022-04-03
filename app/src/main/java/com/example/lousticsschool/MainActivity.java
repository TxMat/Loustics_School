package com.example.lousticsschool;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;

import com.example.lousticsschool.db.AppDb;
import com.example.lousticsschool.db.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDb db;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerview = findViewById(R.id.recyclerView);

        db = AppDb.getInstance(getApplicationContext());

        ArrayList<User> users = new ArrayList<User>();

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("User " + i);
            users.add(user);
        }

        adapter = new RecyclerViewAdapter(this, users);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);


        Button btn = findViewById(R.id.Btn);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MathActivity.class);
            intent.putExtras(MathModel.getFirstBundle(10));
            startActivity(intent);
        });
    }

    //ask for a confirmation when the user wants to exit the app using the back button
    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Lousics School")
                .setMessage("Veux-tu vraiment quitter l'application?")
                .setPositiveButton("Oui", (dialog, which) -> finish())
                .setNegativeButton("Non", null)
                .show();
    }

    private void getTasks() {
        ///////////////////////
        // Classe asynchrone permettant de récupérer des taches et de mettre à jour le listView de l'activité
        class GetTasks extends AsyncTask<Void, Void, List<User>> {

            @Override
            protected List<User> doInBackground(Void... voids) {
                return AppDb.getInstance(getApplicationContext()).userDao().getAll();
            }

            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);
                adapter.notifyDataSetChanged();


            }
        }

        //////////////////////////
        // IMPORTANT bien penser à executer la demande asynchrone
        // Création d'un objet de type GetTasks et execution de la demande asynchrone
        GetTasks gt = new GetTasks();
        gt.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTasks();
    }

}