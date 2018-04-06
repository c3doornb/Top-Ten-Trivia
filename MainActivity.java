package com.robotdestroyer.topten;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //Layout Objects
    private Button btnGeography;
    private Button btnHistory;
    private Button btnSports;
    private Button btnPop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    //Initializes all category buttons.
    // They will call startGame() and send to it the category as a String.
    private void init() {
        btnGeography = findViewById(R.id.btnGeography);

        btnGeography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(btnGeography.getText().toString());
            }
        });

        btnHistory = findViewById(R.id.btnHistory);

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(btnHistory.getText().toString());
            }
        });

        btnSports = findViewById(R.id.btnSports);

        btnSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(btnSports.getText().toString());
            }
        });

        btnPop = findViewById(R.id.btnPop);

        btnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(btnPop.getText().toString());
            }
        });
    }

    //Switches to the game activity and tells it which category is selected
    private void startGame(String category) {
        // Start NewActivity.class
        Intent myIntent = new Intent(MainActivity.this,
                GameActivity.class);
        myIntent.putExtra("Cat", category.toLowerCase());
        startActivity(myIntent);
    }
}
