package com.newland.karaoke.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.newland.karaoke.R;
import com.newland.karaoke.adapter.HistoryDetailAdapter;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVOrderProduct;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.view.ScollViewListView;

import java.util.List;

import static com.newland.karaoke.utils.DateUtil.getSimpleDate;
import static com.newland.karaoke.utils.Utility.getPayType;
import static com.newland.karaoke.utils.Utility.getRoomType;
import static com.newland.karaoke.utils.Utility.getRoomPic;

/**
 *详细历史订单的fragment
 */
public class HistoryOderdetailFragment extends BaseFragment {

    private   KTVOrderInfo ktvOrderInfo;
    private   KTVRoomInfo  ktvRoomInfo;
    private   List<KTVOrderProduct> ktvOrderProducts;
    private HistoryDetailAdapter detailOrderAdapter;
    //region UI变量声明
    private  ImageView roomPicture;
    private  TextView roomInfo;
    private  TextView roomType;
    private  TextView roomPirce;
    private  TextView detail_order_amount;
    private  TextView detail_order_number ;
    private  TextView detail_pay_type ;
    private  TextView detail_order_time ;
    private  TextView detail_pay_time ;
    private  ListView list_history;
    private ScrollView detail_scrollview;
    //endregion


    public HistoryOderdetailFragment( KTVOrderInfo ktvOrderInfo) {
        this.ktvOrderInfo = ktvOrderInfo;
        ktvRoomInfo = ktvOrderInfo.getRoom_id();
        ktvOrderProducts = ktvOrderInfo.getProductList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_oderdetail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar(view,getString(R.string.order_detail));
        initUIData(view);
        showUIData();
        initProductList();
    }

    /**
     * 获取ui
     */
    private  void initUIData(View view){
         roomPicture =(ImageView) view.findViewById(R.id.order_item_picture);
         roomInfo =(TextView) view.findViewById(R.id.order_item_name);
         roomType =(TextView) view.findViewById(R.id.order_item_count);
         roomPirce =(TextView) view.findViewById(R.id.order_item_price);
         detail_order_amount =(TextView) view.findViewById(R.id.detail_order_amount);
         detail_order_number =(TextView) view.findViewById(R.id.detail_order_number);
         detail_pay_type =(TextView) view.findViewById(R.id.detail_pay_type);
         detail_order_time =(TextView) view.findViewById(R.id.detail_order_time);
         detail_pay_time =(TextView) view.findViewById(R.id.detail_pay_time);
         list_history = (ScollViewListView)view.findViewById(R.id.detail_product_listview);
         detail_scrollview = (ScrollView)view.findViewById(R.id.detail_scrollview);
    }

    /**
     * 用来显示获取的UI数据
     */
    private void showUIData() {
        roomPicture.setBackgroundResource(getRoomPic(ktvRoomInfo.getRoom_type()));
        //roomPicture.setImageDrawable(getActivity().getDrawable(R.drawable.small_ktv));//暂时代替
        roomInfo.setText(ktvRoomInfo.getRoom_name());
        roomType.setText(getRoomType(ktvRoomInfo.getRoom_type()));
        roomPirce.setText(getString(R.string.dollar)+" "+ktvRoomInfo.getRoom_price());
        detail_order_amount.setText(getString(R.string.order_amount)+ktvOrderInfo.getPay_amount());
        detail_order_number.setText(" "+ktvOrderInfo.getOrder_number());
        detail_pay_type.setText(" "+getPayType(ktvOrderInfo.getOrder_pay_type()));
        detail_order_time.setText(" "+getSimpleDate(ktvOrderInfo.getOrder_start_time()));
        detail_pay_time.setText(" "+getSimpleDate(ktvOrderInfo.getOrder_end_time()));

    }


    /**
     * 初始化历史订单列表数据
     */
    private  void initProductList()
    {
        if(ktvOrderProducts.size()<=0)
            return;

        detailOrderAdapter = new HistoryDetailAdapter(ktvOrderProducts, getContext());
        list_history.setAdapter(detailOrderAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
