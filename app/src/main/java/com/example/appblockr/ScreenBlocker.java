package com.example.appblockr;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appblockr.shared.SharedPrefUtil;

public class ScreenBlocker extends AppCompatActivity {
    Button close_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_screen_blocker);
        initIconApp();

        close_btn = findViewById(R.id.close_block_screen_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initIconApp() {
        if (getIntent().getStringExtra("broadcast_receiver") != null) {
            ImageView icon = findViewById(R.id.app_icon);
            TextView blockInfo = findViewById(R.id.empty_blocked_list_text);
            String current_app = new SharedPrefUtil(this).getLastApp();
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = getPackageManager().getApplicationInfo(current_app, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            icon.setImageDrawable(applicationInfo.loadIcon(getPackageManager()));
            blockInfo.setText(applicationInfo.loadLabel(getPackageManager()).toString().toUpperCase() + " is blocked by AppBlockr. Do something productive.");
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
       /* Intent intent = new Intent(this, ScreenBlocker.class);
        startActivity(intent);*/
        /* ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}