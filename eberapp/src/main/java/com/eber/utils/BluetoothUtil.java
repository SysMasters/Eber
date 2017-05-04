package com.eber.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.eber.interfaces.BluetoothListener;
import com.eber.ui.binddevice.BindDeviceActivity2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Administrator on 2016/12/23.
 */
public class BluetoothUtil {
    private InputStream is;    //输入流，用来接收蓝牙数据
//    private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号
    private final static String MY_UUID = "00060000-F8CE-11E4-ABF4-0002A5D5C51B";   //SPP服务UUID号
    BluetoothDevice _device = null;     //蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket
    boolean bRun = true;
    boolean isThreadRun = false;
    boolean bThread = false;
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
    private static final int BLUETOOTH_CONN = 1;
    private int bluetoothDeviceCount = 0;

    private static BluetoothUtil util;

    public static BluetoothUtil getIntence() {
        if (util == null) {
            util = new BluetoothUtil();
        }
        return util;
    }

    private BluetoothListener bluetoothListener;
    public void setOnBluetoothListener(BluetoothListener bluetoothListener){
        this.bluetoothListener = bluetoothListener;
    }

    public void removeOnBluetoothListener(){
        this.bluetoothListener = null;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == BLUETOOTH_CONN){
                Log.i("state===",""+String.valueOf(msg.obj));
            }
        }
    };

    // 链接蓝牙
    public void connectBluetooth(final BluetoothDevice device1) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 得到蓝牙设备句柄
                _device = device1; //_bluetooth.getRemoteDevice(address);
                // 用服务号得到socket
                try{
                    _socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                }catch(IOException e){
                    handler.sendMessage(handler.obtainMessage(BLUETOOTH_CONN, "连接失败！"));
                }
                //连接socket
                try{
                    _socket.connect();
                    handler.sendMessage(handler.obtainMessage(BLUETOOTH_CONN, "连接"+_device.getName()+"成功！"));
                    isThreadRun = true;
                    bThread = false;
                    if (null != bluetoothListener){
                        bluetoothListener.bluetoothState(true);
                    }
                }catch(IOException e){
                    try{
                        handler.sendMessage(handler.obtainMessage(BLUETOOTH_CONN, "连接失败！"));
                        isThreadRun = false;
                        _socket.close();
                        _socket = null;
                    }catch(IOException ee){
                        handler.sendMessage(handler.obtainMessage(BLUETOOTH_CONN, "连接失败！"));
                    }
                    return;
                }
                //打开接收线程
                try{
                    is = _socket.getInputStream();   //得到蓝牙数据输入流
                }catch(IOException e){
                    handler.sendMessage(handler.obtainMessage(BLUETOOTH_CONN, "接收数据失败！"));
                    return;
                }
                if(!bThread){
                    if (ReadThread.getState() == Thread.State.NEW)
                        ReadThread.start();
                    if (ReadThread.getState() == Thread.State.TERMINATED)
                        ReadThread.run();
                    bThread=true;
                }else{
                    bRun = true;
                }
            }
        }).start();
    }

    //接收数据线程
    Thread ReadThread=new Thread(){
        public void run(){
            int num = 0;
            byte[] buffer = new byte[1024];
            byte[] buffer_new = new byte[1024];
            int i = 0;
            int n = 0;
            String smsg = "";
            String s = "";
                try {
                    while (isThreadRun) {
                        num = is.read(buffer);         //读入数据
                        for (i = 0; i < num; i++) {
                            if (buffer[i] == 0x24) {
                                n = 0;
                                smsg = "";
                            }
                            if ((buffer[i] == 0x2a)) {
                                buffer_new[n] = buffer[i];
                                n++;
                                s = new String(buffer_new, 0, n);
                                smsg += s;   //写入接收缓存
                                Log.i("========", smsg);
                                // 解析smsg
//                                AnalyzeDataUtil.getIntence().analyzeData(smsg);
                                smsg = "";
                                n = 0;
                            } else {
                                buffer_new[n] = buffer[i];
                                n++;
                            }
                        }
                        Thread.sleep(50);
                    }
                } catch (IOException e) {
                    if (null != bluetoothListener){
                        bluetoothListener.bluetoothState(false);
                    }
                    e.printStackTrace();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
//            }
        }
    };

    /**
     * 销毁蓝牙连接
     */
    public void bluetoothDestroy(Context context){
        // 注销action接收器
        context.unregisterReceiver(mReceiver);
        if(_socket!=null)  //关闭连接socket
            try{
                is.close();
                _socket.close();
                _socket = null;
                isThreadRun = false;
                bRun = false;
            }catch(IOException e){}
        //	_bluetooth.disable();  //关闭蓝牙服务
    }


    // 蓝牙按钮事件
    public void onConnectButtonClicked(Context context){
        if (null == _bluetooth){
            Toast.makeText(context, "请确认手机是否具有蓝牙功能！", Toast.LENGTH_LONG).show();
            return;
        }
        if(_bluetooth.isEnabled()==false){  // 如果蓝牙服务不可用则提示
            // 打开蓝牙并设置设备可以被搜索
            new Thread(){
                public void run(){
                    if(_bluetooth.isEnabled()==false){
                        _bluetooth.enable();
                    }
                }
            }.start();
            Toast.makeText(context, " 打开蓝牙中...", Toast.LENGTH_LONG).show();
            return;
        }
        //如未连接设备则打开DeviceListActivity进行设备搜索
        if(_socket == null){      // 蓝牙未连接，进行连接操作
            // 注册接收查找到设备action接收器
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(mReceiver, filter);
            // 注册查找结束action接收器
            filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            context.registerReceiver(mReceiver, filter);
            // 得到本地蓝牙句柄
            _bluetooth = BluetoothAdapter.getDefaultAdapter();
            doDiscovery();
        } else {        // 蓝牙已连接，断开连接操作
            //关闭连接socket
            try{
                isThreadRun = false;
                is.close();
                _socket.close();
                _socket = null;
                bRun = false;
                //
                if (null != bluetoothListener){
                    bluetoothListener.bluetoothState(false);
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return;
    }

    /**
     * 开始服务和设备查找
     */
    private void doDiscovery() {
        // 关闭再进行的服务查找
        if (_bluetooth.isDiscovering()) {
            _bluetooth.cancelDiscovery();
        }
        //并重新开始
        _bluetooth.startDiscovery();
    }

    // 查找到设备和搜索完成action监听器
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {      // 查找到设备action
                // 得到蓝牙设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i("device_list",""+device.getName()+"   "+device.getAddress());
                if(device.getName().matches("^EBER.*")){
                    Toast.makeText(context, "查找到设备", Toast.LENGTH_LONG).show();
                    bluetoothDeviceCount++;
                    // 连接蓝牙
                    BluetoothUtil.getIntence().connectBluetooth(device);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) { // 搜索完成action
                if (bluetoothDeviceCount <= 0){
                    Toast.makeText(context, "未查找到设备，重新搜索", Toast.LENGTH_LONG).show();
                    _bluetooth.startDiscovery();
                }else{
                    bluetoothDeviceCount = 0;
                }
            }
        }
    };

    /**
     * 发送蓝牙数据
     * @param data  蓝牙数据
     */
    public void onSendBluetoothData(String data){
        int i=0;
        int n=0;
        try{
            OutputStream os = _socket.getOutputStream();   //蓝牙连接输出流
            byte[] bos = data.getBytes();
            for(i=0;i<bos.length;i++){
                if(bos[i]==0x0a)n++;
            }
            byte[] bos_new = new byte[bos.length+n];
            n=0;
            for(i=0;i<bos.length;i++){ //手机中换行为0a,将其改为0d 0a后再发送
                if(bos[i]==0x0a){
                    bos_new[n]=0x0d;
                    n++;
                    bos_new[n]=0x0a;
                }else{
                    bos_new[n]=bos[i];
                }
                n++;
            }

            os.write(bos_new);
        }catch(IOException e){
        }
    }
}
