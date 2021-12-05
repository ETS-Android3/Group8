package com.example.SecurityApp;

import java.util.ArrayList;

public class Attempt {
    private int attemptTime;
    private String lockType;
    private boolean unlockSuccess;
    private String unlockPattern;

    public Attempt(int attemptTime, String lockType, boolean unlockSuccess, String unlockPattern) {
        this.attemptTime = attemptTime;
        this.lockType = lockType;
        this.unlockSuccess = unlockSuccess;
        this.unlockPattern = unlockPattern;
    }

    public int getAttemptTime() {
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
