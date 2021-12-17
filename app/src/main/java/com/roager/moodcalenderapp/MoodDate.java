package com.roager.moodcalenderapp;

import android.graphics.Bitmap;

public class MoodDate {
    private String date;
    private String text;
    private int mood;
    private Bitmap bitmap;

    public MoodDate(String date, String text, int mood) {
        this.date = date;
        this.text = text;
        this.mood = mood;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
