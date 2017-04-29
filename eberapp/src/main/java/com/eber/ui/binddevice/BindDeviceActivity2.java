package com.eber.ui.binddevice;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.eber.R;
import com.eber.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by wxd on 2017/4/23.
 */
public class BindDeviceActivity2 extends BaseActivity {
    
    @ViewInject(R.id.bind_device_fl)
    private FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bind_device_2);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setListener() {

    }
}
