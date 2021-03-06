package com.eber.bfs.ui.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eber.bfs.EBERApp;
import com.eber.bfs.R;
import com.eber.bfs.base.BaseActivity;
import com.eber.bfs.http.BaseCallback;
import com.eber.bfs.http.HttpUrls;
import com.eber.bfs.views.ruler.RulerWheel;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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
    @ViewInject(R.id.goal_scope)
    TextView tvScope;
    @ViewInject(R.id.goal_now_wight)
    TextView tvNowWigth;
    @ViewInject(R.id.target_value)
    TextView tvTargetValue;
    @ViewInject(R.id.goal_xujianzhong)
    TextView tvXu;
    @ViewInject(R.id.goal_datalen)
    TextView tvDateLen;
    @ViewInject(R.id.goal_btn)
    Button btn;


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
        btn.setOnClickListener(clickLis);
        param = new HashMap<>();
        param.put("memberId", String.valueOf(EBERApp.nowUser.id));
        netUtils.get(HttpUrls.FINDMEMBERAIM, true, param, new BaseCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                JSONObject jo = JSON.parseObject(response);
                if (jo.getInteger("retcode") != 1){
                    Toast.makeText(MyGoalAct.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                }else {
                    tvScope.setText(jo.getString("weightMin")+" - "+jo.getString("weightMax")+ " kg");
                    tvNowWigth.setText(jo.getString("weightNow")+"");
                    tvTargetValue.setText(jo.getString("weightAim"));
                    rulerView.setSelectedValue(Double.parseDouble(jo.getString("weightAim")) % 1 == 0
                            ? (int)Double.parseDouble(jo.getString("weightAim"))+""
                            : jo.getString("weightAim"));
                    tvDateLen.setText(jo.getString("needDay")+"天");
                    tvXu.setText(jo.getString("aimType")+jo.getString("needKg"));
                }
            }
        });
    }

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            param = new HashMap<>();
            param.put("memberId", String.valueOf(EBERApp.nowUser.id));
            param.put("weightAim", tvTargetValue.getText().toString());
            netUtils.get(HttpUrls.ADDORUPDATEAIM, true, param, new BaseCallback() {
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    JSONObject jo = JSON.parseObject(response);
                    Toast.makeText(MyGoalAct.this, jo.getString("msg"), Toast.LENGTH_SHORT).show();
                    if (jo.getInteger("retcode") == 1){
                        finish();
                    }
                }
            });
        }
    };
    
    
    private void rulerData(){
        List<String> list = new ArrayList<>();
        for (int i = 30; i <= 150; i += 1) {
            list.add(i + "");
            if(i != 150)
                for (int j = 1; j < 10; j++) {
                    list.add(i + "." + j);
                }
        }
        rulerView = (RulerWheel) findViewById(R.id.ruler_view);
        rulerView.setData(list);
        rulerView.setSelectedValue("60");
        rulerView.setScrollingListener(new RulerWheel.OnWheelScrollListener<String>() {
            @Override
            public void onChanged(RulerWheel wheel, String oldValue, String newValue) {
//                tvCurValue.setText(newValue + "");
                tvTargetValue.setText(newValue);
                double nowWigth = Double.parseDouble(tvNowWigth.getText().toString());
                double cha = nowWigth - Double.parseDouble(newValue);
                double chaju = Math.abs(cha);
                BigDecimal b   =   new   BigDecimal(chaju);
                String   f1   =   b.setScale(1,   BigDecimal.ROUND_HALF_UP).toString();
                tvDateLen.setText((int) (chaju * 7) +"天");
                if (cha > 0){
                    tvXu.setText("减重"+f1);
                }else{
                    tvXu.setText("增重"+f1);
                }

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
