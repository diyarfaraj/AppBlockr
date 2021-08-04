
package com.example.appblockr.broadcast;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.appblockr.ScreenBlocker;
import com.example.appblockr.shared.SharedPrefUtil;
import com.example.appblockr.utils.Utils;

import java.util.List;

public class ReceiverApplock extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Utils utils = new Utils(context);
        SharedPrefUtil prefUtil = new SharedPrefUtil(context);
        List<String> lockedApps = prefUtil.getLockedAppsList();
        String appRunning = utils.getLauncherTopApp();
        //String lastApp = utils.getLastApp();

        if (appRunning != null) {
            Log.d("APP RUNNINGG: ", appRunning);
        }

        if(lockedApps.contains(appRunning)){
            prefUtil.clearLastApp();
            prefUtil.setLastApp(appRunning);
            killThisPackageIfRunning(context, appRunning);
            Intent i = new Intent(context, ScreenBlocker.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("broadcast_receiver", "broadcast_receiver");
            context.startActivity(i);
        }
    }


    public static void killThisPackageIfRunning(final Context context, String packageName){
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(startMain);
        activityManager.killBackgroundProcesses(packageName);
    }
}
