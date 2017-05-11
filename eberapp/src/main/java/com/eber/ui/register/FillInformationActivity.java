package com.eber.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.bean.Member;
import com.eber.http.BaseCallback;
import com.eber.http.HttpUrls;
import com.eber.http.StringCallback;
import com.eber.listener.OnValueChangeListener;
import com.eber.ui.binddevice.BindDeviceActivity1;
import com.eber.utils.Base64;
import com.eber.utils.SPKey;
import com.eber.views.ruler.RulerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/21.
 */
public class FillInformationActivity extends BaseActivity {

    public final static int REQUEST_CODE = 100;

    @ViewInject(R.id.title_content)
    private TextView tvTitle;

    @ViewInject(R.id.fill_info_sex_rg)
    private RadioGroup sexRg;
    @ViewInject(R.id.fill_info_username)
    private EditText etUserName;

    @ViewInject(R.id.fill_info_ruler_height)
    private RulerView rvHeight;
    @ViewInject(R.id.fill_info_height_text)
    private TextView tvHeightText;

    @ViewInject(R.id.fill_info_ruler_year)
    private RulerView rvYear;
    @ViewInject(R.id.fill_info_year_text)
    private TextView tvYearText;

    @ViewInject(R.id.fill_info_ruler_month)
    private RulerView rvMonth;
    @ViewInject(R.id.fill_info_month_text)
    private TextView tvMonthText;
    @ViewInject(R.id.fill_info_success)
    private TextView btnSuccess;

    private int sex = 1;

    private boolean isCreateChidUser = false;
    private boolean isEdit = false;
    private Calendar mCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fill_information);
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);


        tvTitle.setText("填写信息");

        sexRg.setOnCheckedChangeListener(checkLis);
        rvHeight.setOnValueChangeListener(heightValueLis);
        rvYear.setOnValueChangeListener(yearValueLis);
        rvMonth.setOnValueChangeListener(monthValueLis);
        btnSuccess.setOnClickListener(clickLis);

        isEdit = getIntent().getBooleanExtra("isEdit", false);
        isCreateChidUser = getIntent().getBooleanExtra("isCreateChidUser", false);
        String sex = getIntent().getStringExtra("sex");
        String height = getIntent().getStringExtra("height");
        String birthday = getIntent().getStringExtra("birthday");
        String name = getIntent().getStringExtra("name");

        if (isEdit) {
            try {
                String h = height.replace("", "").replace("cm", "").replace("-", "");
                if (!TextUtils.isEmpty(h)) {
                    int hei = (int) Double.parseDouble(h);
                    tvHeightText.setText(hei + "");
                    rvHeight.setValue(hei);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            if (TextUtils.equals("男", sex) || TextUtils.isEmpty(sex)) {
                sexRg.check(R.id.fill_info_male);
            } else {
                sexRg.check(R.id.fill_info_woman);
            }
            if (!TextUtils.isEmpty(birthday)) {
                mCalendar = Calendar.getInstance();
                try {
                    mCalendar.setTime(new SimpleDateFormat("yyyy-MM").parse(birthday));
                    tvYearText.setText(mCalendar.get(Calendar.YEAR) + "");
                    tvMonthText.setText((mCalendar.get(Calendar.MONTH) + 1) + "");
                    rvMonth.setValue((mCalendar.get(Calendar.MONTH) + 1));
                    rvYear.setValue(mCalendar.get(Calendar.YEAR));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            if (!TextUtils.isEmpty(name)) {
                etUserName.setText(name);
            }

        }
    }

    @Override
    public void findViews() {

    }

    @Override
    public void setListener() {

    }

    private RadioGroup.OnCheckedChangeListener checkLis = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.fill_info_male:
                    sex = 1;
                    break;
                case R.id.fill_info_woman:
                    sex = 2;
                    break;
            }
        }
    };

    public void titleBackClick(View v) {
        finish();
    }

    private OnValueChangeListener heightValueLis = new OnValueChangeListener() {
        @Override
        public void onValueChange(int value) {
            tvHeightText.setText(String.valueOf(value));
        }
    };
    private OnValueChangeListener yearValueLis = new OnValueChangeListener() {
        @Override
        public void onValueChange(int value) {
            tvYearText.setText(String.valueOf(value));
        }
    };
    private OnValueChangeListener monthValueLis = new OnValueChangeListener() {
        @Override
        public void onValueChange(int value) {
            tvMonthText.setText(String.valueOf(value));
        }
    };


    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isCreateChidUser) {
                if (TextUtils.equals(etUserName.getText().toString().trim(), EBERApp.user.userName)) {
                    Toast.makeText(mContext, "用户名不可重复", Toast.LENGTH_SHORT).show();
                    return;
                }
                param = new HashMap<>();
                param.put("userName", Base64.getEncodeStr(etUserName.getText().toString().trim()));
                param.put("parentId", EBERApp.user.id + "");
                param.put("birthday", tvYearText.getText().toString() + "-" + getFormatMonth(tvMonthText.getText().toString()));
                param.put("sex", "" + sex);
                param.put("height", tvHeightText.getText().toString());
                netUtils.get(HttpUrls.REGISTERMEMBER, true, param, new BaseCallback() {
                    @Override
                    public void onResponse(String response) {
                        super.onResponse(response);
                        JSONObject jo = JSON.parseObject(response);
                        int retcode = jo.getInteger("retcode");
                        if (retcode == 1) {
                            Member member = JSON.parseObject(jo.getString("member"), Member.class);
                            Intent intent = new Intent();
                            intent.putExtra("member", member);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                        Toast.makeText(mContext, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                if (!isEdit) {
                    param = new HashMap<>();
                    param.put("memberId", EBERApp.user.id + "");
                    param.put("userName", etUserName.getText().toString().trim());
                    param.put("birthday", tvYearText.getText().toString() + "-" + getFormatMonth(tvMonthText.getText().toString()));
                    param.put("sex", sex + "");
                    param.put("height", tvHeightText.getText().toString() + "");
                    netUtils.get(HttpUrls.MODIFYMEMBERINFO, true, param, new StringCallback("member") {
                        @Override
                        public void onSuccess(String resultJson) {
                            EBERApp.user.userName = etUserName.getText().toString().trim();
                            EBERApp.user.birthday = tvYearText.getText() + "-" + getFormatMonth(tvMonthText.getText().toString());
                            EBERApp.user.sex = sex;
                            EBERApp.user.height = Integer.parseInt(tvHeightText.getText() + "");
                            String userJson = JSON.toJSONString(EBERApp.user);
                            EBERApp.nowUser = EBERApp.user;
                            EBERApp.spUtil.putData(SPKey.USER, userJson);
                            startActivity(BindDeviceActivity1.class);
                        }
                    });
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("name", etUserName.getText().toString());
                    intent.putExtra("sex", sex == 1 ? "男" : "女");
                    String month = tvMonthText.getText().toString();
                    if (month.length() != 2) {
                        month = "0" + month;
                    }
                    intent.putExtra("birthday", tvYearText.getText() + "-" + month);
                    intent.putExtra("height", tvHeightText.getText().toString());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        }
    };

    private String getFormatMonth(String month) {
        if (month.length() != 2) {
            month = "0" + month;
        }
        return month;
    }
}
