package com.eber.bfs.ui.my;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.bfs.EBERApp;
import com.eber.bfs.R;
import com.eber.bfs.base.BaseFragment;
import com.eber.bfs.bean.Member;
import com.eber.bfs.http.BaseCallback;
import com.eber.bfs.http.HttpUrls;
import com.eber.bfs.ui.MainActivity;
import com.eber.bfs.ui.login.LoginActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;

import static com.eber.bfs.ui.register.FillInformationActivity.REQUEST_CODE;

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


    @ViewInject(R.id.f_my_sign_in_lay)
    LinearLayout signInLay;                  
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
        // 当前如果是子用户，只能看到我的目标和我的资料
        int isVisible = EBERApp.nowUser.isParentUser() ? View.VISIBLE : View.GONE;
        signInLay.setVisibility(isVisible);
        llRemind.setVisibility(isVisible);
        llAccountManager.setVisibility(isVisible);
        llDeviceManager.setVisibility(isVisible);
        llClearCache.setVisibility(isVisible);
        llFaq.setVisibility(isVisible);
        llAbout.setVisibility(isVisible);
        llUnlogin.setVisibility(isVisible);
        
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
        imgPhoto.setOnClickListener(clickLis);
    }

    private void getMyData() {
        param = new HashMap<>();
        param.put("memberId", String.valueOf(EBERApp.nowUser.id));
        netUtils.get(HttpUrls.TOMYPAGE, true, param, new BaseCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                JSONObject jo = JSON.parseObject(response);
                if (jo.getInteger("sex") == 1) {
                    imgPhoto.setImageResource(R.mipmap.ico_man);
                } else {
                    imgPhoto.setImageResource(R.mipmap.ic_info_sex_woman_seleted);
                }
                tvName.setText(jo.getString("userName"));
                tvID.setText(jo.getString("cellphone"));
                tvSignature.setText(jo.getString("description"));
                tvSignIn.setText(jo.getString("signTime") + "次");
                tvIntegral.setText(jo.getString("sumScore") + "积分");
                if (jo.getInteger("flag") >= 1) {
                    imgBadge1.setImageResource(R.mipmap.ico_my_badge_1);
                }
                if (jo.getInteger("flag") >= 2) {
                    imgBadge2.setImageResource(R.mipmap.ico_my_badge_1);
                }
                if (jo.getInteger("flag") >= 3) {
                    imgBadge3.setImageResource(R.mipmap.ico_my_badge_1);
                }
            }
        });
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.f_my_remind:// 提醒
                    Intent in9 = new Intent(mActivity, MyRemindAct.class);
                    startActivity(in9);
                    break;
                case R.id.f_my_target:// 目标
                    Intent in8 = new Intent(mActivity, MyGoalAct.class);
                    startActivity(in8);
                    break;
                case R.id.f_my_account_manager:// 账号管理
                    startActivity(AccountManagerActivity.class);
                    break;
                case R.id.f_my_device_manager:// 设备管理
                    Intent in6 = new Intent(mActivity, EquipmentManagementAct.class);
                    startActivity(in6);
                    break;
                case R.id.f_my_photo_img:
                case R.id.f_my_update_info:// 修改资料
                    Intent in7 = new Intent(mActivity, ModifyDataAct.class);
                    in7.putExtra("description", tvSignature.getText().toString());
                    startActivityForResult(in7, UPDATE_INFO);
                    break;
                case R.id.f_my_clear_cache:// 清理缓存
                    Toast.makeText(mActivity, "正在清理", 4000).show();
                    Toast.makeText(mActivity, "清理完成", Toast.LENGTH_SHORT).show();
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
                if (jo.getInteger("retcode") == 1) {
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
                if (jo.getInteger("retcode") == 1) {
                    tvSignIn.setText(jo.getString("signTime") + "次");
                    tvIntegral.setText(jo.getString("sumScore") + "积分");
                }
                ;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == UPDATE_INFO) {
            getMyData();
            if (data != null){
                Member member = data.getParcelableExtra("member");
                ((MainActivity)mActivity).reloadMembers(member);
            }
        }
    }
}
