package com.eber.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.ui.MainActivity;
import com.eber.ui.register.EnrollActivity;
import com.eber.utils.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this);
    }

    @Override
    public void setListener() {

    }

    public void loginBtnClick(View view) {
        switch (view.getId()) {
            case R.id.login_login_btn:
                // 登录操作
                startActivity(MainActivity.class);
                break;
            case R.id.login_enroll_tv:
                // 注册
                Intent intent = new Intent(LoginActivity.this, EnrollActivity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, "enroll", Toast.LENGTH_LONG).show();
                break;
            case R.id.login_qq_img:
                Toast.makeText(LoginActivity.this, "qq login", Toast.LENGTH_LONG).show();
                break;
            case R.id.login_wx_img:
                Toast.makeText(LoginActivity.this, "wx login", Toast.LENGTH_LONG).show();
                break;
            case R.id.login_wb_img:
                Toast.makeText(LoginActivity.this, "wb login", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
