package com.example.fastingtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button loseWeight = findViewById(R.id.loseWeight);
        Button improveHealth = findViewById(R.id.improveHealth);


        loseWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveNext("loseWeight");
            }
        });

        improveHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveNext("improveHealth");
            }
        });

    }

    private void moveNext(String s) {
        Intent i = new Intent(this, ProfileDetails.class);
        i.putExtra("choice", s);

        startActivity(i);
    }

}