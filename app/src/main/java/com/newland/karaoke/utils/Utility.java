package com.newland.karaoke.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.newland.karaoke.R;
import com.newland.karaoke.constant.KTVType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Utility {

    public static int[] imageUrl = {R.drawable.product_1,R.drawable.product_2,R.drawable.product_3,R.drawable.product_4,R.drawable.product_5,R.drawable.product_6 ,R.drawable.product_7};

    /**
     *   照片路径转化为图片返回
     */

    public static  Bitmap  getPirBitMap(Context context,String path)
    {
        if (TextUtils.isEmpty(path))
            //如果没有添加图片返回默认图片
            return  BitmapFactory.decodeResource(context.getResources(), R.drawable.product_image);

        if(new File(path).exists())
            return BitmapFactory.decodeFile(path);

        //如果没有图片返回默认图片
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
        switch (type){
            case KTVType.RoomType.BIG:
                return "Big";
            case KTVType.RoomType.MIDDLE:
                return "Middle";
            case KTVType.RoomType.SMAlL:
                return "Small";
            default:
                return "info missing!";
        }
    }

    /**
     * 判断支付类型返回字符串
     * @param type
     * @return
     */
    public static String getPayType(int type)
    {
        switch (type){
            case KTVType.PayType.CASH:
                return "Cash";
            case KTVType.PayType.CARD:
                return "Card";
            case KTVType.PayType.QRCODE:
                return "QRcode";
            default:
                return  "info missing!";
        }
    }

    /**
     *收起系统键盘的操作
     */
    public static void closeSoftKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 得到房间图片
     * @param type
     * @return
     */
    public static int getRoomPic(int type){
        switch (type){
            case KTVType.RoomType.BIG:
                return R.drawable.room_big;
            case KTVType.RoomType.MIDDLE:
                return R.drawable.room_mid;
            case KTVType.RoomType.SMAlL:
                return R.drawable.room_small;
            default:
                return R.drawable.room_big;
        }
    }



}
