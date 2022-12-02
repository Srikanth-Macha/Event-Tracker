package com.example.fastingtracker;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class BroadcastService extends Service {

    CountDownTimer timer;
    long startTime;

//    SharedPreferences pref;
//    SharedPreferences.Editor editor;

    static final String COUNTDOWN_BR = "com.example.fastingtracker";
    Intent intent1 = new Intent(COUNTDOWN_BR);

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTime = intent.getLongExtra("startTime", 55000);

//        pref = getSharedPreferences("Timer_Details", MODE_PRIVATE);
//        editor = pref.edit();

        timer = new CountDownTimer(startTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                intent1.putExtra("Time finished", millisUntilFinished);

                sendBroadcast(intent1);
            }

            @Override
            public void onFinish() {
                Toast.makeText(BroadcastService.this, "Congrats you have finished the fasting challenge🔥", Toast.LENGTH_SHORT).show();
            }
        };

        timer.start();
//        new NotificationTimer.Builder(this)
//                .setSmallIcon(R.mipmap.fasting_icon)
//                .play(startTime);

//        editor.putBoolean("TimerRunning", true);
//        editor.apply();

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        if (timer != null)
            timer.cancel();

        super.onDestroy();

//        editor.remove("TimerRunning");
//        editor.apply();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
