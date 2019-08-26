package com.newland.karaoke.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void showLongText(Context context,String content)
    {
        Toast.makeText(context,content, Toast.LENGTH_LONG).show();
    }

    public static void showLongText(Context context,int i)
    {
        Toast.makeText(context, String.valueOf(i), Toast.LENGTH_LONG).show();
    }

    public static void showShortText(Context context,String content)
    {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void showShortText(Context context,int i)
    {
        Toast.makeText(context, String.valueOf(i), Toast.LENGTH_SHORT).show();
    }
}
