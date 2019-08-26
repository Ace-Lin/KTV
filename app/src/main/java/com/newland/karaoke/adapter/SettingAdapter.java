package com.newland.karaoke.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newland.karaoke.R;

import java.util.List;

public class SettingAdapter extends BaseAdapter {

    private List<String> nameDatas;
    private Context context;
    public SettingAdapter(List<String> data,Context mContext) {
        this.nameDatas = data;
        this.context = mContext;

    }

    @Override
    public int getCount() {
        return nameDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        SettingAdapter.ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.setting_item,viewGroup,false);
            viewHolder = new SettingAdapter.ViewHolder();
            viewHolder.txt_btn_name = (TextView) view.findViewById(R.id.setting_btn_text);
            view.setTag(viewHolder);
        }else{
            viewHolder = (SettingAdapter.ViewHolder) view.getTag();
        }

        viewHolder.txt_btn_name.setText(nameDatas.get(i));

        return view;
    }

    private class ViewHolder{
        TextView txt_btn_name;
    }
}
