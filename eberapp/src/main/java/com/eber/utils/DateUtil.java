package com.eber.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wxd on 2017/5/7.
 */

public class DateUtil {

    /**
     * 显示时间，如果与当前时间差别小于一天，则自动用**秒(分，小时)前，如果大于一天则用format规定的格式显示
     *
     * @author wxy
     * @param ctime
     *            时间
     * @param format
     *            格式 格式描述:例如:yyyy-MM-dd yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String showTime(Date ctime, String format) {
        //System.out.println("当前时间是："+new Timestamp(System.currentTimeMillis()));


        //System.out.println("发布时间是："+df.format(ctime).toString());
        String r = "";
        if(ctime==null)return r;
        if(format==null)format="MM-dd HH:mm:ss";

        long nowtimelong = System.currentTimeMillis();

        long ctimelong = ctime.getTime();
        long result = Math.abs(nowtimelong - ctimelong);

        if(result < 60000){// 一分钟内
            long seconds = result / 1000;
            if(seconds == 0){
                r = "刚刚";
            }else{
                r = seconds + "秒前";
            }
        }else if (result >= 60000 && result < 3600000){// 一小时内
            long seconds = result / 60000;
            r = seconds + "分钟前";
        }else if (result >= 3600000 && result < 86400000){// 一天内
            long seconds = result / 3600000;
            r = seconds + "小时前";
        }else if (result >= 86400000 && result < 1702967296){// 三十天内
            long seconds = result / 86400000;
            r = seconds + "天前";
        }else{// 日期格式
            format="MM-dd";
            SimpleDateFormat df = new SimpleDateFormat(format);
            r = df.format(ctime).toString();
        }
        return r;
    }
}
