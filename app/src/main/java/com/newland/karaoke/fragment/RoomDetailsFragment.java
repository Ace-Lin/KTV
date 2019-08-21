package com.newland.karaoke.fragment;

import android.content.Context;
import android.net.RouteInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.newland.karaoke.R;
import com.newland.karaoke.adapter.RoomAdapter;
import com.newland.karaoke.database.KTVRoomInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来显示所有房间信息的Fragment
 */
public class RoomDetailsFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private FragmentManager fManager;
    private List<KTVRoomInfo> RoomDatas;
    private ListView list_news;

    public RoomDetailsFragment(FragmentManager fManager) {
        this.fManager = fManager;
        initRoomData();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_room_details, container, false);
        list_news = (ListView) view.findViewById(R.id.room_listview);
        RoomAdapter myAdapter = new RoomAdapter(RoomDatas, getActivity());
        list_news.setAdapter(myAdapter);
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 初始化获取数据库数据
     */
    private  void initRoomData()
    {
        RoomDatas= LitePal.findAll(KTVRoomInfo.class);
    }


}
