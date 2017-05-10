package com.eber.http;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.EBERApp;

/**
 * 返回2个json对象数组
 * Created by WangLibo on 2017/4/29.
 */

public abstract class StringCallback2 extends BaseCallback {

    private String[] params;

    public StringCallback2(String... params) {
        this.params = params;
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jo = JSON.parseObject(response);
            int resultCode = jo.getInteger("retcode");
            Log.i("=======resultCode", ": " + resultCode);
            if (resultCode == 1) {
                for (int i = 0; i < params.length; i++) {
                    String param = this.params[i];
                    this.params[i] = jo.getString(param);
                }
                onSuccess(params);
            } else {
                String msg = jo.getString("msg");
                Toast.makeText(EBERApp.sContext, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(Exception e) {
        super.onError(e);
        onFaile(e);
    }

    public abstract void onSuccess(String... result);


    public void onFaile(Exception e) {

    }
}
