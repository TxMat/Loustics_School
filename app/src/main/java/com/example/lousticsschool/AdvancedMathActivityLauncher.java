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
    private Slider slider;

    private String operator_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_math_launcher);

        chipGroup = findViewById(R.id.cgOperation);
        Button startButton = findViewById(R.id.btnLaunch);
        slider = findViewById(R.id.slider);

        operator_list = "";


        startButton.setOnClickListener(v -> {
            List<Integer> selectedIds = chipGroup.getCheckedChipIds();
            System.out.println(selectedIds);
            StringBuilder sb = new StringBuilder(operator_list);
            for (int i = 0; i < selectedIds.size(); i++) {
                int id = selectedIds.get(i);
                if (id == R.id.chipAdd) {
                    sb.append("+");
                }
                if (id == R.id.chipSubtract) {
                    sb.append("-");
                }
                if (id == R.id.chipMultiply) {
                    sb.append("x");
                }
                if (id == R.id.chipDivide) {
                    sb.append("/");
                }
            }
            operator_list = sb.toString();
            if (operator_list.length() == 0) {
                Toast.makeText(getApplicationContext(), "Selectionne au moins un type d'operation", Toast.LENGTH_SHORT).show();
            } else {
                UtilsMethods.startMathActivity(this, (User) getIntent().getSerializableExtra("user"), operator_list, (int) slider.getValue());
            }
        });

    }
}