package com.eber.base;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.eber.R;
import com.eber.http.NetUtils;
import com.eber.ui.WebActivity;
import com.eber.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected NetUtils netUtils;
    protected Map<String, String> param = new HashMap<>();
    protected Context mContext;

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((17[0-9])(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        netUtils = new NetUtils();
        ActivityManager.getInstance().addActivity(this);
        setStatusBar();
        findViews();
        setListener();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.theme_color), 0);
    }

    public void titleBackClick(View v) {
        finish();
    }

    public void findViews() {
        ViewUtils.inject(this);
    }

    public abstract void setListener();

    protected <E extends View> E findView(int resId) {
        View view = findViewById(resId);
        return (E) view;
    }

    protected void startActivity(Class<?> tClass) {
        Intent intent = new Intent(this, tClass);
        startActivity(intent);
    }

    public void startWebActivity(String title, String url) {
        Intent intent1 = new Intent(this, WebActivity.class);
        intent1.putExtra("title", title);
        intent1.putExtra("url", url);
        startActivity(intent1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }

    public void exitAllBarringStackTop(){
        ActivityManager.getInstance().exitAllBarringStackTop();
    }
}
