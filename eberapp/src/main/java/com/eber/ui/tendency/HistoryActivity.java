package com.eber.ui.tendency;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsw.calendar.component.MonthView;
import com.dsw.calendar.entity.CalendarInfo;
import com.dsw.calendar.views.CircleCalendarView;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 历史记录
 * Created by WangLibo on 2017/4/30.
 */

public class HistoryActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.history_circleMonthView)
    private CircleCalendarView circleCalendarView;
    @ViewInject(R.id.title_content)
    private TextView tvCenter;
    @ViewInject(R.id.title_right)
    private ImageView ivRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_history);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        tvCenter.setText("历史记录");
        ivRight.setImageResource(R.mipmap.ic_index_share);
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
}
