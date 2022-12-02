package com.example.fastingtracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fastingtracker.databinding.ActivityProfileDetailsBinding;

import java.text.DecimalFormat;
import java.util.Objects;

public class ProfileDetails extends AppCompatActivity {
    ActivityProfileDetailsBinding binding;
    String bmi, height, weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String choice = getIntent().getStringExtra("choice");

        binding.bmiText.setVisibility(View.INVISIBLE);
        binding.startFasting.setVisibility(View.INVISIBLE);

        binding.calcBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeKeyboard();

                height = Objects.requireNonNull(binding.height.getText()).toString();
                weight = Objects.requireNonNull(binding.weight.getText()).toString();

                if (height.isEmpty() || weight.isEmpty())
                    Toast.makeText(ProfileDetails.this, "Height and weight cannot be empty", Toast.LENGTH_SHORT).show();
                else {
                    double h = Double.parseDouble(height) / 100;
                    double w = Double.parseDouble(weight);

                    DecimalFormat decimalFormat = new DecimalFormat("0.00");

                    bmi = decimalFormat.format(w / (h * h));

                    binding.bmiText.setText("Your BMI is : " + bmi);

                    binding.bmiText.setVisibility(View.VISIBLE);
                    binding.startFasting.setVisibility(View.VISIBLE);
                }

            }
        });

        binding.startFasting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.nameEditText.getText().toString();

                if (name.isEmpty())
                    Toast.makeText(ProfileDetails.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                else {
                    Intent i = new Intent(ProfileDetails.this, MainActivity.class);
                    i.putExtra("choice", choice);
                    i.putExtra("height", height);
                    i.putExtra("weight", weight);
                    i.putExtra("bmi", bmi);
                    i.putExtra("name", name);

                    startActivity(i);
                }

            }
        });

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}