package com.eber.ui.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.http.HttpUrls;
import com.eber.http.StringCallback;
import com.eber.ui.register.FillInformationActivity;
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
    private TextView etUserName;
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
    private Button btnOK;
    @ViewInject(R.id.user_name_layout)
    private RelativeLayout userNameLayout;
    @ViewInject(R.id.user_sex_layout)
    private RelativeLayout userSexLayout;
    @ViewInject(R.id.user_birthday_layout)
    private RelativeLayout userBirthdayLayout;
    @ViewInject(R.id.user_height_layout)
    private RelativeLayout userHeightLayout;


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
        userNameLayout.setOnClickListener(clickLis);
        userSexLayout.setOnClickListener(clickLis);
        userBirthdayLayout.setOnClickListener(clickLis);
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
                tvHeight.setText((TextUtils.isEmpty(jo.getString("height")) ? "- " : jo.getString("height")) + "cm");
                tvBirthdy.setText(jo.getString("birthday"));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FillInformationActivity.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra("name");
                String sex = data.getStringExtra("sex");
                String birthday = data.getStringExtra("birthday");
                String height = data.getStringExtra("height");
                etUserName.setText(name);
                tvSex.setText(sex);
                tvBirthdy.setText(birthday);
                tvHeight.setText(height);
            }
        }
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.up_info_up_password:      // 修改密码

                    break;
                case R.id.up_info_ok_btn:       // 最下方完成按钮

                    break;
                case R.id.user_name_layout:// 跳转修改资料
                case R.id.user_sex_layout:
                case R.id.user_birthday_layout:
                case R.id.user_height_layout:
                    Intent intent = new Intent(mContext, FillInformationActivity.class);
                    intent.putExtra("name", etUserName.getText().toString());
                    intent.putExtra("sex", tvSex.getText().toString());
                    intent.putExtra("birthday", tvBirthdy.getText().toString());
                    intent.putExtra("height", tvHeight.getText().toString());
                    intent.putExtra("isEdit",true);
                    startActivityForResult(intent, FillInformationActivity.REQUEST_CODE);
                    break;
            }
        }
    };

    public void updateInfo2(View v) {
        // 进入修改资料第二页面
    }

}