package com.eber.ui.register;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.http.HttpUrls;
import com.eber.http.StringCallback;
import com.eber.listener.OnValueChangeListener;
import com.eber.ui.binddevice.BindDeviceActivity1;
import com.eber.utils.SPKey;
import com.eber.views.ruler.RulerView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/4/21.
 */
public class FillInformationActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fill_information);
        super.onCreate(savedInstanceState);
        tvTitle.setText("填写信息");
    }

    @Override
    public void findViews() {

    }

    @Override
    public void setListener() {
        sexRg.setOnCheckedChangeListener(checkLis);
        rvHeight.setOnValueChangeListener(heightValueLis);
        rvYear.setOnValueChangeListener(yearValueLis);
        rvMonth.setOnValueChangeListener(monthValueLis);
        btnSuccess.setOnClickListener(clickLis);
    }

    private RadioGroup.OnCheckedChangeListener checkLis = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
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
            param = new HashMap<>();
            param.put("memberId", EBERApp.user.id+"");
            param.put("userName", etUserName.getText().toString().trim());
            param.put("birthday", tvYearText.getText()+"-"+tvMonthText.getText());
            param.put("sex", sex+"");
            param.put("height", tvHeightText.getText()+"");
            netUtils.get(HttpUrls.MODIFYMEMBERINFO, true, param, new StringCallback("member") {
                @Override
                public void onSuccess(String resultJson) {
                    EBERApp.user.userName = etUserName.getText().toString().trim();
                    EBERApp.user.birthday = tvYearText.getText()+"-"+tvMonthText.getText();
                    EBERApp.user.sex = sex;
                    EBERApp.user.height = Integer.parseInt(tvHeightText.getText()+"");
                    String userJson = JSON.toJSONString(EBERApp.user);
                    EBERApp.spUtil.putData(SPKey.USER, userJson);
                    startActivity(BindDeviceActivity1.class);
                }
            });
        }
    };
}
