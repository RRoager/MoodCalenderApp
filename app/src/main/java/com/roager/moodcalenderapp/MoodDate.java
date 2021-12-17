package com.roager.moodcalenderapp;

public class MoodDate {
    private String date;
    private String text;
    private int mood;

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
}
