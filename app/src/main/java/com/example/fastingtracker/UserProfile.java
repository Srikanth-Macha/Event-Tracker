package com.example.fastingtracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fastingtracker.databinding.ActivityUserProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserProfile extends AppCompatActivity {
    String bmi, height, weight, name;
    BottomNavigationView bottomNavigationView;

    ActivityUserProfileBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences pref = getSharedPreferences("User_data", MODE_PRIVATE);

        if (pref.contains("name")) {
            Toast.makeText(this, "from preferences", Toast.LENGTH_SHORT).show();

            bmi = pref.getString("bmi", "");
            height = pref.getString("height", "");
            weight = pref.getString("weight", "");
            name = pref.getString("name", "");
        } else {
            bmi = getIntent().getStringExtra("bmi");
            height = getIntent().getStringExtra("height");
            weight = getIntent().getStringExtra("weight");
            name = getIntent().getStringExtra("name");
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.person);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.fasting:
                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("height", height);
                    i.putExtra("weight", weight);
                    i.putExtra("name", name);
                    i.putExtra("bmi", bmi);

                    startActivity(i);
                    finish();
                    return true;

                case R.id.person:
                    Toast.makeText(UserProfile.this, "Profile", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.share:
                    Intent in = new Intent(this, UserProgress.class);
                    in.putExtra("ShareProgress", true);

                    Toast.makeText(this, "Share your progress", Toast.LENGTH_SHORT).show();

                    startActivity(in);
                    break;

                default:
                    break;
            }

            return false;
        });

        binding.name.setText(name);
        binding.heightText.setText("Your height : " + height + " cm");
        binding.weightText.setText("Your weight : " + weight + " kg");
        binding.bmiText.setText("Your BMI : " + bmi);

        double bmiValue = Double.parseDouble(bmi);

        if (bmiValue < 18.5) {
            binding.status.setText("UnderWeighted");
            binding.status.setTextColor(getResources().getColor(R.color.blue));
        } else if (bmiValue < 25.5) {
            binding.status.setText("Normal");
            binding.status.setTextColor(getResources().getColor(R.color.green));
        } else {
            binding.status.setText("OverWeighted");
            binding.status.setTextColor(getResources().getColor(R.color.red));
        }

        binding.checkProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this, UserProgress.class));
            }
        });

    }
}