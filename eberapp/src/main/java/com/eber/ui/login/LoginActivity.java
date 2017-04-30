package com.eber.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.bean.User;
import com.eber.http.HttpUrls;
import com.eber.http.StringCallback2;
import com.eber.ui.MainActivity;
import com.eber.ui.register.EnrollActivity;
import com.eber.utils.OtherUtils;
import com.eber.utils.SPKey;
import com.eber.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/18.
 */
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.login_phone_num_et)
    private EditText etPhone;
    @ViewInject(R.id.login_password_et)
    private EditText etPassword;
    @ViewInject(R.id.login_login_btn)
    private Button btnLogin;
    @ViewInject(R.id.login_enroll_tv)
    private TextView tvEnroll;
    @ViewInject(R.id.login_qq_img)
    private ImageView imgQQLogin;
    @ViewInject(R.id.login_wx_img)
    private ImageView imgWXLogin;
    @ViewInject(R.id.login_wb_img)
    private ImageView imgWBLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this);
    }

    @Override
    public void setListener() {
        etPhone.setText(EBERApp.spUtil.getStringData(SPKey.USER_NAME));
        etPassword.setText(EBERApp.spUtil.getStringData(SPKey.PASS_WORD));
    }

    public void loginBtnClick(View view) {
        switch (view.getId()) {
            case R.id.login_login_btn:      // 登录
                String phone = etPhone.getText().toString();
                String password = etPassword.getText().toString();
                if (!phone.matches(REGEX_MOBILE)){
                    Toast.makeText(LoginActivity.this, "请正确输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6){
                    Toast.makeText(LoginActivity.this, "密码长度不足6位", Toast.LENGTH_SHORT).show();
                    return;
                }
                param = new HashMap<>();
                param.put("cellphone", phone);
                param.put("password", password);
                param.put("loginIP", OtherUtils.getHostIP());
                param.put("deviceType", "2");
                param.put("installationId", OtherUtils.getAndroidId(LoginActivity.this));
                netUtils.get(HttpUrls.LOGIN, true, param, new StringCallback2("member", "sessionId", "memberRecord", "memberArray") {
                    @Override
                    public void onSuccess(String... result) {
                        User user = JSON.parseObject(result[0], User.class);
                        user.sessionId = result[1];
                        EBERApp.spUtil.putData(SPKey.USER, JSON.toJSONString(user));

                        // 登录操作
                        EBERApp.spUtil.putData(SPKey.USER_NAME, etPhone.getText().toString());
                        EBERApp.spUtil.putData(SPKey.PASS_WORD, etPassword.getText().toString());
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("memberRecord", result[2]);
                        intent.putExtra("memberArray", result[3]);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
            case R.id.login_enroll_tv:      // 注册
                Intent intent = new Intent(LoginActivity.this, EnrollActivity.class);
                startActivity(intent);
                break;
            case R.id.login_qq_img:

                break;
            case R.id.login_wx_img:
                break;
            case R.id.login_wb_img:
                break;
        }
    }
}
