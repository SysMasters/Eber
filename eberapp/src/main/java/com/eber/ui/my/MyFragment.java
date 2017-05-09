package com.eber.ui.my;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseFragment;
import com.eber.http.BaseCallback;
import com.eber.http.HttpUrls;
import com.eber.ui.login.LoginActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by wxd on 2017/4/29.
 */

public class MyFragment extends BaseFragment {

    @ViewInject(R.id.f_my_photo_img)
    private ImageView imgPhoto;
    @ViewInject(R.id.f_my_badge_img1)
    private ImageView imgBadge1;
    @ViewInject(R.id.f_my_badge_img2)
    private ImageView imgBadge2;
    @ViewInject(R.id.f_my_badge_img3)
    private ImageView imgBadge3;
    @ViewInject(R.id.f_my_user_name_tv)
    private TextView tvName;
    @ViewInject(R.id.f_my_user_id_tv)
    private TextView tvID;
    @ViewInject(R.id.f_my_user_signature_tv)
    private TextView tvSignature;
    @ViewInject(R.id.f_my_sign_in_tv)
    private TextView tvSignIn;
    @ViewInject(R.id.f_my_integral_tv)
    private TextView tvIntegral;



    @ViewInject(R.id.f_my_remind)
    LinearLayout llRemind;                  // 提醒
    @ViewInject(R.id.f_my_target)
    LinearLayout llTarget;                  // 目标
    @ViewInject(R.id.f_my_account_manager)
    LinearLayout llAccountManager;          // 账号管理
    @ViewInject(R.id.f_my_device_manager)
    LinearLayout llDeviceManager;           // 设备管理
    @ViewInject(R.id.f_my_update_info)
    LinearLayout llUpdateInfo;              // 修改资料
    @ViewInject(R.id.f_my_clear_cache)
    LinearLayout llClearCache;              // 清理缓存
    @ViewInject(R.id.f_my_faq)
    LinearLayout llFaq;                      // 常见问题
    @ViewInject(R.id.f_my_about)
    LinearLayout llAbout;                    // 关于我们
    @ViewInject(R.id.f_my_un_login)
    LinearLayout llUnlogin;                  // 退出登录

    private static final int UPDATE_INFO = 1;

    @Override
    public int bindLayout() {
        return R.layout.fragment_my;
    }

    @Override
    public void onBusiness() {
        getMyData();
        tvSignIn.setOnClickListener(clickLis);
        llRemind.setOnClickListener(clickLis);
        llTarget.setOnClickListener(clickLis);
        llAccountManager.setOnClickListener(clickLis);
        llDeviceManager.setOnClickListener(clickLis);
        llUpdateInfo.setOnClickListener(clickLis);
        llClearCache.setOnClickListener(clickLis);
        llFaq.setOnClickListener(clickLis);
        llAbout.setOnClickListener(clickLis);
        llUnlogin.setOnClickListener(clickLis);
    }

    private void getMyData() {
        if (param ==null){
            return;
        }
        param = new HashMap<>();
        param.put("memberId", String.valueOf(EBERApp.nowUser.id));
        netUtils.get(HttpUrls.TOMYPAGE, false, param, new BaseCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                JSONObject jo = JSON.parseObject(response);
                if (jo.getInteger("sex") == 1){
                    imgPhoto.setImageResource(R.mipmap.ico_man);
                }else{
                    imgPhoto.setImageResource(R.mipmap.ico_woman);
                }
                tvName.setText(jo.getString("userName"));
                tvID.setText(jo.getString("cellphone"));
                tvSignature.setText(jo.getString("description"));
                tvSignIn.setText(jo.getString("signTime")+"次");
                tvIntegral.setText(jo.getString("sumScore")+"积分");
                if (jo.getInteger("flag") >= 1){
                    imgBadge1.setImageResource(R.mipmap.ico_my_badge_1);
                }
                if (jo.getInteger("flag") >= 2){
                    imgBadge2.setImageResource(R.mipmap.ico_my_badge_1);
                }
                if (jo.getInteger("flag") >= 3){
                    imgBadge3.setImageResource(R.mipmap.ico_my_badge_1);
                }
            }
        });
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.f_my_remind:// 提醒
                    Intent in9 = new Intent(mActivity, MyRemindAct.class);
                    startActivity(in9);
                    break;
                case R.id.f_my_target:// 目标
                    Intent in8 = new Intent(mActivity, MyGoalAct.class);
                    startActivity(in8);
                    break;
                case R.id.f_my_account_manager:// 账号管理

                    break;
                case R.id.f_my_device_manager:// 设备管理
                    Intent in6 = new Intent(mActivity, EquipmentManagementAct.class);
                    startActivity(in6);
                    break;
                case R.id.f_my_update_info:// 修改资料
                    Intent in7 = new Intent(mActivity, ModifyDataAct.class);
                    in7.putExtra("description", tvSignature.getText().toString());
                    startActivityForResult(in7, UPDATE_INFO);
                    break;
                case R.id.f_my_clear_cache:// 清理缓存

                    break;
                case R.id.f_my_faq: // 常见问题
                    Intent in4 = new Intent(mActivity, QATActivity.class);
                    startActivity(in4);
                    break;
                case R.id.f_my_about: // 关于我们
                    Intent in5 = new Intent(mActivity, AboutUsAct.class);
                    startActivity(in5);
                    break;
                case R.id.f_my_un_login: // 退出登录
                    unLogin();
                    break;
                case R.id.f_my_sign_in_tv:      // 签到
                    signIn();
                    break;
            }
        }
    };

    private void unLogin() {
        param = new HashMap<>();
        param.put("memberId", String.valueOf(EBERApp.nowUser.id));
        netUtils.get(HttpUrls.LOGOUT, true, param, new BaseCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                JSONObject jo = JSON.parseObject(response);
                Toast.makeText(mActivity, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                if (jo.getInteger("retcode") == 1){
                    // 跳转登录
                    startActivity(new Intent(mActivity, LoginActivity.class));
                    // 关闭
                    mActivity.exitAllBarringStackTop();
                }
            }
        });
    }

    private void signIn() {
        param = new HashMap<>();
        param.put("memberId", String.valueOf(EBERApp.nowUser.id));
        param.put("scoreType", "2");
        netUtils.get(HttpUrls.ADDSCORE, true, param, new BaseCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                JSONObject jo = JSON.parseObject(response);
                Toast.makeText(mActivity, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                if (jo.getInteger("retcode") == 1){
                    tvSignIn.setText(jo.getString("signTime")+"次");
                    tvIntegral.setText(jo.getString("sumScore")+"积分");
                };
            }
        });
    }
}
