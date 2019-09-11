package com.newland.karaoke.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.newland.karaoke.R;
import com.newland.karaoke.model.ProductModel;

import java.util.List;

public class OrderDetailProductBoughtAdapter extends ArrayAdapter<ProductModel> {
    private int resourceId;
    private View.OnClickListener onClickListener;

    public List<ProductModel> getProductModels() {
        return productModels;
    }

    private List<ProductModel> productModels;

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OrderDetailProductBoughtAdapter(Context context, int textViewResourceId, List<ProductModel> objects){
        super(context,textViewResourceId,objects);
        this.resourceId=textViewResourceId;
        this.productModels=objects;

    }

    @Override
    public ProductModel getItem(int position) {
        return productModels.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductModel product=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ViewHolder viewHolder=new ViewHolder();

        //实例化控件
        viewHolder.tv_item_product_bought_name=(TextView)view.findViewById(R.id.tv_item_detail_bought_name);
        viewHolder.tv_item_product_bought_num=(TextView)view.findViewById(R.id.tv_item_detail_bought_num);
        viewHolder.tv_item_product_bought_price=(TextView)view.findViewById(R.id.tv_item_detail_bought_price);
        viewHolder.iv_item_product_bought_add=(ImageButton)view.findViewById(R.id.iv_item_detail_bought_add);
        viewHolder.iv_item_product_bought_sub=(ImageButton)view.findViewById(R.id.iv_item_detail_bought_sub);
        viewHolder.tv_item_product_bought_stock=(TextView)view.findViewById(R.id.tv_item_detail_bought_stock);
        viewHolder.tv_item_detail_order_bought_num=(TextView)view.findViewById(R.id.tv_item_detail_order_bought_num);


        //设置控件值
        viewHolder.tv_item_product_bought_name.setText(product.getProduct_name());
        viewHolder.tv_item_product_bought_price.setText("$ "+product.getProduct_price());
        viewHolder.tv_item_product_bought_stock.setText("stock:"+product.getProduct_count());
        viewHolder.tv_item_detail_order_bought_num.setText("bought:"+product.getProduct_order());
        if(product.getProduct_count()<1){
            //viewHolder.tv_item_product_bought_num.setText("none");
            viewHolder.iv_item_product_bought_add.setClickable(false);
            viewHolder.iv_item_product_bought_add.setVisibility(View.INVISIBLE);
            viewHolder.iv_item_product_bought_sub.setClickable(false);
            viewHolder.iv_item_product_bought_sub.setVisibility(View.INVISIBLE);
        }
        else{
            viewHolder.tv_item_product_bought_num.setText(String.valueOf(product.getProduct_num()));
            viewHolder.iv_item_product_bought_add.setOnClickListener(onClickListener);
            viewHolder.iv_item_product_bought_add.setTag(position);
            viewHolder.iv_item_product_bought_sub.setOnClickListener(onClickListener);
            viewHolder.iv_item_product_bought_sub.setTag(position);
        }
        return view;
    }

    private class ViewHolder{
        public TextView tv_item_product_bought_name;
        public TextView tv_item_product_bought_num;
        public TextView tv_item_product_bought_price;
        public ImageButton iv_item_product_bought_add;
        public ImageButton iv_item_product_bought_sub;
        public TextView tv_item_product_bought_stock;
        public TextView tv_item_detail_order_bought_num;
    }

}
