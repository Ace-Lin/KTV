package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.newland.karaoke.R;
import com.newland.karaoke.adapter.HistoryOrderAdapter;
import com.newland.karaoke.adapter.SettingProductAdapter;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.fragment.HistoryOrderlistFragment;
import com.newland.karaoke.fragment.ProductDetailsFragment;
import com.newland.karaoke.fragment.RoomDetailsFragment;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static com.newland.karaoke.utils.ToastUtil.showShortText;

public class TransactionActivity extends BaseActivity {

    public boolean isOrderlistPager;//首先判断是不是列表页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        intiFragment();
    }

    /**
     * 初始化选择是商品还是房间的fragment
     */
    private  void intiFragment(){
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();

        HistoryOrderlistFragment roomDetailsFragment=new HistoryOrderlistFragment();

        fTransaction.replace(R.id.detail_list_content, roomDetailsFragment);
        isOrderlistPager=true;
        fTransaction.commit();
    }


    @Override
    public void basefinish() {
        if (isOrderlistPager)
        finish();
    }

}
