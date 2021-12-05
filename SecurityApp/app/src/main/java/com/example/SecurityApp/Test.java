package com.example.SecurityApp;

import java.util.ArrayList;

public class Test {
    private ArrayList<Attempt> attempts;
    private int uid;
    public Test(int uid) {
        attempts = new ArrayList<Attempt>();
        this.uid = uid;
    }

    public void addAttempt(Attempt a){
        attempts.add(a);
    }
    public ArrayList<Attempt> getAttempts() {
        return attempts;
    }
    //Finds if the last attempt was a success;
    public boolean testComplete(){
        if(attempts.size()==0) return true;
        if(attempts.get(attempts.size()-1).isUnlockSuccess()) return true;
        return false;
    }
}
