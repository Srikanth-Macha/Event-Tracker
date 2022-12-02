package com.example.fastingtracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences pref = getSharedPreferences("User_data", MODE_PRIVATE);

                if (pref.contains("name"))
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                else
                    startActivity(new Intent(SplashScreen.this, SecondActivity.class));

                finish();

            }
        }, 2000);

    }
}