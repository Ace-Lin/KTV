package com.newland.karaoke.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.newland.karaoke.R;
import com.newland.karaoke.adapter.RoomAdapter;
import com.newland.karaoke.database.KTVRoomInfo;

import org.litepal.LitePal;

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
    private List<KTVRoomInfo> RoomDatas;
    private SwipeMenuListView list_news;
    private  SwipeMenuCreator creator;
    private Context context;

    public RoomDetailsFragment(FragmentManager fManager,Context context) {
        this.fManager = fManager;
        this.context = context;
        initRoomData();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        creatSwipeMenu();
        View view = inflater.inflate(R.layout.fragment_room_details, container, false);
        list_news = (SwipeMenuListView) view.findViewById(R.id.room_listview);
        RoomAdapter myAdapter = new RoomAdapter(RoomDatas, getActivity());
        list_news.setAdapter(myAdapter);
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

    /**
     * 初始化获取数据库数据
     */
    private  void initRoomData()
    {
        RoomDatas= LitePal.findAll(KTVRoomInfo.class);
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
                // edit
                Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                // delete;
               // mAppList.remove(position);
                //mAdapter.notifyDataSetChanged();
                Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}
