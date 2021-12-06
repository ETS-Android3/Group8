package com.example.SecurityApp;

public class Attempt {
    private int id;
    private double attemptTime;
    private String lockType;
    private boolean unlockSuccess;
    private String unlockPattern;
    private int rotation;
    private boolean randomized;

    public Attempt(int id, double attemptTime, boolean unlockSuccess, String lockType, String unlockPattern, int rotation, boolean randomized) {
        this.id = id;
        this.attemptTime = attemptTime;
        this.lockType = lockType;
        this.unlockSuccess = unlockSuccess;
        this.unlockPattern = unlockPattern;
        this.rotation = rotation;
        this.randomized = randomized;
    }
    public int getId() {
        return id;
    }

    public double getAttemptTime() {
        return attemptTime;
    }

    public int getRotation() {
        return rotation;
    }

    public boolean isRandomized() {
        return randomized;
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
