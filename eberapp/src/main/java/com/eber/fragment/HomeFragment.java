package com.eber.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.R;
import com.eber.base.BaseFragment;
import com.eber.bean.BodyIndex;
import com.eber.bean.Member;
import com.eber.bean.MemberRecord;
import com.eber.bean.User;
import com.eber.ui.home.LocalMemberActivity;
import com.eber.utils.DisplayUtil;
import com.eber.utils.TextViewUtil;
import com.eber.views.decoration.DividerGridItemDecoration;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangLibo on 2017/4/23.
 */

public class HomeFragment extends BaseFragment {

    @ViewInject(R.id.index_title_head)
    private ImageView ivHead;
    @ViewInject(R.id.index_title_label)
    private TextView tvTitleLabel;
    @ViewInject(R.id.index_weight)
    private TextView tvWeight;// 体重
    @ViewInject(R.id.index_bmi)
    private TextView tvBMI;// BMI
    @ViewInject(R.id.index_score)
    private TextView tvScore;   // 得分
    @ViewInject(R.id.index_bodyage)
    private TextView tvAge;   // 年龄
    @ViewInject(R.id.index_bmi_change)
    private TextView tvBMIChange;   // 脂肪变化
    @ViewInject(R.id.index_bmi_change_img)
    private ImageView imgBMIChange;   // 脂肪变化箭头图
    @ViewInject(R.id.index_weightChange)
    private TextView tvweightChange;   // 体重变化
    @ViewInject(R.id.index_weightChange_img)
    private ImageView imgweightChange;   // 体重变化箭头图
    @ViewInject(R.id.index_change_text)
    private TextView tvChange;   // 体重、脂肪变化概述


    private PopupWindow mPopupWindow;
    @ViewInject(R.id.index_root)
    private RelativeLayout rlRoot;

    @ViewInject(R.id.index_gridView)
    private RecyclerView mGridView;

    private CommonAdapter<Member> mMembersAdapter;
    private List<Member> members;// 成员列表
    private MemberRecord memberRecord;      // 称重记录
    private List<User> users;      // 用户与子用户集合

    private CommonAdapter<BodyIndex> mAdapter;
    private List<BodyIndex> bodyIndices;

