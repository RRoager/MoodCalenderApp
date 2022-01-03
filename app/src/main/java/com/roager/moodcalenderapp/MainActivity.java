package com.roager.moodcalenderapp;

import static java.time.LocalDate.now;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.roager.moodcalenderapp.repository.Repository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button setMoodBtn;
    private Button calenderBtn;
    private Button statisticsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Repository.init();

        setMoodBtn = findViewById(R.id.setMoodBtn);
        calenderBtn = findViewById(R.id.calenderBtn);
        statisticsBtn = findViewById(R.id.statisticsBtn);

        setMoodBtn.setOnClickListener(v -> {
            // Finder dags dato og formattere den korrekt
            LocalDate localDate = now();
            String date = localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            Repository.createMoodDate(date);
            // Bruger datoen til sætte currentMoodDate
            Repository.setCurrentMoodDate(date);
            Intent intent=new Intent(MainActivity.this, CreateMoodActivity.class);

            // Smider datoen med som extra i intentet
            intent.putExtra("date", date);

            startActivity(intent);
        });

        calenderBtn.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this, CalenderActivity.class);
            startActivity(intent);
        });

        statisticsBtn.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        });
    }
}