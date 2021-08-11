package com.example.appblockr.model;

import android.graphics.drawable.Drawable;

public class AppModel {
    String appName;
    Drawable icon;
    int status;
    String packageName;

    public AppModel(String appName, Drawable icon, int status, String packageName) {
        this.appName = appName;
        this.icon = icon;
        this.status = status;
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int compareTo(AppModel app) {
        return this.getAppName().compareTo(app.appName);
    }
}
