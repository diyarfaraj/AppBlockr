package com.example.appsift.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appsift.R;
import com.example.appsift.model.AppModel;
import com.example.appsift.shared.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

public class AllAppAdapter extends RecyclerView.Adapter<AllAppAdapter.adapter_design_backend> {

    List<AppModel> apps = new ArrayList<>();
    Context ctx;
    List<String> lockedApps = new ArrayList<>();


    public AllAppAdapter(List<AppModel> apps, Context ctx) {
        this.apps = apps;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public adapter_design_backend onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.adapter_design, parent, false);
        adapter_design_backend design = new adapter_design_backend(view);
        return design;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_design_backend holder, int position) {
        AppModel app = apps.get(position);
        holder.appName.setText(app.getAppName());
        holder.appIcon.setImageDrawable(app.getIcon());
        if(app.getStatus() == 0){
            holder.appStatus.setImageResource(R.drawable.unlocked_icon);
        } else {
            holder.appStatus.setImageResource(R.drawable.locked_icon);
            lockedApps.add(app.getPackageName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.getStatus()==0){
                    app.setStatus(1);
                    holder.appStatus.setImageResource(R.drawable.locked_icon);
                    Toast.makeText(ctx, app.getAppName()+ " is locked", Toast.LENGTH_LONG).show();
                    lockedApps.add(app.getPackageName());
                    //update data
                    SharedPrefUtil.getInstance(ctx).createLockedAppsList(lockedApps);
                } else {
                    app.setStatus(0);
                    holder.appStatus.setImageResource(R.drawable.unlocked_icon);
                    Toast.makeText(ctx, app.getAppName()+ " is unlocked", Toast.LENGTH_LONG).show();
                    lockedApps.remove(app.getPackageName());
                    //update data
                    SharedPrefUtil.getInstance(ctx).createLockedAppsList(lockedApps);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public class  adapter_design_backend extends RecyclerView.ViewHolder {
        TextView appName;
        ImageView appIcon, appStatus;

        public adapter_design_backend(@NonNull View itemView) {
            super(itemView);
            appName = itemView.findViewById(R.id.appname);
            appIcon = itemView.findViewById(R.id.appicon);
            appStatus = itemView.findViewById(R.id.appstatus);

        }
    }

}
