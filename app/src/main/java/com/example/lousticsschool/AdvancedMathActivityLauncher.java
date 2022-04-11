package com.example.lousticsschool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.lousticsschool.db.User;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.Slider;

import java.util.List;

public class AdvancedMathActivityLauncher extends AppCompatActivity {

    private ChipGroup chipGroup;
    private Button StartButton;
    private Slider slider;

    private String operator_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_math_launcher);

        chipGroup = findViewById(R.id.cgOperation);
        StartButton = findViewById(R.id.btnLaunch);
        slider = findViewById(R.id.slider);

        operator_list = "";


        StartButton.setOnClickListener(v -> {
            List<Integer> selectedIds = chipGroup.getCheckedChipIds();
            System.out.println(selectedIds);
            for (int i = 0; i < selectedIds.size(); i++) {
                int id = selectedIds.get(i);
                if (id == R.id.chipAdd) {
                    operator_list += "+";
                }
                if (id == R.id.chipSubtract) {
                    operator_list += "-";
                }
                if (id == R.id.chipMultiply) {
                    operator_list += "x";
                }
                if (id == R.id.chipDivide) {
                    operator_list += "/";
                }
            }
            if (operator_list.length() == 0) {
                Toast.makeText(getApplicationContext(), "Selectionne au moins un type d'operation", Toast.LENGTH_SHORT).show();
            } else {
                UtilsMethods.startMathActivity(this, (User) getIntent().getSerializableExtra("user"), operator_list, (int) slider.getValue());
            }
        });

    }
}