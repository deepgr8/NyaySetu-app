package com.example.nyaysetu;

class User {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String name;
    private String email;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
