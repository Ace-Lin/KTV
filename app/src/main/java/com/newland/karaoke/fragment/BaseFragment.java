package com.newland.karaoke.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.newland.karaoke.R;
import com.newland.karaoke.activity.BaseActivity;

public class BaseFragment extends Fragment implements View.OnClickListener {
    /**
     * 通用的ToolBar标题
     */
   public TextView commonTitle;

    /**
     * 通用的ToolBar后退
     */
    public Button btnBack;
    /**
     * 通用的ToolBar
     */
    public Toolbar commonToolBar;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
   }

    /**
     * 显示toolbar和title
     */
    public void initToolbar(View view, String title)
    {
        commonToolBar = (Toolbar)view.findViewById(R.id.setting_toolbar);

        btnBack = (Button)view.findViewById(R.id.fragment_back_btn);
        btnBack.setOnClickListener(this);

        commonTitle=(TextView)view.findViewById(R.id.setting_title);
        commonTitle.setText(title);
    }



    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.fragment_back_btn) {
            //重写的话，记得super
            ((BaseActivity) getActivity()).basefinish();
        }
    }
}
