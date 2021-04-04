package com.example.appsift.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appsift.R;
import com.example.appsift.model.AppModel;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.adapter_design_backend> {

    List<AppModel> apps = new ArrayList<>();
    Context ctx;

    public AppAdapter(List<AppModel> apps, Context ctx) {
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
        }

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
