package com.eber.ui.launcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.eber.EBERApp;
import com.eber.ui.MainActivity;
import com.eber.R;
import com.eber.ui.login.LoginActivity;

/**
 * Created by Administrator on 2017/4/18.
 */
public class WelcomeActivity extends Activity {

    private Handler handler = new Handler();
    private int versionCode = 0;
    private String passWord = "";
    private String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.activity_welcome);
        versionCode = EBERApp.spUtil.getIntData("version_code");
        passWord = EBERApp.spUtil.getStringData("pass_word");
        userName = EBERApp.spUtil.getStringData("user_name");
        handler.postDelayed(r, 500);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent intent = null;
            if (versionCode < EBERApp.versionUtil.getVersionCode())       // 当前版本第一次启动，跳转滑屏页
                intent = new Intent(WelcomeActivity.this, GuideActivity.class);
            else if(passWord.equals(""))        // 无密码记录，跳转登录页
                intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            else if (!passWord.equals("") && !userName.equals(""))
                intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
}
