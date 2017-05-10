package com.eber.http;

import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.EBERApp;

/**
 * 返回json对象
 * Created by WangLibo on 2017/4/29.
 */

public abstract class StringCallback extends BaseCallback {

    private String paramArray;

    public StringCallback(String paramArray) {
        this.paramArray = paramArray;
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jo = JSON.parseObject(response);
            int resultCode = jo.getInteger("retcode");
            if (resultCode == 1) {
                onSuccess(jo.getString(paramArray));
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

    public abstract void onSuccess(String resultJson);

    public void onFaile(Exception e) {
        
    }
}
