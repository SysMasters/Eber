package com.eber.ui.binddevice;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.bean.BodyInfo;
import com.eber.bluetooth.BluetoothUtil;
import com.eber.bluetooth.ClientManager;
import com.eber.utils.StatusBarUtil;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.UUID;

/**
 * Created by wxd on 2017/4/23.
 */
public class BindDeviceActivity2 extends BaseActivity implements View.OnClickListener {


    private View vBack;

    private BluetoothUtil bluetoothUtil;
    private BluetoothClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_device_2);

        init();
    }

    private void init() {
        vBack = findView(R.id.title_back);

        vBack.setOnClickListener(this);


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

    private BluetoothUtil.OnBluetoothMeasureListener onBluetoothMeasureListener = new BluetoothUtil.OnBluetoothMeasureListener() {
        @Override
        public void onWeigh() {
        }

        @Override
        public void onMeasureData(BodyInfo data) {// 返回测量数据
        }

        @Override
        public void onDisconnected() {// 连接断开
            Log.e("", "------onDisconnected---连接已断开");
        }

        @Override
        public void onConnectSuccess() {
            readScaleInfo();
        }
    };


    /**
     * 读取秤体信息
     */
    private void readScaleInfo() {

    }


    private final BleWriteResponse mWriteRsp = new BleWriteResponse() {
        @Override
        public void onResponse(int code) {
            if (code == Constants.REQUEST_SUCCESS) {
                Log.i("", "mWriteRsp   success");
            } else {
                Log.e("", "mWriteRsp   failed");
            }
        }
    };

    private final BleNotifyResponse mNotifyRsp = new BleNotifyResponse() {

        @Override
        public void onResponse(int code) {
            if (code == Constants.REQUEST_SUCCESS) {
                Log.i("", "mNotifyRsp   success");
            } else {
                Log.i("", "mNotifyRsp   failed");
            }
        }

        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {

        }
    };

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
        mClient.unregisterBluetoothStateListener(mBluetoothStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothUtil.onResume();
    }
}
