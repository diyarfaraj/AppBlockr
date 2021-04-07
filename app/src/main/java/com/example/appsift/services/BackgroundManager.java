package com.example.appsift.services;

import android.app.ActivityManager;
import android.content.Context;

public class BackgroundManager {
    private static final int period = 15*1000;
    private static BackgroundManager instance;
    private Context context;

    public static BackgroundManager getInstance(){
        if(instance == null) {
            instance = new BackgroundManager();
        }
        return instance;
    }

    public BackgroundManager init(Context ctx){
        context = ctx;
        return  this;
    }

    public boolean isServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo: manager.getRunningServices(Integer.MAX_VALUE)){

        }
    }

}
