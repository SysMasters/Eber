package com.eber.http;

/**
 * Created by WangLibo on 2017/4/29.
 */

interface OnHttpResult {
    void onResponse(String response);

    void onError(Exception e);
}
