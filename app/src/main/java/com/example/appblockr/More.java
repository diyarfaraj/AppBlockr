package com.example.appblockr;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import mehdi.sakout.aboutpage.AboutPage;


public class More extends AppCompatActivity {
    int verCode;
    String versionName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addIconToBar();
        setTitle(" Settings");
        versionElement();
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher_appblckr_icon)
               /* .addItem(versionElement)
                .addItem(adsElement)*/
                .setDescription("AppBlockr \n Version: " + versionName + verCode)
               // .addItem(versionElement())

                .addGroup("Connect with us")
                .addEmail("elmehdi.sakout@gmail.com")
                .addWebsite("https://mehdisakout.com/")


               /* .addFacebook("the.medy")
                .addTwitter("medyo80")
                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                .addPlayStore("com.ideashower.readitlater.pro")
                .addGitHub("medyo")
                .addInstagram("medyo80")*/
                .create();

        setContentView(R.layout.activity_more);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.info);

        linearLayout.addView(aboutPage);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_settings);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_locked_apps:
                        startActivity(new Intent(getApplicationContext(),
                                MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_all_apps:
                        startActivity(new Intent(getApplicationContext(),
                                ShowAllApps.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_settings:
                  /*      startActivity(new Intent(getApplicationContext(),
                                More.class));
                        overridePendingTransition(0,0);*/
                        return true;
                }
                return false;
            }
        });
    }

    private void addIconToBar(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_zz);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_more);
    }

    boolean versionElement() {
        try {
            PackageInfo pInfo = getApplicationContext() .getPackageManager().getPackageInfo(getApplicationContext() .getPackageName(), 0);
            versionName = pInfo.versionName;
            verCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    return true;

    }
}