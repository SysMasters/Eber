package com.eber.bfs.http;

import android.widget.Toast;

import com.eber.bfs.EBERApp;

/**
 * Created by WangLibo on 2017/5/6.
 */

public abstract class BaseCallback implements OnHttpResult {
    @Override
    public void onResponse(String response) {

    }

    @Override
    public void onError(Exception e) {
        Toast.makeText(EBERApp.sContext, "连接服务器失败", Toast.LENGTH_SHORT).show();
    }
}
