package com.eber.bfs.ui.my;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eber.bfs.R;
import com.eber.bfs.adapters.QatAdapter;
import com.eber.bfs.base.BaseActivity;
import com.eber.bfs.bean.QAT;
import com.eber.bfs.http.BaseCallback;
import com.eber.bfs.http.HttpUrls;
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
                    QAT qat = new QAT();
                    qat.typeName = jo.getString("name");
                    qatList.add(qat);
                    List<QAT> qats = JSONArray.parseArray(jo.getString("array"), QAT.class);
                    for (QAT q : qats){
                        qatList.add(q);
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
            QAT item = qatList.get(position);
            if (null == item.typeName || item.typeName.equals(""))
                startWebActivity("常见问题", String.format("http://112.74.62.116:8080/ieber/QAAPP/findQAById.shtml?QAId=%s", qatList.get(position).id));
        }
    };
}
