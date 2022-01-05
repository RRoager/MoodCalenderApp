package com.roager.moodcalenderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.roager.moodcalenderapp.repository.Repository;

public class CreateMoodActivity extends AppCompatActivity {

    private TextView todaysDateView;
    private EditText editTextView;
    private NumberPicker moodPicker;
    private FloatingActionButton saveBtn;
    private FloatingActionButton deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mood);

        todaysDateView = findViewById(R.id.dateView);
        editTextView = findViewById(R.id.editTextView);
        moodPicker = findViewById(R.id.moodPicker);
        saveBtn = findViewById(R.id.saveBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        // Initialiserer et string array med de forskellige moods
        String[] moods = {"Choose your mood","Great","Good", "Average", "Bad", "Terrible"};

        // Sætter min, max og moods til at blive vist på moodPickeren
        moodPicker.setMinValue(0);
        moodPicker.setMaxValue(moods.length-1);
        moodPicker.setWrapSelectorWheel(true);
        moodPicker.setDisplayedValues(moods);

        editTextView.setText(Repository.getCurrentMoodDate().getText());
        moodPicker.setValue(Repository.getCurrentMoodDate().getMood());
        todaysDateView.setText(Repository.getCurrentMoodDate().getDate());

        // Laver onClickListener  på save knappen og gemmer dataen fra felterne
        saveBtn.setOnClickListener(v -> {
            String date = todaysDateView.getText().toString();
            String text = editTextView.getText().toString();
            int mood = moodPicker.getValue();
            // Sikre at mood altid er valgt før der gemmes
            if (mood != 0) {
                // Kalder saveMoodDate og sender den nye data med
                Repository.saveMoodDate(date, text, mood);
                Toast.makeText(CreateMoodActivity.this, "Today's mood has been saved.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CreateMoodActivity.this, "Please choose a mood before saving.", Toast.LENGTH_LONG).show();
            }
        });

        // Laver onClickListener  på delete knappen
        deleteBtn.setOnClickListener(v -> {
            // Laver en dialogbox
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateMoodActivity.this);
            builder.setMessage("Are you sure you would like to delete?")
                    .setCancelable(true)
                    // Hvis der trykkes "Yes" slettes MoodDaten og man bliver sendt til Main og der vises en Toast besked
                    .setPositiveButton("Yes", (dialog, id) -> {
                        Repository.deleteMoodDate();
                        Toast.makeText(CreateMoodActivity.this, "The mood has been deleted.", Toast.LENGTH_LONG).show();
                        Intent intent =new Intent(CreateMoodActivity.this, MainActivity.class);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", null).show();
        });
    }
}