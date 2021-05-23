package com.example.appsift;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
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
    static List<AppModel> lockedAppsList = new ArrayList<>();
    List<String> prefAppList;
    Integer lockedApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BackgroundManager.getInstance().init(this).startService();
        password = SharedPrefUtil.getInstance(this).getString(KEY);
        final Context context = this;
        overlayPermission();
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
                        fragment = new AllAppsFragment();
                        break;
                    case 1:
                        fragment = new LockedAppsFragment(context, lockedAppsList);
                        break;
                    case 2:
                        fragment = new SettingsFragment();
                        break;
                }
                loadFragment(fragment);

            }
        });

        updateLockedAppsNotification(bottomNavigation);
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

        showAllAppsBtn = findViewById(R.id.all_apps_button_img);
        showAllAppsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isAccessGranted()){
                    if(!password.isEmpty()){
                        startActivity(new Intent(MainActivity.this, ShowAllApps.class ));
                    } else {
                        Toast.makeText(context, "Please set a password first", Toast.LENGTH_LONG).show();
                    }
                } else  {
                    Toast.makeText(MainActivity.this, "Please allow app usage permission", Toast.LENGTH_LONG).show();
                }
            }
        });

        permissionBtn = findViewById(R.id.permit_btn);
        permissionBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
            }
        });

        passwordBtn = findViewById(R.id.set_password_btn);
        if(password.isEmpty()){
            passwordBtn.setText("Set Password");
        } else {
            passwordBtn.setText("Update Password");
        }

        passwordBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(password.isEmpty()){
                    setPassword(context);
                } else {
                    updatePassword(context);
                }

            }
        });

        lockedAppBtn = findViewById(R.id.lockedAppsBtn);
        lockedAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAccessGranted()){
                    if(!password.isEmpty()){
                        startActivity(new Intent(MainActivity.this, LockedApps.class ));
                    } else {
                        Toast.makeText(context, "Please set a password first", Toast.LENGTH_LONG).show();
                    }
                } else  {
                    Toast.makeText(MainActivity.this, "Please allow app usage permission", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getLockedApps(Context ctx) {
        List<String> prefAppList = SharedPrefUtil.getInstance(ctx).getLockedAppsList();
        List<ApplicationInfo> packageInfos = ctx.getPackageManager().getInstalledApplications(0);

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
        //adapter.notifyDataSetChanged();
//        progressDialog.dismiss();
    }
    @Override
    protected void onResume() {
        updateLockedAppsNotification(bottomNavigation);
        super.onResume();
    }

    private void updateLockedAppsNotification(MeowBottomNavigation bottomNavigation) {
        prefAppList = SharedPrefUtil.getInstance(this).getLockedAppsList();
        lockedApps = prefAppList.size();
        bottomNavigation.setCount(1,lockedApps.toString());
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment,fragment)
                .commit();
    }

    private void setPassword(Context ctx){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView t1 = new TextView(ctx);
        t1.setText("Enter your password");
        linearLayout.addView(t1);
        EditText input = new EditText(ctx);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        linearLayout.addView(input);
        dialog.setView(linearLayout);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPrefUtil.getInstance(ctx).putString(KEY, input.getText().toString());
                password = input.getText().toString();
                passwordBtn.setText("Update Password");
                Toast.makeText(ctx, "Password set successfully", Toast.LENGTH_LONG).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updatePassword(Context ctx){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView oldPass = new TextView(ctx);
        oldPass.setText("Enter old password");
        linearLayout.addView(oldPass);
        EditText input = new EditText(ctx);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        linearLayout.addView(input);

        TextView newPass = new TextView(ctx);
        newPass.setText("Enter new password");
        linearLayout.addView(newPass);
        EditText input2 = new EditText(ctx);
        input2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        linearLayout.addView(input2);

        dialog.setView(linearLayout);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(password.equals(input.getText().toString())){
                    SharedPrefUtil.getInstance(ctx).putString(KEY, input2.getText().toString());
                    password = input2.getText().toString();
                    passwordBtn.setText("Update Password");
                    Toast.makeText(ctx, "Password updated successfully", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ctx, "Sorry old password was incorrect", Toast.LENGTH_LONG).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

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