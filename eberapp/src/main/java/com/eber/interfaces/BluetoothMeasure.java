package com.eber.interfaces;

import com.eber.bean.BodyInfo;
import com.eber.bluetooth.BluetoothUtil;

/**
 * Created by WangLibo on 2017/5/9.
 */

public abstract  class BluetoothMeasure implements BluetoothUtil.OnBluetoothMeasureListener {

    @Override
    public void onWeigh() {

    }

    @Override
    public void onMeasureData(BodyInfo data) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectSuccess() {

    }

    @Override
    public void onVersion(String deviceVersion, String bluetoothVersion) {

    }

}