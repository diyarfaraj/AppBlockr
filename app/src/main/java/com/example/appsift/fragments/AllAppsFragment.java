package com.example.appsift.fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.appsift.adapter.LockedAppAdapter;
import com.example.appsift.model.AppModel;

import java.util.ArrayList;
import java.util.List;

public class AllAppsFragment extends Fragment {
    RecyclerView recyclerView;
    static List<AppModel> apps = new ArrayList<>();
    LockedAppAdapter adapter;
    ProgressDialog progressDialog;
    static Context ctx;



    public AllAppsFragment(Context context, List<AppModel> apps) {
        this.ctx = context;
        this.apps = apps;

    }


    // TODO: Rename and change types and number of parameters
    public static AllAppsFragment newInstance(String param1, String param2) {
        AllAppsFragment fragment = new AllAppsFragment(ctx, apps);
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
        return inflater.inflate(R.layout.fragment_all_apps2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView =(RecyclerView) view.findViewById(R.id.allAppsList);
        adapter = new LockedAppAdapter(apps, ctx);
        recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        recyclerView.setAdapter(adapter);
   /*     progressDialog = new ProgressDialog(this);
        progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                getInstalledApps();
            }
        });*/
    }
}