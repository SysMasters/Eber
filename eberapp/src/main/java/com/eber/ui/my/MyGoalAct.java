package com.eber.ui.my;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.TextView;

import com.eber.R;
import com.eber.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by 烛九阴 on 2017/5/6.
 * 我的目标
 */

public class MyGoalAct extends BaseActivity {

    @ViewInject(R.id.title_content)
    TextView title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_mygoal_act);
        super.onCreate(savedInstanceState);
        title.setText("我的目标");
    }

    @Override
    public void setListener() {

    }
}
