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
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import SecurityApp.R;

public class ScrabbleLock extends AppCompatActivity implements View.OnClickListener {
    private final Button[] btn = new Button[26];
    private SwitchCompat setRandomSwitch;
    private List<Character> letterList;
    private long startTime, endTime;
    private double elapsedTime;
    private boolean isFirstClick = true;
    private Button btn_unfocus;
    private DataBase db;
    private StringBuilder testPassword = new StringBuilder();
    private int uid;
    private boolean randomize;
    private Button enterButton;
    private SwitchCompat setScrabblePassword;
    private EditText editText;
    private Gson gson;
    private String json;
    private Button startTest;
    private TextView testNo;
    private int testNumber;
    private ArrayList<String> testPasswords;
    private final int[] btn_id = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9, R.id.btn10, R.id.btn11,
            R.id.btn12, R.id.btn13, R.id.btn14, R.id.btn15,
            R.id.btn16, R.id.btn17, R.id.btn18, R.id.btn19,
            R.id.btn20, R.id.btn21, R.id.btn22, R.id.btn23,
            R.id.btn24, R.id.btn25};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_scrabble_lock);
        gson = new Gson();
        Intent intent = getIntent();
        json = intent.getStringExtra("Database");
        db = gson.fromJson(json, DataBase.class);
        uid = intent.getIntExtra("UID", 0);
        randomize = false;
        //Testing Buttons
        testNo = (TextView) findViewById(R.id.testNo);
        testNo.setText("Press to Begin");
        startTest = (Button) findViewById(R.id.startTest);
        startTest.setOnClickListener(this);
        testPasswords = new ArrayList<String>();
        //Unscrambled
        testPasswords.add("SECUR");
        testPasswords.add("UPDOG");
        testPasswords.add("SHESH");
        testPasswords.add("CRYPT");
        testPasswords.add("DRSNG");
        //Scrambled
        testPasswords.add("MDFVE");
        testPasswords.add("SIXTY");
        testPasswords.add("NIGHN");
        testPasswords.add("PASSS");
        testPasswords.add("WORDS");
        // Clear the edittext field
        editText = (EditText) findViewById(R.id.editTextTextPassword);
        editText.setText("");
        testNumber = 0;

        letterList = generateLetters(db.getScrabblePasswordById(uid));

        setRandomSwitch = (SwitchCompat) findViewById(R.id.setRandomSwitch);
        setRandomSwitch.setOnClickListener(this);

        enterButton = (Button)findViewById(R.id.enter_button);
        enterButton.setOnClickListener(this);
        setScrabblePassword = (SwitchCompat) findViewById(R.id.setScrabblePassword);

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
        if (v.getId() == R.id.setRandomSwitch) {
            randomize = setRandomSwitch.isChecked();
            System.out.println("Randomize is " + randomize);
            letterList = generateLetters(db.getScrabblePasswordById(uid));
            for (int i = 0; i < btn.length; i++) {
                btn[i].setText(String.valueOf(letterList.get(i)));
            }

            // Can just return since the rest of the actions have to do with buttons
            return;
        }

        // Find the button clicked
        Button clicked = (Button) findViewById(v.getId());
        EditText editText = (EditText) findViewById(R.id.editTextTextPassword);

        // Check if it is the first click, if so start a timer
        if (isFirstClick) {
            // Start timer
            startTime = System.currentTimeMillis();
            System.out.println("Starting Timer");
            isFirstClick = false;
        }

        // Check if it was the enter button
        if (v.getId() == R.id.enter_button) {
            // Stop timer
            endTime = System.currentTimeMillis();
            elapsedTime = ((double) (endTime - startTime)) / 1000;
            System.out.println(String.format("Ending timer...\nElapsed Time was: %.2f", elapsedTime));

            // Clear the edit text
            editText.setText("");


            // Check switch to see if password is being set or checked

            if (setScrabblePassword.isChecked()) {
                // Set password mode
                db.setScrabblePasswordById(uid,testPassword.toString());
                System.out.println("Setting " + db.getUsers().get(uid).getName() + "'s password to be " + testPassword);
                testPassword = new StringBuilder();
                isFirstClick=true;
            } else {
                // Check password mode
                //Set the result
                Boolean result = false;
                // Test the data or set it as the password
                if (testPassword(testPassword.toString(), db.getScrabblePasswordById(uid))) {
                    // Passwords matched
                    System.out.println("Passwords matched.");
                    //Set result to show passwords matched
                    result = true;

                    // Display a popup
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(String.format("Password was Correct!\nElapsed Time: %.3f", elapsedTime));
                    builder.setCancelable(true);
                    AlertDialog alert = builder.create();
                    alert.show();
                    isFirstClick=true;

                } else {
                    // Passwords did not matc
                    System.out.println("Incorrect password. Try again");

                    // Reset all button colors
                    for (int i = 0; i < 26; i++) {
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

                    // Make the next click start the timer again
                    isFirstClick = true;
                }
                db.newAttempt(uid,elapsedTime,"Scrabble",result,testPassword.toString(), 0, randomize);
                testPassword.delete(0, testPassword.length());
            }
        }
        else if(v.getId() == R.id.startTest){
            nextTest();
            isFirstClick = true;
        }
        else {
            // Was one of the other buttons clicked
            // Set focus on the button clicked
            //setFocus(btn_unfocus, clicked);

            // Add the character to the edittext string
            editText.append(clicked.getText());

            // Add the character to the testpassword for testing
            testPassword.append(clicked.getText());

            System.out.println("Current password: " + testPassword);
        }
    }
    private void nextTest(){
        testNumber++;
        System.out.println("Next Test");
        //Unscrambled
        if(testNumber <= 5){
            testNo.setText("Unscrambled Test #: " + testNumber);
            if(setRandomSwitch.isChecked()) setRandomSwitch.performClick();
        }
        //Scrambled
        if(testNumber > 5){
            testNo.setText("Unscrambled Test #: " + testNumber);
            if(!setRandomSwitch.isChecked())setRandomSwitch.performClick();
        }
        //Reset Test
        if(testNumber > 10){
            testNumber = 0;
            testNo.setText("Tests Complete! Press to restart.");
            return;
        }
        db.setScrabblePasswordById(uid, testPasswords.get(testNumber-1));

    }
    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.btn_unfocus = btn_focus;
    }

    private ArrayList<Character> generateLetters(String password){
        ArrayList<Character> letters = new ArrayList<Character>();

        if(randomize){

            for(int i = 0; i < 26; i++){
                letters.add((char)('A' + i));
            }
            Collections.shuffle(letters);
            return letters;
        }
        for(int i = 0; i < 26; i++){
            letters.add((char)('A' + i));
        }
        return letters;
    }
    // Tests the input password to check if it matches
    private boolean testPassword(String test, String actual) {
        return test.equals(actual);
    }

}
