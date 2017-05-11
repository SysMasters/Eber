package com.eber.ui.my;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eber.R;
import com.eber.adapters.QatAdapter;
import com.eber.base.BaseActivity;
import com.eber.bean.QAT;
import com.eber.http.BaseCallback;
import com.eber.http.HttpUrls;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wxd on 2017/5/9.
 */

public class QATActivity extends BaseActivity {

    @ViewInject(R.id.title_content)
    private TextView tvTitle;
    @ViewInject(R.id.qat_lv)
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_qat);
        super.onCreate(savedInstanceState);
        tvTitle.setText("常见问题");
    }

    private List<QAT> qatList = new ArrayList<>();
    private QatAdapter adapter;

    @Override
    public void setListener() {
        lv.setOnItemClickListener(itemClickLis);
        param = new HashMap<>();
        netUtils.get(HttpUrls.FINDQAALL, true, param, new BaseCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                JSONArray ja = JSON.parseArray(response);
                for (int i = 0; i < ja.size(); i++){
                    JSONObject jo = JSON.parseObject(JSON.toJSONString(ja.get(i)));
                    List<QAT> qats = JSONArray.parseArray(jo.getString("array"), QAT.class);
                    for (QAT qat : qats){
                        qatList.add(qat);
                    }
                }
                adapter = new QatAdapter(QATActivity.this, R.layout.layout_qat_item, qatList);
                lv.setAdapter(adapter);
            }
        });
    }


    private AdapterView.OnItemClickListener itemClickLis = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            qatList.get(position);
            startWebActivity(qatList.get(position).title, String.format("http://112.74.62.116:8080/ieber/QAAPP/findQAById.shtml?QAId=%s", qatList.get(position).id));
        }
    };
}
