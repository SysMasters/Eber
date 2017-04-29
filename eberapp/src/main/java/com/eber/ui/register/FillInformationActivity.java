package com.eber.ui.register;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.listener.OnValueChangeListener;
import com.eber.ui.binddevice.BindDeviceActivity1;
import com.eber.views.RulerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/4/21.
 */
public class FillInformationActivity extends BaseActivity {

    @ViewInject(R.id.title_content)
    private TextView tvTitle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fill_information);
        super.onCreate(savedInstanceState);
        tvTitle.setText("填写信息");
    }

    @Override
    public void findViews() {
        ViewUtils.inject(this);
    }

    @Override
    public void setListener() {
        rvHeight.setOnValueChangeListener(heightValueLis);
        rvYear.setOnValueChangeListener(yearValueLis);
        rvMonth.setOnValueChangeListener(monthValueLis);
        btnSuccess.setOnClickListener(clickLis);
    }

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
            startActivity(BindDeviceActivity1.class);
        }
    };
}
