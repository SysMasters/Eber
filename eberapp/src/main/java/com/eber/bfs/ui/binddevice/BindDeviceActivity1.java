package com.eber.bfs.ui.binddevice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eber.bfs.R;
import com.eber.bfs.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by wxd on 2017/4/23.
 */
public class BindDeviceActivity1 extends BaseActivity {

    @ViewInject(R.id.title_content)
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bind_device_1);
        super.onCreate(savedInstanceState);
        tvTitle.setText("选择型号");
    }

    @Override
    public void setListener() {

    }

    public void bindClick1(View v) {
        Intent intent = new Intent(BindDeviceActivity1.this, BindDeviceActivity2.class);
        startActivity(intent);
        finish();
    }
}
