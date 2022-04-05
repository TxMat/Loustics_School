package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lousticsschool.db.AppDb;
import com.example.lousticsschool.db.User;
import com.example.lousticsschool.db.UserDao;

public class CreateAccountActivity extends AppCompatActivity {

    private final AppDb db = AppDb.getInstance(this);

    private Button Sumbit;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Sumbit = findViewById(R.id.AddUserButton);

        etName = findViewById(R.id.editTextTextPersonName);

        Sumbit.setOnClickListener(v -> saveUser());


    }

    private void saveUser() {

        // Récupérer les informations contenues dans les vues
        final String sName = etName.getText().toString().trim();

        // Vérifier les informations fournies par l'utilisateur
        if (sName.isEmpty()) {
            etName.setError("Task required");
            etName.requestFocus();
            return;
        }

        /**
         * Création d'une classe asynchrone pour sauvegarder la tache donnée par l'utilisateur
         */
        class SaveUser extends AsyncTask<Void, Void, User> {

            @Override
            protected User doInBackground(Void... voids) {

                // creating a task
                User usr = new User();
                usr.setName(sName);

                UserDao tmp = db.userDao();

                // adding to database
                long id = db.userDao().insert(usr);

                // mettre à jour l'id de la tache
                // Nécessaire si on souhaite avoir accès à l'id plus tard dans l'activité
                usr.setId(id);


                return usr;
            }

            @Override
            protected void onPostExecute(User usr) {
                super.onPostExecute(usr);

                // Quand la tache est créée, on arrête l'activité AddTaskActivity (on l'enleve de la pile d'activités)
                setResult(RESULT_OK);
                finish();
                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
            }
        }

        //////////////////////////
        // IMPORTANT bien penser à executer la demande asynchrone
        SaveUser st = new SaveUser();
        st.execute();
    }
}