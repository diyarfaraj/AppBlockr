package com.example.appblockr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appblockr.R;
import com.example.appblockr.model.AppModel;
import com.example.appblockr.shared.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

public class LockedAppAdapter extends RecyclerView.Adapter<LockedAppAdapter.adapter_design_backend> {

    List<AppModel> apps = new ArrayList<>();
    Context ctx;
    List<String> lockedApps = new ArrayList<>();


    public LockedAppAdapter(List<AppModel> apps, Context ctx) {
        this.apps = apps;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public adapter_design_backend onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.locked_adapter_design, parent, false);
        adapter_design_backend design = new adapter_design_backend(view);
        return design;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_design_backend holder, int position) {
        AppModel app = apps.get(position);
        holder.appName.setText(app.getAppName());
        holder.appIcon.setImageDrawable(app.getIcon());
        if (app.getStatus() == 0) {
            holder.appStatus.setImageResource(R.drawable.unlocked_icon);
        } else {
            holder.appStatus.setImageResource(R.drawable.ic_baseline_delete_24);
            lockedApps.add(app.getPackageName());
        }


        holder.appStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.setStatus(0);
                lockedApps.remove(app.getPackageName());
                //update data
                SharedPrefUtil.getInstance(ctx).createLockedAppsList(lockedApps);
                deleteItem(holder, position);
                //((MainActivity)ctx).updateLockedAppsNotification();
            }
            /* }*/
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    private void deleteItem(RecyclerView.ViewHolder holder
            , int position) {
        apps.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, apps.size());
        holder.itemView.setVisibility(View.GONE);
    }

    public class adapter_design_backend extends RecyclerView.ViewHolder {
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
