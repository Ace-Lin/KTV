package com.newland.karaoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newland.karaoke.R;
import com.newland.karaoke.database.KTVRoomInfo;

import java.util.List;

public class SettingRoomAdapter extends BaseAdapter {

    private List<KTVRoomInfo> roomDatas;
    private Context mContext;

    public SettingRoomAdapter(List<KTVRoomInfo> mData, Context mContext) {
        this.roomDatas = mData;
        this.mContext = mContext;

    }
    @Override
    public int getCount() {
        return roomDatas.size();
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
        ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.room_item,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.txt_room_name = (TextView) view.findViewById(R.id.set_room_name);
            viewHolder.txt_room_type = (TextView) view.findViewById(R.id.set_room_type);
            viewHolder.txt_room_price = (TextView) view.findViewById(R.id.set_room_price);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txt_room_name.setText(roomDatas.get(i).getRoom_name());
        viewHolder.txt_room_type.setText(JudgeType(roomDatas.get(i).getRoom_type()));
        viewHolder.txt_room_price.setText(String.valueOf(roomDatas.get(i).getRoom_price()));
        return view;
    }

    private class ViewHolder{
        TextView txt_room_name;
        TextView txt_room_type;
        TextView txt_room_price;
    }

    /**
     * 判断房间类型返回字符串
     * @param type
     * @return
     */
    private  String JudgeType(int type)
    {
        String str=null;
        switch (type)
        {
            case 0:
                str="Big";
                break;
            case 1:
                str="Middle";
                break;
            case 2:
                str="Small";
                break;
                default:
        }

        return str;
    }

}
