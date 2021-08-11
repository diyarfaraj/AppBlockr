package com.example.appblockr.services;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.appblockr.broadcast.ReceiverApplock;

public class ServiceApplock extends IntentService {


    public ServiceApplock() {
        super("ServiceApplock");
    }

    private void runApplock() {
        long endTime = System.currentTimeMillis() + 210;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    Intent intent = new Intent(this, ReceiverApplock.class);
                    sendBroadcast(intent);
                    wait(endTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        runApplock();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        BackgroundManager.getInstance().init(this).startService();
        BackgroundManager.getInstance().init(this).startAlarmManager();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onDestroy() {
        BackgroundManager.getInstance().init(this).startService();
        BackgroundManager.getInstance().init(this).startAlarmManager();
        super.onDestroy();
    }
}
