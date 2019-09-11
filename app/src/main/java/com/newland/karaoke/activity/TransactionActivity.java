package com.newland.karaoke.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.newland.karaoke.R;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.fragment.BaseFragment;
import com.newland.karaoke.fragment.HistoryOderdetailFragment;
import com.newland.karaoke.fragment.HistoryOrderlistFragment;
import com.newland.karaoke.fragment.SearchFragment;

public class TransactionActivity extends BaseActivity {
    //定义三种fragment类型
    private FragmentManager fManager;//获取管理器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        hideStatusBar();
        initFragment();
    }

    /**
     * 初始化布局加载的fragment
     */
    private void initFragment(){
        fManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.history_fragment_content,  new HistoryOrderlistFragment());
        fTransaction.commit();
    }

    /**
     * 打开对应的fragment
     */
    public void openFragment(@Nullable KTVOrderInfo orderInfo)
    {
        FragmentTransaction fTransaction = fManager.beginTransaction();

        if (orderInfo!=null)
            fTransaction.replace(R.id.history_fragment_content, new HistoryOderdetailFragment(orderInfo));
        else
            fTransaction.replace(R.id.history_fragment_content, new SearchFragment());

        fTransaction.addToBackStack(null);
        fTransaction.commit();
    }

    @Override
    public void basefinish() {

        if (getSupportFragmentManager().getBackStackEntryCount() <= 0)//这里是取出我们返回栈存在Fragment的个数
            finish();
        else
            getSupportFragmentManager().popBackStack();
    }

}
