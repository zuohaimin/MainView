package com.cinsc.MainView.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author: 束手就擒
 * @Date: 18-10-20 下午6:39
 * @Description:
 */
public class TimeUtil {
    public static boolean isSameDate(Date d1, Date d2) {
        if(null == d1 || null == d2)
            return false;
        //return getOnlyDate(d1).equals(getOnlyDate(d2));
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        return  cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
    }
}
