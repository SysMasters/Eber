package com.eber.ui.binddevice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.interfaces.BluetoothListener;
import com.eber.utils.BluetoothUtil;
import com.eber.utils.StatusBarUtil;

/**
 * Created by wxd on 2017/4/23.
 */
public class BindDeviceActivity2 extends BaseActivity {

    private static final int BLUETOOTHSTATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bind_device_2);
        super.onCreate(savedInstanceState);
        BluetoothUtil.getIntence().setOnBluetoothListener(bluetoothListener);
    }

    private BluetoothListener bluetoothListener = new BluetoothListener() {
        @Override
        public void bluetoothState(boolean bluetoothState) {
            handler.sendMessage(handler.obtainMessage(BLUETOOTHSTATE, bluetoothState));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        BluetoothUtil.getIntence().onConnectButtonClicked(BindDeviceActivity2.this);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == BLUETOOTHSTATE){
                Toast.makeText(BindDeviceActivity2.this, "蓝牙链接状态"+msg.obj, Toast.LENGTH_SHORT).show();
            }
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
    protected void onDestroy() {
        super.onDestroy();
        BluetoothUtil.getIntence().removeOnBluetoothListener();
        BluetoothUtil.getIntence().bluetoothDestroy(BindDeviceActivity2.this);
    }
}
