package com.eber.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.base.BaseFragment;
import com.eber.fragment.HomeFragment;
import com.eber.ui.find.FindFragment;
import com.eber.ui.my.MyFragment;
import com.eber.ui.tendency.TendencyFragment;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MainActivity extends BaseActivity {

    private BaseFragment mContent;
    private BaseFragment mFragHome;
    private BaseFragment mFragFind;
    private BaseFragment mFragMy;
    private BaseFragment mFragTendency;

    @ViewInject(R.id.index_radioGroup)
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        initContent();
    }


    @Override
    public void setListener() {
        radioGroup.setOnCheckedChangeListener(checkedLis);
    }


    private RadioGroup.OnCheckedChangeListener checkedLis = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.index_home:// 首页
                    if (mFragHome == null) {
                        mFragHome = new HomeFragment();
                    }
                    switchContent(mFragHome);
                    break;
                case R.id.index_tendency:// 趋势
                    if (mFragTendency == null) {
                        mFragTendency = new TendencyFragment();
                    }
                    switchContent(mFragTendency);
                    break;
                case R.id.index_discover:// 发现
                    if (mFragFind == null) {
                        mFragFind = new FindFragment();
                    }
                    switchContent(mFragFind);
                    break;
                case R.id.index_setting:// 我的
                    if (mFragMy == null) {
                        mFragMy = new MyFragment();
                    }
                    switchContent(mFragMy);
                    break;
            }
        }
    };


    /**
     * 初始化显示内容
     **/
    private void initContent() {
        mFragHome = new HomeFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.add(R.id.main_content, mFragHome);
        mContent = mFragHome;
        transaction.commit();
    }

    /**
     * 修改显示的内容 不会重新加载
     **/
    public void switchContent(BaseFragment to) {
        if (mContent != to) {
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            if (!to.isAdded()) { // 先判断是否被add过
                transaction.hide(mContent).add(R.id.main_content, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContent).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            //用commitAllowingStateLoss() 替换 commit()
            //解决 java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState 错误
            mContent = to;
        }
    }


}
