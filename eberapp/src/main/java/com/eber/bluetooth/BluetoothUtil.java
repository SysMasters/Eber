package com.eber.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.eber.bean.BodyInfo;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.List;
import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;
import static java.lang.Integer.parseInt;

/**
 * 蓝牙通讯工具类
 * Created by WangLibo on 2017/5/7.
 */

public class BluetoothUtil {

    public final static String UUIDStr = "0000ffe1-0000-1000-8000-00805f9b34fb";
    private final static String TAG = "bluetooth";
    private Context mContext;
    private BluetoothDevice mDevice;
    private String mMac;
    private boolean mConnected;
    private OnBluetoothMeasureListener onBluetoothMeasureListener;

    public BluetoothUtil(Context context) {
        this.mContext = context;
        // 蓝牙连接状态监听
    }

    /**
     * 开始连接设备
     */
    public void startConnect() {
        searchDevice();
    }

    /**
     * 停止扫描
     */
    public void stopSearch() {
        ClientManager.getClient().stopSearch();
    }

    public void onDestroy() {
        if (mDevice == null) {
            return;
        }
        ClientManager.getClient().disconnect(mDevice.getAddress());
        ClientManager.getClient().unregisterConnectStatusListener(mDevice.getAddress(), mConnectStatusListener);
    }

    public String getMAC() {
        if (!TextUtils.isEmpty(mMac)) {
            return mMac;
        }
        return "";
    }

    /**
     * 搜索设备
     */
    private void searchDevice() {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(5000, 2).build();

        ClientManager.getClient().search(request, mSearchResponse);
    }

    /**
     * 查找设备回调监听
     */
    private final SearchResponse mSearchResponse = new SearchResponse() {
        @Override
        public void onSearchStarted() {
            Log.i(TAG, "扫描蓝牙中。。。");
        }

        @Override
        public void onDeviceFounded(SearchResult device) {
            Beacon beacon = new Beacon(device.scanRecord);
            if (device.getName().contains("EBER")) {
                mMac = device.getAddress();
                ClientManager.getClient().registerConnectStatusListener(mMac, mConnectStatusListener);
                connectDeviceIfNeeded();
            }
        }

        @Override
        public void onSearchStopped() {
            Log.i(TAG, "扫描已停止");
        }

        @Override
        public void onSearchCanceled() {
            Log.i(TAG, "扫描已取消");
        }
    };

