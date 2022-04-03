package com.example.lousticsschool;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
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
    private RecyclerView recyclerView;
    private ArrayList<User> usersList = new ArrayList<>();

    private Button CreateAccountButton;
    private Button GuestButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        db = AppDb.getInstance(getApplicationContext());

        adapter = new RecyclerViewAdapter(this, usersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        CreateAccountButton = findViewById(R.id.CreateAccountButton);
        GuestButton = findViewById(R.id.GuestButton);

        CreateAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });


        GuestButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoggedActivity.class);
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

    private void getUsers() {
        ///////////////////////
        // Classe asynchrone permettant de récupérer des taches et de mettre à jour le listView de l'activité
        class GetUsers extends AsyncTask<Void, Void, List<User>> {

            @Override
            protected List<User> doInBackground(Void... voids) {
                return AppDb.getInstance(getApplicationContext()).userDao().getAll();
            }

            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);
                if (users == null) {
                    usersList = new ArrayList<>();
                }
                usersList = (ArrayList<User>) users;
                adapter.notifyDataSetChanged();


            }
        }

        //////////////////////////
        // IMPORTANT bien penser à executer la demande asynchrone
        // Création d'un objet de type GetTasks et execution de la demande asynchrone
        GetUsers gu = new GetUsers();
        gu.execute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUsers();
    }

}