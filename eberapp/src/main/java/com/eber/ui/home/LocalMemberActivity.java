package com.eber.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eber.R;
import com.eber.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by WangLibo on 2017/4/23.
 */

public class LocalMemberActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.title_content)
    private TextView tvContent;
    @ViewInject(R.id.title_back)
    private TextView tvBack;
    @ViewInject(R.id.title_content)
    private TextView tvRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_member);
        init();
    }

    private void init() {
        tvContent.setText("本地成员");
    }

    @Override
    public void setListener() {
        tvBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                finish();
                break;
            case R.id.title_right:

                break;
        }
    }
}
