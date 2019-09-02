package com.newland.karaoke.utils;

import android.content.Context;
import android.util.TypedValue;

import java.text.DecimalFormat;

public class DensityUtil {

    /**
     * 限制两位小数
     */
    public static   DecimalFormat df_two = new DecimalFormat("0.00");

    /**
     * dp转px
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }




}
