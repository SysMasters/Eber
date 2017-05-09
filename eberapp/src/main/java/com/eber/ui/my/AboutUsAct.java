package com.eber.ui.my;

import android.app.Activity;
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
//                        jo.getString("sourceID"): "h_ff4434815bbd",
//                        jo.getString("content"): "https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzIxMTI2MzM0NA==#wechat_redirect",
//                        jo.getInteger("retcode"): 1,
//                        jo.getString("msg"): "取得公众号成功"
                    }
                });
            }
        });
    }

    private void startWicat(String sourceID) {
//        String appId = "你的ID";//开发者平台ID
//        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId, false);
//
//        if (api.isWXAppInstalled()) {
//            JumpToBizProfile.Req req = new JumpToBizProfile.Req();
//            req.toUserName = sourceID; // 公众号原始ID
//            req.extMsg = "";
//            req.profileType = JumpToBizProfile.JUMP_TO_NORMAL_BIZ_PROFILE; // 普通公众号
//            api.sendReq(req);
//        }else{
//            Toast.makeText(AboutUsAct.this, "微信未安装", Toast.LENGTH_SHORT).show();
//        }
    }
}
