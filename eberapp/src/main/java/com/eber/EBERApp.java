package com.eber;

import android.support.multidex.MultiDexApplication;

import com.eber.utils.SPUtil;
import com.eber.utils.VersionUtil;

/**
 * Created by Administrator on 2017/4/18.
 */
public class EBERApp extends MultiDexApplication {

    public static SPUtil spUtil;
    public static VersionUtil versionUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        spUtil = new SPUtil(getApplicationContext());
        versionUtil = new VersionUtil(getApplicationContext());
    }
}
