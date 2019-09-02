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
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.fragment.HistoryOderdetailFragment;
import com.newland.karaoke.fragment.HistoryOrderlistFragment;
import com.newland.karaoke.fragment.ProductDetailsFragment;
import com.newland.karaoke.fragment.RoomDetailsFragment;
import com.newland.karaoke.fragment.SearchFragment;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static com.newland.karaoke.utils.ToastUtil.showShortText;

public class TransactionActivity extends BaseActivity {
    //定义三种fragment类型
    public enum FragmentType{    HOME,SEARCH,DETAIL    }
    private FragmentManager fManager;//获取管理器
    private HistoryOderdetailFragment detailFragment;
    private HistoryOrderlistFragment listFragment;
    private SearchFragment searchFragment;
    private boolean isSearch;//搜索标志位
    private FragmentType currFragment = FragmentType.HOME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        fManager = getSupportFragmentManager();
        showListFragment();

    }

    /**
     * 显示历史清单fragment
     */
        private  void showListFragment(){

        FragmentTransaction fTransaction = fManager.beginTransaction();
        //首先隐藏详细fragment
         hideAllFragment(fTransaction);
        //再显示清单fragment
        if (listFragment == null){

            listFragment = new HistoryOrderlistFragment();
            fTransaction.add(R.id.history_fragment_content, listFragment);
        }
        else {
            fTransaction.show(listFragment);
        }

       currFragment = FragmentType.HOME;
        fTransaction.commit();
    }


    /**
     * 显示清单详细信息的fragment
     */
    public  void  openDetailFragment(KTVOrderInfo orderInfo)
    {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        //首先隐藏清单fragment
        hideAllFragment(fTransaction);
        //再显示详细fragment
        if (detailFragment == null){

            detailFragment=new HistoryOderdetailFragment(orderInfo);
            fTransaction.add(R.id.history_fragment_content, detailFragment);
        }
        else {
            detailFragment.setKTVOrderInfo(orderInfo);
            fTransaction.show(detailFragment);
       }


        currFragment = FragmentType.DETAIL;
        fTransaction.commit();
    }

    /**
     * 打开搜索页面o
     */
    public void openSearchFragment(){
        FragmentTransaction fTransaction = fManager.beginTransaction();
        //首先隐藏详细fragment
        hideAllFragment(fTransaction);
        //再显示清单fragment
        if (searchFragment == null){

            searchFragment = new SearchFragment();
            fTransaction.add(R.id.history_fragment_content, searchFragment);
        }
        else {
            fTransaction.remove(searchFragment);
            searchFragment = new SearchFragment();
            fTransaction.add(R.id.history_fragment_content, searchFragment);
        }
        isSearch = true;
        currFragment = FragmentType.SEARCH;
        fTransaction.commit();
    }

    /**
     * 返回搜索页面o
     */
    public void backSearchFragment(){
        FragmentTransaction fTransaction = fManager.beginTransaction();
        //首先隐藏详细fragment
        hideAllFragment(fTransaction);
        //再显示清单fragment
        if (searchFragment == null){

            searchFragment = new SearchFragment();
            fTransaction.add(R.id.history_fragment_content, searchFragment);
        }
        else {
            fTransaction.show(searchFragment);
        }

        currFragment = FragmentType.SEARCH;
        fTransaction.commit();
    }

    /**
     * 隐藏所有fragment
     * @param fTransaction
     */
    private void hideAllFragment(FragmentTransaction fTransaction)
    {
        if (detailFragment!=null)fTransaction.hide(detailFragment);
        if (listFragment!=null)fTransaction.hide(listFragment);
        if (searchFragment!=null)fTransaction.hide(searchFragment);
    }



    @Override
    public void basefinish() {

        switch (currFragment) {
            case HOME:
                finish();
                break;
            case SEARCH:
                isSearch = false;
                showListFragment();
                break;
            case DETAIL:
                if (isSearch)
                    backSearchFragment();
                else
                     showListFragment();
                break;
        }
    }



}
