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

public class ProductAdapter extends ArrayAdapter<ProductModel> {
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

    public ProductAdapter(Context context, int textViewResourceId, List<ProductModel> objects){
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
        viewHolder.tv_item_product_name=(TextView)view.findViewById(R.id.tv_item_product_name);
        viewHolder.tv_item_product_num=(TextView)view.findViewById(R.id.tv_item_product_num);
        viewHolder.tv_item_product_price=(TextView)view.findViewById(R.id.tv_item_product_price);
        viewHolder.iv_item_product_add=(ImageButton)view.findViewById(R.id.iv_item_product_add);
        viewHolder.iv_item_product_sub=(ImageButton)view.findViewById(R.id.iv_item_product_sub);

        //设置控件值
        viewHolder.tv_item_product_name.setText(product.getProduct_name());
        viewHolder.tv_item_product_num.setText(String.valueOf(product.getProduct_num()));
        viewHolder.tv_item_product_price.setText(String.valueOf(product.getProduct_price()));

        viewHolder.iv_item_product_add.setOnClickListener(onClickListener);
        viewHolder.iv_item_product_add.setTag(position);
        viewHolder.iv_item_product_sub.setOnClickListener(onClickListener);
        viewHolder.iv_item_product_sub.setTag(position);
        return view;
    }

    private class ViewHolder{
        public TextView tv_item_product_name;
        public TextView tv_item_product_num;
        public TextView tv_item_product_price;
        public ImageButton iv_item_product_add;
        public ImageButton iv_item_product_sub;
    }

}
