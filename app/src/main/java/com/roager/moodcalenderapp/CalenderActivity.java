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

        // Tilføjer listener til kalenderen
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Sørger for at der er 0 foran enkelt cifrede måneder og dage
                String stringDayOfMonth = "";
                String stringMonth = "";
                if (dayOfMonth < 10) {
                    stringDayOfMonth = "0" + dayOfMonth;
                } else {
                    stringDayOfMonth = String.valueOf(dayOfMonth);
                }
                if (month < 10) {
                    stringMonth = "0" + (month + 1);
                } else {
                    stringMonth = String.valueOf(month + 1);
                }
                // Laver en String date ud fra den dato der er trykket på kalenderen
                String date = stringDayOfMonth + "-" + stringMonth + "-" + year;

                // Sætter currentMoodDate til at være denne dato
                Repository.setCurrentMoodDate(date);
                Intent intent = new Intent(CalenderActivity.this, DateActivity.class);

                // Gemmer den valgte dato til brug i DateActivity
                //intent.putExtra("date", date);

                startActivity(intent);
            }
        });
    }
}