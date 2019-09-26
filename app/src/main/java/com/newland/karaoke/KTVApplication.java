package com.newland.karaoke;

import android.app.Application;
import android.content.Context;
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
        //CreateDatabase();
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


    //调试：初始化数据库
    public static void CreateDatabase(){

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
            ktvProduct1.setProduct_picture(String.valueOf(R.drawable.product_1));
            ktvProduct1.setProduct(new ArrayList<KTVOrderProduct>());
            ktvProduct1.save();

            KTVProduct ktvProduct2=new KTVProduct();
            ktvProduct2.setProduct_name("可比克薯片");
            ktvProduct2.setProduct_count(20);
            ktvProduct2.setProduct_price(4);
            ktvProduct2.setProduct_picture(String.valueOf(R.drawable.product_2));
            ktvProduct2.setProduct(new ArrayList<KTVOrderProduct>());
            ktvProduct2.save();

            KTVProduct ktvProduct3=new KTVProduct();
            ktvProduct3.setProduct_name("德芙牛奶巧克力");
            ktvProduct3.setProduct_count(5);
            ktvProduct3.setProduct_price(9);
            ktvProduct3.setProduct_picture(String.valueOf(R.drawable.product_3));
            ktvProduct3.setProduct(new ArrayList<KTVOrderProduct>());
            ktvProduct3.save();

            KTVProduct ktvProduct4=new KTVProduct();
            ktvProduct4.setProduct_name("雪碧");
            ktvProduct4.setProduct_count(15);
            ktvProduct4.setProduct_price(3);
            ktvProduct4.setProduct_picture(String.valueOf(R.drawable.product_4));
            ktvProduct4.setProduct(new ArrayList<KTVOrderProduct>());
            ktvProduct4.save();

            KTVProduct ktvProduct5=new KTVProduct();
            ktvProduct5.setProduct_name("水");
            ktvProduct5.setProduct_count(1);
            ktvProduct5.setProduct_price(3);
            ktvProduct5.setProduct(new ArrayList<KTVOrderProduct>());
            ktvProduct5.save();

            //增加用户
            KTVUserInfo userInfo=new KTVUserInfo();
            userInfo.setIdentity_card_no("123432435");
            userInfo.setMobile_phone("15059115150");
            userInfo.setUser_photo("");
            userInfo.setUser_name("yiyi");
            userInfo.save();

            KTVUserLogin userLogin=new KTVUserLogin();
            userLogin.setUser_info(userInfo);
            userLogin.setUser_account("yi");
            userLogin.setUser_password("12345");
            userLogin.save();

            //添加商品订单
            KTVOrderProduct ktvOrderProduct=new KTVOrderProduct();
            ktvOrderProduct.setProduct_quantity(2);
            ktvOrderProduct.setProduct(ktvProduct4);
            ktvOrderProduct.save();
            //添加商品订单
            KTVOrderProduct ktvOrderProduct1=new KTVOrderProduct();
            ktvOrderProduct1.setProduct_quantity(1);
            ktvOrderProduct1.setProduct(ktvProduct3);
            ktvOrderProduct1.save();
            //添加订单
            KTVOrderInfo ktvOrderInfo=new KTVOrderInfo();
            ktvOrderInfo.setOrder_start_time(new Date());
            ktvOrderInfo.setOrder_end_time(new Date());
            ktvOrderInfo.setOrder_number("15472719");
            ktvOrderInfo.setRoom_id(ktvRoomInfo3);
            ktvOrderInfo.setPay_amount(85);
            List<KTVOrderProduct> list=new ArrayList<KTVOrderProduct>();
            list.add(ktvOrderProduct);
            list.add(ktvOrderProduct1);
            ktvOrderInfo.setProductList(list);
            ktvOrderInfo.setOrder_status(KTVType.OrderStatus.UNPAID);
            ktvOrderInfo.save();
            //更新商品订单
            ktvOrderProduct.setKtvOrderInfo(ktvOrderInfo);
            ktvOrderProduct.save();



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
