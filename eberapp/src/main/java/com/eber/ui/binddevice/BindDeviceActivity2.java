package com.eber.ui.binddevice;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.bluetooth.BluetoothUtil;
import com.eber.bluetooth.ClientManager;
import com.eber.http.HttpUrls;
import com.eber.http.StringCallback2;
import com.eber.interfaces.BluetoothMeasure;
import com.eber.utils.StatusBarUtil;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;

import static com.eber.R.id.binding_again_btn;

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

        bluetoothUtil = new BluetoothUtil(this);
        mClient = ClientManager.getClient();
        if (mClient.isBluetoothOpened()) {
            connect();
        } else {
            mClient.registerBluetoothStateListener(mBluetoothStateListener);
            mClient.openBluetooth();
        }
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
