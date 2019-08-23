package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.newland.karaoke.R;
import com.newland.karaoke.fragment.AddProductFragment;
import com.newland.karaoke.fragment.AddRoomFragment;
import com.newland.karaoke.fragment.ProductDetailsFragment;
import com.newland.karaoke.fragment.RoomDetailsFragment;

/**
 * 作为添加商品和房间信息的fragmen的载体
 */
public class AddActivity extends BaseActivity {

    private int detailstype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

              initBaseView(R.id.setting_toolbar);
        setToolBarTitle(getString(R.string.setting_AddRoom));

        intiFragment();
    }

    /**
     * 初始化选择是商品还是房间的fragment
     */
    private  void intiFragment(){

        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();

        AddRoomFragment addRoomFragment=new AddRoomFragment(this);
        AddProductFragment addProductFragment=new AddProductFragment(this);

        detailstype = getIntent().getIntExtra(getString(R.string.add_type),0);

        if (detailstype == 2)
            fTransaction.replace(R.id.add_content,addRoomFragment);
        else
            fTransaction.replace(R.id.add_content,addProductFragment);

        fTransaction.commit();
    }


    @Override
    public void basefinish() {
        finish();
    }
}
