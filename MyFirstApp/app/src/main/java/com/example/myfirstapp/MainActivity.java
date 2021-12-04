package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

//CREDITS
//https://github.com/aritraroy/PatternLockView
//https://stackoverflow.com/questions/32534076/what-is-the-best-way-to-do-a-button-group-that-can-be-selected-and-activate-inde/32545086
public class MainActivity extends AppCompatActivity {

    Button pattern_lock_button, scrabble_lock_button, add_user_button;
    Spinner user_spinner;
    EditText add_user_text;
    ArrayList users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Elements
        pattern_lock_button = (Button)findViewById(R.id.pattern_lock_button);
        scrabble_lock_button = (Button)findViewById(R.id.scrabble_lock_button);
        add_user_button = (Button)findViewById(R.id.add_user_button);
        user_spinner = findViewById(R.id.user_spinner);
        add_user_text = findViewById(R.id.add_user_text) ;


        users = new ArrayList<String>();
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_spinner.setAdapter(adapter);
        adapter.addAll(users);
        // Button Listeners
        pattern_lock_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DisplayPatternLock.class));
            }
        });
        scrabble_lock_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, display_scrabble_lock.class));
            }
        });
        add_user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = add_user_text.getText().toString();
                if(!users.contains(s)) users.add(s);
                System.out.println(users);
                adapter.clear();
                adapter.addAll(users);

            }
        });


    }
    /** Called when the user taps the Pattern Lock button */
    public void displayLockScreen(View view) {
      setContentView(view);
    }



}