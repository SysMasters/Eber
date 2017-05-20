package com.eber.bfs.ui.check;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Measure;
import android.media.MediaPlayer;
import android.os.Bundle;
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

import com.eber.bfs.EBERApp;
import com.eber.bfs.R;
import com.eber.bfs.base.BaseActivity;
import com.eber.bfs.bean.BodyInfo;
import com.eber.bfs.bluetooth.BluetoothUtil;
import com.eber.bfs.bluetooth.ClientManager;
import com.eber.bfs.interfaces.BluetoothMeasure;
import com.eber.bfs.ui.MainActivity;
import com.eber.bfs.utils.StatusBarUtil;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;

import java.util.Calendar;

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
        // 判断是否有权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            // 判断是否需要 向用户解释，为什么要申请该权限
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
            MediaPlayer mp = MediaPlayer.create(MeasureActivity.this, R.raw.iphone_glasses);
            mp.start();
            if (mBodyInfo != null) {
                return;
            }
            mBodyInfo = data;
            Intent intent = new Intent(mContext, MainActivity.class);
            MainActivity.mBodyInfo = data;
            MainActivity.mac = bluetoothUtil.getMAC();
            intent.putExtra("BodyInfo", data);
            intent.putExtra("mac", bluetoothUtil.getMAC());
            startActivity(intent);
            finish();
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
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            // 开始测量
            bluetoothUtil.startMeasure(year);
        }


    };

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
