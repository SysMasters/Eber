package com.eber.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.eber.EBERApp;
import com.eber.R;
import com.eber.adapters.EquipmentManagementAdapter;
import com.eber.base.BaseActivity;
import com.eber.bean.Equipment;
import com.eber.http.HttpUrls;
import com.eber.http.StringCallback2;
import com.eber.ui.binddevice.BindDeviceActivity1;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by 烛九阴 on 2017/5/6.
 * 设备管理
 */

public class EquipmentManagementAct extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.title_right)
    private TextView tvAdd;
    @ViewInject(R.id.title_content)
    TextView title;
    private ListView list;
    private List<Equipment> alls;
    private EquipmentManagementAdapter equipmentAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_equipment_management_act);
        super.onCreate(savedInstanceState);
        tvAdd.setText("＋");
        tvAdd.setTextSize(25);
        title.setText("设备管理");
        list = (ListView) findViewById(R.id.message_view);
        initListView();
    }

    @Override
    public void setListener() {
        tvAdd.setOnClickListener(this);
    }

    /**
     * ListView初始化方法
     */
    private void initListView() {
        loadData();
        equipmentAdapter = new EquipmentManagementAdapter(this, alls);
        list.setAdapter(equipmentAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent in = new Intent(EquipmentManagementAct.this, EquipmentInfoAct.class);
                String name = equipmentAdapter.getData().get(position).typename;
                in.putExtra("name", name);
                in.putExtra("equipId", equipmentAdapter.getData().get(position).equipId);
                startActivityForResult(in, 11);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == 11 && data != null){
            for (int i  = 0; i < alls.size(); i++) {
                if (data.getStringExtra("equipId").equals(alls.get(i).equipId)) {
                    equipmentAdapter.getData().remove(i);
                }
            }
            equipmentAdapter.notifyDataSetChanged();
        }
    }

    private void loadData() {
        netUtils.get(String.format(HttpUrls.FINDEQUIPLIST, EBERApp.nowUser.id + ""), true, null, new StringCallback2("memberEquipArray") {
            @Override
            public void onSuccess(String... result) {
                JSONArray ja = JSON.parseArray(result[0]);
                alls = JSON.parseArray(result[0], Equipment.class);
                equipmentAdapter.getData().addAll(alls);
                equipmentAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:// 添加设备
                startActivity(BindDeviceActivity1.class);
                break;
        }
    }
}
