package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.newland.karaoke.KTVApplication;
import com.newland.karaoke.R;
import com.newland.karaoke.adapter.LeftContentAdapter;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.database.KTVUserInfo;
import com.newland.karaoke.database.KTVUserLogin;
import com.newland.karaoke.model.LeftContentModel;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //主界面四个按钮
private ImageButton checkIn;
private ImageButton order;
private ImageButton settings;
private ImageButton shift;
private ListView lv_main_left;
private List<LeftContentModel> leftContentModels;
private LeftContentAdapter adapter;
private DrawerLayout mDrawLayout;
private CircleImageView mHeadImage;//头像


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initUI();
        initEvent();
    }
    void initData(){
        leftContentModels=new ArrayList<>();
        leftContentModels.add(new LeftContentModel(R.drawable.middle,"Login",KTVType.MineType.LOGIN));
        leftContentModels.add(new LeftContentModel(R.drawable.middle,"Personal Info",KTVType.MineType.PERSONAL_INFO));
        leftContentModels.add(new LeftContentModel(R.drawable.middle,"Set Password",KTVType.MineType.SET_PWD));
        leftContentModels.add(new LeftContentModel(R.drawable.middle,"Log out",KTVType.MineType.LOG_OUT));
    }
    void initUI(){
        //初始化UI
        mDrawLayout=(DrawerLayout)findViewById(R.id.dl_main);

        checkIn=(ImageButton)findViewById(R.id.bt_checkin);
        order=(ImageButton)findViewById(R.id.bt_order);
        settings=(ImageButton)findViewById(R.id.bt_settings);
        shift=(ImageButton)findViewById(R.id.bt_transactions);

        //初始化list
        lv_main_left=(ListView)findViewById(R.id.lv_main_left);
        adapter=new LeftContentAdapter(this,R.layout.item_left_content,leftContentModels);
        lv_main_left.setAdapter(adapter);
        //头像
        mHeadImage = (CircleImageView) findViewById(R.id.ci_main_photo);
    }
    private void initEvent(){
        //设置监听
        checkIn.setOnClickListener(this);
        order.setOnClickListener(this);
        settings.setOnClickListener(this);
        shift.setOnClickListener(this);

        lv_main_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                if(id == -1) {
                    // 点击的是headerView或者footerView
                    return;
                }
                int realPosition=(int)id;
                LeftContentModel item=adapter.getItem(realPosition);
                // 响应代码
                switch(item.getId()){
                    case KTVType.MineType.LOGIN:
                        Intent login=new Intent(MainActivity.this,ShiftActivity.class);
                        startActivity(login);
                        break;
                    case KTVType.MineType.PERSONAL_INFO:

                        break;
                    case KTVType.MineType.SET_PWD:
                        break;
                    case KTVType.MineType.LOG_OUT:
                        break;
                    default:
                        break;
                }
                mDrawLayout.closeDrawer(Gravity.LEFT);

            }
        });

        //监听侧边菜单栏打开或关闭的状态
        mDrawLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {

            }
            //打开菜单栏后触发的方法
            @Override
            public void onDrawerOpened(View view) {
                Toast.makeText(MainActivity.this, "打开了侧边栏" , Toast.LENGTH_SHORT).show();
            }
            //关闭菜单栏后触发的方法
            @Override
            public void onDrawerClosed(View view) {
                Toast.makeText(MainActivity.this, "关闭了侧边栏" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
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
                Intent intent_tansaction=new Intent(MainActivity.this,TransactionActivity.class);
                startActivity(intent_tansaction);
                break;
            default:
                break;
        }
    }
}
