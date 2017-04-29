package com.eber.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.eber.R;
import com.eber.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;

/**
 * Created by Administrator on 2017/4/18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this);
    }
}
