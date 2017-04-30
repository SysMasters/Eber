package com.eber.ui;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.eber.R;
import com.eber.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by wxd on 2017/4/30.
 */

public class WebActivity extends BaseActivity {

    @ViewInject(R.id.title_content)
    private TextView tvTitle;
    @ViewInject(R.id.web_view)
    private WebView wv;

    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web);
        super.onCreate(savedInstanceState);
        tvTitle.setText(getIntent().getStringExtra("title"));
        url = getIntent().getStringExtra("url");
        wv.loadUrl(url);
    }

    @Override
    public void setListener() {

    }
}
