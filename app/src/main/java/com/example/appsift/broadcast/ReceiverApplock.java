
package com.example.appsift.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.appsift.PatternLockAct;
import com.example.appsift.shared.SharedPrefUtil;
import com.example.appsift.utils.Utils;

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

      /*  if(lastApp != null){
            Log.d("LAST APP: ", lastApp);
        }*/
// jämförrrrr namnen det enda som saknas
        if(lockedApps.contains(appRunning)){
            //if(!appRunning.equals(utils.getLastApp())){
                //utils.clearLastApp();
                //utils.setLastApp(appRunning);
                Intent i = new Intent(context, PatternLockAct.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("broadcast_receiver", "broadcast_receiver");
                context.startActivity(i);
         //   }
        }
    }
}
