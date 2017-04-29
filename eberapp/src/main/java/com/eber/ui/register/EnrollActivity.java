package com.eber.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eber.R;
import com.eber.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/4/19.
 */
public class EnrollActivity extends BaseActivity {

    @ViewInject(R.id.title_content)
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        ViewUtils.inject(this);
        tvTitle.setText("用户注册");
    }

    @Override
    public void setListener() {
        
    }


    public void viewClick(View v){
        Intent intent = new Intent(EnrollActivity.this, FillInformationActivity.class);
        startActivity(intent);
    }
}
