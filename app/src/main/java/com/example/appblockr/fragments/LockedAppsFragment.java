package com.example.appblockr.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appblockr.R;
import com.example.appblockr.adapter.LockedAppAdapter;
import com.example.appblockr.model.AppModel;
import com.example.appblockr.shared.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

public class LockedAppsFragment extends Fragment {
    RecyclerView lockedRecyclerView;
    static List<AppModel> apps = new ArrayList<>();
    List<String> newApp = new ArrayList<>();
    LockedAppAdapter adapter;
    ProgressDialog progressDialog;
    static Context ctx;
    static List<ApplicationInfo> packageInfos;

    View view;


    public LockedAppsFragment( Context context,  List<AppModel> apps) {
        // Required empty public constructor
        this.ctx = context;
        this.apps = apps;
    }

    public static LockedAppsFragment newInstance(String param1, String param2) {
        LockedAppsFragment fragment = new LockedAppsFragment(ctx,apps);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(ctx != null){
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                }
            });
        }
        return inflater.inflate(R.layout.fragment_locked_apps2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lockedRecyclerView =(RecyclerView) view.findViewById(R.id.lockedAppsList);
        adapter = new LockedAppAdapter(apps, ctx);
        lockedRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        lockedRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume( ) {
        super.onResume();

            List<String> prefAppList = SharedPrefUtil.getInstance(ctx).getLockedAppsList();
            List<ApplicationInfo> packageInfos = ctx.getPackageManager().getInstalledApplications(0);
            if(apps.size() != prefAppList.size()){
                apps.clear();
                for (int i = 0; i < packageInfos.size(); i++) {
                    if(packageInfos.get(i).icon > 0) {
                        String name = packageInfos.get(i).loadLabel(ctx.getPackageManager()).toString();
                        Drawable icon = packageInfos.get(i).loadIcon(ctx.getPackageManager());
                        String packageName = packageInfos.get(i).packageName;
                        if(prefAppList.contains(packageName)){
                            apps.add(new AppModel(name,icon, 1, packageName));
                        } else {
                            continue;
                        }

                    }

                }

        }
    }
}