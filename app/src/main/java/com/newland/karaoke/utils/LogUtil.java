package com.newland.karaoke.utils;

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
      //  Log.d(TAG, currC.getName()+content);//完整类名，包括包的路径
        Log.d(TAG, currC.getSimpleName()+"->"+content);
    }

    public static void logWarn(String content,Class currC)
    {
        Log.w(TAG, currC.getSimpleName()+"->"+content);
    }

    public static void logError(String content,Class currC)
    {
        Log.e(TAG, currC.getSimpleName()+"->"+content);
    }
}
