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

        class DeleteUser extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                AppDb.getInstance(context).userDao().delete(user);
                return null;
            }

        }


        DeleteUser du = new DeleteUser();
        du.execute();
    }

    public static void goToLoggedMenu(AppCompatActivity activity, User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Abandonner");
        builder.setMessage("Voulez-vous vraiment abandonner ?\nVous perdrez toutes vos réponses.");
        builder.setPositiveButton("Oui, je veux quitter l'exercice", (dialog, which) -> {
                    Intent intent = new Intent(activity, LoggedActivity.class);
                    //clear all the activity stack
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("user", user);
                    activity.startActivity(intent);
                    activity.finish();
                }
        );
        builder.setNegativeButton("Non, je veux continuer", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    public static int[] randomizeArray(int length) {
        // create an array of integers
        int[] arr = new int[length];
        // fill the array with numbers from 0 to length
        for (int i = 0; i < length; i++) {
            arr[i] = i;
        }
        // shuffle the array
        for (int i = 0; i < length; i++) {
            int rnd = (int) (Math.random() * length);
            int temp = arr[i];
            arr[i] = arr[rnd];
            arr[rnd] = temp;
        }
        return arr;
    }
}