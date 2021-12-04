package com.example.myfirstapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

//CREDITS
//https://github.com/aritraroy/PatternLockView
//https://stackoverflow.com/questions/32534076/what-is-the-best-way-to-do-a-button-group-that-can-be-selected-and-activate-inde/32545086
public class MainActivity extends AppCompatActivity {
    DataBase db;
    Button pattern_lock_button, scrabble_lock_button, add_user_button;
    Spinner user_spinner;
    EditText add_user_text;
    ArrayList user_names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        db = new DataBase();
        //Elements
        pattern_lock_button = (Button)findViewById(R.id.pattern_lock_button);
        scrabble_lock_button = (Button)findViewById(R.id.scrabble_lock_button);
        add_user_button = (Button)findViewById(R.id.add_user_button);
        user_spinner = findViewById(R.id.user_spinner);
        add_user_text = findViewById(R.id.add_user_text) ;


        user_names = new ArrayList<String>();
        ArrayList<User> users = db.getUsers();
        for(User user:users){
            user_names.add(user.getName());
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        user_spinner.setAdapter(adapter);
        adapter.addAll(user_names);
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
                if(!user_names.contains(s)) user_names.add(s);
                System.out.println(user_names);
                adapter.clear();
                adapter.addAll(user_names);
                User u = new User(s,users.size());
                users.add(u);
                db.setUsers(users);
            }
        });


    }
    /** Called when the user taps the Pattern Lock button */
    public void displayLockScreen(View view) {
      setContentView(view);
    }



}