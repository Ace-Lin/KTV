package com.newland.karaoke.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 返回标准化的时间
     * @param date 现在时间格式
     * @return
     */
    public  static String getSimpleDate(Date date)
    {

        SimpleDateFormat ft = new SimpleDateFormat ("dd-MM-yyyy hh:mm:ss");
        return ft.format(date);
    }


    /**
     *返放回不带格式的字符串日期,用于交易单号
     * @param date
     * @return
     */
    public  static String getNoFormatDate(Date date)
    {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddhhmmss");
        return ft.format(date);
    }

    /**
     * 返回一个指定日期的零点
     * @return
     */
    public static String getCurrentDayBegin(Calendar currentDate)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar. YEAR, currentDate.get(Calendar. YEAR));
        calendar.set(Calendar. MONTH, currentDate.get(Calendar. MONTH));
        calendar.set(Calendar. DAY_OF_MONTH , currentDate.get(Calendar. DAY_OF_MONTH));
        calendar.set(Calendar. HOUR_OF_DAY, 0);//虽然显示12 但是长整型还是按照0点执行
        calendar.set(Calendar. MINUTE, 0);
        calendar.set(Calendar. SECOND, 0);
        return  String.valueOf(calendar.getTimeInMillis());
    }

    /**
     * 返回一个指定日期当天结束时间
     * @return
     */
    public static String getCurrentDayEnd(Calendar currentDate)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar. YEAR, currentDate.get(Calendar. YEAR));
        calendar.set(Calendar. MONTH, currentDate.get(Calendar. MONTH));
        calendar.set(Calendar. DAY_OF_MONTH , currentDate.get(Calendar. DAY_OF_MONTH));
        calendar.set(Calendar. HOUR_OF_DAY, 24);//虽然显示12 但是长整型还是按照24点执行
        calendar.set(Calendar. MINUTE, 0);
        calendar.set(Calendar. SECOND, 0);
        return  String.valueOf(calendar.getTimeInMillis());
    }
}
