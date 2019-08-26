package com.newland.karaoke.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.newland.karaoke.R;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.fragment.AddProductFragment;
import com.newland.karaoke.fragment.AddRoomFragment;

/**
 * 作为添加商品和房间信息的fragmen的载体
 */
public class AddActivity extends BaseActivity {

    private int fragment_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initBaseView(R.id.setting_toolbar);


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

        fragment_type = getIntent().getIntExtra(getString(R.string.fragment_type),0);

        if (fragment_type == KTVType.FragmentType.ADDROOM) {
            setToolBarTitle(getString(R.string.setting_AddRoom));
            fTransaction.replace(R.id.add_content,addRoomFragment);
        }
        else if(fragment_type == KTVType.FragmentType.ADDPRODUCT){
            setToolBarTitle(getString(R.string.setting_AddProduct));
            fTransaction.replace(R.id.add_content,addProductFragment);
        }
        else if(fragment_type == KTVType.FragmentType.EDITROOM){
            setToolBarTitle(getString(R.string.setting_AddRoom));
            addRoomFragment.updateRoom(getIntent().getIntExtra(getString(R.string.edit_detail_id),0));
            fTransaction.replace(R.id.add_content,addRoomFragment);
        }
        else if(fragment_type == KTVType.FragmentType.EDITRODUCT) {
            setToolBarTitle(getString(R.string.setting_AddProduct));
            addProductFragment.updateProduct(getIntent().getIntExtra(getString(R.string.edit_detail_id),0));
            fTransaction.replace(R.id.add_content, addProductFragment);
        }

        fTransaction.commit();
    }


    @Override
    public void basefinish() {
        finish();
    }
}
