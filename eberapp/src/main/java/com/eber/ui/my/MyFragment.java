package com.eber.ui.my;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.eber.R;
import com.eber.base.BaseFragment;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by wxd on 2017/4/29.
 */

public class MyFragment extends BaseFragment {

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

    @Override
    public int bindLayout() {
        return R.layout.fragment_my;
    }

    @Override
    public void onBusiness() {
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
                    startActivity(in7);
                    break;
                case R.id.f_my_clear_cache:// 清理缓存

                    break;
                case R.id.f_my_faq: // 常见问题

                    break;
                case R.id.f_my_about: // 关于我们
                    Intent in5 = new Intent(mActivity, AboutUsAct.class);
                    startActivity(in5);
                    break;
                case R.id.f_my_un_login: // 退出登录

                    break;
            }
        }
    };
}
