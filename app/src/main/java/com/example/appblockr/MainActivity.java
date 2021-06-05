package com.example.appblockr;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appblockr.adapter.AllAppAdapter;
import com.example.appblockr.adapter.LockedAppAdapter;
import com.example.appblockr.model.AppModel;
import com.example.appblockr.services.BackgroundManager;
import com.example.appblockr.shared.SharedPrefUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<AppModel> allInstalledApps = new ArrayList<>();
    static List<AppModel> lockedAppsList = new ArrayList<>();
    static Context context;
    LockedAppAdapter lockedAppsAdapter = new LockedAppAdapter(lockedAppsList,context);
    AllAppAdapter installedAppsAdapter = new AllAppAdapter(allInstalledApps,context);
    RecyclerView recyclerView;
    LockedAppAdapter adapter;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BackgroundManager.getInstance().init(this).startService();
        progressDialog = new ProgressDialog(this);
        final Context context = this;
        accessPermission();
        overlayPermission();
        getLockedApps(context);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_locked_apps);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_locked_apps:
                        return true;
                    case R.id.nav_all_apps:
                        startActivity(new Intent(getApplicationContext(),
                                ShowAllApps.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_settings:
                        startActivity(new Intent(getApplicationContext(),
                                ScreenBlocker.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.lockedAppsListt);
        adapter = new LockedAppAdapter(lockedAppsList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                getLockedApps(context);
            }
        });

    }

    public void getLockedApps(Context ctx) {
        List<String> prefAppList = SharedPrefUtil.getInstance(ctx).getLockedAppsList();
        List<ApplicationInfo> packageInfos = ctx.getPackageManager().getInstalledApplications(0);
        lockedAppsList.clear();
        for (int i = 0; i < packageInfos.size(); i++) {
            if(packageInfos.get(i).icon > 0) {
                String name = packageInfos.get(i).loadLabel(ctx.getPackageManager()).toString();
                Drawable icon = packageInfos.get(i).loadIcon(ctx.getPackageManager());
                String packageName = packageInfos.get(i).packageName;
                if(prefAppList.contains(packageName)){
                        lockedAppsList.add(new AppModel(name,icon, 1, packageName));
                } else {
                    continue;
                }

            }

        }
        installedAppsAdapter.notifyDataSetChanged();
        lockedAppsAdapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.setTitle("Fetching Apps");
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            }
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void accessPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isAccessGranted()) {
                new AlertDialog.Builder(this)
                        .setTitle("Usage Access Permission")
                        .setMessage("This app needs your permission to see your usage information")
                        .setPositiveButton("Open settings", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                                startActivityForResult(intent, 102);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        }
    }

    public void overlayPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission Request")
                        .setMessage("This app needs your permission to overlay the system apps")
                        .setPositiveButton("Open settings", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                startActivityForResult(myIntent, 101);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
            }
        }
    }


}