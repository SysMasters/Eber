package com.eber.ui.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.bean.Equipment;
import com.eber.http.HttpUrls;
import com.eber.http.StringCallback2;
import com.eber.ui.binddevice.BindDeviceActivity2;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;

/**
 * Created by 烛九阴 on 2017/5/6.
 * 设备信息
 */

public class EquipmentInfoAct extends BaseActivity {
    @ViewInject(R.id.title_content)
    TextView title;
    @ViewInject(R.id.remove_device_btn)
    Button btnRemove;

    private TextView equipmentName;
    private String id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_equipment_info_act);
        super.onCreate(savedInstanceState);
        title.setText("设备信息");
        String name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("equipId");
        equipmentName = (TextView)findViewById(R.id.info_name);
        equipmentName.setText(name);
    }

    @Override
    public void setListener() {
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                param = new HashMap<>();
                param.put("memberId", EBERApp.nowUser.id + "");
                param.put("equipId", id);
                netUtils.get(HttpUrls.DELMEMBEREQUIP, true, param, new StringCallback2() {
                    @Override
                    public void onSuccess(String... result) {
                        Toast.makeText(EquipmentInfoAct.this, "删除成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("equipId", id);
                        setResult(11, intent);
                        finish();
                    }
                });
            }
        });
    }
}
