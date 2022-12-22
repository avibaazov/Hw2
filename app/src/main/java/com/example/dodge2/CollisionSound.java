package com.example.dodge2;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

public class CollisionSound extends AsyncTask<Void, Void, Void> {
    private Context context;

    public CollisionSound(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MediaPlayer player = MediaPlayer.create(this.context, R.raw.car_crush);
        player.setLooping(false); // Set looping
        player.setVolume(1.0f, 1.0f);
        player.start();
        return null;
    }
}
