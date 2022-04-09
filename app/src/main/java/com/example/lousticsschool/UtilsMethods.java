package com.example.lousticsschool;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lousticsschool.db.AppDb;
import com.example.lousticsschool.db.User;

import java.util.ArrayList;
import java.util.List;

public class UtilsMethods extends AppCompatActivity {

    // ask user if they want to logout
    public static void Logout(AppCompatActivity activity) {
        new android.app.AlertDialog.Builder(activity)
                .setTitle("Logout")
                .setMessage("Voulez vous vraiment vous déconnecter ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);
                        }
                )
                .setNegativeButton("Non", null)
                .show();
    }


    public static int calculateFromString(String s) {
        // split string by space
        String[] calc = s.split(" ");
        // determine the operator
        String operator = calc[1];
        // get the numbers
        int num1 = Integer.parseInt(calc[0]);
        int num2 = Integer.parseInt(calc[2]);
        // perform the calculation
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "x":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                return 0;
        }

    }

    public static void deleteUser(User user, Context context) {
        ///////////////////////
        // Classe asynchrone permettant de supprimer un utilisateur
        class DeleteUser extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                AppDb.getInstance(context).userDao().delete(user);
                return null;
            }

        }

        //////////////////////////
        // IMPORTANT bien penser à executer la demande asynchrone
        // Création d'un objet de type DeleteUser et execution de la demande asynchrone
        DeleteUser du = new DeleteUser();
        du.execute();
    }

}