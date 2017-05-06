package com.eber.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.R;
import com.eber.base.BaseFragment;
import com.eber.bean.Indicate;
import com.eber.bean.Member;
import com.eber.bean.MemberRecord;
import com.eber.bean.User;
import com.eber.ui.home.LocalMemberActivity;
import com.eber.ui.slideinfo.SlideInfoActivity;
import com.eber.utils.DisplayUtil;
import com.eber.views.decoration.DividerGridItemDecoration;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qxinli.umeng.UmengUtil;
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
    @ViewInject(R.id.index_title_share)
    private ImageView ivShare;
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

    private CommonAdapter<Indicate> mAdapter;
    private List<Indicate> indicates;

    @Override
    public int bindLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onBusiness() {
        String memberRecordStr = getActivity().getIntent().getStringExtra("memberRecord");
        if (null != memberRecordStr && !memberRecordStr.equals("")) {
            memberRecord = JSON.parseObject(memberRecordStr, MemberRecord.class);
            setViewValues(memberRecord);
            mGridView.setLayoutManager(new GridLayoutManager(mActivity, 3));
            mGridView.addItemDecoration(new DividerGridItemDecoration(mActivity));
            mAdapter = new CommonAdapter<Indicate>(mActivity, R.layout.view_index_item, indicates) {
                @Override
                protected void convert(ViewHolder holder, Indicate item, final int position) {
                    ImageView ivImage = holder.getView(R.id.index_item_image);
                    TextView tvDesc = holder.getView(R.id.index_item_desc);// 脂肪率(14%)
                    TextView tvBody = holder.getView(R.id.index_item_body);//胖瘦
                    GradientDrawable gd = (GradientDrawable) tvBody.getBackground();
                    switch (item.indicateName) {
                        case "体水分":
                            ivImage.setImageResource(R.mipmap.ic_index_wet);
                            tvDesc.setText(item.indicateName + "(" + item.value + "%)");
                            if (item.name.equals("不足")) {
                                gd.setColor(Color.parseColor("#2ce3d7"));// 设置颜色
                            } else if (item.name.equals("标准")) {
                                gd.setColor(Color.parseColor("#69d55f"));// 设置颜色
                            } else if (item.name.equals("优秀")) {
                                gd.setColor(Color.parseColor("#2aac18"));// 设置颜色
                            }
                            break;
                        case "皮下脂肪":
                            ivImage.setImageResource(R.mipmap.ic_index_axunge);
                            tvDesc.setText(item.indicateName + "(" + item.value + "%)");
                            if (item.name.equals("偏低")) {
                                gd.setColor(Color.parseColor("#2ce3d7"));// 设置颜色
                            } else if (item.name.equals("标准")) {
                                gd.setColor(Color.parseColor("#69d55f"));// 设置颜色
                            } else if (item.name.equals("偏高")) {
                                gd.setColor(Color.parseColor("#ff7485"));// 设置颜色
                            }
                            break;
                        case "骨质疏松风险":
                            ivImage.setImageResource(R.mipmap.ic_index_rarefaction);
                            tvDesc.setEms(6);
                            tvDesc.setText(item.indicateName + item.value + "");
                            if (item.name.equals("低风险")) {
                                gd.setColor(Color.parseColor("#69d55f"));// 设置颜色
                            } else if (item.name.equals("中风险")) {
                                gd.setColor(Color.parseColor("#ff7485"));// 设置颜色
                            } else if (item.name.equals("高风险")) {
                                gd.setColor(Color.parseColor("#f5495d"));// 设置颜色
                            }
                            break;
                        case "蛋白质":
                            ivImage.setImageResource(R.mipmap.ic_index_protein);
                            tvDesc.setText(item.indicateName + "(" + item.value + "%)");
                            if (item.name.equals("不足")) {
                                gd.setColor(Color.parseColor("#2ce3d7"));// 设置颜色
                            } else if (item.name.equals("标准")) {
                                gd.setColor(Color.parseColor("#69d55f"));// 设置颜色
                            } else if (item.name.equals("优秀")) {
                                gd.setColor(Color.parseColor("#2aac18"));// 设置颜色
                            }
                            break;
                        case "骨量":
                            ivImage.setImageResource(R.mipmap.ic_index_bone);
                            tvDesc.setText(item.indicateName + item.value + "kg");
                            if (item.name.equals("不足")) {
                                gd.setColor(Color.parseColor("#2ce3d7"));// 设置颜色
                            } else if (item.name.equals("标准")) {
                                gd.setColor(Color.parseColor("#69d55f"));// 设置颜色
                            } else if (item.name.equals("优秀")) {
                                gd.setColor(Color.parseColor("#2aac18"));// 设置颜色
                            }
                            break;
                        case "内脏脂肪等级":
                            ivImage.setImageResource(R.mipmap.ic_index_viscera);
                            tvDesc.setEms(6);
                            tvDesc.setText(item.indicateName + item.value + "级");
                            if (item.name.equals("标准")) {
                                gd.setColor(Color.parseColor("#69d55f"));// 设置颜色
                            } else if (item.name.equals("偏高")) {
                                gd.setColor(Color.parseColor("#ff7485"));// 设置颜色
                            } else if (item.name.equals("危险")) {
                                gd.setColor(Color.parseColor("#f5495d"));// 设置颜色
                            }
                            break;
                        case "肌肉量":
                            ivImage.setImageResource(R.mipmap.ic_index_muscle);
                            tvDesc.setText(item.indicateName + "(" + item.value + "%)");
                            if (item.name.equals("不足")) {
                                gd.setColor(Color.parseColor("#2ce3d7"));// 设置颜色
                            } else if (item.name.equals("标准")) {
                                gd.setColor(Color.parseColor("#69d55f"));// 设置颜色
                            } else if (item.name.equals("优秀")) {
                                gd.setColor(Color.parseColor("#2aac18"));// 设置颜色
                            }
                            break;
                        case "脂肪率":
                            ivImage.setImageResource(R.mipmap.ic_index_axunge_percentage);
                            tvDesc.setText(item.indicateName + "(" + item.value + "%)");
                            if (item.name.equals("偏瘦")) {
                                gd.setColor(Color.parseColor("#2ce3d7"));// 设置颜色
                            } else if (item.name.equals("标准")) {
                                gd.setColor(Color.parseColor("#69d55f"));// 设置颜色
                            } else if (item.name.equals("微胖")) {
                                gd.setColor(Color.parseColor("#ff7485"));// 设置颜色
                            } else if (item.name.equals("肥胖")) {
                                gd.setColor(Color.parseColor("#2aac18"));// 设置颜色
                            }
                            break;
                        case "基础代谢":
                            ivImage.setImageResource(R.mipmap.ic_index_metabolism);
                            tvDesc.setText(item.indicateName + "\n" + item.value + "卡路里");
                            //                        2ce3d7 不达标     69d55f 标准    ff7485 偏高    f5495d 严重   2aac18 优
                            if (item.name.equals("偏低")) {
                                gd.setColor(Color.parseColor("#2ce3d7"));// 设置颜色
                            } else if (item.name.equals("优秀")) {
                                gd.setColor(Color.parseColor("#2aac18"));// 设置颜色
                            }
                            break;
                    }
                    tvBody.setText(item.name);
                    holder.setOnClickListener(R.id.index_item_root, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 跳转至滑屏页
                            Intent intent = new Intent(mActivity, SlideInfoActivity.class);
                            mActivity.startActivity(intent);
                        }
                    });

                }
            };
            mGridView.setAdapter(mAdapter);
        }
        String usersStr = getActivity().getIntent().getStringExtra("memberArray");
        if (usersStr != null && !usersStr.equals("")) {
            users = JSON.parseArray(usersStr, User.class);
        }
        ivHead.setOnClickListener(clickLis);
        ivShare.setOnClickListener(clickLis);
        setData();
        initBodyInfoData();
    }

    private void initBodyInfoData() {
        indicates = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Indicate b = new Indicate();
            indicates.add(b);
        }
    }

    private void setViewValues(MemberRecord m) {
        JSONObject jo = JSON.parseObject(m.weightChange);
        double weightChange = jo.getDouble("weightChange");
        String weightChangeStr = jo.getString("weightChangeStr");
        jo = JSON.parseObject(m.fatRateChange);
        double fatRateChange = jo.getDouble("fatRateChange");
        String BMIChangeStr = jo.getString("fatRateChangeStr");
        ;
        tvTitleLabel.setText(m.updateTime);
        tvScore.setText(m.score + "");
        tvWeight.setText(m.weight + "");
        tvAge.setText(m.bodyAge + "");
        tvBMIChange.setText(fatRateChange + "%");
        if (fatRateChange >= 0) {
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
        indicates = JSON.parseArray(m.indicateType, Indicate.class);
        Indicate indicate = null;
        for (int i = 0; i < indicates.size(); i++) {
            if (indicates.get(i).indicateName.equals("BMI")) {
                indicate = indicates.get(i);
                indicates.remove(i);
            }
        }
        if (null != indicate) {
            tvBMI.setText(indicate.value);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    private void setData() {
        //        String weight = "48.1";
        //        String bmi = "25.6";
        //        SpannableString weightSpannableString = TextViewUtil.getSizeSpanSpToPx(mActivity, weight, weight.length() - 1, weight.length(), 35);
        //        tvWeight.setText(weightSpannableString);
        //        SpannableString bmiSpannableString = TextViewUtil.getSizeSpanSpToPx(mActivity, weight, bmi.length() - 1, bmi.length(), 18);
        //        tvBMI.setText(bmiSpannableString);

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
                case R.id.index_title_share:// 分享
                    UmengUtil.shareImage(mActivity);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengUtil.onActivityResult(mActivity, requestCode, resultCode, data);
    }
}
