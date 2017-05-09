package com.eber.ui.check;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.bean.BodyInfo;
import com.eber.bluetooth.BluetoothUtil;
import com.eber.bluetooth.ClientManager;
import com.eber.http.HttpUrls;
import com.eber.http.StringCallback2;
import com.eber.utils.StatusBarUtil;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import pl.droidsonroids.gif.GifImageView;

/**
 * 称重测量
 * Created by WangLibo on 2017/5/9.
 */

public class MeasureActivity extends BaseActivity implements View.OnClickListener {


    private Button btnClose;
    private GifImageView gvImage;
    private LinearLayout llWeigh;// 上秤

    private BluetoothUtil bluetoothUtil;
    private BluetoothClient mClient;
    private BodyInfo mBodyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        btnClose = findView(R.id.measure_close);
        gvImage = findView(R.id.measure_gif);
        llWeigh = findView(R.id.measure_please_scale);

        btnClose.setOnClickListener(this);

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
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this);
    }

    @Override
    public void setListener() {

    }

    private final BluetoothStateListener mBluetoothStateListener = new BluetoothStateListener() {
        @Override
        public void onBluetoothStateChanged(boolean openOrClosed) {
            connect();
        }

    };

    private BluetoothUtil.OnBluetoothMeasureListener onBluetoothMeasureListener = new BluetoothUtil.OnBluetoothMeasureListener() {
        @Override
        public void onWeigh() {// 上秤
            //            gvImage.set
            llWeigh.setVisibility(View.GONE);
            gvImage.setVisibility(View.VISIBLE);
        }

        @Override
        public void onMeasureData(BodyInfo data) {// 返回测量数据
            if (mBodyInfo != null) {
                return;
            }
            mBodyInfo = data;
            mBodyInfo.toString();
            submitRecord();
        }

        @Override
        public void onDisconnected() {// 连接断开
            Log.e("", "------onDisconnected---连接已断开");
        }

        @Override
        public void onConnectSuccess() {
            // 开始测量
            bluetoothUtil.startMeasure();
        }
    };

    /**
     * 提交称重记录
     */
    private void submitRecord() {
        param.clear();
        param.put("memberId", EBERApp.nowUser.id + "");
        param.put("Weight", mBodyInfo.weight);
        param.put("fatRate", mBodyInfo.fatRate);
        param.put("subcutaneousfat", mBodyInfo.subcutaneousfat);
        param.put("bodywater", mBodyInfo.bodywater);
        param.put("organfat", mBodyInfo.organfat);
        param.put("basicmetabolism", mBodyInfo.basicmetabolism);
        param.put("muscle", mBodyInfo.muscle);
        param.put("bodyAge", mBodyInfo.bodyAge);
        param.put("MAC", bluetoothUtil.getMAC());
        param.put("bone", mBodyInfo.bone);
        netUtils.get(HttpUrls.ADDRECORD, false, param, new StringCallback2("memberRecord") {
            @Override
            public void onSuccess(String... result) {
                Log.e("", "=====result=======" + result);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.measure_close:
                finish();
                break;
        }
    }


    /**
     * 搜索设备
     */
    private void searchDevice() {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(5000, 2).build();

        ClientManager.getClient().search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                // 查找到设备
            }

            @Override
            public void onSearchStopped() {

            }

            @Override
            public void onSearchCanceled() {

            }
        });
    }

    private void connect() {
        bluetoothUtil.startConnect();
        bluetoothUtil.setOnBluetoothMeasureListener(onBluetoothMeasureListener);
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
