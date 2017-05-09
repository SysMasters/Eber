package com.eber.ui.my;

import android.os.Bundle;
import android.widget.TextView;

import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.http.BaseCallback;
import com.eber.http.HttpUrls;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by wxd on 2017/5/9.
 */

public class QATActivity extends BaseActivity {

    @ViewInject(R.id.title_content)
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_qat);
        super.onCreate(savedInstanceState);
        tvTitle.setText("常见问题");
    }

    @Override
    public void setListener() {
        param = new HashMap<>();
        netUtils.get(HttpUrls.FINDQAALL, true, param, new BaseCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
            }
        });
    }
}