    /**
     * 开始连接设备
     */
    private void connectDevice() {

        // 获取device对象
        mDevice = BluetoothUtils.getRemoteDevice(mMac);

        // 配置相关连接参数
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(3)
                .setConnectTimeout(20000)
                .setServiceDiscoverRetry(3)
                .setServiceDiscoverTimeout(10000)
                .build();

        // 开始连接
        ClientManager.getClient().connect(mDevice.getAddress(), options, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == Constants.REQUEST_SUCCESS) {
                    boolean flag = true;
                    List<BleGattService> services = profile.getServices();
                    for (BleGattService service : services) {
                        List<BleGattCharacter> characters = service.getCharacters();
                        for (BleGattCharacter character : characters) {
                            if (TextUtils.equals(character.getUuid().toString(), UUIDStr)) {// 判断当前可用的服务
                                DetailItem item = new DetailItem(DetailItem.TYPE_CHARACTER, character.getUuid(), service.getUUID());
                                mCharacter = item.uuid;
                                mService = item.service;
                                if (onBluetoothMeasureListener != null){
                                    onBluetoothMeasureListener.onConnectSuccess();
                                }
                                flag = false;
                                break;
                            }
                        }
                    }
                    if (flag) {
                        Toast.makeText(mContext, "连接不可用", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private final BleConnectStatusListener mConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {
                mConnected = (status == STATUS_CONNECTED);
                connectDeviceIfNeeded();
            } else if (status == STATUS_DISCONNECTED) {
                if (null != onBluetoothMeasureListener) {
                    onBluetoothMeasureListener.onDisconnected();
                }
            }

        }
    };

    private void connectDeviceIfNeeded() {
        if (!mConnected) {
            connectDevice();
        }
    }


    private byte[] mac;
    private byte[] sendByte;
    private java.util.UUID mService;
    private java.util.UUID mCharacter;
    private boolean isSendOne = true;
    private boolean isSendTow = true;

    /**
     * 开始测量
     */
    public void startMeasure() {
        if (mService == null && mCharacter == null) {
            return;
        }
        // 打开通知
        ClientManager.getClient().notify(mMac, mService, mCharacter, mNotifyRsp);

        mac = hexStringToByte(mMac.replace(":", ""));
        byte[] delUser = new byte[]{0x09, 0x0B, 0x04, (byte) 0xA1};
        byte[] userNum = new byte[]{0x01};
        sendByte = new byte[mac.length + delUser.length + 1];
        System.arraycopy(delUser, 0, sendByte, 0, delUser.length);
        System.arraycopy(mac, 0, sendByte, delUser.length, mac.length);
        System.arraycopy(userNum, 0, sendByte, sendByte.length - 1, userNum.length);
        ClientManager.getClient().write(mMac, mService, mCharacter, sendByte, mWriteRsp);
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
        public void onNotify(UUID service, UUID character, byte[] value) {
            if (service.equals(mService) && character.equals(mCharacter)) {
                String data = ByteUtils.byteToString(value);
                if (data.matches("^080504A1.*")) {
                    Log.i("msg=======", "删除成功");
                    mac = hexStringToByte(mMac.replace(":", ""));
                    byte[] addUser = new byte[]{0x09, 0x0E, 0x04, (byte) 0xA0};
                    byte[] userInfo = pingUserInfo(172, 25, 0);
                    sendByte = new byte[addUser.length + mac.length + userInfo.length];
                    System.arraycopy(addUser, 0, sendByte, 0, addUser.length);
                    System.arraycopy(mac, 0, sendByte, addUser.length, mac.length);
                    System.arraycopy(userInfo, 0, sendByte, addUser.length + mac.length, userInfo.length);
                    ClientManager.getClient().write(mMac, mService, mCharacter, sendByte, mWriteRsp);
                }
                if (data.matches("^080504A0.*")) {
                    if (parseInt(data.substring(8, 10), 16) < 10) {
                        // 新建成功
                        Log.i("msg=======", "新建成功");
                        mac = hexStringToByte(mMac.replace(":", ""));
                        byte[] selUser = new byte[]{0x09, 0x0F, 0x04, (byte) 0xA5};
                        byte[] info = pingInfo(172, 25, 0);
                        sendByte = new byte[selUser.length + mac.length + info.length];
                        System.arraycopy(selUser, 0, sendByte, 0, selUser.length);
                        System.arraycopy(mac, 0, sendByte, selUser.length, mac.length);
                        System.arraycopy(info, 0, sendByte, selUser.length + mac.length, info.length);
                        ClientManager.getClient().write(mMac, mService, mCharacter, sendByte, mWriteRsp);
                    } else {
                        // 新建失败
                        Log.i("msg=======", "新建失败");
                        mac = hexStringToByte(mMac.replace(":", ""));
                        byte[] addUser = new byte[]{0x09, 0x0E, 0x04, (byte) 0xA0};
                        byte[] userInfo = pingUserInfo(172, 25, 0);
                        sendByte = new byte[addUser.length + mac.length + userInfo.length];
                        System.arraycopy(addUser, 0, sendByte, 0, addUser.length);
                        System.arraycopy(mac, 0, sendByte, addUser.length, mac.length);
                        System.arraycopy(userInfo, 0, sendByte, addUser.length + mac.length, userInfo.length);
                        ClientManager.getClient().write(mMac, mService, mCharacter, sendByte, mWriteRsp);
                    }
                }
                if (data.matches("^080504A5.*")) {
                    if (parseInt(data.substring(8, 10)) != 1) {
                        Log.i("msg=======", "选择用户测量失败");
                        mac = hexStringToByte(mMac.replace(":", ""));
                        byte[] selUser = new byte[]{0x0F, 0x04, (byte) 0xA5};
                        byte[] info = pingInfo(172, 25, 0);
                        sendByte = new byte[selUser.length + mac.length + info.length];
                        System.arraycopy(selUser, 0, sendByte, 0, selUser.length);
                        System.arraycopy(mac, 0, sendByte, selUser.length, mac.length);
                        System.arraycopy(info, 0, sendByte, selUser.length + mac.length, info.length);
                        String addUserStr = ByteUtils.byteToString(sendByte);
                        ClientManager.getClient().write(mMac, mService, mCharacter, sendByte, mWriteRsp);
                    } else {
                        Log.i("msg=======", "选择用户测量成功");
                    }
                }
                if (data.matches("^080704B001.*")) {
                    // 上秤
                    if (null != onBluetoothMeasureListener) {
                        onBluetoothMeasureListener.onWeigh();
                    }
                }
                if (data.matches("^080704B0.*")) {
                    if (parseInt(data.substring(8, 10)) == 0) {
                        Log.i("msg=======", "测量结束");
                        isSendOne = true;
                        isSendTow = true;
                        ClientManager.getClient().write(mMac, mService, mCharacter, end, mWriteRsp);
                    }
                }
                if (data.matches("^081104B102.*")) {
                    if (parseInt(data.substring(10, 12)) == 1) {
                        Log.i("msg=======ONE", "" + data);
                        // 储存data为第1包数据
                        if (isSendOne) {
                            ClientManager.getClient().write(mMac, mService, mCharacter, resOne, mWriteRsp);
                            endOneData = data;
                            isSendOne = false;
                        }
                    }
                }
                if (data.matches("^081004B102.*")) {
                    if (parseInt(data.substring(10, 12)) == 2) {
                        Log.i("msg=======TWO", "" + data);
                        // 储存data为第2包数据
                        if (isSendTow) {
                            ClientManager.getClient().write(mMac, mService, mCharacter, resTwo, mWriteRsp);
                            endTwoData = data;
                            isSendTow = false;
                            analysisData(endOneData, endTwoData);       // 解析数据
                        }
                    }
                }
            }
        }

        @Override
        public void onResponse(int code) {
            if (code == Constants.REQUEST_SUCCESS) {
                Log.i("", "mNotifyRsp   success");
            } else {
                Log.i("", "mNotifyRsp   failed");
            }
        }
    };

    private void analysisData(String data1, String data2) {
        String data = data1.substring(14) + data2.substring(14);
        //        00003635 38 E0 00 D2 02 60 02 1A 1E 05 AC 00 C7 06 17
        int weight = Integer.parseInt(data.substring(8, 12), 16)/200;
        int bf = Integer.parseInt(data.substring(12, 16), 16)/10/10;
        int watrer = Integer.parseInt(data.substring(16, 20), 16)/10;
        int Muscle = Integer.parseInt(data.substring(20, 24), 16)/10;
        int bone = Integer.parseInt(data.substring(24, 26), 16)/10;
        int BMR = Integer.parseInt(data.substring(26, 30), 16);
        int SFat = Integer.parseInt(data.substring(30, 34), 16)/10;
        int fat = Integer.parseInt(data.substring(34, 36), 16);
        int age = Integer.parseInt(data.substring(36, 38), 16);

        BodyInfo info = new BodyInfo();
        info.organfat = fat + "";
        info.bodyAge = age + "";
        info.subcutaneousfat = SFat + "";
        info.basicmetabolism = BMR + "";
        info.muscle = Muscle + "";
        info.bodywater = watrer + "";
        info.fatRate = bf + "";
        info.weight = weight + "";
        info.bone = bone+"";

        if (onBluetoothMeasureListener != null) {
            onBluetoothMeasureListener.onMeasureData(info);
        }
        // 按照协议6解析这两包数据
        // 数据事例
        //                        ONE: 081104B10201-头 09-用户编号 00003635-时间 38 E0 00 D2 02 60
        //WeightH	WeighthL	BFH	BFL	WatrerH	WatrerL	蛋白质
       // WeightH	WeighthL	BFH	BFL	WatrerH	WatrerL	蛋白质

        //                        TWO: 081004B10202-头 09-用户编号 02 1A 1E 05 AC 00 C7 06 17
        //MuscleH	MuscleL	Bone	BMRH	BMRL	SFatH	SFatL	InFat	BodyAge	体型
        //MuscleH	MuscleL	Bone	BMRH	BMRL	SFatH	SFatL	InFat	BodyAge	体型

    }


    String endOneData = "";
    String endTwoData = "";

    /**
     * 16进制的字符串表示转成字节数组
     *
     * @param hexString 16进制格式的字符串
     * @return 转换后的字节数组
     **/
    public static byte[] hexStringToByte(String hexString) {
        if (TextUtils.isEmpty(hexString))
            throw new IllegalArgumentException("this hexString must not be empty");

        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    /**
     * 收到第一包和第二包反馈给设备的数据
     */
    private byte[] resOne = new byte[]{0x05, 0x04, (byte) 0xB1, 0x01};
    private byte[] resTwo = new byte[]{0x05, 0x04, (byte) 0xB1, 0x02};
    /**
     * 实时测量结束向设备发送的数据
     */
    private byte[] end = new byte[]{0x09, 0x04, 0x04, (byte) 0xB0};

    private byte[] pingInfo(int heght, int age, int sex) {
        String h = gaoweiboling(Integer.toHexString(heght));
        String a = gaoweiboling(Integer.toHexString(age));
        String s = gaoweiboling(Integer.toHexString(sex));
        return hexStringToByte("01" + h + a + s + "01");
    }

    private byte[] pingUserInfo(int heght, int age, int sex) {
        String h = gaoweiboling(Integer.toHexString(heght));
        String a = gaoweiboling(Integer.toHexString(age));
        String s = gaoweiboling(Integer.toHexString(sex));
        String data = ByteUtils.byteToString(hexStringToByte("00" + h + a + s));
        return hexStringToByte("00" + h + a + s);
    }


    private String gaoweiboling(String data) {
        if (data.length() < 2) {
            data = "0" + data;
        }
        return data;
    }


    public void onResume() {
        ClientManager.getClient().registerConnectStatusListener(mMac, mConnectStatusListener);
    }

    public void onPause() {
        ClientManager.getClient().unregisterConnectStatusListener(mMac, mConnectStatusListener);
    }


    public interface OnBluetoothMeasureListener {
        // 上秤
        void onWeigh();

        // 测量数据
        void onMeasureData(BodyInfo data);

        // 连接断开
        void onDisconnected();
        
        // 连接成功
        void onConnectSuccess();
    }

    public void setOnBluetoothMeasureListener(OnBluetoothMeasureListener listener) {
        this.onBluetoothMeasureListener = listener;
    }
}
