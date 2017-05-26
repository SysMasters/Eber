package com.eber.bfs.ui.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.bfs.R;
import com.eber.bfs.base.BaseActivity;
import com.eber.bfs.http.BaseCallback;
import com.eber.bfs.http.HttpUrls;
import com.eber.bfs.ui.register.EnrollActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by wxd on 2017/5/10.
 */

public class ForgetPasswordActivity extends BaseActivity {

    @ViewInject(R.id.title_content)
    private TextView title;
    @ViewInject(R.id.fgt_pwd_phone)
    private EditText etPhone;
    @ViewInject(R.id.fgt_pwd_code)
    private EditText etCode;
    @ViewInject(R.id.fgt_pwd_pwd)
    private EditText etPwd;
    @ViewInject(R.id.fgt_pwd_eyes)
    private ImageView imgEye;
    @ViewInject(R.id.fgt_pwd_getcode)
    private Button btnGetCode;
    @ViewInject(R.id.fgt_pwd_ok)
    private Button btnOK;

    private String titleStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forget_password);
        super.onCreate(savedInstanceState);
        titleStr = getIntent().getStringExtra("title");
        if (null != titleStr && !titleStr.equals("")){
            title.setText(titleStr);
        }else{
            title.setText("忘记密码");
        }
    }

    @Override
    public void setListener() {
        imgEye.setOnClickListener(clickLis);
        btnGetCode .setOnClickListener(clickLis);
        btnOK.setOnClickListener(clickLis);
    }

    boolean isShowPwd = false;

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.fgt_pwd_eyes:
                    isShowPwd = !isShowPwd;
                    if (isShowPwd){
                        imgEye.setImageResource(R.mipmap.ico_open_eyes);
                        etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    }else{
                        imgEye.setImageResource(R.mipmap.ico_close_eyes);
                        etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }
                    break;
                case R.id.fgt_pwd_getcode:
                    String phone = etPhone.getText().toString();
                    if (!phone.matches(REGEX_MOBILE)){
                        Toast.makeText(ForgetPasswordActivity.this, "请正确输入手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    param = new HashMap<>();
                    param.put("phoneNum",phone);
                    netUtils.get(HttpUrls.GETVCODEONLY, true, param, new BaseCallback() {
                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            JSONObject jo = JSON.parseObject(response);
                            if (jo.getInteger("retcode") == 1){
                                Toast.makeText(ForgetPasswordActivity.this, "验证码已发送成功", Toast.LENGTH_SHORT).show();
                                timer.start();
                            }else{
                                Toast.makeText(ForgetPasswordActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                case R.id.fgt_pwd_ok:
                    String phoneE = etPhone.getText().toString();
                    String code = etCode.getText().toString();
                    String password = etPwd.getText().toString();
                    if (!phoneE.matches(REGEX_MOBILE)){
                        Toast.makeText(ForgetPasswordActivity.this, "请正确输入手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (code.length() < 4){
                        Toast.makeText(ForgetPasswordActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password.length() < 6){
                        Toast.makeText(ForgetPasswordActivity.this, "密码长度不足6位", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    param = new HashMap<>();
                    param.put("phoneNum", phoneE);
                    param.put("vCode", code);
                    param.put("newPassword", password);
                    netUtils.get(HttpUrls.FORGETPASSWORD, true, param, new BaseCallback() {
                        @Override
                        public void onResponse(String response) {
                            super.onResponse(response);
                            JSONObject jo = JSON.parseObject(response);
                            Toast.makeText(ForgetPasswordActivity.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                            if (titleStr.equals("修改密码"))
                                finish();
                            else if (titleStr.equals("忘记密码"))
                                startActivity(LoginActivity.class);
                        }
                    });
                    break;
            }
        }
    };

    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        int i = 60;
        @Override
        public void onTick(long millisUntilFinished) {
            btnGetCode.setText(--i + "秒后重发");
            btnGetCode.setEnabled(false);
        }

        @Override
        public void onFinish() {
            btnGetCode.setText("获取验证码");
            btnGetCode.setEnabled(true);
        }
    };
}
