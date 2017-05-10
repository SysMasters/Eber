package com.eber.ui.my;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.eber.EBERApp;
import com.eber.R;
import com.eber.base.BaseActivity;
import com.eber.utils.SPKey;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by 烛九阴 on 2017/5/6.
 * 我的提醒
 */

public class MyRemindAct extends BaseActivity {

    @ViewInject(R.id.title_content)
    TextView title;
    @ViewInject(R.id.remind_no_state)
    TextView tvState;
    @ViewInject(R.id.remind_zao_img)
    ImageView imgZao;
    @ViewInject(R.id.remind_zhong_img)
    ImageView imgZhong;
    @ViewInject(R.id.remind_wan_img)
    ImageView imgWan;
    @ViewInject(R.id.remind_shequ_img)
    ImageView imgShequ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_myremind_act);
        super.onCreate(savedInstanceState);
        title.setText("我的提醒");
        String r = EBERApp.spUtil.getStringData(SPKey.REMIND);
        if (!r.equals("")){
            String[] rs = r.split(",");
            zao = Boolean.parseBoolean(rs[0]);
            zhong = Boolean.parseBoolean(rs[1]);
            wan = Boolean.parseBoolean(rs[2]);
            shequ = Boolean.parseBoolean(rs[3]);
            setViewVaule(zao, zhong, wan, shequ);
        }
    }

    /**
     * 开启提醒
     */
    private void startRemind(int hour){

        //得到日历实例，主要是为了下面的获取时间
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();

        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        //获取上面设置的时间的毫秒值
        long selectTime = mCalendar.getTimeInMillis();

        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if(systemTime > selectTime) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //AlarmReceiver.class为广播接受者
        Intent intent = new Intent(MyRemindAct.this, AutoReceiver.class);
        intent.setAction("VIDEO_TIMER");
        intent.putExtra("hour", hour);
        PendingIntent pi = PendingIntent.getBroadcast(MyRemindAct.this, hour, intent, 0);
        //得到AlarmManager实例
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
    }


    /**
     * 关闭提醒
     */
    private void stopRemind(int hour){
        Intent intent = new Intent(MyRemindAct.this, AutoReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(MyRemindAct.this, hour,
                intent, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        //取消警报
        am.cancel(pi);
    }

    private void setViewVaule(boolean zao, boolean zhong, boolean wan, boolean shequ) {
        if (zao){
            startRemind(9);
            imgZao.setImageResource(R.mipmap.icon_on);
        }else{
            stopRemind(9);
            imgZao.setImageResource(R.mipmap.icon_off);
        }
        if (zhong){
            startRemind(13);
            imgZhong.setImageResource(R.mipmap.icon_on);
        }else{
            stopRemind(13);
            imgZhong.setImageResource(R.mipmap.icon_off);
        }
        if (wan){
            startRemind(19);
            imgWan.setImageResource(R.mipmap.icon_on);
        }else{
            stopRemind(19);
            imgWan.setImageResource(R.mipmap.icon_off);
        }
        if (shequ){
            imgShequ.setImageResource(R.mipmap.icon_on);
        }else{
            imgShequ.setImageResource(R.mipmap.icon_off);
        }
        if (zao || zhong || wan || shequ){
            tvState.setText("已开启");
        }else{
            tvState.setText("已关闭");
        }
        EBERApp.spUtil.putData(SPKey.REMIND, zao + "," + zhong + "," + wan + "," + shequ);
    }

    @Override
    public void setListener() {
        imgZao.setOnClickListener(clickLis);
        imgZhong.setOnClickListener(clickLis);
        imgWan.setOnClickListener(clickLis);
        imgShequ.setOnClickListener(clickLis);
    }

    private boolean zao;
    private boolean zhong;
    private boolean wan;
    private boolean shequ;

    private View.OnClickListener clickLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.remind_zao_img:
                    zao = !zao;
                    setViewVaule(zao, zhong, wan, shequ);
                    break;
                case R.id.remind_zhong_img:
                    zhong = !zhong;
                    setViewVaule(zao, zhong, wan, shequ);
                    break;
                case R.id.remind_wan_img:
                    wan = !wan;
                    setViewVaule(zao, zhong, wan, shequ);
                    break;
                case R.id.remind_shequ_img:
                    shequ = !shequ;
                    setViewVaule(zao, zhong, wan, shequ);
                    break;
            }
        }
    };
}
