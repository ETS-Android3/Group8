package com.example.SecurityApp;

import java.util.ArrayList;

public class Test {
    private ArrayList<Attempt> attempts;
    private int uid;
    public Test(ArrayList<Attempt> attempts, int uid) {
        this.attempts = attempts;
        this.uid = uid;
    }

    public ArrayList<Attempt> getAttempts() {
        return attempts;
    }
}
