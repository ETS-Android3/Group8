package com.example.SecurityApp;

import java.util.ArrayList;

public class Attempt {
    private int id;
    private double attemptTime;
    private String lockType;
    private boolean unlockSuccess;
    private String unlockPattern;

    public Attempt(int id, double attemptTime, String lockType, boolean unlockSuccess, String unlockPattern) {
        this.id = id;
        this.attemptTime = attemptTime;
        this.lockType = lockType;
        this.unlockSuccess = unlockSuccess;
        this.unlockPattern = unlockPattern;
    }
    public int getId() {
        return id;
    }

    public double getAttemptTime() {
        return attemptTime;
    }

    public String getLockType() {
        return lockType;
    }

    public boolean isUnlockSuccess() {
        return unlockSuccess;
    }

    public String getUnlockPattern() {
        return unlockPattern;
    }
}
