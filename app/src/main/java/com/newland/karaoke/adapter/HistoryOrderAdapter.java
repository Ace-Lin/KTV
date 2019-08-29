package com.newland.karaoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.newland.karaoke.R;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVRoomInfo;

import java.util.List;

import static com.newland.karaoke.utils.Utility.getSimpleDate;

public class HistoryOrderAdapter extends BaseAdapter {

    private List<KTVOrderInfo> orderDatas;
    private Context mContext;

    public HistoryOrderAdapter(List<KTVOrderInfo> mData, Context mContext) {
        this.orderDatas = mData;
        this.mContext = mContext;

    }
    @Override
    public int getCount() {
        return orderDatas.size();
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
        KTVOrderInfo ktvOrderInfo = orderDatas.get(i);
        ViewHolder viewHolder;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.history_item,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.txt_order_number = (TextView) view.findViewById(R.id.history_order_number);
            viewHolder.txt_order_time = (TextView) view.findViewById(R.id.history_order_time);
            viewHolder.txt_room_no = (TextView) view.findViewById(R.id.order_room_no);
            viewHolder.txt_product_count = (TextView) view.findViewById(R.id.order_product_count);
            viewHolder.txt_order_amount = (TextView) view.findViewById(R.id.order_amount);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.txt_order_number.setText(ktvOrderInfo.getOrder_number());
        viewHolder.txt_order_time.setText(getSimpleDate(ktvOrderInfo.getOrder_end_time()));
        viewHolder.txt_room_no.setText(mContext.getString(R.string.order_room_no)+" "+ktvOrderInfo.getRoom_id().getRoom_name());
        viewHolder.txt_product_count.setText(mContext.getString(R.string.order_product_count)+" "+ktvOrderInfo.getProductList().size());
        viewHolder.txt_order_amount.setText(mContext.getString(R.string.order_amount)+ktvOrderInfo.getPay_amount());
        return view;
    }

    private class ViewHolder{
        TextView txt_order_number;
        TextView txt_order_time;
        TextView txt_room_no;
        TextView txt_product_count;
        TextView txt_order_amount;
    }



}
