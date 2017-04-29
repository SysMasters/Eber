package com.eber.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2017/4/18.
 */
public class VersionUtil {

    private Context mContext;

    public VersionUtil(Context context){
        this.mContext = context;
    }


    /**
     * 获取版本号
     * @return
     */
    public int getVersionCode(){
        PackageManager manager = mContext.getPackageManager();//获取包管理器
        PackageInfo info;
        try {
            //通过当前的包名获取包的信息
            info = manager.getPackageInfo(mContext.getPackageName(),0);//获取包对象信息
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
        return info.versionCode;
    }

    /**
     * 获取版本名称
     * @return
     */
    public String getVersionName(){
        PackageManager manager = mContext.getPackageManager();
        try {
            //第二个参数代表额外的信息，例如获取当前应用中的所有的Activity
            PackageInfo packageInfo = manager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES
            );
//            ActivityInfo[] activities = packageInfo.activities;
//            showActivities(activities);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

}
