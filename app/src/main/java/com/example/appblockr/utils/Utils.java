package com.example.appblockr.utils;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.List;

import static android.app.AppOpsManager.MODE_ALLOWED;

public class Utils {

    UsageStatsManager usageStatsManager;
    private final String EXTRA_LAST_APP = "EXTRA_LAST_APP";
    private final String LOCKED_APPS = "LOCKED_APPS";
    private final Context context;

  /*  public boolean isLock(String packageName){
        Log.d("IS LOCK: ", packageName);
        //return Paper.book().read(packageName) != null;
    }

    public void lock(String packageName){
        Log.d("LOCK: ", packageName);
        //Paper.book().write(packageName,packageName);
    }

    public void unLock(String packageName){
        Log.d("DELETE: ", packageName);
//        Paper.book().delete(packageName);
    }

    public void setLastApp(String packageName){
//        Paper.book().write(EXTRA_LAST_APP, packageName);
    }


    public String getLastApp(){
//        return Paper.book().read(EXTRA_LAST_APP);
    }

    public void clearLastApp(){
//        Paper.book().delete(EXTRA_LAST_APP);
    }*/

    public Utils(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean checkPermission(Context ctx) {
        AppOpsManager appOpsManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            appOpsManager = (AppOpsManager) ctx.getSystemService(Context.APP_OPS_SERVICE);
        }
        int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), ctx.getPackageName());
        return mode == MODE_ALLOWED;
    }

    public String getLauncherTopApp() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningTaskInfo> taskInfoList = manager.getRunningTasks(1);
            if (null != taskInfoList && !taskInfoList.isEmpty()) {
                return taskInfoList.get(0).topActivity.getPackageName();
            }
        } else {
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);

            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();
                }
            }
            if (!TextUtils.isEmpty(result))
                Log.d("RESULT", result);
            return result;
        }
        return "";
    }

}
