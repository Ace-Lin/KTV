package com.newland.karaoke.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utility {

    /**
     *   照片路径转化为图片返回
     */

    public static   Bitmap  getPirBitMap(String path)
    {
        File file = new File(path);
        if(file.exists())
            return BitmapFactory.decodeFile(path);

        return null;
    }

    /**
     * 输入字符串提取汉字
     * @param str
     * @return
     */
    public static String getNum(String str)
    {
        String dest = "";
        if (str!= null) {
            dest = str.replaceAll("[^0-9]","");

        }
        return dest;
    }


    /**
     * 判断房间类型返回字符串
     * @param type
     * @return
     */
    public static String getRoomType(int type)
    {
        switch (type)
        {
            case 0:
                return "Big";

            case 1:
                return "Middle";

            case 2:
                return "Small";
            default:
                return  null;
        }
    }

    /**
     * 判断支付类型返回字符串
     * @param type
     * @return
     */
    public static String getPayType(int type)
    {
        switch (type)
        {
            case 0:
                return "Cash";

            case 1:
                return "Card";

            case 2:
                return "QRcode";
            default:
                return  null;
        }
    }
}
