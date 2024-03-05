package com.example.nyaysetu;

public class AdvocateModel {
    String name;
    int experience;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public AdvocateModel(String name, int experience) {
        this.name = name;
        this.experience = experience;
    }
}
