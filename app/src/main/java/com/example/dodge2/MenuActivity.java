package com.example.dodge2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;


public class MenuActivity extends AppCompatActivity {
    private Button modeBT;
    private Button modeSensor;
    private MaterialTextView highScore;
    private int topScore = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();


        modeBT.setOnClickListener(view -> {
            startBTActivity();
        });
        modeSensor.setOnClickListener(view -> {
            startSensorActivity();
        });
        SharedPreferences preferences = getSharedPreferences("score_prefs", MODE_PRIVATE);
        int score = preferences.getInt("score", 0);
        checkIfHighScore(score);
        highScore.setText("High Score: " + topScore);

    }


    private void checkIfHighScore(int score) {
        if (score > topScore) {

            topScore = score;


        }

    }


    private void startSensorActivity() {
        Intent intent = new Intent(this, SensorActivity.class);
        startActivity(intent);
    }

    private void startBTActivity() {
        Intent intent = new Intent(this, ButtonActivity.class);
        startActivity(intent);
    }

    private void findViews() {
        modeBT = findViewById(R.id.button2);
        modeSensor = findViewById(R.id.button);
        highScore = findViewById(R.id.textView2);
    }

}
