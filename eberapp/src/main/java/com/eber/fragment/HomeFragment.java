package com.eber.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eber.R;
import com.eber.base.BaseFragment;
import com.eber.utils.DisplayUtil;
import com.eber.utils.TextViewUtil;
import com.lidroid.xutils.view.annotation.ViewInject;

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
    }

    private void showUserPopupWindow() {
        View popupView = mActivity.getLayoutInflater().inflate(R.layout.popup_index_user, null);
        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.showAtLocation(rlRoot, 0, 0, DisplayUtil.dp2px(mActivity, 25));

        ImageView ivClose = (ImageView) popupView.findViewById(R.id.popup_index_user_close);
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
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.index_title_head:
                    showUserPopupWindow();
                    break;
            }
        }
    };

}
