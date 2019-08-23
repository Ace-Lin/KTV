package com.newland.karaoke;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderProduct;
import com.newland.karaoke.database.KTVProduct;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.database.KTVUserInfo;
import com.newland.karaoke.database.KTVUserLogin;

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
        CreateDatabase();
    }
    public static Context getContext(){
         return context;
    }

    //调试：初始化数据库
    public void CreateDatabase(){

            SQLiteDatabase db= LitePal.getDatabase();
            LitePal.deleteAll(KTVUserLogin.class);
            LitePal.deleteAll(KTVUserInfo.class);

            KTVUserLogin user_login=new KTVUserLogin();
            user_login.setUser_account("15880168030");
            user_login.setUser_password("123456");
            user_login.save();

            KTVUserInfo user_info=new KTVUserInfo();
            user_info.setUser_id(user_login);
            user_info.setIdentity_card_no("1234");
            user_info.save();

            //添加房间信息
            KTVRoomInfo ktvRoomInfo1=new KTVRoomInfo();
            ktvRoomInfo1.setRoom_name("A01");
            ktvRoomInfo1.setRoom_price(100);
            ktvRoomInfo1.setRoom_status(KTVType.RoomStatus.FREE);
            ktvRoomInfo1.setRoom_type(KTVType.RoomType.MIDDLE);
            ktvRoomInfo1.save();

            KTVRoomInfo ktvRoomInfo2=new KTVRoomInfo();
            ktvRoomInfo2.setRoom_name("B02");
            ktvRoomInfo2.setRoom_price(200);
            ktvRoomInfo2.setRoom_type(KTVType.RoomType.BIG);
            ktvRoomInfo2.setRoom_status(KTVType.RoomStatus.FREE);
            ktvRoomInfo2.save();

            KTVRoomInfo ktvRoomInfo3=new KTVRoomInfo();
            ktvRoomInfo3.setRoom_name("C02");
            ktvRoomInfo3.setRoom_price(70);
            ktvRoomInfo3.setRoom_type(KTVType.RoomType.SMAlL);
            ktvRoomInfo3.setRoom_status(KTVType.RoomStatus.FREE);
            ktvRoomInfo3.save();

            //添加商品信息
            KTVProduct ktvProduct1=new KTVProduct();
            ktvProduct1.setProduct_name("牛奶");
            ktvProduct1.setProduct_count(10);
            ktvProduct1.setProduct_price(6);
            ktvProduct1.setProduct(new ArrayList<KTVOrderProduct>());
            ktvProduct1.save();

            KTVProduct ktvProduct2=new KTVProduct();
            ktvProduct2.setProduct_name("可比克薯片");
            ktvProduct2.setProduct_count(20);
            ktvProduct2.setProduct_price(4);
            ktvProduct2.setProduct(new ArrayList<KTVOrderProduct>());
            ktvProduct2.save();

            KTVProduct ktvProduct3=new KTVProduct();
            ktvProduct3.setProduct_name("德芙牛奶巧克力");
            ktvProduct3.setProduct_count(5);
            ktvProduct3.setProduct_price(9);
            ktvProduct3.setProduct(new ArrayList<KTVOrderProduct>());
            ktvProduct3.save();

            KTVProduct ktvProduct4=new KTVProduct();
            ktvProduct4.setProduct_name("雪碧");
            ktvProduct4.setProduct_count(15);
            ktvProduct4.setProduct_price(3);
            ktvProduct4.setProduct(new ArrayList<KTVOrderProduct>());
            ktvProduct4.save();





    }
}
