package com.example.fastingtracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String bmi, height, weight, name;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("User_data", MODE_PRIVATE);
        if (!pref.contains("name")) {
            bmi = getIntent().getStringExtra("bmi");
            height = getIntent().getStringExtra("height");
            weight = getIntent().getStringExtra("weight");
            name = getIntent().getStringExtra("name");

            // To save user data, use SharedPreferences
            saveUserData(pref);
        }

        Toast.makeText(this, "Hello👋, " + pref.getString("name", ""), Toast.LENGTH_SHORT).show();

        ArrayList<Pair<Integer, Integer>> list = new ArrayList<>();
        list.add(new Pair<>(14, 10));
        list.add(new Pair<>(16, 8));
        list.add(new Pair<>(18, 6));
        list.add(new Pair<>(20, 4));

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new RecyclerAdapter(list, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.fasting);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.person:
                    Intent i = new Intent(this, UserProfile.class);

                    i.putExtra("height", height);
                    i.putExtra("weight", weight);
                    i.putExtra("name", name);
                    i.putExtra("bmi", bmi);

                    startActivity(i);
                    finish();
                    return true;

                case R.id.fasting:
                    Toast.makeText(MainActivity.this, "fasting", Toast.LENGTH_SHORT).show();
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
    }

    private void saveUserData(SharedPreferences pref) {

        SharedPreferences.Editor editor = pref.edit();

        editor.putString("bmi", bmi);
        editor.putString("height", height);
        editor.putString("weight", weight);
        editor.putString("name", name);

        editor.apply();
    }
}