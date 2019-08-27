package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.newland.karaoke.KTVApplication;
import com.newland.karaoke.R;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.database.KTVUserInfo;
import com.newland.karaoke.database.KTVUserLogin;

import org.litepal.LitePal;

import java.util.List;

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
                Intent intent_shift=new Intent(MainActivity.this,TransactionActivity.class);
                startActivity(intent_shift);
                break;
            default:
                break;
        }
    }
}
