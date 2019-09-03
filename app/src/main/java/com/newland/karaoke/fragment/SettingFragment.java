package com.newland.karaoke.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.newland.karaoke.R;
import com.newland.karaoke.activity.SettingActivity;
import com.newland.karaoke.adapter.SettingAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class SettingFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        initToolbar(view,getString(R.string.setting));
        initUI(view);
        return view;
    }

    /**
     * 初始化UI信息
     */
    private void initUI(View view)
    {
        List<String> nameData =new ArrayList<>();
        nameData.add(getString(R.string.setting_roomDetails));
        nameData.add(getString(R.string.setting_productDetails));
        nameData.add(getString(R.string.setting_AddRoom));
        nameData.add(getString(R.string.setting_AddProduct));
        ListView list_news = (ListView)view.findViewById(R.id.setting_listview);
        SettingAdapter myAdapter = new SettingAdapter(nameData, getContext());
        list_news.setAdapter(myAdapter);
        list_news.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ((SettingActivity)getActivity()).openFragment(i);
    }
}
