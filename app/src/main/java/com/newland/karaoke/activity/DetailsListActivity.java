package com.newland.karaoke.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.newland.karaoke.R;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.fragment.ProductDetailsFragment;
import com.newland.karaoke.fragment.RoomDetailsFragment;

/**
 * 作为显示所有商品和房间信息的fragmen的载体
 */
public class DetailsListActivity extends BaseActivity {

    private int fragment_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_list);
        initBaseView(R.id.setting_toolbar);
        intiFragment();
    }

    /**
     * 初始化选择是商品还是房间的fragment
     */
    private  void intiFragment(){
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();

        RoomDetailsFragment roomDetailsFragment=new RoomDetailsFragment(fManager,this);
        ProductDetailsFragment productDetailsFragment =new ProductDetailsFragment(fManager,this);

        fragment_type = getIntent().getIntExtra(getString(R.string.fragment_type),0);
        if (fragment_type == KTVType.FragmentType.ROOMDETAIL) {
            setToolBarTitle(getString(R.string.setting_roomDetails));
            fTransaction.replace(R.id.detail_list_content, roomDetailsFragment);
        }
        else {
            setToolBarTitle(getString(R.string.setting_productDetails));
            fTransaction.replace(R.id.detail_list_content,productDetailsFragment);
        }

        fTransaction.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.setting_add) {
            addEvent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 添加按钮点击事件,添加商品或者房间
     */
    private void addEvent()
    {
        Intent intent=new Intent();
        intent = new Intent(this,AddActivity.class);

        switch (fragment_type) {
            case KTVType.FragmentType.ROOMDETAIL:
                intent.putExtra(getString(R.string.fragment_type), KTVType.FragmentType.ADDROOM);
                break;
            case KTVType.FragmentType.PRODUCTDETAIL:
                intent.putExtra(getString(R.string.fragment_type), KTVType.FragmentType.ADDPRODUCT);
                break;
        }
        startActivity(intent);

    }

        @Override
    public void basefinish() {
        finish();
    }
}
