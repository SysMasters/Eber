package com.eber.ui.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.eber.EBERApp;
import com.eber.bean.User;
import com.eber.http.HttpUrls;
import com.eber.http.NetUtils;
import com.eber.http.StringCallback2;
import com.eber.ui.MainActivity;
import com.eber.R;
import com.eber.ui.login.LoginActivity;
import com.eber.utils.SPKey;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/18.
 */
public class WelcomeActivity extends Activity {

    private Handler handler = new Handler();
    private int versionCode = 0;
    private String jsonUser = "";
    private User user;
    private NetUtils netUtils;
    private String memberRecord;
    private String memberArray;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.activity_welcome);
        versionCode = EBERApp.spUtil.getIntData(SPKey.VERSION_CODE);
        jsonUser = EBERApp.spUtil.getStringData(SPKey.USER);
        handler.postDelayed(r, 2000);
        user = JSON.parseObject(jsonUser, User.class);
        if (user == null){
            return;
        }
        netUtils = new NetUtils();
        Map<String, String> parm = new HashMap<>();
        parm.put("memberId", String.valueOf(user.id));
        parm.put("sessionId", user.sessionId);
        Log.i("====url: ", HttpUrls.CHECKSESSIONID+"?memberId="+String.valueOf(user.id)+"&sessionId="+user.sessionId);
        netUtils.get(HttpUrls.CHECKSESSIONID, false, parm, new StringCallback2("member", "sessionId", "indicateType", "memberArray") {
            @Override
            public void onSuccess(String... result) {
                User user = JSON.parseObject(result[0], User.class);
                user.sessionId = result[1];
                EBERApp.spUtil.putData(SPKey.USER, JSON.toJSONString(user));
                isLogin = true;
                memberRecord = result[2];
                memberArray = result[3];
            }
        });
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent intent = null;
            if (versionCode < EBERApp.versionUtil.getVersionCode())       // 当前版本第一次启动，跳转滑屏页
                intent = new Intent(WelcomeActivity.this, GuideActivity.class);
            else{
                if (isLogin){
                    intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    intent.putExtra("memberRecord", memberRecord);
                    intent.putExtra("memberArray", memberArray);
                }else{
                    intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                }
            }
            startActivity(intent);
            finish();
        }
    };
}
