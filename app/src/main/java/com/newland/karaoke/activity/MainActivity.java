package com.newland.karaoke.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.newland.karaoke.R;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVOrderProduct;
import com.newland.karaoke.database.KTVProduct;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.database.KTVUserInfo;
import com.newland.karaoke.database.KTVUserLogin;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.newland.karaoke.utils.DateUtil.getNoFormatDate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //主界面四个按钮
private ImageButton checkIn;
private ImageButton order;
private ImageButton settings;
private ImageButton shift;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        //CreateDatabase();
        SearchData();
        //addOrder();
    }

    void initUI(){
        //初始化UI

        checkIn=(ImageButton)findViewById(R.id.bt_checkin);
        order=(ImageButton)findViewById(R.id.bt_order);
        settings=(ImageButton)findViewById(R.id.bt_settings);
        shift=(ImageButton)findViewById(R.id.bt_transactions);

        //设置监听
        checkIn.setOnClickListener(this);
        order.setOnClickListener(this);
        settings.setOnClickListener(this);
        shift.setOnClickListener(this);



    }
    void CreateDatabase(){

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


    }
    void SearchData(){
       List<KTVUserLogin> temp=LitePal.findAll(KTVUserLogin.class);
       for(KTVUserLogin user:temp)
       {
           Log.d("账户：",user.getUser_account());
           Log.d("id名：",""+user.getId());

       }

//        List<KTVUserInfo> info=LitePal.findAll(KTVUserInfo.class);
//        for(KTVUserInfo user:info)
//        {
//            Log.d("详细信息id名：",""+user.getId());
//            Log.d("详细信息账户：",user.getUser_id().getUser_account());
//            Log.d("用户id名：",""+user.getUser_id().getId());
//        }

    }

    /**
     * 测试添加订单信息
     */
    private void addOrder()
    {
        //LitePal.deleteAll(KTVOrderInfo.class);
        List<KTVRoomInfo> ktvRoomInfos =LitePal.findAll(KTVRoomInfo.class);
        List<KTVProduct> ktvProducts = LitePal.findAll(KTVProduct.class);
        
        KTVOrderProduct ktvOrderProduct =new KTVOrderProduct();
        ktvOrderProduct.setProduct(ktvProducts.get(0));
        ktvOrderProduct.setProduct_quantity(5);
        ktvOrderProduct.save();

        KTVOrderProduct ktvOrderProduct1 =new KTVOrderProduct();
        ktvOrderProduct1.setProduct(ktvProducts.get(1));
        ktvOrderProduct1.setProduct_quantity(5);
        ktvOrderProduct1.save();

        KTVOrderProduct ktvOrderProduct2 =new KTVOrderProduct();
        ktvOrderProduct2.setProduct(ktvProducts.get(0));
        ktvOrderProduct2.setProduct_quantity(5);
        ktvOrderProduct2.save();

        KTVOrderProduct ktvOrderProduct3 =new KTVOrderProduct();
        ktvOrderProduct3.setProduct(ktvProducts.get(0));
        ktvOrderProduct3.setProduct_quantity(5);
        ktvOrderProduct3.save();

        KTVOrderProduct ktvOrderProduct4 =new KTVOrderProduct();
        ktvOrderProduct4.setProduct(ktvProducts.get(0));
        ktvOrderProduct4.setProduct_quantity(5);
        ktvOrderProduct4.save();

        KTVOrderProduct ktvOrderProduct5 =new KTVOrderProduct();
        ktvOrderProduct5.setProduct(ktvProducts.get(0));
        ktvOrderProduct5.setProduct_quantity(5);
        ktvOrderProduct5.save();
        
        List<KTVOrderProduct> ktvOrderProducts =new ArrayList<>();
        ktvOrderProducts.add(ktvOrderProduct);
        
        List<KTVOrderProduct> ktvOrderProducts1 =new ArrayList<>();
        ktvOrderProducts1.add(ktvOrderProduct1);
        ktvOrderProducts1.add(ktvOrderProduct2);
        
        List<KTVOrderProduct> ktvOrderProducts2 =new ArrayList<>();
        ktvOrderProducts2.add(ktvOrderProduct);
        ktvOrderProducts2.add(ktvOrderProduct1);
        ktvOrderProducts2.add(ktvOrderProduct2);
        ktvOrderProducts2.add(ktvOrderProduct3);
        ktvOrderProducts2.add(ktvOrderProduct4);
        ktvOrderProducts2.add(ktvOrderProduct5);


//        KTVOrderInfo ktvOrderInfo = new KTVOrderInfo();
//        ktvOrderInfo.setOrder_start_time(new Date());
//        ktvOrderInfo.setOrder_end_time(new Date());
//        ktvOrderInfo.setOrder_number(getNoFormatDate(new Date()));
//        ktvOrderInfo.setRoom_id(ktvRoomInfos.get(0));
//        ktvOrderInfo.setProductList(ktvOrderProducts);
//        ktvOrderInfo.setOrder_status(KTVType.OrderStatus.PAID);
//        ktvOrderInfo.setPay_amount(800.00);
//        ktvOrderInfo.save();
//
//
//        KTVOrderInfo ktvOrderInfo1 = new KTVOrderInfo();
//        ktvOrderInfo1.setOrder_start_time(new Date());
//        ktvOrderInfo1.setOrder_end_time(new Date());
//        ktvOrderInfo1.setOrder_number(getNoFormatDate(new Date()));
//        ktvOrderInfo1.setRoom_id(ktvRoomInfos.get(1));
//        ktvOrderInfo1.setProductList(ktvOrderProducts1);
//        ktvOrderInfo1.setOrder_status(KTVType.OrderStatus.PAID);
//        ktvOrderInfo1.setPay_amount(900.00);
//        ktvOrderInfo1.save();


        KTVOrderInfo ktvOrderInfo2 = new KTVOrderInfo();
        ktvOrderInfo2.setOrder_start_time(new Date());
        ktvOrderInfo2.setOrder_end_time(new Date());
        ktvOrderInfo2.setOrder_number(getNoFormatDate(new Date()));
        ktvOrderInfo2.setRoom_id(ktvRoomInfos.get(2));
        ktvOrderInfo2.setProductList(ktvOrderProducts2);
        ktvOrderInfo2.setOrder_status(KTVType.OrderStatus.PAID);
        ktvOrderInfo2.setPay_amount(1200.00);
        ktvOrderInfo2.save();

    }



    @Override
    public void onClick(View view) {
        //处理监听
        switch(view.getId()){
            case R.id.bt_checkin:

                Intent intent_check=new Intent(MainActivity.this,CheckInActivity.class);
                startActivity(intent_check);
                break;
            case R.id.bt_order:
                Intent intent_order=new Intent(MainActivity.this,OrderActivity.class);
                startActivity(intent_order);
                break;
            case R.id.bt_settings:
                Intent intent_settings=new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent_settings);
                break;
            case R.id.bt_transactions:
                Intent intent_shift=new Intent(MainActivity.this, TransactionActivity.class);
                startActivity(intent_shift);
                break;
            default:
                break;
        }
    }
}
