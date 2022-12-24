package com.example.dodge2;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ButtonActivity extends AppCompatActivity {

    private MaterialButton left_BTN;
    private MaterialButton right_BTN;
    private ShapeableImageView carPlacement0;//first to the left
    private ShapeableImageView carPlacement1;
    private ShapeableImageView carPlacement2;//middle
    private ShapeableImageView carPlacement3;
    private ShapeableImageView carPlacement4;//last to the right

    final int DELAY = 1000;
    private Timer timer = new Timer();
    private ShapeableImageView[][] myImages;
    private ShapeableImageView[] game_IMG_hearts;
    boolean gameOver = false;
    private CollisionSound collisionSound;
    ImageView[] cars;
    private MaterialTextView score;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        startTimer();
        left_BTN.setOnClickListener(view -> {
            moveCar(-1);
        });
        right_BTN.setOnClickListener(view -> {
            moveCar(1);
        });

    }






    int currentLane = 2;//middle start of game
    long startTime = 0;

    private void moveCar(int direction) {
        if (direction == 1) {
            if (currentLane < 4) {
                cars[currentLane].setVisibility(View.INVISIBLE);
                currentLane++;
                cars[currentLane].setVisibility(View.VISIBLE);
            }
        }
        if (direction == -1) {
            if (currentLane > 0) {
                cars[currentLane].setVisibility(View.INVISIBLE);
                currentLane--;
                cars[currentLane].setVisibility(View.VISIBLE);

            }
        }


    }

    private void startTimer() {


        SharedPreferences preferences = getSharedPreferences("score_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        final int[] i = {0};
            startTime = System.currentTimeMillis();
            timer = new Timer();
            timer.scheduleAtFixedRate(
                    new TimerTask() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(gameOver==false) {
                                        randomlyAddMeteor();
                                        moveMeteor();
                                        checkCollision();
                                       score.setText("Score: " + i[0]);
                                        i[0]++;


                                    }
                                    if(gameOver==true){
                                        timer.cancel();
                                        timer.purge();

                                        editor.apply();
                                        editor.putInt("score", i[0]);
                                        editor.apply();
                                        finish();

                                    }



                                }
                            });
                        }
                    }
                    , 1500, 1500);
        }


    int strikeCount = 0;


    private void checkCollision() {

        if (this.myImages[3][0].getVisibility() == View.VISIBLE && carPlacement0.getVisibility() == View.VISIBLE) {
            toast("COLLISION!");
            executeCollisionSound();

            //vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            strikeCount++;
            removeMeteor(myImages[3][0]);

        }
        if (this.myImages[3][1].getVisibility() == View.VISIBLE && carPlacement1.getVisibility() == View.VISIBLE) {
            toast("COLLISION!");
            // vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

            executeCollisionSound();

            strikeCount++;
            removeMeteor(myImages[3][1]);

        }
        if (this.myImages[3][2].getVisibility() == View.VISIBLE && carPlacement2.getVisibility() == View.VISIBLE) {
            toast("COLLISION!");
            // vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            executeCollisionSound();
            strikeCount++;

            removeMeteor(myImages[3][2]);


        }
        if (this.myImages[3][3].getVisibility() == View.VISIBLE && carPlacement3.getVisibility() == View.VISIBLE) {
            toast("COLLISION!");
            // vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            executeCollisionSound();
            strikeCount++;

            removeMeteor(myImages[3][3]);
        }
        if (this.myImages[3][4].getVisibility() == View.VISIBLE && carPlacement4.getVisibility() == View.VISIBLE) {
            toast("COLLISION!");
            // vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
           executeCollisionSound();

            strikeCount++;

            removeMeteor(myImages[3][4]);
        }
        if (strikeCount > 0 && strikeCount < 4) {
            game_IMG_hearts[game_IMG_hearts.length - strikeCount].setVisibility(View.INVISIBLE);
        }
        if(strikeCount == 3){
            gameOver = true;
            toast("GAME OVER");
            timer.cancel();
        }


    }
    private void executeCollisionSound() {

            collisionSound = new CollisionSound(this);

        collisionSound.execute();
    }
    void removeMeteor(ShapeableImageView img) {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                img.setVisibility(View.INVISIBLE);
            }
        }, 300);

    }

    private void toast(String str) {

        Toast
                .makeText(this, str, Toast.LENGTH_LONG)
                .show();
    }

    private void randomlyAddMeteor() {
        Random rand = new Random();
        int n = rand.nextInt(5); final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                myImages[0][n].setVisibility(View.VISIBLE);
            }
        }, 300);




    }



    private void moveMeteor() {
        boolean  flags[][] = new boolean[myImages.length][myImages[0].length];
        for (int i = 0; i < myImages.length; i++) {
            for (int j = 0; j < myImages[i].length; j++) {
                if (this.myImages[i][j].getVisibility() == View.VISIBLE && i < 3&&flags[i][j]==false) {
                    this.myImages[i + 1][j].setVisibility(View.VISIBLE);
                    this.myImages[i][j].setVisibility(View.INVISIBLE);
                    flags[i+1][j] = true;
                } else if (i == 3) {
                    removeMeteor(myImages[i][j]);
                }
            }
        }
    }


    private void findViews() {
        initViewArray(myImages);
        game_IMG_hearts = new ShapeableImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),

        };
        left_BTN = findViewById(R.id.left_BTN);
        right_BTN = findViewById(R.id.right_BTN);
        carPlacement0 = findViewById(R.id.car1);
        carPlacement1 = findViewById(R.id.car2);
        carPlacement2 = findViewById(R.id.car3);
        carPlacement3 = findViewById(R.id.car4);
        carPlacement4 = findViewById(R.id.car5);
        cars = new ImageView[]{carPlacement0, carPlacement1, carPlacement2, carPlacement3, carPlacement4};
        //set invisible

        carPlacement0.setVisibility(View.INVISIBLE);
        carPlacement1.setVisibility(View.INVISIBLE);
        carPlacement3.setVisibility(View.INVISIBLE);
        carPlacement4.setVisibility(View.INVISIBLE);
