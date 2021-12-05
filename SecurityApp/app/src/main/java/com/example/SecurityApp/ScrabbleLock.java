package com.example.SecurityApp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import SecurityApp.R;

public class ScrabbleLock extends AppCompatActivity implements View.OnClickListener {
    // Properties
    private final Button[] btn = new Button[15];
    private List<Character> letterList;
    private List<Character> testPassword;
    private Button btn_unfocus;
    private final int[] btn_id = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9, R.id.btn10, R.id.btn11,
            R.id.btn0, R.id.btn13, R.id.btn14, R.id.btn15};

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
        // Set focus on the button clicked
        Button clicked = (Button)findViewById(v.getId());
        setFocus(btn_unfocus, (Button)findViewById(v.getId()));

        // Add the character to the edittext string
        EditText editText = (EditText) findViewById(R.id.editTextTextPassword);
        editText.append(clicked.getText());

        // TODO: Add the character clicked to the letters list/check if it belongs
        // TODO: Check why the bottom right and left buttons dont work
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
}
