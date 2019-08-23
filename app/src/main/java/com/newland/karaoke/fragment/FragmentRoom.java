package com.newland.karaoke.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.newland.karaoke.R;
import com.newland.karaoke.activity.CheckInActivity;
import com.newland.karaoke.adapter.RoomAdapter;
import com.newland.karaoke.database.KTVRoomInfo;

import org.litepal.LitePal;

import java.util.List;

import static android.content.ContentValues.TAG;

public class FragmentRoom extends Fragment implements View.OnClickListener{
    private Spinner sp_fg_room_choose;
    private ListView lv_fg_room_info;
    private ImageView iv_fg_room_next;
    private List<KTVRoomInfo> roomInfos=null;
    private CheckInActivity mCheckIn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fagment_room,container,false);
        initUI(view);
        return view;
    }
    public  void initUI(View view){
        //初始化spinner
        sp_fg_room_choose = (Spinner) view.findViewById(R.id.sp_fg_room_choose);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomInfos= LitePal.findAll(KTVRoomInfo.class,true);
        for(KTVRoomInfo room:roomInfos){
            adapter.add(room.getRoom_name());
        }
        sp_fg_room_choose.setAdapter(adapter);
        sp_fg_room_choose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCheckIn.setmKTVRoomInfo(roomInfos.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //初始化ListView
        lv_fg_room_info=view.findViewById(R.id.lv_fg_room_info);
        RoomAdapter roomAdapter=new RoomAdapter(getActivity(),R.layout.item_fragment_room,roomInfos);
        lv_fg_room_info.setAdapter(roomAdapter);

        //初始化next按钮
        iv_fg_room_next=view.findViewById(R.id.iv_fg_room_next);
        iv_fg_room_next.setOnClickListener(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context!=null){
            mCheckIn=(CheckInActivity)context;
        }

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_fg_room_next:

                //更新Activity上方图标
                ImageView iv_top_product=mCheckIn.findViewById(R.id.iv_top_product);
                ImageView iv_top_room=mCheckIn.findViewById(R.id.iv_top_room);
                iv_top_room.setImageResource(R.drawable.room_normal);
                iv_top_product.setImageResource(R.drawable.snack_pressed);
                //切换Fragment
                //mCheckIn.HideAllFragment();
                mCheckIn.ShowFragment(mCheckIn.getmFragmentProduct());
                break;
        }
    }
}
