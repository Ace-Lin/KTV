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

public class BaseFragment extends Fragment {
    /**
     * 通用的ToolBar标题
     */
   public TextView commonTitle;

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
        commonToolBar.setNavigationIcon(R.drawable.icon_back_left);
        commonToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)getActivity()).basefinish();
            }
        });

        commonTitle=(TextView)view.findViewById(R.id.setting_title);
        commonTitle.setText(title);
    }

}
