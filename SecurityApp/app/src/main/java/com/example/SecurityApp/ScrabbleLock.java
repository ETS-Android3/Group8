package com.example.SecurityApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import SecurityApp.R;

public class ScrabbleLock extends AppCompatActivity implements View.OnClickListener {
    // Properties
    private User currentUser;
    private DataBase db;

    private final Button[] btn = new Button[26];
    private List<Character> letterList;
    StringBuilder testPassword;
    private Button btn_unfocus;
    private final int[] btn_id = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9, R.id.btn10, R.id.btn11,
            R.id.btn12, R.id.btn13, R.id.btn14, R.id.btn15,
            R.id.btn16, R.id.btn17, R.id.btn18, R.id.btn19,
            R.id.btn20, R.id.btn21, R.id.btn22, R.id.btn23, R.id.btn24, R.id.btn25};
    private boolean isFirstClick = true;
    private boolean randomize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_scrabble_lock);
        letterList = generateLetters("Password");

        for(int i = 0; i < btn.length; i++){
            System.out.println(i);
            btn[i] = (Button) findViewById(btn_id[i]);
            btn[i].setBackgroundColor(Color.rgb(100, 207, 207));
            btn[i].setOnClickListener(this);
            btn[i].setText(String.valueOf(letterList.get(i)));
        }

        // Clear the edittext field
        EditText editText = (EditText) findViewById(R.id.editTextTextPassword);
        editText.setText("");
    }
    @Override
    public void onClick(View v) {
        // Find the button clicked
        Button clicked = (Button)findViewById(v.getId());
        EditText editText = (EditText) findViewById(R.id.editTextTextPassword);

        // Check if it was the enter button
        if(clicked.getText().equals("Enter")) {
            // TODO: Stop timer

            // Check switch to see if password is being set or checked
            SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.setScrabblePassword);
            if(switchCompat.isChecked()) {
                // Set password mode
                currentUser.setScrabblePassword(testPassword.toString());
            } else {
                // Check password mode
                // Test the data or set it as the password
                if(testPassword(testPassword.toString(), currentUser.getScrabblePassword())) {
                    // Passwords matched
                    // TODO: Figure out what to add like a thumbs up or smth
                } else {
                    // Passwords did not match
                    // Clear testpassword and the edittext
                    testPassword.delete(0, testPassword.length());
                    editText.setText("");
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
        Character[] letters = new Character[15];
        Random r = new Random();

        for(int i = 0; i < password.length(); i++){
            letters[i] = password.charAt(i);
        }
        for(int i = password.length(); i < 15; i++){
            letters[i] = (char)(r.nextInt(26) + 'a');
        }
        List<Character> letterList = Arrays.asList(letters);
        Collections.shuffle(letterList);
        return letterList;
    }

    /**
     * Tests the input password to check if it matches
     *
     * @param test the input password we want to test against
     * @param actual the actual password
     * @return true if the passwords match, false if not
     */
    private boolean testPassword(String test, String actual) {
        return test.equals(actual);
    }
}
