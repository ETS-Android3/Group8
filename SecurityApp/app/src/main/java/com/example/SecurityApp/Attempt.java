package com.example.SecurityApp;

import java.util.ArrayList;

public class Attempt {
    private double attemptTime;
    private String lockType;
    private boolean unlockSuccess;
    private String unlockPattern;

    public Attempt(double attemptTime, String lockType, boolean unlockSuccess, String unlockPattern) {
        this.attemptTime = attemptTime;
        this.lockType = lockType;
        this.unlockSuccess = unlockSuccess;
        this.unlockPattern = unlockPattern;
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
