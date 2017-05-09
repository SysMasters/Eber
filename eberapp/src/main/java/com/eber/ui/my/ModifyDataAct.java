package com.eber.ui.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.http.BaseCallback;
import com.eber.http.HttpUrls;
import com.eber.http.StringCallback;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by 烛九阴 on 2017/5/6.
 * 修改资料1
 */

public class ModifyDataAct extends BaseActivity {
    @ViewInject(R.id.title_content)
    TextView title;
    @ViewInject(R.id.up_info_user_name)
    private EditText etUserName;
    @ViewInject(R.id.up_info_description)
    private EditText etDescription;
    @ViewInject(R.id.up_info_cellphone)
    private TextView tvCellPhone;
    @ViewInject(R.id.up_info_sex)
    private TextView tvSex;
    @ViewInject(R.id.up_info_height)
    private TextView tvHeight;
    @ViewInject(R.id.up_info_birthday)
    private TextView tvBirthdy;
    @ViewInject(R.id.up_info_up_password)
    private RelativeLayout upPassWord;
    @ViewInject(R.id.up_info_ok_btn)
    private RelativeLayout btnOK;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_modify_data_act);
        super.onCreate(savedInstanceState);
        title.setText("修改资料");
    }

    @Override
    public void setListener() {
        upPassWord.setOnClickListener(clickLis);
        btnOK.setOnClickListener(clickLis);
        param = new HashMap<>();
        param.put("memberId", String.valueOf(EBERApp.nowUser.id));
        netUtils.get(HttpUrls.GETMEMBERINFO, true, param, new StringCallback("member") {
            @Override
            public void onSuccess(String resultJson) {
                JSONObject jo = JSON.parseObject(resultJson);
                etUserName.setText(jo.getString("userName"));
                etDescription.setText(getIntent().getStringExtra("description"));
                tvCellPhone.setText(jo.getString("cellphone"));
                if (jo.getInteger("sex") == 1)
                    tvSex.setText("男");
                else
                    tvSex.setText("女");
                tvHeight.setText(jo.getInteger("height")+"cm");
                tvBirthdy.setText(jo.getString("birthday"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.up_info_up_password:      // 修改密码

                    break;
                case R.id.up_info_ok_btn:       // 最下方完成按钮

                    break;
            }
        }
    };

    public void updateInfo2(View v){
        // 进入修改资料第二页面
    }
}
