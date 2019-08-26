package com.newland.karaoke.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.newland.karaoke.R;
import com.newland.karaoke.activity.AddActivity;
import com.newland.karaoke.adapter.SettingRoomAdapter;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVRoomInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static com.newland.karaoke.utils.DensityUtil.dp2px;

/**
 * 用来显示所有房间信息的Fragment
 */
public class RoomDetailsFragment extends Fragment implements SwipeMenuListView.OnMenuItemClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private FragmentManager fManager;
    private List<KTVRoomInfo> roomDatas= new ArrayList<>();
    private SwipeMenuListView list_news;
    private  SwipeMenuCreator creator;
    private Context context;
    private SettingRoomAdapter roomAdapter;

    public RoomDetailsFragment(FragmentManager fManager,Context context) {
        this.fManager = fManager;
        this.context = context;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        creatSwipeMenu();
        roomDatas =LitePal.findAll(KTVRoomInfo.class);
        View view = inflater.inflate(R.layout.fragment_room_details, container, false);
        list_news = (SwipeMenuListView) view.findViewById(R.id.room_listview);
        roomAdapter = new SettingRoomAdapter(roomDatas, getActivity());
        list_news.setAdapter(roomAdapter);
        list_news.setMenuCreator(creator);    // 设置 creator
        list_news.setOnMenuItemClickListener(this);
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


    @Override
    public void onStart()
    {
        super.onStart();
        updateListview();
    }

    /**
     * 更改数据之后刷新数据
     */
    private  void updateListview()
    {
        //这种做法notifyDataSetChanged不会刷新，对roomDatas的内存指向做了修改，
        // 但是该指向并没有通知到adapter中的list，也就是说list指向没有发生变化
        // roomDatas =LitePal.findAll(KTVRoomInfo.class);

        roomDatas.clear();
        roomDatas.addAll(LitePal.findAll(KTVRoomInfo.class));
        roomAdapter.notifyDataSetChanged();
    }



    /**
     * 创建左滑需要的menu
     */
    private void creatSwipeMenu()
    {
         creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "edit" item
                SwipeMenuItem editItem = new SwipeMenuItem(context);
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(50,185, 193)));
                // set item width
                editItem.setWidth(dp2px(context,40));
                // set item title
                editItem.setIcon(R.drawable.icon_set_edit);
                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(context);
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(50,185, 193)));
                // set item width
                deleteItem.setWidth(dp2px(context,40));
                // set a icon
                deleteItem.setIcon(R.drawable.icon_set_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
    }

/**
 * 点击SwipMenu触发的事件
 */
 @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
             switch (index) {
            case 0:
                Intent intent=new Intent();
                intent = new Intent(context, AddActivity.class);
                intent.putExtra(getString(R.string.fragment_type), KTVType.FragmentType.EDITROOM);
                intent.putExtra(getString(R.string.edit_detail_id),roomDatas.get(position).getId());
                startActivity(intent);
                break;
            case 1:
                LitePal.delete(KTVRoomInfo.class,roomDatas.get(position).getId());
                updateListview();
                break;
        }
        return false;
    }

}
