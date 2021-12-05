package com.example.SecurityApp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import SecurityApp.R;

public class ScrabbleLock extends AppCompatActivity implements View.OnClickListener {
    // Properties
    private User user;
    private DataBase db;

    StringBuilder testPassword = new StringBuilder();
    private List<Character> letterList;
    private long startTime, endTime;
    private double elapsedTime;
    private boolean isFirstClick = true;
    private boolean randomize = false;
    private Button btn_unfocus;
    private Button enterButton;
    private final Button[] btn = new Button[26];
    private final int[] btn_id = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9, R.id.btn10, R.id.btn11,
            R.id.btn12, R.id.btn13, R.id.btn14, R.id.btn15,
            R.id.btn16, R.id.btn17, R.id.btn18, R.id.btn19,
            R.id.btn20, R.id.btn21, R.id.btn22, R.id.btn23,
            R.id.btn24, R.id.btn25};
    private SwitchCompat setRandomSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_scrabble_lock);

        Intent intent = getIntent();
        db = (DataBase)intent.getSerializableExtra("DataBase");
        user = (User)intent.getSerializableExtra("User");

        // Clear the edittext field
        EditText editText = (EditText) findViewById(R.id.editTextTextPassword);
        editText.setText("");

        setRandomSwitch = (SwitchCompat)findViewById(R.id.setRandomSwitch);
        setRandomSwitch.setOnClickListener(this);

        enterButton = (Button)findViewById(R.id.enter_button);
        enterButton.setOnClickListener(this);

        // Create the randomized letter list
        letterList = generateLetters(user.getScrabblePassword());
        for(int i = 0; i < btn.length; i++){
            System.out.println(i);
            btn[i] = (Button) findViewById(btn_id[i]);
            btn[i].setBackgroundColor(Color.rgb(100, 207, 207));
            btn[i].setOnClickListener(this);
            btn[i].setText(String.valueOf(letterList.get(i)));
        }
    }

    @Override
    public void onClick(View v) {

        // First check if it was the random switch
        if(v.getId() == R.id.setRandomSwitch) {
            randomize = setRandomSwitch.isChecked();
            System.out.println("Randomize is " + randomize);
            letterList = generateLetters(user.getScrabblePassword());
            for(int i = 0; i < btn.length; i ++){
                btn[i].setText(String.valueOf(letterList.get(i)));
            }

            // Can just return since the rest of the actions have to do with buttons
            return;
        }

        // Find the button clicked
        Button clicked = (Button)findViewById(v.getId());
        EditText editText = (EditText) findViewById(R.id.editTextTextPassword);

        // Check if it is the first click, if so start a timer
        if(isFirstClick) {
            // Start timer
            startTime = System.currentTimeMillis();
            System.out.println("Starting Timer");
            isFirstClick = false;
        }

        // Check if it was the enter button
        if(v.getId() == R.id.enter_button) {
            // Stop timer
            endTime = System.currentTimeMillis();
            elapsedTime = ((double)(endTime - startTime)) / 1000;
            System.out.println(String.format("Ending timer...\nElapsed Time was: %.2f", elapsedTime));

            // Clear the edit text
            editText.setText("");

            // Check switch to see if password is being set or checked
            SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.setScrabblePassword);
            if(switchCompat.isChecked()) {
                // Set password mode
                user.setScrabblePassword(testPassword.toString());
                System.out.println("Setting " + user.getName() + "'s password to be " + testPassword);
                // TODO: Send new password to the database - may be able to put in User.setpassword()

            } else {
                // Check password mode
                // Test the data or set it as the password
                if(testPassword(testPassword.toString(), user.getScrabblePassword())) {
                    // Passwords matched
                    System.out.println("Passwords matched.");

                    // Display a popup
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(String.format("Password was Correct!\nElapsed Time: %.3f", elapsedTime));
                    builder.setCancelable(true);
                    AlertDialog alert = builder.create();
                    alert.show();

                    // TODO: Send attempt to the database

                } else {
                    // Passwords did not match
                    // Clear testpassword
                    testPassword.delete(0, testPassword.length());
                    System.out.println("Incorrect password. Try again");

                    // Reset all button colors
                    for(int i = 0; i < 26; i++) {
                        btn[i].setBackgroundColor(Color.rgb(100, 207, 207));
                        btn[i].setTextColor(Color.WHITE);
                    }
                    btn_unfocus = null;

                    // Display a popup
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(String.format("Incorrect Password\nElapsed Time: %.3f", elapsedTime));
                    builder.setCancelable(true);
                    AlertDialog alert = builder.create();
                    alert.show();

                    // TODO: Send data to the database

                    // Make the next click start the timer again
                    isFirstClick = true;
                }
            }
        } else {
            // Was one of the other buttons clicked
            // Set focus on the button clicked
            setFocus(btn_unfocus, clicked);

            // Add the character to the edittext string
            editText = (EditText) findViewById(R.id.editTextTextPassword);
            editText.append(clicked.getText());

            // Add the character to the testpassword for testing
            testPassword.append(clicked.getText());

            System.out.println("Current password: " + testPassword);
        }
    }

    private void setFocus(Button btn_unfocus, Button btn_focus){

        if(btn_unfocus != null) {
            btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
            btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        }

        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(3, 106, 150));

        this.btn_unfocus = btn_focus;
    }

    private List<Character> generateLetters(String password){
        ArrayList<Character> letters = new ArrayList<>();
        Random r = new Random();

        if(randomize) {
            for(int i = 0; i < 26; i++) {
                letters.add((char)('A' + i));
            }
            Collections.shuffle(letters);
            return letters;
        }

        // Not randomize
        for(int i = 0; i < 26; i++) {
            letters.add((char)('A' + i));
        }
        return letters;
    }

    // Tests the input password to check if it matches
    private boolean testPassword(String test, String actual) {
        return test.equals(actual);
    }
}
