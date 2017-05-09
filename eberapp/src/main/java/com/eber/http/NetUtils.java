package com.eber.http;

import android.util.Log;

import com.eber.utils.Loading;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * 网络请求工具类
 * Created by WangLibo on 2017/4/29.
 */

public class NetUtils {

    private OnHttpResult httpResult;

    /**
     * get请求
     *
     * @param url           接口地址
     * @param isShowLoading 是否显示加载中对话框
     * @param paramMap      参数map
     * @param httpResult    回调
     */
    public void get(String url, boolean isShowLoading, Map paramMap, final OnHttpResult httpResult) {
        if (isShowLoading) {
            Loading.setCancelUrl(url);
            Loading.show();
        }
        this.httpResult = httpResult;
        OkHttpUtils
                .get()
                .url(url)
                .params(paramMap)
                .build()
                .execute(callback);
    }


    private StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            Loading.dismiss();
            httpResult.onError(e);
        }

        @Override
        public void onResponse(String response, int id) {
            Loading.dismiss();
            httpResult.onResponse(response);
        }
    };


}
