package com.example.lousticsschool;


import android.app.AlertDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class LogoutMethods extends AppCompatActivity {

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

    /*public static void Quit(AppCompatActivity activity) {
        new android.app.AlertDialog.Builder(activity)
                .setTitle("Quit")
                .setMessage("Are you sure you want to quit?")
                .setPositiveButton("Yes", (dialog, which) -> activity.finish())
                .setNegativeButton("No", null)
                .show();
    }*/
}