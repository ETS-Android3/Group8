package com.example.SecurityApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import SecurityApp.R;

public class ScrabbleLock extends AppCompatActivity implements View.OnClickListener {
    private final Button[] btn = new Button[15];
    private List<Character> letterList;
    private Button btn_unfocus;
    private User user;
    private DataBase db;
    private final int[] btn_id = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9, R.id.btn10, R.id.btn11,
            R.id.btn0, R.id.btn13, R.id.btn14, R.id.btn15};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_scrabble_lock);

        Intent intent = getIntent();
        db = (DataBase) intent.getSerializableExtra("Database");
        user = (User) intent.getSerializableExtra("User");

        letterList = generateLetters("Password");

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
        //setForcus(btn_unfocus, (Button) findViewById(v.getId()));
        //Or use switch
        switch (v.getId()){
            case R.id.btn0:
                System.out.println(btn[0].getText());
                break;

            case R.id.btn1:
                setFocus(btn_unfocus, btn[1]);
                break;

            case R.id.btn2:
                setFocus(btn_unfocus, btn[2]);
                break;

            case R.id.btn3:
                setFocus(btn_unfocus, btn[3]);
                break;
        }
    }

    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
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
