package com.roager.moodcalenderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.roager.moodcalenderapp.model.MoodDate;
import com.roager.moodcalenderapp.repository.Repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private Button monthYearBtn;
    private TextView monthYearTextView;
    private TextView greatTextView;
    private TextView goodTextView;
    private TextView averageTextView;
    private TextView badTextView;
    private TextView terribleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        monthYearBtn = findViewById(R.id.monthYearBtn);
        monthYearTextView = findViewById(R.id.monthYearTextView);
        greatTextView = findViewById(R.id.greatTextView);
        goodTextView = findViewById(R.id.goodTextView);
        averageTextView = findViewById(R.id.averageTextView);
        badTextView = findViewById(R.id.badTextView);
        terribleTextView = findViewById(R.id.terribleTextView);

        monthYearBtn.setOnClickListener(view ->
                showDatePickerDialog()
        );
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // Sætter StatisticsActivity til at være context
                this,

                // Sætter special DatePicker style så det er en spinner
                R.style.CustomDatePickerDialogTheme,

                // Laver listeneren og får fat i valgte måned og år som gives med i getMoodStatisticsByMonthAndYear() metode  kaldet
                (view, year, month, day) -> {
                    String monthAndYear = month+1 + "-" + year;
                    monthYearTextView.setText(monthAndYear);
                    getMoodStatisticsByMonthAndYear(monthAndYear);
                },

                // Sætter viste måned og år til at være systemets
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );

        // Fjerner day-muligheden fra datepickeren
        ((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

        // Fjerner muligheden for at vælge en dato i fremtiden ved at sætte maxDate til systemet
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    public void getMoodStatisticsByMonthAndYear(String monthAndYear) {
        // Henter moodDates listen fra repository
        List<MoodDate> moodDates = Repository.getMoodDatesList();
        // Laver ny tom liste til at indeholde månedens moods
        List<Integer> moodsForMonthOfYearList = new ArrayList<>();

        // Finder alle moods ud fra det specifikke måned og år og tilføjer dem til moodsForMonthOfYearList
        for (MoodDate mood: moodDates) {
            if (mood.getDate().contains(monthAndYear)) {
                moodsForMonthOfYearList.add(mood.getMood());
            }
        }

        // Finder ud af hvor mange der er af hver type mood
        String noOfGreatDays = String.valueOf(Collections.frequency(moodsForMonthOfYearList, 1));
        String noOfGoodDays = String.valueOf(Collections.frequency(moodsForMonthOfYearList, 2));
        String noOfAverageDays = String.valueOf(Collections.frequency(moodsForMonthOfYearList, 3));
        String noOfBadDays = String.valueOf(Collections.frequency(moodsForMonthOfYearList, 4));
        String noOfTerribleDays = String.valueOf(Collections.frequency(moodsForMonthOfYearList, 5));

        // Sætter texten til at være antal af de forskellige moods
        greatTextView.setText(noOfGreatDays);
        goodTextView.setText(noOfGoodDays);
        averageTextView.setText(noOfAverageDays);
        badTextView.setText(noOfBadDays);
        terribleTextView.setText(noOfTerribleDays);
    }
}