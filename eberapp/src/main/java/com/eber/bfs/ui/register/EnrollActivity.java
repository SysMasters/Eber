package com.eber.bfs.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.eber.bfs.EBERApp;
import com.eber.bfs.R;
import com.eber.bfs.base.BaseActivity;
import com.eber.bfs.bean.User;
import com.eber.bfs.http.HttpUrls;
import com.eber.bfs.http.StringCallback;
import com.eber.bfs.http.StringCallback2;
import com.eber.bfs.ui.WebActivity;
import com.eber.bfs.utils.OtherUtils;
import com.eber.bfs.utils.SPKey;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/19.
 */
public class EnrollActivity extends BaseActivity {

    @ViewInject(R.id.title_content)
    private TextView tvTitle;
    @ViewInject(R.id.login_phone_num_et)
    private EditText etPhone;
    @ViewInject(R.id.login_code_et)
    private EditText etCode;
    @ViewInject(R.id.login_password_et)
    private EditText etPassword;
    @ViewInject(R.id.login_get_code_btn)
    private Button btnGetCode;
    @ViewInject(R.id.login_ok_btn)
    private Button btnOK;
    @ViewInject(R.id.login_check_box)
    private CheckBox cb;
    @ViewInject(R.id.login_agreement_tv)
    private TextView tvAgreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_enroll);
        super.onCreate(savedInstanceState);
        tvTitle.setText("用户注册");
    }

    @Override
    public void setListener() {
        btnGetCode.setOnClickListener(clickLis);
        btnOK.setOnClickListener(clickLis);
        cb.setOnClickListener(clickLis);
        tvAgreement.setOnClickListener(clickLis);
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login_get_code_btn:
                    String phone = etPhone.getText().toString();
                    if (!phone.matches(REGEX_MOBILE)){
                        Toast.makeText(EnrollActivity.this, "请正确输入手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    param = new HashMap<>();
                    param.put("phoneNum",phone);
                    netUtils.get(HttpUrls.GETVCODE, true, param, new StringCallback("") {
                                @Override
                                public void onSuccess(String resultJson) {
                                    Toast.makeText(EnrollActivity.this, "验证码已发送成功", Toast.LENGTH_SHORT).show();
                                    timer.start();
                                }
                            });
                    break;
                case R.id.login_ok_btn:
                    if (!cb.isChecked()){
                        Toast.makeText(EnrollActivity.this, "请同意《EBER服务协议》", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String phoneE = etPhone.getText().toString();
                    String code = etCode.getText().toString();
                    String password = etPassword.getText().toString();
                    if (!phoneE.matches(REGEX_MOBILE)){
                        Toast.makeText(EnrollActivity.this, "请正确输入手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (code.length() < 4){
                        Toast.makeText(EnrollActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password.length() < 6){
                        Toast.makeText(EnrollActivity.this, "密码长度不足6位", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    param = new HashMap<>();
                    param.put("phoneNum",phoneE);
                    param.put("vCode",code);
                    param.put("password",password);
                    param.put("rigesterIP", OtherUtils.getHostIP());
                    param.put("deviceType","2");
                    param.put("installationId",OtherUtils.getAndroidId(EnrollActivity.this));
                    netUtils.get(HttpUrls.REGISTERBYCELLPHONE, true, param, new StringCallback2("member","sessionId") {
                        @Override
                        public void onSuccess(String... result) {
                            User user = JSON.parseObject(result[0],User.class);
                            user.sessionId = result[1];
                            EBERApp.user = user;
                            String userJson = JSON.toJSONString(user);
                            EBERApp.spUtil.putData(SPKey.USER, userJson);
                            Intent intent = new Intent(EnrollActivity.this, FillInformationActivity.class);
                            intent.putExtra("title", "填写信息");
                            startActivity(intent);
                        }
                    });
                    break;
                case R.id.login_agreement_tv:
                    // 服务协议
                    startWebActivity("用户协议",
                            "http://112.74.62.116:8080/ieber/dictionaryAPP/getAgreement.shtml");
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
