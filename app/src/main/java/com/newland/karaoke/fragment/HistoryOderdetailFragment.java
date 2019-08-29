package com.newland.karaoke.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.newland.karaoke.R;
import com.newland.karaoke.adapter.DetailOrderAdapter;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVOrderProduct;
import com.newland.karaoke.database.KTVRoomInfo;

import java.util.List;

import static com.newland.karaoke.utils.Utility.getPayType;
import static com.newland.karaoke.utils.Utility.getRoomType;
import static com.newland.karaoke.utils.Utility.getSimpleDate;

/**
 *详细历史订单的fragment
 */
public class HistoryOderdetailFragment extends BaseFragment {

  private   KTVOrderInfo ktvOrderInfo;
  private   KTVRoomInfo  ktvRoomInfo;
  private   List<KTVOrderProduct> ktvOrderProducts;

    public HistoryOderdetailFragment(KTVOrderInfo ktvOrderInfo) {
        this.ktvOrderInfo = ktvOrderInfo;
        ktvRoomInfo=ktvOrderInfo.getRoom_id();
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
      //  setHasOptionsMenu(true);
        initBaseView(view,R.id.setting_toolbar,getString(R.string.order_history));
        initOrderList(view);
        initUIData(view);
    }

    /**
     * 初始化其他ui信息
     */
    private  void initUIData(View view){
        ImageView roomPicture =(ImageView) view.findViewById(R.id.order_room_picture);
        TextView roomInfo =(TextView) view.findViewById(R.id.order_room_name);
        TextView roomType =(TextView) view.findViewById(R.id.order_room_type);
        TextView roomPirce =(TextView) view.findViewById(R.id.order_room_price);
        TextView detail_order_amount =(TextView) view.findViewById(R.id.detail_order_amount);
        TextView detail_order_number =(TextView) view.findViewById(R.id.detail_order_number);
        TextView detail_pay_type =(TextView) view.findViewById(R.id.detail_pay_type);
        TextView detail_order_time =(TextView) view.findViewById(R.id.detail_order_time);
        TextView detail_pay_time =(TextView) view.findViewById(R.id.detail_pay_time);

        roomPicture.setImageDrawable(getActivity().getDrawable(R.drawable.small_ktv));//暂时代替
        roomInfo.setText(ktvRoomInfo.getRoom_name());
        roomType.setText(getRoomType(ktvRoomInfo.getRoom_type()));
        roomPirce.setText(getString(R.string.dollar)+" "+ktvRoomInfo.getRoom_price());
        detail_order_amount.setText(" "+ktvOrderInfo.getPay_amount());
        detail_order_number.setText(" "+ktvOrderInfo.getOrder_number());
        detail_pay_type.setText(" "+getPayType(ktvOrderInfo.getOrder_pay_type()));
        detail_order_time.setText(" "+getSimpleDate(ktvOrderInfo.getOrder_start_time()));
        detail_pay_time.setText(" "+getSimpleDate(ktvOrderInfo.getOrder_end_time()));
    }

    /**
     * 初始化历史订单列表数据
     */
    private  void initOrderList(View view)
    {
        if(ktvOrderProducts.size()<=0)
            return;

        ListView list_history = (ListView)view.findViewById(R.id.detail_product_listview);
        DetailOrderAdapter detailOrderAdapter = new DetailOrderAdapter(ktvOrderProducts, getContext());
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
