package com.eber.ui.tendency;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.eber.R;
import com.eber.base.BaseFragment;
import com.eber.views.BrokenLineView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wxd on 2017/4/29.
 * 趋势
 */

public class TendencyFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.tendency_line)
    private BrokenLineView line;    // 折线图控件
    @ViewInject(R.id.tendency_share)
    private ImageView imgShare;     // 分享
    @ViewInject(R.id.tendency_navi_root_rg)
    private RadioGroup rgRoot;      // 导航栏外层父控件
    @ViewInject(R.id.tendency_navi_weight_rb)
    private RadioButton rbWeight;    // 体重
    @ViewInject(R.id.tendency_navi_fat_rb)
    private RadioButton rbFat;    // 脂肪率
    @ViewInject(R.id.tendency_navi_muscle_rb)
    private RadioButton rbMuscle;    // 肌肉量
    @ViewInject(R.id.tendency_navi_water_rb)
    private RadioButton rbWater;    // 体水分
    @ViewInject(R.id.tendency_navi_protein_rb)
    private RadioButton rbProtein;    // 蛋白质
    /* 上方导航栏间隔控件  设置颜色 */
    @ViewInject(R.id.tendency_navi_line1)
    private View tendency_navi_line1;
    @ViewInject(R.id.tendency_navi_line2)
    private View tendency_navi_line2;
    @ViewInject(R.id.tendency_navi_line3)
    private View tendency_navi_line3;
    @ViewInject(R.id.tendency_navi_line4)
    private View tendency_navi_line4;
    @ViewInject(R.id.tendency_week_rb)
    private RadioButton rbWeek;     // 周
    @ViewInject(R.id.tendency_month_rb)
    private RadioButton rbMonth;    // 月
    @ViewInject(R.id.tendency_year_rb)
    private RadioButton rbYear;     // 年

    List<View> views = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.fragment_tendency;
    }

    @Override
    public void onBusiness() {

        setListener();

        List<Float> nums = new ArrayList<>();
        nums.add(14f);
        nums.add(20f);
        nums.add(12.5f);
        nums.add(15.5f);
        nums.add(17.2f);
        List<String> texts = new ArrayList<>();
        texts.add("12-21");
        texts.add("12-22");
        texts.add("12-23");
        texts.add("12-24");
        texts.add("12-25");
        line.setNums(nums)      // 数据集
                .setTexts(texts)    // 坐标文字集合
                .setTextSize(16)
                .setDivideHeight(5)     // 文字图标间距
                .draw();
    }

    private void addViews(){
        views.add(tendency_navi_line1);
        views.add(tendency_navi_line2);
        views.add(tendency_navi_line3);
        views.add(tendency_navi_line4);
        views.add(rgRoot);
        views.add(rbWeight);
        views.add(rbFat);
        views.add(rbMuscle);
        views.add(rbWater);
        views.add(rbProtein);
        views.add(rbWeek);
        views.add(rbMonth);
        views.add(rbYear);
    }

    private void setListener(){
        line.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        rbWeight.setOnClickListener(this);
        rbFat.setOnClickListener(this);
        rbMuscle.setOnClickListener(this);
        rbWater.setOnClickListener(this);
        rbProtein.setOnClickListener(this);
        rbWeek.setOnClickListener(this);
        rbMonth.setOnClickListener(this);
        rbYear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tendency_line:

                break;
            case R.id.tendency_share:

                break;
            case R.id.tendency_navi_weight_rb:
                setViewColor(/*R.id.tendency_navi_weight_rb, */R.color.weight);
                break;
            case R.id.tendency_navi_fat_rb:
                setViewColor(/*R.id.tendency_navi_weight_rb, */R.color.fat);
                break;
            case R.id.tendency_navi_muscle_rb:

                break;
            case R.id.tendency_navi_water_rb:

                break;
            case R.id.tendency_navi_protein_rb:

                break;
            case R.id.tendency_week_rb:

                break;
            case R.id.tendency_month_rb:

                break;
            case R.id.tendency_year_rb:

                break;
        }
    }

    private void setViewColor(int color){
//        tendency_navi_line1.setBackgroundColor(mActivity.getResources().getColor(color));
//        tendency_navi_line2.setBackgroundColor(mActivity.getResources().getColor(color));
//        tendency_navi_line3.setBackgroundColor(mActivity.getResources().getColor(color));
//        tendency_navi_line4.setBackgroundColor(mActivity.getResources().getColor(color));
        GradientDrawable gd = (GradientDrawable) rgRoot.getBackground();
        gd.setStroke(4, color);
        rgRoot.setBackground(gd);
//        rbWeight.setBackgroundColor(mActivity.getResources().getColor(color));
//        rbFat.setTextColor(color);
//        rbMuscle.setBackgroundColor(mActivity.getResources().getColor(color));
//        rbWater.setBackgroundColor(mActivity.getResources().getColor(color));
//        rbProtein.setBackgroundColor(mActivity.getResources().getColor(color));
//        rbWeek
//        rbMonth
//        rbYear
    }
}
