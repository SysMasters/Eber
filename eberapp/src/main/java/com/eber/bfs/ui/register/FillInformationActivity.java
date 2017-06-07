package com.eber.bfs.ui.register;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.bfs.EBERApp;
import com.eber.bfs.R;
import com.eber.bfs.base.BaseActivity;
import com.eber.bfs.http.BaseCallback;
import com.eber.bfs.http.HttpUrls;
import com.eber.bfs.http.StringCallback;
import com.eber.bfs.listener.OnValueChangeListener;
import com.eber.bfs.ui.binddevice.BindDeviceActivity1;
import com.eber.bfs.utils.Base64;
import com.eber.bfs.utils.SPKey;
import com.eber.bfs.views.CustomDialog;
import com.eber.bfs.views.ruler.RulerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        tvTitle.setText(getIntent().getStringExtra("title"));
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
            if (value > 2015){
                rvYear.setValue(2015);
                tvYearText.setText("2015");
            }
            if (value < 1937){
                rvYear.setValue(1937);
                tvYearText.setText("1937");
            }
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
            int selectYear = Integer.parseInt(tvYearText.getText().toString());
            int nowYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
            if (nowYear-selectYear <= 16){
                showAgeDialog(FillInformationActivity.this, "未成年人的身体处于快速成长的阶段，因此测量结果可能有偏差，建议您", "仅关注体重变化", "，其他指标作为参考。");
            }else if (nowYear-selectYear >= 60){
                showAgeDialog(FillInformationActivity.this, "老年人的身体成分与成年人有较大差异，因此建议您", "仅关注体重变化", "，其他指标作为参考。");
            }else{
                clickIncident();
            }
        }
    };

    private void clickIncident(){
        if (isCreateChidUser) {
            if (TextUtils.isEmpty(etUserName.getText().toString().trim())){
                Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.equals(etUserName.getText().toString().trim(), EBERApp.user.userName)) {
                Toast.makeText(mContext, "用户名不可重复", Toast.LENGTH_SHORT).show();
                return;
            }
            param = new HashMap<>();
            param.put("userName", Base64.getEncodeStr(etUserName.getText().toString().trim()));
            param.put("parentId", EBERApp.user.parentId + "");
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
                        Intent intent = new Intent();
                        intent.putExtra("member", jo.getString("member"));
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                    Toast.makeText(mContext, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            if (!isEdit) {
                if (TextUtils.isEmpty(etUserName.getText().toString().trim())){
                    Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                param = new HashMap<>();
                param.put("memberId", EBERApp.user.id + "");
                param.put("userName", Base64.getEncodeStr(etUserName.getText().toString().trim()));
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
                if (TextUtils.isEmpty(etUserName.getText().toString().trim())){
                    Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
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

    private void showAgeDialog(Context context, String str1, String str2, String str3){
        final Dialog dialog1 = new Dialog(context);

        View contentView1 = LayoutInflater.from(context).inflate(
                R.layout.dialog_age_remind, null);
        dialog1.setContentView(contentView1);
        dialog1.setCanceledOnTouchOutside(true);
        Button btn = (Button) contentView1.findViewById(R.id.age_remind_dialog_btn);
        TextView tv = (TextView) contentView1.findViewById(R.id.age_remind_dialog_msg);
        String msg = str1 + "<font color='#AB6B04' size='36'>" + str2 + "</font>" + str3;
        tv.setText(Html.fromHtml(msg));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                clickIncident();
            }
        });
        dialog1.show();
    }

    private String getFormatMonth(String month) {
        if (month.length() != 2) {
            month = "0" + month;
        }
        return month;
    }
}
