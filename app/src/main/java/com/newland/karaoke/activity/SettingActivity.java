package com.newland.karaoke.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.newland.karaoke.R;
import com.newland.karaoke.adapter.SettingAdapter;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.utils.FileUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity implements  AdapterView.OnItemClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initBaseView(R.id.setting_toolbar);
        setToolBarTitle(getString(R.string.setting));
        initBtn();
        FileUtils.createDir(getExternalFilesDir("/Picture").getPath());
        //AddRoom();
    }

    /**
     * 初始化按钮信息
     */
    private  void initBtn()
    {
        List<String> nameData =new ArrayList<>();
        nameData.add(getString(R.string.setting_roomDetails));
        nameData.add(getString(R.string.setting_productDetails));
        nameData.add(getString(R.string.setting_AddRoom));
        nameData.add(getString(R.string.setting_AddProduct));
        ListView list_news = (ListView)findViewById(R.id.setting_listview);
        SettingAdapter myAdapter = new SettingAdapter(nameData, this);
        list_news.setAdapter(myAdapter);
        list_news.setOnItemClickListener(this);
    }




    @Override
    public void basefinish() {
        finish();
    }



    private void AddRoom()
    {
        SQLiteDatabase db= LitePal.getDatabase();

        KTVRoomInfo room1=new KTVRoomInfo();
        room1.setRoom_name("180");
        room1.setRoom_type(0);
        room1.setRoom_price(180);
        room1.save();

        KTVRoomInfo room2=new KTVRoomInfo();
        room2.setRoom_name("181");
        room2.setRoom_type(1);
        room2.setRoom_price(100);
        room2.save();

        KTVRoomInfo room3=new KTVRoomInfo();
        room3.setRoom_name("182");
        room3.setRoom_type(1);
        room3.setRoom_price(100);
        room3.save();

        KTVRoomInfo room4=new KTVRoomInfo();
        room4.setRoom_name("183");
        room4.setRoom_type(2);
        room4.setRoom_price(80);
        room4.save();

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       Intent intent=new Intent();
        switch (i) {
           case 0:
               intent = new Intent(this,DetailsListActivity.class);
               intent.putExtra(getString(R.string.details_type),i);
               break;
           case 1:
               intent = new Intent(this,DetailsListActivity.class);
               intent.putExtra(getString(R.string.details_type),i);
               break;
           case 2:
               intent = new Intent(this,AddActivity.class);
               intent.putExtra(getString(R.string.add_type),i);
               break;
           case 3:
               intent = new Intent(this,AddActivity.class);
               intent.putExtra(getString(R.string.add_type),i);
               break;
           default:
       }

        startActivity(intent);
    }
}
