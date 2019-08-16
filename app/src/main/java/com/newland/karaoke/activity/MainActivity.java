package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.newland.karaoke.R;
import com.newland.karaoke.database.KTVUserInfo;
import com.newland.karaoke.database.KTVUserLogin;

import org.litepal.LitePal;

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
        CreateDatabase();
    }

    void initUI(){
        //初始化UI

        checkIn=(ImageButton)findViewById(R.id.bt_checkin);
        order=(ImageButton)findViewById(R.id.bt_order);
        settings=(ImageButton)findViewById(R.id.bt_settings);
        shift=(ImageButton)findViewById(R.id.bt_shift);

        //设置监听
        checkIn.setOnClickListener(this);
        order.setOnClickListener(this);
        settings.setOnClickListener(this);
        shift.setOnClickListener(this);

    }
    void CreateDatabase(){
        SQLiteDatabase db= LitePal.getDatabase();
        KTVUserLogin user_login=new KTVUserLogin();
        user_login.setUser_account("15880168030");
        user_login.setUser_password("123456");
        user_login.save();

        KTVUserInfo user_info=new KTVUserInfo();
        user_info.setUser_id(user_login);
        user_info.setIdentity_card_no("1234");
        user_info.save();


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
            case R.id.bt_shift:
                Intent intent_shift=new Intent(MainActivity.this,ShiftActivity.class);
                startActivity(intent_shift);
                break;
            default:
                break;
        }
    }
}
