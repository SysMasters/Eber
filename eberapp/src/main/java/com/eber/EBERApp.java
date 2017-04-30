package com.eber;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.eber.utils.SPUtil;
import com.eber.utils.VersionUtil;

/**
 * Created by Administrator on 2017/4/18.
 */
public class EBERApp extends MultiDexApplication {

    public static Context sContext;
    public static SPUtil spUtil;
    public static VersionUtil versionUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        spUtil = new SPUtil(getApplicationContext());
        versionUtil = new VersionUtil(getApplicationContext());
    }
}
