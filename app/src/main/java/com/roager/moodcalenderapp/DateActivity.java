package com.roager.moodcalenderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.roager.moodcalenderapp.repository.Repository;

public class DateActivity extends AppCompatActivity implements Updatable {

    private TextView selectedDateView;
    private TextView moodTextView;
    private TextView moodContentView;
    private ImageView moodImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        selectedDateView = findViewById(R.id.dateView);
        moodTextView = findViewById(R.id.moodTextView);
        moodContentView = findViewById(R.id.moodContentView);
        moodImageView = findViewById(R.id.moodImageView);
        // Downloader billede fra db og sætter caller til at være denne klasse
        Repository.downloadBitmapForCurrentMoodDate(this);

        // Initialiserer et string array med de forskellige moods
        String[] moods = {"Unfortunately not added", "Great", "Good", "Average", "Bad", "Terrible"};

        //Intent intent = getIntent();

        // Sætter mood ud fra det index moodet har samt text og dato
        moodTextView.setText(moods[Repository.getCurrentMoodDate().getMood()]);
        moodContentView.setText(Repository.getCurrentMoodDate().getText());
        selectedDateView.setText(Repository.getCurrentMoodDate().getDate());

        /*
        // Tjekker at intentet indeholder noget og sætter derefter datofeltet
        if (intent != null) {
            String date = intent.getStringExtra("date");
            selectedDateView.setText(date);
        }
        */
    }

    @Override
    public void updateMoodImage(Object object) {
        // Sætter mood billedet
        moodImageView.setImageBitmap(Repository.getCurrentMoodDate().getBitmap());
    }
}