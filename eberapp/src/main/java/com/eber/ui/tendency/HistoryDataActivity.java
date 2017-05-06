package com.eber.ui.tendency;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.bean.BodyInfo;
import com.eber.utils.DisplayUtil;
import com.eber.utils.ShareUtil;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qxinli.umeng.UmengUtil;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by WangLibo on 2017/5/1.
 */

public class HistoryDataActivity extends BaseActivity {


    private ShareUtil shareUtil;
    @ViewInject(R.id.history_data_listview)
    private ListView mListView;

    @ViewInject(R.id.title_content)
    private TextView tvContent;
    @ViewInject(R.id.title_right)
    private ImageView tvRight;

    @ViewInject(R.id.history_data_weight)
    private TextView tvWeight;// 体重
    @ViewInject(R.id.history_data_BMI)
    private TextView tvBMI;// BMI
    @ViewInject(R.id.history_data_bodyage)
    private TextView tvBodayAge;// 身体年龄
    @ViewInject(R.id.history_data_fat)
    private TextView tvFat;// 脂肪率
    @ViewInject(R.id.history_data_water)
    private TextView tvWater;// 体水分
    @ViewInject(R.id.history_data_fat_down)
    private TextView tvFatDown;// 皮下脂肪
    @ViewInject(R.id.history_data_protein)
    private TextView tvProtein;// 蛋白质
    @ViewInject(R.id.history_data_muscle)
    private TextView tvMuscle;// 肌肉量
    @ViewInject(R.id.history_data_bone)
    private TextView tvBone;// 骨量
    @ViewInject(R.id.history_data_neizang)
    private TextView tvNeizang;// 内脏脂肪等级
    @ViewInject(R.id.history_data_basis)
    private TextView tvBasis;// 基础代谢
    @ViewInject(R.id.history_data_bonebad)
    private TextView tvBoneBad;// 骨质疏松

