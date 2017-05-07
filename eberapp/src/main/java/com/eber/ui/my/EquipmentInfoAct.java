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
 * 设备信息
 */

public class EquipmentInfoAct extends BaseActivity {
    @ViewInject(R.id.title_content)
    TextView title;
    private TextView equipmentName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_equipment_info_act);
        super.onCreate(savedInstanceState);
        title.setText("设备信息");
        String name = getIntent().getStringExtra("name");
        equipmentName = (TextView)findViewById(R.id.info_name);
        equipmentName.setText(name);
    }

    @Override
    public void setListener() {

    }
}
