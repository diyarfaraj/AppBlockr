package com.example.appblockr.adapter;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appblockr.R;
import com.example.appblockr.model.AppModel;
import com.example.appblockr.shared.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

public class AllAppAdapter extends RecyclerView.Adapter<AllAppAdapter.adapter_design_backend> implements Filterable {
    List<AppModel> apps = new ArrayList<>();
    List<AppModel> appsFullList;
    Context ctx;
    List<String> lockedApps = new ArrayList<>();
    private final Filter appListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AppModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(appsFullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (AppModel app : appsFullList) {
                    if (app.getAppName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(app);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            apps.clear();
            apps.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public AllAppAdapter(List<AppModel> apps, Context ctx) {
        this.apps = apps;
        this.ctx = ctx;
        appsFullList = apps;
    }

    @NonNull
    @Override
    public adapter_design_backend onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.all_adapter_design, parent, false);
        adapter_design_backend design = new adapter_design_backend(view);
        return design;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_design_backend holder, int position) {
        AppModel app = apps.get(position);
        holder.appName.setText(app.getAppName());
        holder.appIcon.setImageDrawable(app.getIcon());
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        holder.appIcon.setColorFilter(filter);
        if (app.getStatus() == 0) {
            holder.appStatus.setImageResource(0);
            holder.appIcon.setColorFilter(filter);
        } else {
            holder.appStatus.setImageResource(R.drawable.locked_icon);
            holder.appIcon.clearColorFilter();
            lockedApps.add(app.getPackageName());
        }

        holder.appIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (app.getStatus() == 0) {
                    app.setStatus(1);
                    holder.appStatus.setImageResource(R.drawable.locked_icon);
                    lockedApps.add(app.getPackageName());
                    holder.appIcon.clearColorFilter();
                    //update data
                    SharedPrefUtil.getInstance(ctx).createLockedAppsList(lockedApps);
                    //((MainActivity)ctx).updateLockedAppsNotification();
                } else {
                    app.setStatus(0);
                    holder.appStatus.setImageResource(0);
                    lockedApps.remove(app.getPackageName());
                    holder.appIcon.setColorFilter(filter);
                    //update data
                    SharedPrefUtil.getInstance(ctx).createLockedAppsList(lockedApps);
                    // ((MainActivity)ctx).updateLockedAppsNotification();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    @Override
    public Filter getFilter() {
        return appListFilter;
    }

    public void updateList(ArrayList<AppModel> newList) {
        apps = new ArrayList<>();
        apps.addAll(newList);
        notifyDataSetChanged();
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
