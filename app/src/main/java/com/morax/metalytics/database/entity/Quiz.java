package com.morax.metalytics.database.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


public class Quiz {
    public String question;
    public String[] choices;
    public int correctAnswer;
    public byte[] image;

    public Quiz(String question, String[] choices, int correctAnswer, byte[] image) {
        this.question = question;
        this.choices = choices;
        this.correctAnswer = correctAnswer;
        this.image = image;
    }
}
