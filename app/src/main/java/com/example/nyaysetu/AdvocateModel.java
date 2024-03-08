package com.example.nyaysetu;

import android.widget.Button;
import android.widget.RatingBar;

public class AdvocateModel {
    String name;
    String experience;
    float ratingBar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public float getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(float ratingBar) {
        this.ratingBar = ratingBar;
    }


    public AdvocateModel(String name, String experience, float ratingBar) {
        this.name = name;
        this.experience = experience;
        this.ratingBar = ratingBar;
    }
}
