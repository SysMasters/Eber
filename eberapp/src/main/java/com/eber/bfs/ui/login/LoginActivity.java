package com.eber.bfs.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.eber.bfs.EBERApp;
import com.eber.bfs.R;
import com.eber.bfs.base.BaseActivity;
import com.eber.bfs.bean.User;
import com.eber.bfs.common.Constant;
import com.eber.bfs.http.BaseCallback;
import com.eber.bfs.http.HttpUrls;
import com.eber.bfs.http.StringCallback2;
import com.eber.bfs.ui.MainActivity;
import com.eber.bfs.ui.register.EnrollActivity;
import com.eber.bfs.ui.register.FillInformationActivity;
import com.eber.bfs.utils.OtherUtils;
import com.eber.bfs.utils.SPKey;
import com.eber.bfs.utils.StatusBarUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qxinli.umeng.UmengUtil;
import com.qxinli.umeng.login.AuthCallback;
import com.qxinli.umeng.login.BaseInfo;

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
    @ViewInject(R.id.login_forget_password)
    private TextView tvForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        // 关闭
        exitAllBarringStackTop();
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this);
    }

    @Override
    public void setListener() {
        tvForgetPassword.setOnClickListener(clickLis);
        etPhone.setText(EBERApp.spUtil.getStringData(SPKey.USER_NAME));
        etPassword.setText(EBERApp.spUtil.getStringData(SPKey.PASS_WORD));
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.login_forget_password:
                    Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    public void loginBtnClick(View view) {
        switch (view.getId()) {
            case R.id.login_login_btn:      // 登录
                String phone = etPhone.getText().toString();
                String password = etPassword.getText().toString();
                if (!phone.matches(REGEX_MOBILE)) {
                    Toast.makeText(LoginActivity.this, "请正确输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6) {
                    Toast.makeText(LoginActivity.this, "密码长度不足6位", Toast.LENGTH_SHORT).show();
                    return;
                }
                param = new HashMap<>();
                param.put("cellphone", phone);
                param.put("password", password);
                param.put("loginIP", OtherUtils.getHostIP());
                param.put("deviceType", "2");
                param.put("installationId", OtherUtils.getAndroidId(LoginActivity.this));
                netUtils.get(HttpUrls.LOGIN, true, param, new StringCallback2("member", "sessionId", "memberRecord", "memberArray", "memberEquipArray") {
                    @Override
                    public void onSuccess(String... result) {
                        toHomePage(result);
                    }
                });
                break;
            case R.id.login_enroll_tv:      // 注册
                Intent intent = new Intent(LoginActivity.this, EnrollActivity.class);
                startActivity(intent);
                break;
            case R.id.login_qq_img:
                loginByQQ();
                break;
            case R.id.login_wx_img:
                loginByWeChat();
                break;
            case R.id.login_wb_img:
                loginBySina();
                break;
        }
    }


    private void toHomePage(String... result) {
        User user = JSON.parseObject(result[0], User.class);
        user.sessionId = result[1];
        EBERApp.user = user;
        EBERApp.nowUser = user;
        if (user.birthday == null || user.birthday.equals("")){
            Intent intent = new Intent(LoginActivity.this, FillInformationActivity.class);
            intent.putExtra("title", "填写信息");
            startActivity(intent);
            return;
        }
        EBERApp.spUtil.putData(SPKey.USER, JSON.toJSONString(user));
        // 登录操作
        EBERApp.spUtil.putData(SPKey.USER_NAME, etPhone.getText().toString());
        EBERApp.spUtil.putData(SPKey.PASS_WORD, etPassword.getText().toString());
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("memberRecord", result[2]);
        intent.putExtra("memberArray", result[3]);
        intent.putExtra("memberEquipArray", result[4]);
        startActivity(intent);
        finish();
    }

    /**
     * 第三方登录
     *
     * @param platform 平台名称：QQ、webchat、bolg
     */
    private void doThirdLogin(final String platform, final String uid, final String iconUrl) {
        param.clear();
        param.put(platform, uid);
        param.put("rigesterIP", OtherUtils.getHostIP());
        param.put("imgUrl", iconUrl);
        param.put("deviceType", Constant.ANDROID);
        param.put("installationId", OtherUtils.getAndroidId(getApplicationContext()));
        netUtils.get(HttpUrls.REGISTERTHIRDMEMBER, true, param, new BaseCallback() {
            @Override
            public void onResponse(String response) {
                param.clear();
                param.put(platform, uid);
                param.put("loginIP", OtherUtils.getHostIP());
                param.put("deviceType", Constant.ANDROID);
                param.put("installationId", OtherUtils.getAndroidId(getApplicationContext()));
                netUtils.get(HttpUrls.MEMBERLOGINAPP, true, param, new StringCallback2("member", "sessionId", "memberRecord", "memberArray", "memberEquipArray") {
                    @Override
                    public void onSuccess(String... result) {
                        toHomePage(result);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * qq登录
     */
    private void loginByQQ() {
        UmengUtil.loginByQQ(this, new AuthCallback() {
            @Override
            public void onComplete(int var2, BaseInfo info) {
                doThirdLogin(Constant.QQ, info.uid, info.iconurl);
            }

            @Override
            public void onError(int var2, Throwable var3) {

            }

            @Override
            public void onCancel(int var2) {

            }
        });
    }

    /**
     * 微信登录
     */
    private void loginByWeChat() {
        UmengUtil.loginByWeixin(this, new AuthCallback() {
            @Override
            public void onComplete(int var2, BaseInfo info) {
                doThirdLogin(Constant.WEBCHAT, info.uid, info.iconurl);
            }

            @Override
            public void onError(int var2, Throwable var3) {

            }

            @Override
            public void onCancel(int var2) {

            }
        });
    }

    /**
     * 新浪微博登录
     */
    private void loginBySina() {
        UmengUtil.loginBySina(this, new AuthCallback() {
            @Override
            public void onComplete(int var2, BaseInfo info) {
                doThirdLogin(Constant.SINA, info.uid, info.iconurl);
            }

            @Override
            public void onError(int var2, Throwable var3) {

            }

            @Override
            public void onCancel(int var2) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UmengUtil.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengUtil.onDestroy(this);
    }
}
