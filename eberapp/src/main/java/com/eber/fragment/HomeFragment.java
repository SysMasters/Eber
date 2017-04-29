package com.eber.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.eber.R;
import com.eber.base.BaseFragment;
import com.eber.bean.Member;
import com.eber.ui.home.LocalMemberActivity;
import com.eber.utils.DisplayUtil;
import com.eber.utils.TextViewUtil;
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

    private PopupWindow mPopupWindow;
    @ViewInject(R.id.index_root)
    private RelativeLayout rlRoot;

    private CommonAdapter<Member> mMembersAdapter;
    private List<Member> members;// 成员列表

    @Override
    public int bindLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onBusiness() {
        ivHead.setOnClickListener(clickLis);
        setData();
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
