package com.example.appsift.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appsift.R;
import com.example.appsift.adapter.AppAdapter;
import com.example.appsift.model.AppModel;

import java.util.ArrayList;
import java.util.List;

public class LockedAppsFragment extends Fragment {
    RecyclerView recyclerView;
    static List<AppModel> apps = new ArrayList<>();
    AppAdapter adapter;
    ProgressDialog progressDialog;
    static Context ctx;
    static List<ApplicationInfo> packageInfos;


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
        recyclerView =(RecyclerView) view.findViewById(R.id.lockedAppsList);
        adapter = new AppAdapter(apps, ctx);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        recyclerView.setAdapter(adapter);


    }


}