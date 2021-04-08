package com.example.appsift.services;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.appsift.broadcast.RestartServiceWhenStopped;

public class BackgroundManager {
    private static final int period = 15*1000;
    private static final int ALARM_ID = 159874;
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
            if(serviceClass.getName().equals(serviceInfo.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    public void startService(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            if(!isServiceRunning(ServiceApplockJobIntent.class)){
                Intent intent = new Intent(context,ServiceApplockJobIntent.class);
                ServiceApplockJobIntent.enqueueWork(context,intent);
            }
        } else {
            if(!isServiceRunning(ServiceApplock.class)){
                context.startService(new Intent(context,ServiceApplock.class));
            }
        }
    }

    public void stopService(Class<?> serviceClass){
        if(isServiceRunning(serviceClass)){
            context.stopService(new Intent(context, serviceClass));
        }
    }

     public void startAlarmManager(){
        Intent intent = new Intent(context, RestartServiceWhenStopped.class);
         PendingIntent pendingIntent = PendingIntent.getBroadcast(context,ALARM_ID, intent,0);
         AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
         manager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+period,pendingIntent);
     }

     public void stopAlarm(){
         Intent intent = new Intent(context, RestartServiceWhenStopped.class);
         PendingIntent pendingIntent = PendingIntent.getBroadcast(context,ALARM_ID, intent,0);
         AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
         manager.cancel(pendingIntent);
     }

}
