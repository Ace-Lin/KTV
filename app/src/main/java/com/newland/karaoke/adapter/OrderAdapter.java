package com.newland.karaoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.karaoke.R;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.utils.LoadLocalImageUtil;

import java.util.List;
import java.util.Random;

import static com.newland.karaoke.utils.DensityUtil.df_two;
import static com.newland.karaoke.utils.Utility.getRoomPic;
import static com.newland.karaoke.utils.Utility.imageUrl;

public class OrderAdapter extends BaseAdapter implements View.OnClickListener {

    private List<KTVOrderInfo> orderDatas;
    private Context mContext;
    private Callback callback;

    public interface Callback {
         void  listSubClick(View view);
    }

    public OrderAdapter(List<KTVOrderInfo> mData, Context mContext, Callback callback) {
        this.orderDatas = mData;
        this.mContext = mContext;
        this.callback = callback;
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
        KTVOrderInfo orderInfo = orderDatas.get(i);
        KTVRoomInfo roomInfo = orderInfo.getRoom_id();
        ViewHolder viewHolder;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.order_item,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.order_item_picture = (ImageView) view.findViewById(R.id.order_room_image);
            viewHolder.order_item_name = (TextView) view.findViewById(R.id.order_room_name);
            viewHolder.order_item_amount = (TextView) view.findViewById(R.id.order_amount);
            viewHolder.btn_detail = (Button) view.findViewById(R.id.order_btn_details);
            viewHolder.btn_pay = (Button) view.findViewById(R.id.order_btn_pay);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.order_item_picture.setBackgroundResource(getRoomPic(roomInfo.getRoom_type()));
      //  LoadLocalImageUtil.getInstance().displayFromDrawable(getRoomPic(roomInfo.getRoom_type()),viewHolder.order_item_picture);
        viewHolder.order_item_name.setText(roomInfo.getRoom_name());
        viewHolder.order_item_amount.setText(mContext.getString(R.string.order_amount)+" "+ df_two.format(orderInfo.getPay_amount()));
        viewHolder.btn_detail.setOnClickListener(this);
        viewHolder.btn_pay.setOnClickListener(this);
        viewHolder.btn_pay.setTag(i);//需要设置tag
        viewHolder.btn_detail.setTag(i);
        return view;
    }

    @Override
    public void onClick(View view) {
        callback.listSubClick(view);
    }

    private class ViewHolder{
        ImageView order_item_picture;
        TextView order_item_name;
        TextView order_item_amount;
        Button btn_detail;
        Button btn_pay;
    }



}
