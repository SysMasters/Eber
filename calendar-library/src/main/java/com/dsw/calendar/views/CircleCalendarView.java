package com.dsw.calendar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsw.calendar.R;
import com.dsw.calendar.component.CircleMonthView;
import com.dsw.calendar.component.MonthView;
import com.dsw.calendar.component.WeekView;
import com.dsw.calendar.entity.CalendarInfo;
import com.dsw.calendar.theme.IDayTheme;
import com.dsw.calendar.theme.IWeekTheme;

import java.util.List;

/**
 * Created by Administrator on 2016/8/7.
 */
public class CircleCalendarView extends LinearLayout implements View.OnClickListener {
    private WeekView weekView;
    private CircleMonthView circleMonthView;
    public TextView textViewYear;

    public void setSelectDate(int year, int month, int day) {
        circleMonthView.setSelectDate(year, month, day);
    }

    public CircleCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        LayoutParams llParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(context).inflate(R.layout.display_grid_date, null);
        weekView = new WeekView(context, null);
        circleMonthView = new CircleMonthView(context, null);
        addView(view, llParams);
        addView(weekView, llParams);
        addView(circleMonthView, llParams);

        view.findViewById(R.id.left).setOnClickListener(this);
        view.findViewById(R.id.right).setOnClickListener(this);
        textViewYear = (TextView) view.findViewById(R.id.year);
        circleMonthView.setMonthLisener(new MonthView.IMonthLisener() {
            @Override
            public void setTextMonth() {
                setDate();
            }
        });
        setDate();
    }

    public void setDate() {
        int year = circleMonthView.getSelYear();
        int month = circleMonthView.getSelMonth() + 1;
        int date = circleMonthView.getSelDay();
        String monthStr = month < 10 ? "0" + month : month + "";
        String dateStr = date < 10 ? "0" + date : date + "";
        textViewYear.setText(year + "." + monthStr + "." + dateStr);
    }

    public String getMonthStr() {
        int month = circleMonthView.getSelMonth() + 1;
        return month < 10 ? "0" + month : month + "";
    }

    public String getDayStr() {
        int date = circleMonthView.getSelDay();
        return date < 10 ? "0" + date : date + "";
    }

    public String getDateStr() {
        int year = circleMonthView.getSelYear();
        int month = circleMonthView.getSelMonth() + 1;
        int date = circleMonthView.getSelDay();
        String monthStr = month < 10 ? "0" + month : month + "";
        String dateStr = date < 10 ? "0" + date : date + "";
        return year + "." + monthStr + "." + dateStr;
    }
    public String getDateStr(int year ,int month,int date) {
//        int year = circleMonthView.currYear;
//        int month = circleMonthView.currMonth + 1;
//        int date = circleMonthView.currDay;
        String monthStr = month < 10 ? "0" + month : month + "";
        String dateStr = date < 10 ? "0" + date : date + "";
        return year + "." + monthStr + "." + dateStr;
    }


    /**
     * 设置日历点击事件
     *
     * @param dateClick
     */
    public void setDateClick(MonthView.IDateClick dateClick) {
        circleMonthView.setDateClick(dateClick);
        setDate();
    }

    /**
     * 设置星期的形式
     *
     * @param weekString 默认值	"日","一","二","三","四","五","六"
     */
    public void setWeekString(String[] weekString) {
        weekView.setWeekString(weekString);
    }

    public void setCalendarInfos(List<CalendarInfo> calendarInfos) {
        circleMonthView.setCalendarInfos(calendarInfos);
        setDate();
    }

    public void setDayTheme(IDayTheme theme) {
        circleMonthView.setTheme(theme);
    }

    public void setWeekTheme(IWeekTheme weekTheme) {
        weekView.setWeekTheme(weekTheme);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.left) {
            circleMonthView.onLeftClick();
        } else {
            circleMonthView.onRightClick();
        }
    }
}
