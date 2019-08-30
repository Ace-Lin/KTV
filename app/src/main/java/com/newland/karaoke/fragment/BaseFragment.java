package com.newland.karaoke.fragment;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.newland.karaoke.R;

public class BaseFragment extends Fragment {
    /**
     * 通用的ToolBar标题
     */
    private TextView commonTitle;
    /**
     * 通用的ToolBar
     */
    private Toolbar commonToolBar;

    public  Activity mActivity;
    private AppCompatActivity mAppCompatActivity;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
        mAppCompatActivity=(AppCompatActivity)mActivity;
   }

    /**
     * 显示toolbar和title
     */
    public void initBaseView(View view,int id,String title)
    {
        commonToolBar = (Toolbar)view.findViewById(id);
        commonTitle=(TextView)view.findViewById(R.id.setting_title);
        mAppCompatActivity.setSupportActionBar(commonToolBar);
        commonTitle.setText(title);
        setBackArrow();
    }


    /**
     * 设置左上角back按钮
     */
    public void setBackArrow() {

        final Drawable upArrow = getResources().getDrawable(R.drawable.icon_back_left);
        //给ToolBar设置左侧的图标
        mAppCompatActivity.getSupportActionBar().setHomeAsUpIndicator(upArrow);
        // 给左上角图标的左边加上一个返回的图标 。对应ActionBar.DISPLAY_HOME_AS_UP
        mAppCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置actionBar的标题是否显示，对应ActionBar.DISPLAY_SHOW_TITLE。
        mAppCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
    }



}
