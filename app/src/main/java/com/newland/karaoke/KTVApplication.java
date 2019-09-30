package com.newland.karaoke;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVOrderProduct;
import com.newland.karaoke.database.KTVProduct;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.database.KTVUserInfo;
import com.newland.karaoke.database.KTVUserLogin;
import com.newland.karaoke.mesdk.device.SDKDevice;
import com.newland.karaoke.model.PrintModel;
import com.newland.karaoke.model.UserModel;
import com.newland.karaoke.utils.FileUtils;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.os.Handler;

public class KTVApplication extends Application {
    private static Context context;
    private static UserModel currentUser;
    private static boolean isLogin;
    private static Handler mainHandler=null;

    public static Handler getmHandler() {
        return mainHandler;
    }

    public static void setmHandler(Handler handler) {
        mainHandler = handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        LitePal.initialize(this);
        FileUtils.createDir(getExternalFilesDir("/Picture").getPath());
       // initImageLoader(getApplicationContext());

    }
    public static Context getContext(){
         return context;
    }


    private void initImageLoader(Context context){
        /**ImageLoader的配置**/
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY-2) //设置同时运行的线程
                .denyCacheImageMultipleSizesInMemory()  //缓存显示不同大小的同一张图片
                .diskCacheSize(50*1024*1024)  //50MB SD卡本地缓存的最大值
                .memoryCache(new WeakMemoryCache()) //内存缓存
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        //全局初始化配置
        ImageLoader.getInstance().init(config);
    }

    public static UserModel getCurrentUser() {
            return currentUser;
    }

    public static void setCurrentUser(UserModel user) {
            currentUser = user;
    }

    public static void setCurrentUserByUser(KTVUserInfo user_info,int id) {
        currentUser=new UserModel(user_info,id);
    }

    public static boolean isLogin() {
        return isLogin;
    }
    public static void setIsLogin(boolean isLogin) {
        KTVApplication.isLogin = isLogin;
    }

    public static void UpdateUserInfo(){
        KTVUserInfo user=new KTVUserInfo();
        user.setUser_photo(currentUser.getUser_photo());
        user.setMobile_phone(currentUser.getMobile_phone());
        user.setIdentity_card_no(currentUser.getIdentity_card_no());
        user.setUser_name(currentUser.getUser_name());
        user.setUser_email(currentUser.getUser_email());
        user.updateAll("id=?",String.valueOf(currentUser.getId()));
    }
}
