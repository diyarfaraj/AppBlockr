package com.example.appsift.shared;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class SharedPrefUtil {
    private static final String SHARED_APP_PREFERENCE_NAME = "SharedPref";
    Context cxt;
    private SharedPreferences pref;
    private SharedPreferences.Editor mEditor;
    public SharedPrefUtil(Context context) {
        this.pref = context.getSharedPreferences(SHARED_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }
    public static SharedPrefUtil getInstance(Context context) {
        return new SharedPrefUtil(context);
    }
    public void putString(String key, String value) {
        pref.edit().putString(key, value).apply();
    }
    public void putInteger(String key, int value) {
        pref.edit().putInt(key, value).apply();
    }
    public void putBoolean(String key, boolean value) {
        pref.edit().putBoolean(key, value).apply();
    }
    public String getString(String key)
    {
        return pref.getString(key,"");
    }
    public int getInteger(String key)
    {
        return pref.getInt(key,0);
    }
    public boolean getBoolean(String key)
    {
        return pref.getBoolean(key,false);
    }

    //add apps to locked list
    public void createLockedAppsList(List<String> appList){
        for (int i = 0; i < appList.size(); i++) {
            putString("app_"+i, appList.get(i));
        }
        putInteger("listSize", appList.size());
    }

    //get apps from locked list
    public List<String> getLockedAppsList(){
        List<String> temp = new ArrayList<>();
        int size = getInteger("listSize");
        for (int i = 0; i < size; i++) {
            temp.add(getString("app_"+i));
        }
        return temp;
    }

}