    private CommonAdapter<BodyInfo> mAdapter;
    private List<BodyInfo> bodyInfos;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_history_data);
        super.onCreate(savedInstanceState);
        init();
    }


    private void init() {
        shareUtil = new ShareUtil(this);
        tvContent.setText("历史记录");
        tvRight.setImageResource(R.mipmap.ic_index_share);
        initData();
        mAdapter = new CommonAdapter<BodyInfo>(mContext, R.layout.view_history_data_item, bodyInfos) {
            @Override
            protected void convert(ViewHolder holder, BodyInfo item, final int position) {
                final RelativeLayout rlLineParent = holder.getView(R.id.history_item_line_parent);
                TextView tvTime = holder.getView(R.id.history_item_time);
                tvTime.setText(item.updateTime);
                final View vCircle = holder.getView(R.id.history_item_bule);
                final int dp20 = DisplayUtil.dp2px(mContext, 20);
                final int dp10 = DisplayUtil.dp2px(mContext, 10);
                final ViewGroup.LayoutParams rllineParentlp = rlLineParent.getLayoutParams();
                final ViewGroup.LayoutParams layoutParams = vCircle.getLayoutParams();
                GradientDrawable gd = (GradientDrawable) vCircle.getBackground();
                if (selectedPosition == position) {
                    gd.setCornerRadius(dp20);
                    rllineParentlp.width = dp20;
                    rlLineParent.setLayoutParams(rllineParentlp);
                    layoutParams.width = dp20;
                    layoutParams.height = dp20;
                    vCircle.setLayoutParams(layoutParams);
                } else {
                    gd.setCornerRadius(dp10);
                    rllineParentlp.width = dp10;
                    rlLineParent.setLayoutParams(rllineParentlp);
                    layoutParams.width = dp10;
                    layoutParams.height = dp10;
                    vCircle.setLayoutParams(layoutParams);
                }
                if (position == bodyInfos.size() - 1) {
                    rlLineParent.setVisibility(View.INVISIBLE);
                } else {
                    rlLineParent.setVisibility(View.VISIBLE);
                }
            }
        };
        mAdapter.setSelectedPosition(0);
        mListView.setAdapter(mAdapter);

    }

    private void initData() {
        String json = "[\n" +
                "    {\n" +
                "        \"subcutaneousfat\": 29,\n" +
                "        \"weight\": 65,\n" +
                "        \"updateTime\": \"08:31\",\n" +
                "        \"organfat\": 6,\n" +
                "        \"score\": 80,\n" +
                "        \"muscle\": 39,\n" +
                "        \"bodywater\": 50,\n" +
                "        \"id\": 10,\n" +
                "        \"bonerisk\": 2,\n" +
                "        \"protein\": 16,\n" +
                "        \"bone\": 2,\n" +
                "        \"fatRate\": 25,\n" +
                "        \"BMI\": 23.9,\n" +
                "        \"basicmetabolism\": 1500\n" +
                "    },\n" +
                "    {\n" +
                "        \"subcutaneousfat\": 29,\n" +
                "        \"weight\": 65,\n" +
                "        \"updateTime\": \"08:38\",\n" +
                "        \"organfat\": 6,\n" +
                "        \"score\": 80,\n" +
                "        \"muscle\": 39,\n" +
                "        \"bodywater\": 50,\n" +
                "        \"id\": 13,\n" +
                "        \"bodyAge\": 18,\n" +
                "        \"bonerisk\": 2,\n" +
                "        \"bodyShape\": 1,\n" +
                "        \"protein\": 16,\n" +
                "        \"bone\": 2,\n" +
                "        \"fatRate\": 25,\n" +
                "        \"BMI\": 23.9,\n" +
                "        \"basicmetabolism\": 1500\n" +
                "    },\n" +
                "    {\n" +
                "        \"subcutaneousfat\": 29,\n" +
                "        \"weight\": 65,\n" +
                "        \"updateTime\": \"09:14\",\n" +
                "        \"organfat\": 6,\n" +
                "        \"score\": 80,\n" +
                "        \"muscle\": 39,\n" +
                "        \"bodywater\": 50,\n" +
                "        \"id\": 22,\n" +
                "        \"bodyAge\": 18,\n" +
                "        \"bonerisk\": 2,\n" +
                "        \"bodyShape\": 1,\n" +
                "        \"protein\": 16,\n" +
                "        \"bone\": 2,\n" +
                "        \"fatRate\": 25,\n" +
                "        \"BMI\": 23.9,\n" +
                "        \"basicmetabolism\": 1500\n" +
                "    }\n" +
                "]";

        bodyInfos = JSON.parseArray(json, BodyInfo.class);
        if (bodyInfos != null && bodyInfos.size() > 0) {
            setBodyData(bodyInfos.get(0));
        }
    }

    @Override
    public void setListener() {
        mListView.setOnItemClickListener(itemClickLis);
        tvRight.setOnClickListener(clickLis);
    }

    private AdapterView.OnItemClickListener itemClickLis = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mAdapter.setSelectedPosition(position);
            mAdapter.notifyDataSetChanged();
            BodyInfo bodyInfo = bodyInfos.get(position);
            setBodyData(bodyInfo);
        }
    };

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.title_right:// 分享
                    UmengUtil.shareImage(HistoryDataActivity.this);
                    break;
            }
        }
    };

    /**
     * 设置身体数据
     *
     * @param bodyInfo
     */
    private void setBodyData(BodyInfo bodyInfo) {
        tvWeight.setText(bodyInfo.weight + "kg");
        tvBMI.setText(bodyInfo.BMI);
        tvBodayAge.setText((!TextUtils.isEmpty(bodyInfo.bodyAge) ? bodyInfo.bodyAge : "-") + "岁");
        tvFat.setText(bodyInfo.fatRate + "%");
        tvWater.setText(bodyInfo.bodywater + "%");
        tvFatDown.setText(bodyInfo.subcutaneousfat + "%");
        tvProtein.setText(bodyInfo.protein + "%");
        tvMuscle.setText(bodyInfo.muscle + "%");
        tvBone.setText(bodyInfo.bone + "%");
        tvBoneBad.setText(bodyInfo.bonerisk);
        tvNeizang.setText(bodyInfo.organfat + "级");
        tvBasis.setText(bodyInfo.basicmetabolism + "cal");
    }
}
