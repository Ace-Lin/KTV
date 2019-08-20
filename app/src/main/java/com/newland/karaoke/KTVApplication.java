package com.newland.karaoke;

import android.app.Application;
import android.content.Context;

import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVRoomInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class KTVApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        LitePal.initialize(this);
    }
    public static Context getContext(){
         return context;
    }

    //调试：初始化数据库
    public void CreateDatabase(){
        //List<KTVRoomInfo> ktvRoomInfo=new ArrayList<>();
        KTVRoomInfo ktvRoomInfo1=new KTVRoomInfo();
        ktvRoomInfo1.setRoom_name("A01");
        ktvRoomInfo1.setRoom_price(100);
        ktvRoomInfo1.setRoom_status(KTVType.RoomStatus.FREE);
        ktvRoomInfo1.setRoom_type(KTVType.RoomType.MIDDLE);
        ktvRoomInfo1.save();

        KTVRoomInfo ktvRoomInfo2=new KTVRoomInfo();
        ktvRoomInfo2.setRoom_name("A02");
        ktvRoomInfo2.setRoom_price(200);
        ktvRoomInfo2.setRoom_type(KTVType.RoomType.BIG);
        ktvRoomInfo2.setRoom_status(KTVType.RoomStatus.FREE);
        ktvRoomInfo2.save();

    }
}
