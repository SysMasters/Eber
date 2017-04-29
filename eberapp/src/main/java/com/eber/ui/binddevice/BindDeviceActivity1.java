package com.eber.ui.binddevice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eber.R;
import com.eber.base.BaseActivity;

/**
 * Created by wxd on 2017/4/23.
 */
public class BindDeviceActivity1 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bind_device_1);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setListener() {

    }

    public void bindClick1(View v) {
        Intent intent = new Intent(BindDeviceActivity1.this, BindDeviceActivity2.class);
        startActivity(intent);
    }
}
