package com.roager.moodcalenderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DateActivity extends AppCompatActivity {

    private TextView selectedDateView;
    private TextView textView;
    private TextView moodView;
    private ImageView moodImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        selectedDateView = findViewById(R.id.selectedDateView);
        textView = findViewById(R.id.textView);
        moodView = findViewById(R.id.moodView);
        moodImageView = findViewById(R.id.moodImageView);
        Repository.downloadBitmapForCurrentMoodDate();

        // Initialiserer et string array med de forskellige moods
        final String[] moods = {"Unfortunately not added", "Great", "Good", "Average", "Bad", "Terrible"};

        Intent intent = getIntent();

        // Sætter textViews visningsdata
        textView.setText(Repository.getCurrentMoodDate().getText());
        moodView.setText(moods[Repository.getCurrentMoodDate().getMood()]);
        moodImageView.setImageBitmap(Repository.getCurrentMoodDate().getBitmap());

        // Tjekker at intentet indeholder noget og sætter derefter datofeltet
        if (intent != null) {
            String date = intent.getStringExtra("date");
            selectedDateView.setText(date);
        }
    }
}