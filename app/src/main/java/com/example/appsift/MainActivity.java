package com.example.appsift;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.appsift.adapter.AllAppAdapter;
import com.example.appsift.adapter.LockedAppAdapter;
import com.example.appsift.fragments.AllAppsFragment;
import com.example.appsift.fragments.LockedAppsFragment;
import com.example.appsift.fragments.SettingsFragment;
import com.example.appsift.model.AppModel;
import com.example.appsift.services.BackgroundManager;
import com.example.appsift.shared.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button permissionBtn, passwordBtn, lockedAppBtn;
    ImageView showAllAppsBtn;
    String password;
    static final String KEY = "pass";
    MeowBottomNavigation bottomNavigation;
    List<AppModel> allInstalledApps = new ArrayList<>();
    static List<AppModel> lockedAppsList = new ArrayList<>();
    List<String> prefAppList;
    static Context context;
    LockedAppAdapter lockedAppsAdapter = new LockedAppAdapter(lockedAppsList,context);
    AllAppAdapter installedAppsAdapter = new AllAppAdapter(allInstalledApps,context);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BackgroundManager.getInstance().init(this).startService();
        password = SharedPrefUtil.getInstance(this).getString(KEY);
        final Context context = this;
        accessPermission();
        overlayPermission();
        getInstalledApps();
        getLockedApps(context);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(0, R.drawable.ic_baseline_format_list));
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.locked_icon));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_settings_));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener(){
            @Override
            public void onShowItem(MeowBottomNavigation.Model item){
                Fragment fragment = null;
                switch (item.getId()){
                    case 0:
                        fragment = new AllAppsFragment(context,allInstalledApps);
                        loadFragment(fragment);
                        break;
                    case 1:
                        fragment = new LockedAppsFragment(context, lockedAppsList);
                        loadFragment(fragment);
                        break;
                    case 2:
                        fragment = new SettingsFragment();
                        break;
                }
                loadFragment(fragment);
            }
        });

        updateLockedAppsNotification(/*bottomNavigation*/);
        bottomNavigation.show(1, true);
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //Toast.makeText(getApplicationContext(),"you clicked " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                //Toast.makeText(getApplicationContext(),"you  reee clicked " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getInstalledApps() {
        List<String> prefLockedAppList = SharedPrefUtil.getInstance(this).getLockedAppsList();
        /*List<ApplicationInfo> packageInfos = getPackageManager().getInstalledApplications(0);*/
        PackageManager pk = getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = pk.queryIntentActivities(intent, 0);
        for(ResolveInfo resolveInfo: resolveInfoList){
            ActivityInfo activityInfo = resolveInfo.activityInfo;
            String name = activityInfo.loadLabel(getPackageManager()).toString();
            Drawable icon = activityInfo.loadIcon(getPackageManager());
            String packageName = activityInfo.packageName;
            if(!packageName.equalsIgnoreCase("com.android.settings")){
                if(!prefLockedAppList.isEmpty()){
                    //check if apps is locked
                    if(prefLockedAppList.contains(packageName)){
                        allInstalledApps.add(new AppModel(name,icon, 1, packageName));
                    } else {
                        allInstalledApps.add(new AppModel(name,icon, 0, packageName));
                    }
                } else {
                    allInstalledApps.add(new AppModel(name, icon, 0, packageName));
                }
            } else {
                //do not add settings to app list
            }

        }
        installedAppsAdapter.notifyDataSetChanged();
        lockedAppsAdapter.notifyDataSetChanged();

        //progressDialog.dismiss();
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
        //progressDialog.dismiss();
    }
    @Override
    protected void onResume() {
        updateLockedAppsNotification(/*bottomNavigation*/);
        super.onResume();
    }

    public void updateLockedAppsNotification(/*MeowBottomNavigation bottomNavigation)*/ ){

        Integer lockedAppsNr;
        List<String> prefAppListnofication;
        prefAppListnofication = SharedPrefUtil.getInstance(this).getLockedAppsList();
        lockedAppsNr = prefAppListnofication.size();
        bottomNavigation.setCount(1,lockedAppsNr.toString());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment,fragment)
                .commit();
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