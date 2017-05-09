package com.eber.ui.my;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.http.BaseCallback;
import com.eber.http.HttpUrls;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 烛九阴 on 2017/5/6.
 * 关于我们
 */

public class AboutUsAct extends BaseActivity {
    @ViewInject(R.id.title_content)
    TextView title;
    @ViewInject(R.id.lly_weixin)
    LinearLayout ll;
    @ViewInject(R.id.about_version_name)
    TextView tvVersionName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_about_us_act);
        super.onCreate(savedInstanceState);
        title.setText("关于我们");
        tvVersionName.setText("v "+getVersionName());
    }

    private String getVersionName(){
        String pkName = this.getPackageName();
        String versionName = "";
        try {
            versionName =  this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }


    @Override
    public void setListener() {
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                param = new HashMap<>();
                netUtils.get(HttpUrls.GETPUBLICMUMBER, true, param, new BaseCallback() {
                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);
                        JSONObject jo = JSON.parseObject(response);
                        if (jo.getInteger("retcode") == 1){
                            startWicat(jo.getString("sourceID"));
                        }else{
                            Toast.makeText(AboutUsAct.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void startWicat(String sourceID) {
        if (isWeixinAvilible(this)){
            Intent intent = new Intent();
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        }else{
            Toast.makeText(AboutUsAct.this, "微信未安装", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
}