    @Override
    public int bindLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onBusiness() {
        memberRecord = JSON.parseObject(getActivity().getIntent().getStringExtra("memberRecord"), MemberRecord.class);
        users = JSON.parseArray(getActivity().getIntent().getStringExtra("memberArray"), User.class);
        setViewValues(memberRecord);
        ivHead.setOnClickListener(clickLis);
        setData();
        initBodyInfoData();

        mGridView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        mGridView.addItemDecoration(new DividerGridItemDecoration(mActivity));
        mAdapter = new CommonAdapter<BodyIndex>(mActivity, R.layout.view_index_item, bodyIndices) {
            @Override
            protected void convert(ViewHolder holder, BodyIndex item, final int position) {
                ImageView ivImage = holder.getView(R.id.index_item_image);
                TextView tvDesc = holder.getView(R.id.index_item_desc);// 脂肪率(14%)
                tvDesc.setText("脂肪率" + "(14%)");
                TextView tvBody = holder.getView(R.id.index_item_body);//胖瘦

                GradientDrawable gd = (GradientDrawable) tvBody.getBackground();
                gd.setColor(Color.parseColor("#00fff0"));// 设置颜色
                holder.setOnClickListener(R.id.index_item_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "" + position, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };
        mGridView.setAdapter(mAdapter);

    }

    private void initBodyInfoData() {
        bodyIndices = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BodyIndex b = new BodyIndex();
            bodyIndices.add(b);
        }
    }

    private void setViewValues(MemberRecord m) {
        JSONObject jo = JSON.parseObject(m.weightChange);
        double weightChange = jo.getDouble("weightChange");
        String weightChangeStr = jo.getString("weightChangeStr");
        jo = JSON.parseObject(m.BMIChange);
        double BMIChange = jo.getDouble("weightChange");
        String BMIChangeStr = jo.getString("BMIChangeStr");
        ;
        tvTitleLabel.setText(m.updateTime);
        tvScore.setText(m.score + "");
        tvWeight.setText(m.weight + "");
        tvAge.setText(m.bodyAge + "");
        tvBMIChange.setText(BMIChange + "%");
        if (BMIChange >= 0) {
            imgBMIChange.setImageResource(R.mipmap.ic_index_up);
        } else {
            imgBMIChange.setImageResource(R.mipmap.ic_index_down);
        }
        tvweightChange.setText(weightChange + "kg");
        if (weightChange >= 0) {
            imgweightChange.setImageResource(R.mipmap.ic_index_up);
        } else {
            imgweightChange.setImageResource(R.mipmap.ic_index_down);
        }
        tvChange.setText(weightChangeStr + "" + BMIChangeStr);

        jo = JSON.parseObject(m.indicateType);
        List<BodyIndex> bodyIndices = new ArrayList<>();
        BodyIndex bi = new BodyIndex();
        bi = JSON.parseObject(jo.getString("fatRate"), BodyIndex.class);
        bodyIndices.add(bi);
        bi = JSON.parseObject(jo.getString("bodywater"), BodyIndex.class);
        bodyIndices.add(bi);
        bi = JSON.parseObject(jo.getString("subcutaneousfat"), BodyIndex.class);
        bodyIndices.add(bi);
        bi = JSON.parseObject(jo.getString("protein"), BodyIndex.class);
        bodyIndices.add(bi);
        bi = JSON.parseObject(jo.getString("muscle"), BodyIndex.class);
        bodyIndices.add(bi);
        bi = JSON.parseObject(jo.getString("bone"), BodyIndex.class);
        bodyIndices.add(bi);
        bi = JSON.parseObject(jo.getString("organfat"), BodyIndex.class);
        bodyIndices.add(bi);
        bi = JSON.parseObject(jo.getString("basicmetabolism"), BodyIndex.class);
        bodyIndices.add(bi);
        bi = JSON.parseObject(jo.getString("bonerisk"), BodyIndex.class);
        bodyIndices.add(bi);
        bi = JSON.parseObject(jo.getString("BMI"), BodyIndex.class);
        tvBMI.setText(bi.value);

        // 创建adapter 传入集合bodyIndices
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private void setData() {
        String weight = "48.1";
        String bmi = "25.6";
        SpannableString weightSpannableString = TextViewUtil.getSizeSpanSpToPx(mActivity, weight, weight.length() - 1, weight.length(), 35);
        tvWeight.setText(weightSpannableString);
        SpannableString bmiSpannableString = TextViewUtil.getSizeSpanSpToPx(mActivity, weight, bmi.length() - 1, bmi.length(), 18);
        tvBMI.setText(bmiSpannableString);

        loadMemberData();// 成员列表
    }


    private void showUserPopupWindow() {
        View popupView = mActivity.getLayoutInflater().inflate(R.layout.popup_index_user, null);
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.showAtLocation(rlRoot, 0, 0, DisplayUtil.dp2px(mActivity, 25));
        RecyclerView recyclerView = (RecyclerView) popupView.findViewById(R.id.popup_index_user_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(mMembersAdapter = new CommonAdapter<Member>(mActivity, R.layout.view_popup_index_user_item, members) {
            @Override
            protected void convert(ViewHolder holder, Member member, int position) {
                holder.setImageResource(R.id.index_user_head, (TextUtils.equals("1", member.sex) ? R.mipmap.ic_index_head_male : R.mipmap.ic_index_head_woman));
                holder.setOnClickListener(R.id.index_user_root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
            }
        });
        ImageView ivClose = (ImageView) popupView.findViewById(R.id.popup_index_user_close);
        LinearLayout many = (LinearLayout) popupView.findViewById(R.id.index_user_many);
        LinearLayout create = (LinearLayout) popupView.findViewById(R.id.index_user_create);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        many.setOnClickListener(clickLis);
        create.setOnClickListener(clickLis);

    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.index_title_head:
                    showUserPopupWindow();
                    break;
                case R.id.index_user_create:// 创建新成员

                    break;
                case R.id.index_user_many:// 更多成员
                    startActivity(LocalMemberActivity.class);
                    mPopupWindow.dismiss();
                    break;
            }
        }
    };

    private void loadMemberData() {
        members = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Member member = new Member("" + i, "" + i, "男", "成员" + (i + 1));
            if (i == 2 || i == 4 || i == 7) {
                member.sex = "女";
            }
            members.add(member);
        }

    }


}
