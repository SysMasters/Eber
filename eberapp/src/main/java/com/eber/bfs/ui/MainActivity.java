package com.eber.bfs.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.util.Util;
import com.eber.bfs.EBERApp;
import com.eber.bfs.R;
import com.eber.bfs.bean.Member;
import com.eber.bfs.http.HttpUrls;
import com.eber.bfs.http.StringCallback;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qxinli.umeng.UmengUtil;

import java.util.List;


public class MainActivity extends com.eber.bfs.base.BaseActivity {

    private com.eber.bfs.base.BaseFragment mContent;
    private com.eber.bfs.fragment.HomeFragment mFragHome;
    private com.eber.bfs.base.BaseFragment mFragFind;
    private com.eber.bfs.base.BaseFragment mFragMy;
    private com.eber.bfs.base.BaseFragment mFragTendency;

    private String memberRecordJSON;        // 最近一次称重信息
    private String memberArrayJSON;         // 子用户信息
    private String memberEquipArrayJSON;         // 用户设备信息

    @ViewInject(R.id.index_check)
    private TextView tvMeasure;
    @ViewInject(R.id.index_radioGroup)
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        memberRecordJSON = getIntent().getStringExtra("memberRecord");
        memberArrayJSON = getIntent().getStringExtra("memberArray");
        memberEquipArrayJSON = getIntent().getStringExtra("memberEquipArray");
        init();
    }

    private void init() {
        initContent();
    }

    public static com.eber.bfs.bean.BodyInfo mBodyInfo;
    public static String mac;

    @Override
    protected void onResume() {
        super.onResume();
        netUtils.get(HttpUrls.GETCURRENTVERSIONCODE, false, param, new StringCallback("versionList") {
            @Override
            public void onSuccess(String resultJson) {
                JSONArray ja = JSON.parseArray(resultJson);
                JSONObject jo = JSON.parseObject(ja.getString(1));
                String fVersionName = jo.getString("versionname");
                String dVersionName = getVersionName();
                String[] fv = fVersionName.split(".");
                String[] dv = dVersionName.split(".");
                int ifv = Integer.parseInt(fv[0]+fv[1]+fv[2].substring(0, 1));
                int idv = Integer.parseInt(dv[0]+dv[1]+dv[2].substring(0, 1));
                if (ifv <= idv){
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示");
                builder.setMessage("《EBER健康》存在版本更新，为不影响您的正常使用，请您转到应用商城更新最新版本！");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.setNeutralButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();
            }
        });
        //        mBodyInfo = (BodyInfo) getIntent().getSerializableExtra("BodyInfo");
        //        mac = getIntent().getStringExtra("mac");
        if (null != mac && !mac.equals("")) {
            submitRecord(mBodyInfo, mac);
            mac = "";
        }
    }

    private String getVersionName(){
        String pkName = this.getPackageName();
        String versionName = "";
        try {
            versionName =  this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    @Override
    public void setListener() {
        radioGroup.setOnCheckedChangeListener(checkedLis);
        tvMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, com.eber.bfs.ui.check.MeasureActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private int id;

    private RadioGroup.OnCheckedChangeListener checkedLis = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.index_home:// 首页
                    if (id != EBERApp.nowUser.id) {
                        mFragHome = new com.eber.bfs.fragment.HomeFragment();
                        id = EBERApp.nowUser.id;
                    }
                    if (mFragHome == null) {
                        mFragHome = new com.eber.bfs.fragment.HomeFragment();
                        id = EBERApp.nowUser.id;
                    }
                    switchContent(mFragHome);
                    break;
                case R.id.index_tendency:// 趋势
                    if (id != EBERApp.nowUser.id) {
                        mFragTendency = new com.eber.bfs.ui.tendency.TendencyFragment();
                        id = EBERApp.nowUser.id;
                    }
                    if (mFragTendency == null) {
                        mFragTendency = new com.eber.bfs.ui.tendency.TendencyFragment();
                        id = EBERApp.nowUser.id;
                    }
                    switchContent(mFragTendency);
                    break;
                case R.id.index_discover:// 发现
                    if (mFragFind == null) {
                        mFragFind = new com.eber.bfs.ui.find.FindFragment();
                        id = EBERApp.nowUser.id;
                    }
                    switchContent(mFragFind);
                    break;
                case R.id.index_setting:// 我的
                    if (id != EBERApp.nowUser.id) {
                        mFragMy = new com.eber.bfs.ui.my.MyFragment();
                        id = EBERApp.nowUser.id;
                    }
                    if (mFragMy == null) {
                        mFragMy = new com.eber.bfs.ui.my.MyFragment();
                        id = EBERApp.nowUser.id;
                    }
                    switchContent(mFragMy);
                    break;
            }
        }
    };


    /**
     * 初始化显示内容
     **/
    private void initContent() {
        mFragHome = new com.eber.bfs.fragment.HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.add(R.id.main_content, mFragHome);
        mContent = mFragHome;
        transaction.commit();
    }

    /**
     * 修改显示的内容 不会重新加载
     **/
    public void switchContent(com.eber.bfs.base.BaseFragment to) {
        if (mContent != to) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(mContent).add(R.id.main_content, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            //用commitAllowingStateLoss() 替换 commit()
            //解决 java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState 错误
            mContent = to;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengUtil.onActivityResult(this, requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 11) {
            com.eber.bfs.bean.BodyInfo mBodyInfo = (com.eber.bfs.bean.BodyInfo) data.getSerializableExtra("BodyInfo");
            String mac = data.getStringExtra("mac");
            submitRecord(mBodyInfo, mac);
        }
        if (requestCode == 12 && resultCode == 12) {
            mFragHome.findLastRecord();
        }
    }

    /**
     * 提交称重记录
     */
    private void submitRecord(final com.eber.bfs.bean.BodyInfo mBodyInfo, final String mac) {
        if (mFragHome == null) {
            mFragHome = new com.eber.bfs.fragment.HomeFragment();
        }
        switchContent(mFragHome);
        radioGroup.check(R.id.index_home);
        double re;
        try {
            re = Double.parseDouble(mFragHome.tvWeight.getText().toString());
        } catch (Exception e) {
            re = 0;
        }
        if (!TextUtils.isEmpty(mBodyInfo.weight) && re != 0 && Math.abs(Double.parseDouble(mBodyInfo.weight) - re) > 5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("提示");
            builder.setMessage("本次测量与您数据差距较大，请确认是否本人？");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            builder.setNeutralButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    updateMainData(mBodyInfo, mac);
                }
            });
            builder.show();
        } else {
            updateMainData(mBodyInfo, mac);
        }
    }

    private void updateMainData(com.eber.bfs.bean.BodyInfo mBodyInfo, String mac) {
        param.clear();
        Log.i("msg=======", "请求");
        param.put("memberId", EBERApp.nowUser.id + "");
        param.put("weight", mBodyInfo.weight);
        param.put("fatRate", mBodyInfo.fatRate);
        param.put("subcutaneousfat", mBodyInfo.subcutaneousfat);
        param.put("bodywater", mBodyInfo.bodywater);
        param.put("organfat", mBodyInfo.organfat);
        param.put("basicmetabolism", mBodyInfo.basicmetabolism);
        param.put("muscle", mBodyInfo.muscle);
        param.put("bodyAge", mBodyInfo.bodyAge);
        param.put("MAC", mac);
        param.put("bone", mBodyInfo.bone);
        netUtils.get(com.eber.bfs.http.HttpUrls.ADDRECORD, true, param, new com.eber.bfs.http.StringCallback2("memberRecord") {
            @Override
            public void onSuccess(String... result) {
                Log.i("msg=======", "返回");
                mFragHome.setRecordValue(result[0]);
            }
        });
    }


    public void reloadMembers(Member member) {
        List<Member> members = mFragHome.members;
        if (members == null) {
            return;
        }
        for (int i = 0; i < members.size(); i++) {
            Member m = members.get(i);
            if (TextUtils.equals(m.id,member.id)) {
                members.remove(i);
                members.add(i,member);
                break;
            }
        }
        mFragHome.reMembers();
        mFragHome.setSex();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengUtil.onDestroy(this);
    }
}
