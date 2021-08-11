package com.example.appblockr;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import mehdi.sakout.aboutpage.AboutPage;


public class About extends AppCompatActivity {
    int verCode;
    String versionName;
    TextView termsAndCondition;
    TextView privacyPolicy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addIconToBar();
        setTitle(" About");
        versionElement();
        String pp_string = getResources().getString(R.string.privacy_policy);
        String tm_string = getResources().getString(R.string.terms_conditions);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher_appblckr_icon)
                /* .addItem(versionElement)
                 .addItem(adsElement)*/
                .setDescription("AppBlockr \n Version: " + versionName + verCode)
                // .addItem(versionElement())
                .addGroup("Connect with us")
                .addEmail("robocora.apps@gmail.com")
                .addWebsite("https://appblockr.com/")
                /* .addFacebook("the.medy")
                .addTwitter("medyo80")
                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                .addPlayStore("com.ideashower.readitlater.pro")
                .addGitHub("medyo")
                .addInstagram("medyo80")*/
                .create();

        setContentView(R.layout.activity_about);
        LinearLayout linearLayout = findViewById(R.id.info);
        linearLayout.addView(aboutPage);

        termsAndCondition = findViewById(R.id.terms_conditions);
        privacyPolicy = findViewById(R.id.privacy_policy);

        termsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMessage(tm_string, "Terms & Conditions");
            }
        });
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMessage(pp_string, "Privacy Policy");
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_settings);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_locked_apps:
                        startActivity(new Intent(getApplicationContext(),
                                MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_all_apps:
                        startActivity(new Intent(getApplicationContext(),
                                ShowAllApps.class));
                        overridePendingTransition(0, 0);
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

    public void popupMessage(String message, String title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher_zz);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("internet", "Ok btn pressed");
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void addIconToBar() {
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_zz);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        setContentView(R.layout.activity_about);
    }

    boolean versionElement() {
        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            versionName = pInfo.versionName;
            verCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
}