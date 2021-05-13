package com.example.appsift;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appsift.adapter.AppAdapter;
import com.example.appsift.model.AppModel;
import com.example.appsift.shared.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

public class ShowAllApps extends AppCompatActivity {
    RecyclerView recyclerView;
    List<AppModel> apps = new ArrayList<>();
    AppAdapter adapter;
    ProgressDialog progressDialog;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_apps);

        recyclerView = findViewById(R.id.recycleview);
        adapter = new AppAdapter(apps, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                getInstalledApps();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.setTitle("Fetching Apps");
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    public void getInstalledApps() {
        List<String> prefAppList = SharedPrefUtil.getInstance(this).getLockedAppsList();
        List<ApplicationInfo> packageInfos = getPackageManager().getInstalledApplications(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            if(packageInfos.get(i).icon > 0) {
                String name = packageInfos.get(i).loadLabel(getPackageManager()).toString();
                Drawable icon = packageInfos.get(i).loadIcon(getPackageManager());
                String packageName = packageInfos.get(i).packageName;

                if(!prefAppList.isEmpty()){
                    //check if apps is locked
                    if(prefAppList.contains(packageName)){
                        apps.add(new AppModel(name,icon, 1, packageName));
                    } else {
                        apps.add(new AppModel(name,icon, 0, packageName));
                    }
                } else {
                    apps.add(new AppModel(name,icon, 0, packageName));
                }


            }

        }
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }
}