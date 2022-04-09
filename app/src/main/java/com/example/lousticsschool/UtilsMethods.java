package com.example.lousticsschool;


import android.app.AlertDialog;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

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

}