package com.example.appsift;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ScreenBlocker extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_screen_blocker);
        //setPassword();
    }


    private void setPassword(){
        Dialog mDialog;
        mDialog=new Dialog(ScreenBlocker.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.activity_screen_blocker);
        TextView ok,cancel;
        /*ok=(TextView) mDialog.findViewById(R.id.);
        cancel=(TextView) mDialog.findViewById(R.id.dialogno);*/
       /* ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                mDialog.cancel();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                mDialog.cancel();
            }
        });*/
        mDialog.show();
    }

    @Override
    protected void onPause() {
        String activityOnTop;
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        activityOnTop = ar.topActivity.getClassName();

        Log.d("ON PAUUSSE: ","onPause:-------------------------- "+   activityOnTop);
        super.onPause();
    }
}