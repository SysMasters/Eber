package com.eber.ui.tendency;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.eber.R;
import com.eber.base.BaseFragment;
import com.eber.views.BMIBtnGroup;
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
    @ViewInject(R.id.tendency_group)
    private BMIBtnGroup mBmiBtnGroup;


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


    private void setListener() {
        line.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        mBmiBtnGroup.setOnCheckedChangeListener(bmiBtnGroupLis);
    }

    private BMIBtnGroup.OnCheckedChangeListener bmiBtnGroupLis = new BMIBtnGroup.OnCheckedChangeListener() {
        @Override
        public void onWeight(int color) {
            Toast.makeText(mActivity, "体重", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFat(int color) {
            Toast.makeText(mActivity, "脂肪", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMuscle(int color) {
            Toast.makeText(mActivity, "肌肉", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWater(int color) {
            Toast.makeText(mActivity, "体水分", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProtein(int color) {
            Toast.makeText(mActivity, "蛋白质", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onWeek() {
            Toast.makeText(mActivity, "周", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMonth() {
            Toast.makeText(mActivity, "月", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onYear() {
            Toast.makeText(mActivity, "年", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tendency_line:
                startActivity(HistoryActivity.class);
                break;
            case R.id.tendency_share:

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
}
