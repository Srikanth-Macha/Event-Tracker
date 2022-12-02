package com.example.fastingtracker;

import static com.example.fastingtracker.BroadcastService.COUNTDOWN_BR;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class FastingActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private TextView fasting, timerText;
    private Button startButton, stopButton;
    private long startTime;
    private long remainingTime;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // update value
            updateCount(intent);
        }
    };


    // TODO Add shared preferences

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fasting);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            requestPermissions(new String[]{Manifest.permission.FOREGROUND_SERVICE}, PackageManager.PERMISSION_GRANTED);
        }

        // For storing the timer state
        preferences = getSharedPreferences("Timer_Details", MODE_PRIVATE);
        editor = preferences.edit();

        fasting = findViewById(R.id.fastingText);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        timerText = findViewById(R.id.countDownTimer);

        stopButton.setVisibility(View.INVISIBLE);
        fasting.setVisibility(View.INVISIBLE);

        long fastingTime = Long.parseLong(getIntent().getStringExtra("first"));
        startTime = fastingTime * 60 * 60 * 1000;

        startButton.setOnClickListener(view -> {

            startTimer();
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("TimerRunning");
                editor.apply();

                addToProgress();

                Toast.makeText(FastingActivity.this, "Timer stopped", Toast.LENGTH_SHORT).show();

                unregisterReceiver(broadcastReceiver);
                stopService(new Intent(FastingActivity.this, BroadcastService.class));

                stopButton.setVisibility(View.INVISIBLE);
                startButton.setVisibility(View.VISIBLE);
            }
        });

    }

    private void addToProgress() {
        long minutes = ((startTime - remainingTime) / 1000);

        String completedTime = String.valueOf(minutes);

        Set<String> set = preferences.getStringSet("User_progress_completedTime", new LinkedHashSet<>());
        set.add(completedTime);

        editor.putStringSet("User_progress_completedTime", set);
        editor.apply();
    }

    private void startTimer() {
        startButton.setVisibility(View.INVISIBLE);
        fasting.setVisibility(View.VISIBLE);
        stopButton.setVisibility(View.VISIBLE);

        try {
            registerReceiver(broadcastReceiver, new IntentFilter(COUNTDOWN_BR));
        } catch (Exception ignored) {
        }

        Intent i = new Intent(FastingActivity.this, BroadcastService.class);

        if (remainingTime == 0)
            i.putExtra("startTime", startTime);
        else
            i.putExtra("startTime", remainingTime);

        editor.putBoolean("TimerRunning", true);
        editor.apply();

        startService(i);
    }

    private void updateCount(Intent intent) {
        remainingTime = intent.getLongExtra("Time finished", 30000);

        editor.putLong("remainingTime", remainingTime);
        editor.apply();

        updateCountDown();
    }


    private void updateCountDown() {
        String time = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(remainingTime),
                TimeUnit.MILLISECONDS.toMinutes(remainingTime) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(remainingTime)),
                TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime)));

        timerText.setText(time);

        setNotification(time);
    }

    private void setNotification(String time) {
        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "notify_001");

        Intent ii = new Intent(this, BroadcastService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Fasting Timer");
        bigText.setBigContentTitle(time);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.fasting_icon);
        mBuilder.setContentTitle(time);
        mBuilder.setContentText("Keep Fasting🔥🔥");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_LOW);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            registerReceiver(broadcastReceiver, new IntentFilter(COUNTDOWN_BR));
        } catch (Exception e) {
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
        }

        if (preferences.getBoolean("TimerRunning", false)) {

            if (preferences.getLong("remainingTime", 55L) != 55L) {

                startButton.setVisibility(View.INVISIBLE);
                fasting.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);
            }

        }

    }
}