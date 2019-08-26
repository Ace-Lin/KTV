package com.newland.karaoke.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.newland.karaoke.R;
import com.newland.karaoke.adapter.SettingAdapter;
import com.newland.karaoke.database.KTVProduct;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.utils.FileUtils;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
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
      //  AddRoom();
       // AddProjuct();
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

    private void AddProjuct()
    {
        String path =saveImage();
        
        KTVProduct room1=new KTVProduct();
        room1.setProduct_count(80);
        room1.setProduct_name("苹果");
        room1.setProduct_picture(path);
        room1.setProduct_price(80);
        room1.save();
        
        KTVProduct room2=new KTVProduct();
        room2.setProduct_count(70);
        room2.setProduct_name("西瓜");
        room2.setProduct_picture(path);
        room2.setProduct_price(80);
        room2.save();
        
        KTVProduct room3=new KTVProduct();
        room3.setProduct_count(80);
        room3.setProduct_name("芒果");
        room3.setProduct_picture(path);
        room3.setProduct_price(60);
        room3.save();

      
    }

    /**
     * 保存上传的照片
     */
    private String saveImage()
    {
        BitmapDrawable bd = (BitmapDrawable) getDrawable(R.drawable.ic_launcher);
        Bitmap  bitmap=bd.getBitmap();
        File file=null;
        String dir = FileUtils.PICTURE_PATH;
        try {
            File folder = new File(dir);
            if(!folder.exists()){
                folder.mkdir();
            }
            file = new File(dir + "/summer1" + ".jpg");

            if(file.exists()){
                file.delete();
            }
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            //showImage(file.getParent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getPath();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       Intent intent=new Intent();
        switch (i) {
           case 0:
               intent = new Intent(this,DetailsListActivity.class);
               intent.putExtra(getString(R.string.fragment_type),i);
               break;
           case 1:
               intent = new Intent(this,DetailsListActivity.class);
               intent.putExtra(getString(R.string.fragment_type),i);
               break;
           case 2:
               intent = new Intent(this,AddActivity.class);
               intent.putExtra(getString(R.string.fragment_type),i);
               break;
           case 3:
               intent = new Intent(this,AddActivity.class);
               intent.putExtra(getString(R.string.fragment_type),i);
               break;
           default:
       }

        startActivity(intent);
    }
}
