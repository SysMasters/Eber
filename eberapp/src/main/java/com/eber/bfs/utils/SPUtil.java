package com.eber.bfs.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/4/18.
 */
public class SPUtil<T> {

    public SPUtil(Context context){
        editor = context.getSharedPreferences("eber", Context.MODE_PRIVATE).edit();
        pref = context.getSharedPreferences("eber", Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor editor;
    private SharedPreferences pref;

    public void putData(String name, String data){
        editor.putString(name, data);
        editor.commit();
    }

    public void putData(String name, int data){
        editor.putInt(name, data);
        editor.commit();
    }

    public void putData(String name, boolean data){
        editor.putBoolean(name, data);
        editor.commit();
    }

    public String getStringData(String name){
        return pref.getString(name, "");
    }

    public int getIntData(String name){
        return pref.getInt(name, 0);
    }

    public boolean getBooleanData(String name){
        return pref.getBoolean(name, false);
    }
}
