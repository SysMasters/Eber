package com.eber.ui.tendency;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsw.calendar.component.MonthView;
import com.dsw.calendar.entity.CalendarInfo;
import com.dsw.calendar.views.CircleCalendarView;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.bean.BodyInfo;
import com.eber.views.RecycleViewDivider;
import com.eber.views.swipe.SwipeMenuLayout;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.Calendar;
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
            protected void convert(final com.zhy.adapter.recyclerview.base.ViewHolder holder, BodyInfo bodyInfo, int position) {
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
                        bodyInfos.remove(holder.getAdapterPosition());
                        mAdapter.notifyItemRemoved(holder.getAdapterPosition());
                    }
                });
                holder.setOnClickListener(R.id.content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(HistoryDataActivity.class);
                    }
                });

            }
        });
    }

    private void initData() {
        bodyInfos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BodyInfo b = new BodyInfo();
            b.weight = "72";
            b.fatRate = "43.5";
            bodyInfos.add(b);
        }
    }

    @Override
    public void setListener() {
        circleCalendarView.setDateClick(dateClickLis);
        ivRight.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        int currYear = calendar.get(Calendar.YEAR);
        int currMonth = calendar.get(Calendar.MONTH) + 1;
        List<CalendarInfo> list = new ArrayList<CalendarInfo>();
        list.add(new CalendarInfo(currYear, currMonth, 4, ""));
        list.add(new CalendarInfo(currYear, currMonth, 6, ""));
        list.add(new CalendarInfo(currYear, currMonth, 12, ""));
        list.add(new CalendarInfo(currYear, currMonth, 16, ""));
        list.add(new CalendarInfo(currYear, currMonth, 28, ""));
        circleCalendarView.setCalendarInfos(list);

    }


    private MonthView.IDateClick dateClickLis = new MonthView.IDateClick() {
        @Override
        public void onClickOnDate(int year, int month, int day) {
            circleCalendarView.setDate();
            tvDate.setText(circleCalendarView.getDateStr());
            tvMonth.setText(circleCalendarView.getMonthStr() + "月");
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:// 分享
                Toast.makeText(mContext, "分享", Toast.LENGTH_SHORT).show();
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
}
