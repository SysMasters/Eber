package com.eber.http;

import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.EBERApp;

/**
 * 返回2个json对象数组
 * Created by WangLibo on 2017/4/29.
 */

public abstract class StringCallback2 implements OnHttpResult {

    private String param1;
    private String param2;

    public StringCallback2(String param1, String param2) {
        this.param1 = param1;
        this.param2 = param2;
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jo = JSON.parseObject(response);
            int resultCode = jo.getInteger("retcode");
            if (resultCode == 1) {
                onSuccess(jo.getString(param1), jo.getString(param2));
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
        onFaile(e);
    }

    public abstract void onSuccess(String result1, String result2);


    public void onFaile(Exception e) {

    }
}
