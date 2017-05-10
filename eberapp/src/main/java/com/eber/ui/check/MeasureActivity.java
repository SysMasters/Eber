package com.eber.ui.check;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.bean.BodyInfo;
import com.eber.bluetooth.BluetoothUtil;
import com.eber.bluetooth.ClientManager;
import com.eber.http.HttpUrls;
import com.eber.http.StringCallback2;
import com.eber.interfaces.BluetoothMeasure;
import com.eber.utils.StatusBarUtil;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;

import pl.droidsonroids.gif.GifImageView;

/**
 * 称重测量
 * Created by WangLibo on 2017/5/9.
 */

public class MeasureActivity extends BaseActivity implements View.OnClickListener {


    public static final int TYPE_MEASURE = 0;

    private Button btnClose, btnAgain;
    private GifImageView gvImage;
    private LinearLayout llWeigh;// 上秤

    private BluetoothUtil bluetoothUtil;
    private BluetoothClient mClient;
    private BodyInfo mBodyInfo;
    private TextView tvName;
    private RelativeLayout againLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure);

        btnClose = findView(R.id.measure_close);
        againLayout = findView(R.id.again_parent);
        btnAgain = findView(R.id.measure_again);
        gvImage = findView(R.id.measure_gif);
        llWeigh = findView(R.id.measure_please_scale);
        tvName = findView(R.id.check_name);
        tvName.setText("中午好" + (TextUtils.isEmpty(EBERApp.nowUser.userName) ? "," + EBERApp.nowUser.userName : ""));

        btnClose.setOnClickListener(this);
        btnAgain.setOnClickListener(this);

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

    private BluetoothUtil.OnBluetoothMeasureListener onBluetoothMeasureListener = new BluetoothMeasure() {
        @Override
        public void onWeigh() {// 上秤
            //            gvImage.set
            llWeigh.setVisibility(View.GONE);
            gvImage.setVisibility(View.VISIBLE);
            againLayout.setVisibility(View.GONE);
        }

        @Override
        public void onMeasureData(BodyInfo data) {// 返回测量数据
            if (mBodyInfo != null) {
                return;
            }
            mBodyInfo = data;
            submitRecord();
        }

        @Override
        public void onDisconnected() {// 连接断开
            Log.e("", "------onDisconnected---连接已断开");
            llWeigh.setVisibility(View.GONE);
            gvImage.setVisibility(View.GONE);
            againLayout.setVisibility(View.VISIBLE);
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
        Log.i("msg=======", "请求" );
        param.put("memberId", EBERApp.nowUser.id + "");
        param.put("weight", mBodyInfo.weight);
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
                Log.i("msg=======", "返回");
                Intent intent = new Intent();
                intent.putExtra("memberRecord", result[0]);
                setResult(11, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.measure_close:
                finish();
                break;
            case R.id.measure_again:
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


    private void connect() {
        llWeigh.setVisibility(View.GONE);
        gvImage.setVisibility(View.VISIBLE);
        againLayout.setVisibility(View.GONE);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothUtil.onResume();
    }
}
