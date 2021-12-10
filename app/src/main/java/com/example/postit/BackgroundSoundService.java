package com.example.postit;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BackgroundSoundService extends Service {

    MediaPlayer player = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this,R.raw.good_morning);
        player.setLooping(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        if(player.isPlaying()){
            player.stop();
            player.release();
        }
        super.onDestroy();
    }
}
