package com.roager.moodcalenderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.roager.moodcalenderapp.model.MoodDate;
import com.roager.moodcalenderapp.repository.Repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

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

        monthYearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, // Sætter activitien til at være context
                R.style.CustomDatePickerDialogTheme, // Sætter special DatePicker style så det er en spinner
                this, // Sætter activitien til at være listener, kan lade sig gøre fordi vi har implementeret DatePickerDialog.OnDateSetListener på klassen
                Calendar.getInstance().get(Calendar.YEAR), // Sætter viste måned og år til at være systemets
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        // Fjerner day-muligheden fra datepickeren
        ((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        // Fjerner muligheden for at vælge en dato i fremtiden ved at sætte maxDate til systemet
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String monthAndYear = month+1 + "-" + year;
        monthYearTextView.setText(monthAndYear);
        getMoodStatisticsByMonthAndYear(monthAndYear);
    }

    public void getMoodStatisticsByMonthAndYear(String monthAndYear) {
        List<MoodDate> moodDates = Repository.getMoodDatesList();
        List<Integer> moodsForMonthOfYearList = new ArrayList<>();

        for (MoodDate mood: moodDates) {
            if (mood.getDate().contains(monthAndYear)) {
                moodsForMonthOfYearList.add(mood.getMood());
            }
        }

        String noOfGreatDays = Collections.frequency(moodsForMonthOfYearList, 1) + "";
        String noOfGoodDays = Collections.frequency(moodsForMonthOfYearList, 2) + "";
        String noOfAverageDays = Collections.frequency(moodsForMonthOfYearList, 3) + "";
        String noOfBadDays = Collections.frequency(moodsForMonthOfYearList, 4) + "";
        String noOfTerribleDays = Collections.frequency(moodsForMonthOfYearList, 5) + "";

        greatTextView.setText(noOfGreatDays);
        goodTextView.setText(noOfGoodDays);
        averageTextView.setText(noOfAverageDays);
        badTextView.setText(noOfBadDays);
        terribleTextView.setText(noOfTerribleDays);
    }
}