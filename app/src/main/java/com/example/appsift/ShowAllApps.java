package com.example.appsift;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.appsift.adapter.AppAdapter;
import com.example.appsift.model.AppModel;

import java.util.ArrayList;
import java.util.List;

public class ShowAllApps extends AppCompatActivity {
    RecyclerView recyclerView;
    List<AppModel> apps = new ArrayList<>();
    AppAdapter adapter;
    ProgressDialog progressDialog;

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
        List<PackageInfo> packageInfos = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            String name = packageInfos.get(i).applicationInfo.loadLabel(getPackageManager()).toString();
            Drawable icon = packageInfos.get(i).applicationInfo.loadIcon(getPackageManager());
            String packageName = packageInfos.get(i).packageName;
            apps.add(new AppModel(name,icon, 0, packageName));
        }
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }
}