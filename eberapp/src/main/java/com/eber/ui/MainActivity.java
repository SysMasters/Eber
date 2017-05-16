package com.eber.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.base.BaseFragment;
import com.eber.bean.BodyInfo;
import com.eber.bean.Member;
import com.eber.fragment.HomeFragment;
import com.eber.http.HttpUrls;
import com.eber.http.StringCallback2;
import com.eber.ui.check.MeasureActivity;
import com.eber.ui.find.FindFragment;
import com.eber.ui.my.MyFragment;
import com.eber.ui.tendency.TendencyFragment;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class MainActivity extends BaseActivity {

    private BaseFragment mContent;
    private HomeFragment mFragHome;
    private BaseFragment mFragFind;
    private BaseFragment mFragMy;
    private BaseFragment mFragTendency;

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

    public static BodyInfo mBodyInfo;
    public static String mac;

    @Override
    protected void onResume() {
        super.onResume();
        //        mBodyInfo = (BodyInfo) getIntent().getSerializableExtra("BodyInfo");
        //        mac = getIntent().getStringExtra("mac");
        if (null != mac && !mac.equals("")) {
            submitRecord(mBodyInfo, mac);
            mac = "";
        }
    }

    @Override
    public void setListener() {
        radioGroup.setOnCheckedChangeListener(checkedLis);
        tvMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MeasureActivity.class);
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
                        mFragHome = new HomeFragment();
                        id = EBERApp.nowUser.id;
                    }
                    if (mFragHome == null) {
                        mFragHome = new HomeFragment();
                        id = EBERApp.nowUser.id;
                    }
                    switchContent(mFragHome);
                    break;
                case R.id.index_tendency:// 趋势
                    if (id != EBERApp.nowUser.id) {
                        mFragTendency = new TendencyFragment();
                        id = EBERApp.nowUser.id;
                    }
                    if (mFragTendency == null) {
                        mFragTendency = new TendencyFragment();
                        id = EBERApp.nowUser.id;
                    }
                    switchContent(mFragTendency);
                    break;
                case R.id.index_discover:// 发现
                    if (mFragFind == null) {
                        mFragFind = new FindFragment();
                        id = EBERApp.nowUser.id;
                    }
                    switchContent(mFragFind);
                    break;
                case R.id.index_setting:// 我的
                    if (id != EBERApp.nowUser.id) {
                        mFragMy = new MyFragment();
                        id = EBERApp.nowUser.id;
                    }
                    if (mFragMy == null) {
                        mFragMy = new MyFragment();
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
        mFragHome = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.add(R.id.main_content, mFragHome);
        mContent = mFragHome;
        transaction.commit();
    }

    /**
     * 修改显示的内容 不会重新加载
     **/
    public void switchContent(BaseFragment to) {
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
        if (requestCode == 0 && resultCode == 11) {
            BodyInfo mBodyInfo = (BodyInfo) data.getSerializableExtra("BodyInfo");
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
    private void submitRecord(final BodyInfo mBodyInfo, final String mac) {
        if (mFragHome == null) {
            mFragHome = new HomeFragment();
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

    private void updateMainData(BodyInfo mBodyInfo, String mac) {
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
        netUtils.get(HttpUrls.ADDRECORD, true, param, new StringCallback2("memberRecord") {
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
}
