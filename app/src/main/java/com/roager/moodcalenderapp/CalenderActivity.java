package com.roager.moodcalenderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import com.roager.moodcalenderapp.repository.Repository;

public class CalenderActivity extends AppCompatActivity {

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        calendarView = findViewById(R.id.calendarView);

        // Tilføjer listener kalenderen
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + (month + 1) + "-" + year;
                Repository.setCurrentMoodDate(date);
                Intent intent = new Intent(CalenderActivity.this, DateActivity.class);

                // Gemmer den valgte dato til brug i DateActivity
                intent.putExtra("date", date);

                startActivity(intent);
            }
        });
    }
}