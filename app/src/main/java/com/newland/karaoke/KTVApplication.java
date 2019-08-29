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
import com.newland.karaoke.model.UserModel;
import com.newland.karaoke.utils.FileUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
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



            KTVUserInfo user_info=new KTVUserInfo();
            user_info.setIdentity_card_no("1234");
            user_info.setUser_email("747848014@qq.com");
            user_info.save();

            KTVUserLogin user_login=new KTVUserLogin();
            user_login.setUser_account("15880168030");
            user_login.setUser_password("123456");
            user_login.setUser_info(user_info);
            user_login.save();

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

            //增加用户
            KTVUserInfo userInfo=new KTVUserInfo();
            userInfo.setIdentity_card_no("123432435");
            userInfo.setMobile_phone("15059115150");
            userInfo.setUser_photo("none");
            userInfo.setUser_name("yiyi");
            userInfo.save();

            KTVUserLogin userLogin=new KTVUserLogin();
            userLogin.setUser_info(userInfo);
            userLogin.setUser_account("yi");
            userLogin.setUser_password("12345");
            userLogin.save();


    }

        public static UserModel getCurrentUser() {
                return currentUser;
        }

        public static void setCurrentUser(UserModel user) {
                currentUser = user;
        }

        public static void setCurrentUserByUser(KTVUserInfo user_info) {
            currentUser=new UserModel(user_info);
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
