package com.newland.karaoke.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.newland.karaoke.R;
import com.newland.karaoke.activity.SettingActivity;
import com.newland.karaoke.adapter.SettingProductAdapter;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVProduct;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static com.newland.karaoke.utils.DensityUtil.dp2px;

/**
 * 用来显示所有商品信息的Fragment
 */
public class ProductDetailsFragment extends BaseFragment implements SwipeMenuListView.OnMenuItemClickListener, Toolbar.OnMenuItemClickListener {

    private List<KTVProduct> productDatas = new ArrayList<>();
    private SwipeMenuListView list_news;
    private SwipeMenuCreator creator;
    private Context context;
    private SettingProductAdapter productAdapter;

    public ProductDetailsFragment(Context context) {
        this.context = context;
        productDatas= LitePal.findAll(KTVProduct.class);
    }

    /**
     * 更改数据之后刷新数据
     */
    private  void updateListview()
    {
        // roomDatas =LitePal.findAll(KTVRoomInfo.class);

        productDatas.clear();
        productDatas.addAll(LitePal.findAll(KTVProduct.class));
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        creatSwipeMenu();
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        initToolbar(view,getString(R.string.setting_productDetails));
        commonToolBar.inflateMenu(R.menu.add_toolbar);
        commonToolBar.setOnMenuItemClickListener(this);

        list_news = (SwipeMenuListView) view.findViewById(R.id.project_listview);
        productAdapter = new SettingProductAdapter(productDatas, context);
        list_news.setAdapter(productAdapter);
        list_news.setMenuCreator(creator);    // 设置 creator

        list_news.setOnMenuItemClickListener(this);
        return view;
    }


   //每次回退显示进行刷新
    @Override
    public void onStart()
    {
        super.onStart();
        updateListview();
    }



    /**
     * 创建左滑需要的menu
     */
    private void creatSwipeMenu()
    {
        creator = menu -> {
            // create "edit" item
            SwipeMenuItem editItem = new SwipeMenuItem(context);
            // set item background
            editItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
            // set item width
            editItem.setWidth(dp2px(context,40));
            // set item title
            editItem.setIcon(R.drawable.icon_set_edit);
            // add to menu
            menu.addMenuItem(editItem);

            // create "delete" item
            SwipeMenuItem deleteItem = new SwipeMenuItem(context);
            // set item background
            deleteItem.setBackground(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
            // set item width
            deleteItem.setWidth(dp2px(context,60));
            // set a icon
            deleteItem.setIcon(R.drawable.icon_set_delete);
            // add to menu
            menu.addMenuItem(deleteItem);
        };
    }

    /**
     * 点击SwipMenu触发的事件
     */
    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
            case 0:
                ((SettingActivity) getActivity()).openUpdateFragment(KTVType.FragmentType.ADDPRODUCT,productDatas.get(position).getId());
                break;
            case 1:
                LitePal.delete(KTVProduct.class,productDatas.get(position).getId());
                updateListview();
                break;
        }
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId()==R.id.setting_add) {
            ((SettingActivity) getActivity()).openFragment(KTVType.FragmentType.ADDPRODUCT,false);
            return true;
        }
        return false;
    }
}
