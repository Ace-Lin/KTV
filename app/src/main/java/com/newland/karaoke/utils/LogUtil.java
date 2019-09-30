package com.newland.karaoke.utils;

import android.app.Activity;
import android.util.Log;

public class LogUtil {

    public static final String TAG="Jeffrey";

    /**
     * 统一管理输出信息
     * @param currC  当前类名
     * @param content
     */
    public static void debug(String content, Class currC)
    {
        //Log.e(TAG, currC.toString()+"->"+content);
       //  Log.d(TAG, currC.getName()+content);//完整类名，包括包的路径
        Log.d(TAG, currC.getSimpleName()+"->"+content);
    }

    public static void error(String content,Class currC)
    {
        Log.e(TAG, currC.getSimpleName()+"->"+content);
    }


    public static void error(String content, Activity currA)
    {
        Log.e(TAG, currA.toString()+"->"+content);
    }
}
