package com.roager.moodcalenderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.roager.moodcalenderapp.R;
import com.roager.moodcalenderapp.repository.Repository;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Repository.getMoodData();
    }
}