package com.eber.bfs.ui.tendency;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.eber.bfs.EBERApp;
import com.eber.bfs.R;
import com.eber.bfs.base.BaseFragment;
import com.eber.bfs.bean.Trend;
import com.eber.bfs.http.HttpUrls;
import com.eber.bfs.http.StringCallback2;
import com.eber.bfs.views.BMIBtnGroup;
import com.eber.bfs.views.BrokenLineView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qxinli.umeng.UmengUtil;

import java.util.ArrayList;
import java.util.HashMap;
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
    @ViewInject(R.id.tendency_group)
    private BMIBtnGroup mBmiBtnGroup;

    @Override
    public int bindLayout() {
        return R.layout.fragment_tendency;
    }

    @Override
    public void onBusiness() {

        setListener();

        List<Float> nums = new ArrayList<>();
        nums.add(0f);
        nums.add(0f);
        nums.add(0f);
        nums.add(0f);
        nums.add(0f);
        List<String> texts = new ArrayList<>();
        texts.add("12-21");
        texts.add("12-22");
        texts.add("12-23");
        texts.add("12-24");
        texts.add("12-25");
        line.setNums(nums)      // 数据集
                .setUnitText("%")
                .setTexts(texts)    // 坐标文字集合
                .setTextSize(16)
                .setDivideHeight(5)     // 文字图标间距
                .draw();

    }

    private List<Trend> weekTrendArray = new ArrayList<>();
    private List<Trend> monthTrendArray = new ArrayList<>();
    private List<Trend> yearTrendArray = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        param = new HashMap<>();
        param.put("memberId", String.valueOf(EBERApp.nowUser.id));
        netUtils.get(HttpUrls.RECORDTREND, true, param, new StringCallback2("weekTrendArray",
                "monthTrendArray", "yearTrendArray") {
            @Override
            public void onSuccess(String... result) {
                weekTrendArray = JSONArray.parseArray(result[0], Trend.class);
                monthTrendArray = JSONArray.parseArray(result[1], Trend.class);
                yearTrendArray = JSONArray.parseArray(result[2], Trend.class);
                setValue();
            }
        });
    }

    private void setViewVaule(int property, List<Trend> trends) {
        int[] colors = new int[3];
        List<Float> nums = new ArrayList<>();
        String unit = "%";
        if (property == 1) {
            colors = new int[]{
                    R.color.start_color_1, R.color.end_color_1, R.color.line_color_1
            };
            for (Trend t : trends) {
                nums.add(t.weightAverage);
            }
            unit = "kg";
        } else if (property == 2) {
            colors = new int[]{
                    R.color.start_color_2, R.color.end_color_2, R.color.line_color_2
            };
            for (Trend t : trends) {
                nums.add(t.fatRateAverage);
            }
        } else if (property == 3) {
            colors = new int[]{
                    R.color.start_color_3, R.color.end_color_3, R.color.line_color_3
            };
            for (Trend t : trends) {
                nums.add(t.muscleAverage);
            }
        } else if (property == 4) {
            colors = new int[]{
                    R.color.start_color_4, R.color.end_color_4, R.color.line_color_4
            };
            for (Trend t : trends) {
                nums.add(t.bodywaterAverage);
            }
        } else if (property == 5) {
            colors = new int[]{
                    R.color.start_color_5, R.color.end_color_5, R.color.line_color_5
            };
            for (Trend t : trends) {
                nums.add(t.proteinAverage);
            }
        }
        List<String> texts = new ArrayList<>();
        for (Trend t : trends) {
            if (t.updateTime.length() >= 10) {
                texts.add(t.updateTime.substring(5));
            } else {
                texts.add(t.updateTime);
            }
        }
        line.setNums(nums)      // 数据集
                .setUnitText(unit)
                .setTexts(texts)    // 坐标文字集合
                .setTextSize(16)
                .setDivideHeight(5)     // 文字图标间距
                .setStartColor(mActivity.getResources().getColor(colors[0]))
                .setEndColor(mActivity.getResources().getColor(colors[1]))
                .setBrokenLineColor(mActivity.getResources().getColor(colors[2]))
                .draw();
    }

    private void setListener() {
        line.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        mBmiBtnGroup.setOnCheckedChangeListener(bmiBtnGroupLis);
    }

    int property = 1;
    int timeT = 1;

    private BMIBtnGroup.OnCheckedChangeListener bmiBtnGroupLis = new BMIBtnGroup.OnCheckedChangeListener() {
        @Override
        public void onWeight(int color) {
            // 体重
            property = 1;
            setValue();
        }

        @Override
        public void onFat(int color) {
            // 脂肪
            property = 2;
            setValue();
        }

        @Override
        public void onMuscle(int color) {
            // 肌肉
            property = 3;
            setValue();
        }

        @Override
        public void onWater(int color) {
            // 体水分
            property = 4;
            setValue();
        }

        @Override
        public void onProtein(int color) {
            // 蛋白质
            property = 5;
            setValue();
        }

        @Override
        public void onWeek() {
            // 周
            timeT = 1;
            setValue();
        }

        @Override
        public void onMonth() {
            // 月
            timeT = 2;
            setValue();
        }

        @Override
        public void onYear() {
            // 年
            timeT = 3;
            setValue();
        }
    };

    private void setValue() {
        if (timeT == 1) {
            setViewVaule(property, weekTrendArray);
        } else if (timeT == 2) {
            setViewVaule(property, monthTrendArray);
        } else {
            setViewVaule(property, yearTrendArray);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tendency_line:
                startActivity(HistoryRecordActivity.class);
                break;
            case R.id.tendency_share:
                UmengUtil.shareImage(mActivity);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengUtil.onActivityResult(mActivity, requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        UmengUtil.onDestroy(mActivity);
    }
}