//        asteroid1 = findViewById(R.id.m1);
//        asteroid2 = findViewById(R.id.m2);
//        asteroid3 = findViewById(R.id.m3);
        score= findViewById(R.id.game_LBL_score);
    }

    private void initViewArray(ShapeableImageView[][] myImages) {

        this.myImages = new ShapeableImageView[4][5];
        this.myImages[0][0] = findViewById(R.id.m1);
        this.myImages[0][1] = findViewById(R.id.m2);
        this.myImages[0][2] = findViewById(R.id.m3);
        this.myImages[0][3] = findViewById(R.id.m4);
        this.myImages[0][4] = findViewById(R.id.m5);
        this.myImages[1][0] = findViewById(R.id.m6);
        this.myImages[1][1] = findViewById(R.id.m7);
        this.myImages[1][2] = findViewById(R.id.m8);
        this.myImages[1][3] = findViewById(R.id.m9);
        this.myImages[1][4] = findViewById(R.id.m10);
        this.myImages[2][0] = findViewById(R.id.m11);
        this.myImages[2][1] = findViewById(R.id.m12);
        this.myImages[2][2] = findViewById(R.id.m13);
        this.myImages[2][3] = findViewById(R.id.m14);
        this.myImages[2][4] = findViewById(R.id.m15);
        this.myImages[3][0] = findViewById(R.id.m16);
        this.myImages[3][1] = findViewById(R.id.m17);
        this.myImages[3][2] = findViewById(R.id.m18);
        this.myImages[3][3] = findViewById(R.id.m19);
        this.myImages[3][4] = findViewById(R.id.m20);

        setMeteorsInvisible();


    }

    private void setMeteorsInvisible() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                this.myImages[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }
}

