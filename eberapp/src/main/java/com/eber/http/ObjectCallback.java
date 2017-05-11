package com.eber.http;

import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.eber.EBERApp;

import java.util.List;

/**
 * 返回javaBean对象
 * Created by WangLibo on 2017/4/29.
 */

@Deprecated
public abstract class ObjectCallback<E> extends BaseCallback {

    private String paramArray;

    public ObjectCallback(String paramArray) {
        this.paramArray = paramArray;
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jo = JSON.parseObject(response);
            String array = jo.getString(this.paramArray);
            List<E> result = JSON.parseObject(array, new TypeReference<List<E>>() {
            });
            int resultCode = jo.getInteger("retcode");
            if (resultCode == 1) {
                onSuccess(result);
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

    public abstract void onSuccess(List<E> result);


    public void onFaile(Exception e) {

    }
}
