package com.eber.bfs.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eber.bfs.EBERApp;
import com.eber.bfs.R;
import com.eber.bfs.base.BaseActivity;
import com.eber.bfs.http.BaseCallback;
import com.eber.bfs.http.HttpUrls;
import com.eber.bfs.http.StringCallback;
import com.eber.bfs.ui.login.ForgetPasswordActivity;
import com.qxinli.umeng.UmengUtil;
import com.qxinli.umeng.login.AuthCallback;
import com.qxinli.umeng.login.BaseInfo;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;


/**
 * Created by WangLibo on 2017/5/10.
 */

public class AccountManagerActivity extends BaseActivity implements View.OnClickListener {


    private TextView tvTitle, tvMobile, tvQQ, tvWeChat, tvSina;
    private LinearLayout linerUpdatePwd, linerQQ, linerWeChat, linerSina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_account_manager);
        super.onCreate(savedInstanceState);
        init();
    }


    private void init() {
        tvMobile = findView(R.id.mobile);
        linerUpdatePwd = findView(R.id.update_pwd);
        linerQQ = findView(R.id.lly_qq);
        linerWeChat = findView(R.id.lly_weixin);
        linerSina = findView(R.id.lly_weibo);
        tvQQ = findView(R.id.tv_qq);
        tvWeChat = findView(R.id.tv_wechat);
        tvSina = findView(R.id.tv_sina);
        tvTitle = findView(R.id.title_content);

        linerQQ.setOnClickListener(this);
        linerWeChat.setOnClickListener(this);
        linerSina.setOnClickListener(this);
        linerUpdatePwd.setOnClickListener(this);

        tvTitle.setText("账号管理");

        getMemberInfo();
    }


    @Override
    public void setListener() {

    }

    @Override
    public void onClick(View v) {
        boolean isUnBind = false;
        if (v.getTag() != null) {
            isUnBind = (boolean) v.getTag();
        }
        switch (v.getId()) {
            case R.id.lly_qq:
                if (isUnBind) {
                    bindUser(SHARE_MEDIA.QQ);
                } else {
                    unbindUser(SHARE_MEDIA.QQ);
                }
                break;
            case R.id.lly_weibo:
                if (isUnBind) {
                    bindUser(SHARE_MEDIA.SINA);
                } else {
                    unbindUser(SHARE_MEDIA.SINA);
                }
                break;
            case R.id.lly_weixin:
                if (isUnBind) {
                    bindUser(SHARE_MEDIA.WEIXIN);
                } else {
                    unbindUser(SHARE_MEDIA.WEIXIN);
                }
                break;
            case R.id.update_pwd:
                startActivity(ForgetPasswordActivity.class);
                break;
        }
    }


    /**
     * 绑定用户
     */
    private void bindUser(final SHARE_MEDIA platform) {
        UmengUtil.bind(this, platform, new AuthCallback() {
            @Override
            public void onComplete(int var2, BaseInfo info) {
                param = new HashMap<>();
                param.put("memberId", EBERApp.nowUser.id + "");
                if (platform == SHARE_MEDIA.QQ) {
                    param.put("QQ", info.uid);
                }
                if (platform == SHARE_MEDIA.WEIXIN) {
                    param.put("webchat", info.uid);
                }
                if (platform == SHARE_MEDIA.SINA) {
                    param.put("blog", info.uid);
                }
                netUtils.get(HttpUrls.MODIFYLOGININFO, true, param, new BaseCallback() {
                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);
                        JSONObject jo = JSON.parseObject(response);
                        String recode = jo.getString("retcode");
                        if (TextUtils.equals(recode, "1")) {// 成功
                            if (platform == SHARE_MEDIA.QQ) {
                                linerQQ.setTag(false);
                                tvQQ.setText("+ 解绑");
                            }
                            if (platform == SHARE_MEDIA.WEIXIN) {
                                linerWeChat.setTag(false);
                                tvWeChat.setText("+ 解绑");
                            }
                            if (platform == SHARE_MEDIA.SINA) {
                                linerSina.setTag(false);
                                tvSina.setText("+ 解绑");
                            }
                        } else if (TextUtils.equals(recode, "2")) {// 不能绑定
                            Toast.makeText(mContext, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(recode, "3")) {
                            // TODO: 2017/5/11 未导入 
                        }
                    }
                });
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
     * 解绑用户
     *
     * @param platform
     */
    private void unbindUser(final SHARE_MEDIA platform) {
        UmengUtil.bind(this, platform, new AuthCallback() {
            @Override
            public void onComplete(int var2, BaseInfo info) {
                param = new HashMap<>();
                param.put("memberId", EBERApp.nowUser.id + "");
                if (platform == SHARE_MEDIA.QQ) {
                    param.put("QQ", "");
                }
                if (platform == SHARE_MEDIA.WEIXIN) {
                    param.put("webchat", "");
                }
                if (platform == SHARE_MEDIA.SINA) {
                    param.put("blog", "");
                }
                netUtils.get(HttpUrls.MODIFYLOGININFO, true, param, new BaseCallback() {
                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);
                        JSONObject jo = JSON.parseObject(response);
                        String recode = jo.getString("retcode");
                        JSONArray ja = JSON.parseArray(jo.getString("member"));
                        if (TextUtils.equals(recode, "1")) {// 成功
                            if (platform == SHARE_MEDIA.QQ) {
                                linerQQ.setTag(true);
                                tvQQ.setText("+ 绑定");
                            }
                            if (platform == SHARE_MEDIA.WEIXIN) {
                                linerWeChat.setTag(true);
                                tvWeChat.setText("+ 绑定");
                            }
                            if (platform == SHARE_MEDIA.SINA) {
                                linerSina.setTag(true);
                                tvSina.setText("+ 绑定");
                            }
                            Toast.makeText(mContext, "解绑成功", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(recode, "2")) {// 不能绑定
                            Toast.makeText(mContext, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
     * 得到用户信息
     */
    private void getMemberInfo() {
        param = new HashMap<>();
        param.put("memberId", "" + EBERApp.nowUser.id);
        netUtils.get(HttpUrls.GETMEMBERINFO, true, param, new StringCallback("member") {
            @Override
            public void onSuccess(String resultJson) {
                JSONObject jo = JSON.parseObject(resultJson);

                // 手机号
                String phone = jo.getString("cellphone");

                tvMobile.setText(phone);
                /**
                 * 三方绑定原则：
                 * 1 无手机号不能进行三方绑定
                 * 2 手机账号信息和三方账号信息不一致不能绑定
                 * 3 子账户信息名字一致的，信息不一致，子账户不能绑定
                 */
                if (!TextUtils.isEmpty(phone)) {
                    // 第三方账号
                    String qq = jo.getString("QQ");
                    String wechat = jo.getString("webchat");
                    String sina = jo.getString("blog");
                    if (TextUtils.isEmpty(qq)) {
                        tvQQ.setText("+ 绑定");
                        linerQQ.setTag(true);
                    } else {
                        tvQQ.setText("+ 解绑");
                        linerQQ.setTag(false);
                    }
                    if (TextUtils.isEmpty(wechat)) {
                        tvWeChat.setText("+ 绑定");
                        linerWeChat.setTag(true);
                    } else {
                        tvWeChat.setText("+ 解绑");
                        linerWeChat.setTag(false);
                    }
                    if (TextUtils.isEmpty(sina)) {
                        tvSina.setText("+ 绑定");
                        linerSina.setTag(true);
                    } else {
                        tvSina.setText("+ 解绑");
                        linerSina.setTag(false);
                    }
                } else {
                    linerSina.setEnabled(false);
                    linerQQ.setEnabled(false);
                    linerWeChat.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengUtil.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengUtil.onDestroy(this);
    }
}
