package com.eber.bfs.ui.binddevice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eber.bfs.EBERApp;
import com.eber.bfs.R;
import com.eber.bfs.base.BaseActivity;
import com.eber.bfs.bluetooth.BluetoothUtil;
import com.eber.bfs.bluetooth.ClientManager;
import com.eber.bfs.http.HttpUrls;
import com.eber.bfs.http.StringCallback2;
import com.eber.bfs.interfaces.BluetoothMeasure;
import com.eber.bfs.utils.StatusBarUtil;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;

import static com.eber.bfs.R.id.binding_again_btn;

/**
 * Created by wxd on 2017/4/23.
 */
public class BindDeviceActivity2 extends BaseActivity implements View.OnClickListener {


    public static final int TYPE_MEASURE = 1;
    private View vBack;

    private BluetoothUtil bluetoothUtil;
    private BluetoothClient mClient;
    private LinearLayout bindAgainLayout;
    private Button btnAgain;
    private RelativeLayout discernLayout;
    private TextView tvCenter;

    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_device_2);

        init();
    }

    private void init() {
        vBack = findView(R.id.title_back);
        bindAgainLayout = findView(R.id.binding_again);
        btnAgain = findView(binding_again_btn);
        discernLayout = findView(R.id.binding_device);
        tvCenter = findView(R.id.title_content);
        tvCenter.setText("识别设备");

        vBack.setOnClickListener(this);
        btnAgain.setOnClickListener(this);
        discernLayout.setVisibility(View.VISIBLE);
        bindAgainLayout.setVisibility(View.GONE);
        //判断是否有权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //判断是否需要 向用户解释，为什么要申请该权限
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
            }
        }

        bluetoothUtil = new BluetoothUtil(this);
        mClient = ClientManager.getClient();
        if (mClient.isBluetoothOpened()) {
            connect();
        } else {
            mClient.registerBluetoothStateListener(mBluetoothStateListener);
            mClient.openBluetooth();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void connect() {
        bluetoothUtil.startConnect();
        bluetoothUtil.setOnBluetoothMeasureListener(onBluetoothMeasureListener);
    }


    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            connect();
        }

    };

    private BluetoothUtil.OnBluetoothMeasureListener onBluetoothMeasureListener = new BluetoothMeasure() {
        @Override
        public void onDisconnected() {
            super.onDisconnected();
            bindAgainLayout.setVisibility(View.VISIBLE);
            discernLayout.setVisibility(View.GONE);
        }

        @Override
        public void onConnectSuccess() {
            super.onConnectSuccess();

            bluetoothUtil.readScaleInfo();

        }

        @Override
        public void onVersion(String deviceVersion, String bluetoothVersion) {
            super.onVersion(deviceVersion, bluetoothVersion);
            if (flag) {
                return;
            }
            if (!TextUtils.isEmpty(deviceVersion) && !TextUtils.isEmpty(bluetoothVersion)) {
                readScaleInfo(deviceVersion, bluetoothVersion);
            }
        }
    };


    /**
     * 读取秤体信息
     */
    private void readScaleInfo(String deviceVersion, String bluetoothVersion) {
        flag = true;
        param.clear();
        param.put("memberId", EBERApp.nowUser.id + "");
        param.put("mac", bluetoothUtil.getMAC());
        param.put("type", deviceVersion);
        param.put("softVersion", bluetoothVersion);
        netUtils.get(HttpUrls.ADDMEMBEREQUIP, false, param, new StringCallback2("memberEquipArray") {
            @Override
            public void onSuccess(String... result) {
                String deviceName = "";
                String equipId = "";
                JSONObject jo = null;
                if (result[0].contains("[{")) {
                    JSONArray ja = JSON.parseArray(result[0]);
                    jo = ja.getJSONObject(0);
                } else {
                    jo = JSON.parseObject(result[0]);
                }
                deviceName = jo.getString("typename");
                equipId = jo.getString("equipId");
                Log.e("", "===========" + result);
                Intent intent = new Intent(mContext, BindDeviceActivity3.class);
                intent.putExtra("deviceName", deviceName);
                intent.putExtra("equipId", equipId);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this);
    }

    @Override
    public void setListener() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.binding_again_btn:
                discernLayout.setVisibility(View.VISIBLE);
                bindAgainLayout.setVisibility(View.GONE);

                if (mClient.isBluetoothOpened()) {
                    connect();
                } else {
                    mClient.registerBluetoothStateListener(mBluetoothStateListener);
                    mClient.openBluetooth();
                    connect();
                }
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClient.unregisterBluetoothStateListener(mBluetoothStateListener);
        bluetoothUtil.onDestroy();
        bluetoothUtil = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        bluetoothUtil.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothUtil.onResume();
    }

}
