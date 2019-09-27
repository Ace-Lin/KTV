package com.newland.karaoke.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.karaoke.R;
import com.newland.karaoke.database.KTVOrderProduct;
import com.newland.karaoke.database.KTVProduct;
import com.newland.karaoke.utils.LoadLocalImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Random;

import static com.newland.karaoke.utils.Utility.getPirBitMap;
import static com.newland.karaoke.utils.Utility.imageUrl;

public class OrderDetailProductAdapter extends BaseAdapter {

    private List<KTVOrderProduct> orderProductList;
    private Context mContext;

    public OrderDetailProductAdapter(List<KTVOrderProduct> mData, Context mContext) {
        this.orderProductList = mData;
        this.mContext = mContext;

    }
    @Override
    public int getCount() {
        return orderProductList.size();
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
        KTVOrderProduct orderProduct = orderProductList.get(i);
        KTVProduct product = orderProduct.getProduct();
        ViewHolder viewHolder;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.order_detail_product_item,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.order_item_picture = (ImageView) view.findViewById(R.id.iv_order_detail_product_picture);
            viewHolder.order_item_name = (TextView) view.findViewById(R.id.tv_order_detail_product_name);
            viewHolder.order_item_price = (TextView) view.findViewById(R.id.tv_order_detail_product_price);
            viewHolder.order_item_count = (TextView) view.findViewById(R.id.tv_order_detail_product_count);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        //设置图片
        Bitmap bitmap = getPirBitMap(mContext,product.getProduct_picture());
        if (bitmap==null)
            viewHolder.order_item_picture.setImageResource(Integer.valueOf(product.getProduct_picture()));
        else
            viewHolder.order_item_picture.setImageBitmap(bitmap);
        //viewHolder.order_item_picture.setImageBitmap(getPirBitMap(mContext,product.getProduct_picture()));
       // LoadLocalImageUtil.getInstance().displayFromDrawable(imageUrl[new Random().nextInt(imageUrl.length)],viewHolder.order_item_picture);
        viewHolder.order_item_name.setText(product.getProduct_name());
        viewHolder.order_item_price.setText(mContext.getString(R.string.dollar)+" "+product.getProduct_price());
        viewHolder.order_item_count.setText(String.valueOf(orderProduct.getProduct_quantity()));
        return view;
    }

    private class ViewHolder{
        ImageView order_item_picture;
        TextView order_item_name;
        TextView order_item_price;
        TextView order_item_count;
    }



}
