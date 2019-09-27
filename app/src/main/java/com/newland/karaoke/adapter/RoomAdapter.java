package com.newland.karaoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.newland.karaoke.R;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVRoomInfo;

import java.util.List;

import static com.newland.karaoke.utils.Utility.getRoomPic;

public class RoomAdapter extends ArrayAdapter<KTVRoomInfo> {
    private int resourceId;
    public RoomAdapter(Context context, int textViewResourceId, List<KTVRoomInfo> objects){
        super(context,textViewResourceId,objects);
        this.resourceId=textViewResourceId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        KTVRoomInfo room=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView iv_item_room_pic=(ImageView)view.findViewById(R.id.iv_item_room_pic);
        TextView tv_item_room_name=(TextView)view.findViewById(R.id.tv_item_room_name);
        TextView tv_item_room_state=(TextView)view.findViewById(R.id.tv_item_room_state);
        TextView tv_item_room_type=(TextView)view.findViewById(R.id.tv_item_room_type);
        iv_item_room_pic.setImageResource(getRoomPic(room.getRoom_type()));
        tv_item_room_name.setText(room.getRoom_name());
        tv_item_room_state.setText(RoomStateAndType(0,room.getRoom_status()));
        tv_item_room_type.setText(RoomStateAndType(1,room.getRoom_type()));
        return view;
    }
    public String RoomStateAndType(int idType,int roomInfoId){
        if(idType==0){//0-state
            switch(roomInfoId){
                case KTVType.RoomStatus.FREE:
                    return "Empty";
                 case KTVType.RoomStatus.NO_FREE:
                     return "Full";
            }
        }
        else if(idType==1){//1-type
            switch(roomInfoId){
                case KTVType.RoomType.BIG:
                    return "Big";
                case KTVType.RoomType.MIDDLE:
                    return "Middle";
                case KTVType.RoomType.SMAlL:
                    return "Small";
            }
        }
        return "info missing!";
    }
}
