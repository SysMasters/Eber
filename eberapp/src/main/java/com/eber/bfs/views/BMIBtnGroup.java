package com.eber.bfs.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.eber.bfs.R;
import com.eber.bfs.utils.DisplayUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import static com.eber.bfs.R.id.tendency_week_rb;

/**
 * Created by WangLibo on 2017/4/30.
 */

public class BMIBtnGroup extends LinearLayout {

    private View mView;
    private Context mContext;
    private int mColor;// 当前选中的颜色
    @ViewInject(R.id.tendency_navi_root_rg)
    private NestRadioGroup rgRoot;      // 导航栏外层父控件
    @ViewInject(R.id.tendency_navi_date)
    private RadioGroup rgDate;     // 日期radioGroup

    public BMIBtnGroup(Context context) {
        super(context);
        init(context);
    }

    public BMIBtnGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BMIBtnGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.view_bmi_group, this);
        ViewUtils.inject(mView);

        rgRoot.setOnCheckedChangeListener(nestGroupCheckedLis);
        rgDate.setOnCheckedChangeListener(groupCheckedLis);
        // 默认选中
        rgRoot.check(R.id.tendency_navi_weight_rb);
    }

    public void setCheckedViewColor(int checkId, int color) {
        this.mColor = color;
        setCheckedDateColor(R.id.tendency_week_rb);
        GradientDrawable gradientDrawable = (GradientDrawable) rgRoot.getBackground();
        gradientDrawable.setStroke(DisplayUtil.dp2px(mContext, 2), color);
        int childCount = rgRoot.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = rgRoot.getChildAt(i);
            if (view.getId() == checkId && view instanceof RadioButton) {
                // 设置选中颜色
                RadioButton rbView = (RadioButton) view;
                rbView.setTextColor(Color.WHITE);
                rbView.setBackgroundColor(color);

                GradientDrawable gd = null;
                int dp5 = DisplayUtil.dp2px(mContext, 5);
                float[] radis = null;
                if (i == 0) {
                    radis = new float[]{dp5, dp5, 0, 0, 0, 0, dp5, dp5};
                    // 第一个和最后一个设置圆角
                    gd = new GradientDrawable();//创建drawable
                } else if (i == childCount - 1) {
                    radis = new float[]{0, 0, dp5, dp5, dp5, dp5, 0, 0};
                    gd = new GradientDrawable();//创建drawable
                }
                if (gd != null) {
                    gd.setColor(color);
                    gd.setCornerRadii(radis);
                    rbView.setBackground(gd);
                }
            } else {
                if (view instanceof RadioButton) {
                    // 设置未选中radioButton颜色
                    RadioButton rbView = (RadioButton) view;
                    rbView.setTextColor(color);
                    rbView.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    // 设置分割线颜色
                    view.setBackgroundColor(color);
                }
            }
        }
    }

    public void setCheckedDateColor(int checkedId) {
        int childCount = rgDate.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = rgDate.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton rbView = (RadioButton) view;
                int size = DisplayUtil.dp2px(mContext, 25);
                GradientDrawable gd = new GradientDrawable();
                gd.setShape(GradientDrawable.OVAL);
                gd.setSize(size, size);
                int dp2 = DisplayUtil.dp2px(mContext, 2);
                if (rbView.getId() == checkedId) {
                    rbView.setChecked(true);
                    gd.setColor(mColor);
                    rbView.setTextColor(Color.WHITE);
                } else {
                    gd.setStroke(dp2, mColor);
                    gd.setColor(Color.WHITE);
                    rbView.setTextColor(mColor);
                }
                rbView.setBackground(gd);
            }

        }
    }


    private NestRadioGroup.OnCheckedChangeListener nestGroupCheckedLis = new NestRadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(NestRadioGroup group, int checkedId) {
            int color = -1;
            switch (checkedId) {
                case R.id.tendency_navi_weight_rb:// 体重
                    color = ContextCompat.getColor(mContext, R.color.weight);
                    if (null != onCheckedChangeListener)
                        onCheckedChangeListener.onWeight(color);
                    break;
                case R.id.tendency_navi_fat_rb:// 脂肪率
                    color = ContextCompat.getColor(mContext, R.color.fat);
                    if (null != onCheckedChangeListener)
                        onCheckedChangeListener.onFat(color);
                    break;
                case R.id.tendency_navi_muscle_rb:// 肌肉量
                    color = ContextCompat.getColor(mContext, R.color.muscle);
                    if (null != onCheckedChangeListener)
                        onCheckedChangeListener.onMuscle(color);
                    break;
                case R.id.tendency_navi_water_rb:// 体水分
                    color = ContextCompat.getColor(mContext, R.color.wet);
                    if (null != onCheckedChangeListener)
                        onCheckedChangeListener.onWater(color);
                    break;
                case R.id.tendency_navi_protein_rb://  蛋白质
                    color = ContextCompat.getColor(mContext, R.color.protein);
                    if (null != onCheckedChangeListener)
                        onCheckedChangeListener.onProtein(color);
                    break;
            }
            if (color != -1) {
                setCheckedViewColor(checkedId, color);
                setCheckedDateColor(R.id.tendency_week_rb);
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener groupCheckedLis = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
                case tendency_week_rb:// 周
                    if (null != onCheckedChangeListener)
                        onCheckedChangeListener.onWeek();
                    break;
                case R.id.tendency_month_rb:// 月
                    if (null != onCheckedChangeListener)
                        onCheckedChangeListener.onMonth();
                    break;
                case R.id.tendency_year_rb:// 年
                    if (null != onCheckedChangeListener)
                        onCheckedChangeListener.onYear();
                    break;
            }
            setCheckedDateColor(checkedId);
        }
    };

    private OnCheckedChangeListener onCheckedChangeListener;

    public interface OnCheckedChangeListener {
        /**
         * 体重
         *
         * @param color
         */
        void onWeight(int color);

        /**
         * 脂肪率
         *
         * @param color
         */
        void onFat(int color);

        /**
         * 肌肉量
         *
         * @param color
         */
        void onMuscle(int color);

        /**
         * 体水分
         *
         * @param color
         */
        void onWater(int color);

        /**
         * 蛋白质
         *
         * @param color
         */
        void onProtein(int color);

        /**
         * 周
         */
        void onWeek();

        /**
         * 月
         */
        void onMonth();

        /**
         * 年
         */
        void onYear();

    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener lis) {
        this.onCheckedChangeListener = lis;
    }
}
