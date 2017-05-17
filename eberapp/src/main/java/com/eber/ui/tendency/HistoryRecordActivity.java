package com.eber.ui.tendency;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dsw.calendar.component.MonthView;
import com.dsw.calendar.entity.CalendarInfo;
import com.dsw.calendar.views.CircleCalendarView;
import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.bean.BodyInfo;
import com.eber.http.BaseCallback;
import com.eber.http.HttpUrls;
import com.eber.http.StringCallback2;
import com.eber.views.decoration.RecycleViewDivider;
import com.eber.views.swipe.SwipeMenuLayout;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qxinli.umeng.UmengUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 历史记录
 * Created by WangLibo on 2017/4/30.
 */

public class HistoryRecordActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.history_circleMonthView)
    private CircleCalendarView circleCalendarView;
    @ViewInject(R.id.title_content)
    private TextView tvCenter;
    @ViewInject(R.id.title_right)
    private ImageView ivRight;
    @ViewInject(R.id.history_recyclerview)
    private RecyclerView recyclerView;
    @ViewInject(R.id.history_month)
    private TextView tvMonth;
    @ViewInject(R.id.history_date)
    private TextView tvDate;

    private CommonAdapter<BodyInfo> mAdapter;
    private List<BodyInfo> bodyInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_history);
        super.onCreate(savedInstanceState);
        init();
    }

    private List<String> date = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void init() {
        tvCenter.setText("历史记录");
        ivRight.setImageResource(R.mipmap.ic_index_share);
        initData();
        tvDate.setText(circleCalendarView.getDateStr());
        tvMonth.setText(circleCalendarView.getMonthStr() + "月");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 20, Color.TRANSPARENT));
        recyclerView.setAdapter(mAdapter = new CommonAdapter<BodyInfo>(mContext, R.layout.view_history_item, bodyInfos) {
            @Override
            protected void convert(final com.zhy.adapter.recyclerview.base.ViewHolder holder, final BodyInfo bodyInfo, int position) {
                TextView tvWeight = holder.getView(R.id.history_weight);
                TextView tvZhifang = holder.getView(R.id.history_zhifang);
                ImageView ivDel = holder.getView(R.id.history_del);
                tvWeight.setText(bodyInfo.weight + "");
                tvZhifang.setText(bodyInfo.fatRate + "");
                //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
                ((SwipeMenuLayout) holder.getConvertView()).setIos(true);
                ivDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteRecord(bodyInfo.id, holder.getAdapterPosition());
                    }
                });
                holder.setOnClickListener(R.id.content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(bodyInfo.updateTime)) {
                            Calendar calendar = Calendar.getInstance();
                            try {
                                calendar.setTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(bodyInfo.updateTime));
                                String year = String.valueOf(calendar.get(Calendar.YEAR));
                                int m = calendar.get(Calendar.MONTH)+1;
                                String month = "-"+m;
                                if (m <10){
                                    month = "-0"+m;
                                }
                                int d = calendar.get(Calendar.DATE);
                                String date = "-"+d;
                                if (d <10){
                                    date = "-0"+d;
                                }
                                HistoryDataActivity.startActivity(mContext,year+month+date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            
                        }
                    }
                });

            }
        });
    }

    /**
     * 删除用户记录
     */
    private void deleteRecord(String recordid, final int position) {
        param = new HashMap<>();
        param.put("recordId", recordid);
        netUtils.get(HttpUrls.DELETEENTITY, true, param, new StringCallback2() {
            @Override
            public void onSuccess(String... result) {
                Toast.makeText(mContext, "记录删除成功", Toast.LENGTH_SHORT).show();
                bodyInfos.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });
    }

    private void initData() {
        bodyInfos = new ArrayList<>();
        param = new HashMap<>();
        param.put("memberId", String.valueOf(EBERApp.nowUser.id));
        netUtils.get(HttpUrls.FINDRECORDDATE, true, param, new StringCallback2("memberRecordArray", "dateArray") {
            @Override
            public void onSuccess(String... result) {
                List<BodyInfo> datas = JSONArray.parseArray(result[0], BodyInfo.class);
                mAdapter.refresh2(datas);
                JSONArray ja = JSON.parseArray(result[1]);
                for (int i = 0; i < ja.size(); i++) {
                    date.add(ja.getString(i));
                }
                List<CalendarInfo> list = new ArrayList<>();
                for (int i = 0; i < date.size(); i++) {
                    int year = Integer.parseInt(date.get(i).substring(0, 4));
                    int month = Integer.parseInt(date.get(i).substring(5, 7));
                    int day = Integer.parseInt(date.get(i).substring(8, date.get(i).length()));
                    list.add(new CalendarInfo(year, month, day, ""));
                }
                circleCalendarView.setCalendarInfos(list);
            }
        });
    }

    @Override
    public void setListener() {
        circleCalendarView.setDateClick(dateClickLis);
        ivRight.setOnClickListener(this);

        //        Calendar calendar = Calendar.getInstance();
        //        int currYear = calendar.get(Calendar.YEAR);
        //        int currMonth = calendar.get(Calendar.MONTH) + 1;
        //        List<CalendarInfo> list = new ArrayList<CalendarInfo>();
        //        list.add(new CalendarInfo(currYear, currMonth, 4, ""));
        //        list.add(new CalendarInfo(currYear, currMonth, 6, ""));
        //        list.add(new CalendarInfo(currYear, currMonth, 12, ""));
        //        list.add(new CalendarInfo(currYear, currMonth, 16, ""));
        //        list.add(new CalendarInfo(currYear, currMonth, 28, ""));
        //        circleCalendarView.setCalendarInfos(list);

    }

    private void getRecordByDate() {
        param = new HashMap<>();
        param.put("memberId", EBERApp.nowUser.id + "");
        param.put("date", circleCalendarView.getDateStr());
        netUtils.get(HttpUrls.FINDRECORDBYDATE, true, param, new BaseCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                JSONObject jo = JSON.parseObject(response);
                int resultCode = jo.getInteger("retcode");
                if (resultCode == 1) {
                    List<BodyInfo> data = JSON.parseArray(jo.getString("memberRecordArray"), BodyInfo.class);
                    mAdapter.refresh2(data);
                } else {
                    String msg = jo.getString("msg");
                    Toast.makeText(EBERApp.sContext, msg, Toast.LENGTH_SHORT).show();
                    mAdapter.refresh2(new ArrayList<BodyInfo>());
                }
            }
        });
    }


    private MonthView.IDateClick dateClickLis = new MonthView.IDateClick() {
        @Override
        public void onClickOnDate(int year, int month, int day) {
            circleCalendarView.setDate();
            tvDate.setText(circleCalendarView.getDateStr());
            tvMonth.setText(circleCalendarView.getMonthStr() + "月");
            getRecordByDate();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:// 分享
                UmengUtil.shareImage(this);
                break;
        }
    }


    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengUtil.onActivityResult(this, requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengUtil.onDestroy(this);
    }
}
