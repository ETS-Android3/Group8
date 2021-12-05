package com.example.SecurityApp;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String name;
    private int id;
    private String scrabblePassword;
    private String patternPassword;
    private ArrayList<Test> tests;

    public User(String name, int id, String scrabblePassword, String patternPassword) {
        this.name = name;
        this.id = id;
        this.scrabblePassword = scrabblePassword;
        this.patternPassword = patternPassword;
    }

    @Override
    public String toString() {
        return "Users{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScrabblePassword() {
        return scrabblePassword;
    }

    public void setScrabblePassword(String scrabblePassword) {
        this.scrabblePassword = scrabblePassword;
    }

    public String getPatternPassword() {
        return patternPassword;
    }

    public void setPatternPassword(String patternPassword) {
        this.patternPassword = patternPassword;
    }
}
