package com.eber.ui.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.TextView;

import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.views.ruler.RulerWheel;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 烛九阴 on 2017/5/6.
 * 我的目标
 */

public class MyGoalAct extends BaseActivity {

    @ViewInject(R.id.title_content)
    TextView title;
    @ViewInject(R.id.ruler_view)
    RulerWheel rulerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_mygoal_act);
        super.onCreate(savedInstanceState);
        title.setText("我的目标");

        rulerData();
    }

    @Override
    public void setListener() {

    }
    
    
    private void rulerData(){
        List<String> list = new ArrayList<>();
        for (int i = 20; i < 200; i += 1) {
            list.add(i + "");
            for (int j = 1; j < 10; j++) {
                list.add(i + "." + j);
            }
        }
        rulerView = (RulerWheel) findViewById(R.id.ruler_view);
        rulerView.setData(list);
        rulerView.setSelectedValue("60.4");
        rulerView.setScrollingListener(new RulerWheel.OnWheelScrollListener<String>() {
            @Override
            public void onChanged(RulerWheel wheel, String oldValue, String newValue) {
//                tvCurValue.setText(newValue + "");
            }

            @Override
            public void onScrollingStarted(RulerWheel wheel) {

            }

            @Override
            public void onScrollingFinished(RulerWheel wheel) {

            }
        });
    }
}
