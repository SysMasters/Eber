package com.eber.bfs.ui.binddevice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eber.bfs.EBERApp;
import com.eber.bfs.R;
import com.eber.bfs.base.BaseActivity;
import com.eber.bfs.http.HttpUrls;
import com.eber.bfs.http.StringCallback2;
import com.eber.bfs.ui.check.MeasureActivity;

/**
 * Created by WangLibo on 2017/5/9.
 */

public class BindDeviceActivity3 extends BaseActivity implements View.OnClickListener {


    private Button btnAgain, btnMeasure;
    private TextView tvDeviceName;
    private TextView titleCenter;

    private String deviceName;// 设备名称
    private String equipId;// 设备id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_device_3);

        deviceName = getIntent().getStringExtra("deviceName");
        equipId = getIntent().getStringExtra("equipId");
        btnAgain = findView(R.id.bind_again);
        btnMeasure = findView(R.id.bind_begin);
        tvDeviceName = findView(R.id.device_name);
        tvDeviceName.setText(deviceName);
        titleCenter = findView(R.id.title_content);
        titleCenter.setText("识别设备");

        btnAgain.setOnClickListener(this);
        btnMeasure.setOnClickListener(this);
    }

    @Override
    public void setListener() {
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bind_begin:
                startActivity(MeasureActivity.class);
                finish();
                break;
            case R.id.bind_again:
                unBind();
                break;
        }
    }

    /**
     * 解除绑定
     */
    private void unBind() {
        param.clear();
        param.put("memberId", EBERApp.nowUser.id + "");
        param.put("equipId", equipId);
        netUtils.get(HttpUrls.DELMEMBEREQUIP, true, param, new StringCallback2() {
            @Override
            public void onSuccess(String... result) {
                startActivity(BindDeviceActivity2.class);
                finish();
            }
        });
    }
}
