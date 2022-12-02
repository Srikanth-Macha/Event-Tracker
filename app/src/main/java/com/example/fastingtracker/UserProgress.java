package com.example.fastingtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class UserProgress extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_progress);

        preferences = getSharedPreferences("Timer_Details", MODE_PRIVATE);
        editor = preferences.edit();

        lineChart = findViewById(R.id.lineChart);
        ArrayList<Entry> list = new ArrayList<>();

        String s = "21688942646";
        int num = 0;

        for (int i = 1; i < s.length(); i++) {
            list.add(new Entry(i, Float.parseFloat(s.charAt(i) + "")));
            num = i + 1;
        }


        Set<String> set = preferences.getStringSet("User_progress_completedTime", new LinkedHashSet<>());

        if (!set.isEmpty()) {
            for (String str : set) {
                if (!str.isEmpty() && !str.equals("0")) {
                    num++;
                    float f = Float.parseFloat(str);

                    list.add(new Entry(num, f));
                }
            }
        }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        LineDataSet highLineDataSet = new LineDataSet(list, "User Progress");
        highLineDataSet.setDrawCircles(false);
        highLineDataSet.setDrawValues(true);
        highLineDataSet.setValueTextColor(getResources().getColor(R.color.orange));
        highLineDataSet.setValueFormatter(lineChart.getDefaultValueFormatter());
        highLineDataSet.setLineWidth(3);
        highLineDataSet.setColor(Color.GREEN);
        highLineDataSet.setCircleColor(Color.GREEN);
        dataSets.add(highLineDataSet);

        LineData lineData = new LineData(dataSets);
        lineData.setValueTextColor(getResources().getColor(R.color.orange));
        lineChart.setData(lineData);

        lineChart.invalidate();

        if (getIntent().getBooleanExtra("ShareProgress", false)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    shareProgress();
                }
            }, 800);
        }

    }


    private void shareProgress() {
        Bitmap graphBitmap = lineChart.getChartBitmap();

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, getImageUri(UserProgress.this, graphBitmap));
        startActivity(Intent.createChooser(share, "Share via"));
    }

    private Uri getImageUri(@NonNull Context inContext, @NonNull Bitmap inImage) {
        //To convert image into a Uri to share using intent

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}