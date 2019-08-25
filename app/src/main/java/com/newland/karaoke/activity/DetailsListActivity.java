package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.newland.karaoke.R;
import com.newland.karaoke.fragment.ProductDetailsFragment;
import com.newland.karaoke.fragment.RoomDetailsFragment;

/**
 * 作为显示所有商品和房间信息的fragmen的载体
 */
public class DetailsListActivity extends BaseActivity {

    private int detailstype;

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

        detailstype = getIntent().getIntExtra(getString(R.string.details_type),0);
        if (detailstype == 0) {
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
     * 添加按钮点击事件
     */
    private void addEvent()
    {

    }

        @Override
    public void basefinish() {
        finish();
    }
}
