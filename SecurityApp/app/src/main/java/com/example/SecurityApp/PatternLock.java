package com.example.SecurityApp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;
import com.andrognito.rxpatternlockview.RxPatternLockView;
import com.andrognito.rxpatternlockview.events.PatternLockCompleteEvent;
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent;
import com.google.gson.Gson;

import java.util.List;

import SecurityApp.R;
import io.reactivex.functions.Consumer;

public class PatternLock extends AppCompatActivity {
    private PatternLockView mPatternLockView;
    private SwitchCompat setPasswordSwitch;
    private DataBase db;
    private SeekBar seekBar;
    private TextView rotateText;
    private int uid;
    private long startTime, endTime;
    private double elapsedTime;
    private Gson gson;
    private String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pattern_lock);

        gson = new Gson();
        Intent intent = getIntent();
        json = intent.getStringExtra("Database");
        db = gson.fromJson(json, DataBase.class);
        uid = intent.getIntExtra("UID", 0);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        rotateText = (TextView) findViewById(R.id.rotateText);
        rotateText.setText(String.valueOf(seekBar.getProgress()));
        setPasswordSwitch = (SwitchCompat) findViewById(R.id.setPassword);
        
        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        mPatternLockView.setDotCount(3);
        mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        mPatternLockView.setAspectRatioEnabled(true);
        mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        mPatternLockView.setDotAnimationDuration(150);
        mPatternLockView.setPathEndAnimationDuration(100);
        mPatternLockView.setCorrectStateColor(ResourceUtils.getColor(this, R.color.white));
        mPatternLockView.setInStealthMode(false);
        mPatternLockView.setTactileFeedbackEnabled(true);
        mPatternLockView.setInputEnabled(true);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);



        RxPatternLockView.patternComplete(mPatternLockView)
                .subscribe(new Consumer<PatternLockCompleteEvent>() {
                    @Override
                    public void accept(PatternLockCompleteEvent patternLockCompleteEvent) throws Exception {
                        Log.d(getClass().getName(), "Complete: " + patternLockCompleteEvent.getPattern().toString());
                    }
                });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
                rotateText.setText(String.valueOf(pval));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rotateText.setText(String.valueOf(pval));
            }
        });

        RxPatternLockView.patternChanges(mPatternLockView)
                .subscribe(new Consumer<PatternLockCompoundEvent>() {
                    @Override
                    public void accept(PatternLockCompoundEvent event) throws Exception {
                        if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_STARTED) {
                            Log.d(getClass().getName(), "Pattern drawing started");
                            startTime = System.currentTimeMillis();
                            //startAttempt();
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_PROGRESS) {
                            //Code for rotation
                            rotate(seekBar.getProgress());
                            Log.d(getClass().getName(), "Pattern progress: " +
                                    PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_COMPLETE) {
                            Log.d(getClass().getName(), "Pattern drawing started");
                            Log.d(getClass().getName(), "Pattern complete: " +
                                    PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
                            if(!setPasswordSwitch.isChecked()){
                                endTime = System.currentTimeMillis();
                                elapsedTime = ((double) (endTime - startTime)) / 1000;
                                boolean result = patternCheck(PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
                                db.newAttempt(uid,elapsedTime,"Pattern",result,PatternLockUtils.patternToString(mPatternLockView, event.getPattern()), seekBar.getProgress(), false);
                                mPatternLockView.setRotation(0);
                            }
                            else{
                                db.setPatternPasswordById(uid,PatternLockUtils.patternToString(mPatternLockView, event.getPattern()));
                            }

                        } else if (event.getEventType() == PatternLockCompoundEvent.EventType.PATTERN_CLEARED) {
                            Log.d(getClass().getName(), "Pattern has been cleared");
                        }
                    }
                });
    }

    //Method for handling rotation
    private void rotate(int amount){
        mPatternLockView.setRotation(mPatternLockView.getRotation()+amount);
    }
    //Checks if the completed pattern matches the saved pattern password
    private Boolean patternCheck(String pattern){

        if (pattern.equals(db.getPatternPasswordById(uid))) {
            Log.d(getClass().getName(), "Pattern Correct");
            // Display a popup
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(String.format("Password was Correct!\nElapsed Time: %.3f", elapsedTime));
            builder.setCancelable(true);
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }
        else{
            Log.d(getClass().getName(), "Pattern Incorrect");
            // Display a popup
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(String.format("Incorrect Password\nElapsed Time: %.3f", elapsedTime));
            builder.setCancelable(true);
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
    }

    private final PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };
}