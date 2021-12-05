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
    /*Finds if the last attempt was a success;
    No tests completed -1
    Tests already completed 1
    Test in process 0*/
    public int testComplete(){
        if(attempts.size()==0) return -1;
        if(attempts.get(attempts.size()-1).isUnlockSuccess()) return 1;
        return 0;
    }
}
