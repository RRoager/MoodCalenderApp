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
    //private DatePicker datePicker;

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

        //datePicker = findViewById(R.id.datePicker);
        // Fjerner dagen på DatePickeren
        //datePicker.findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

        monthYearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                R.style.CustomDatePickerDialogTheme,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        ((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
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