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

        //实例化控件
        TextView tv_item_product_name=(TextView)view.findViewById(R.id.tv_item_product_name);
        TextView tv_item_product_num=(TextView)view.findViewById(R.id.tv_item_product_num);
        TextView tv_item_product_price=(TextView)view.findViewById(R.id.tv_item_product_price);
        ImageButton iv_item_product_add=(ImageButton)view.findViewById(R.id.iv_item_product_add);
        ImageButton iv_item_product_sub=(ImageButton)view.findViewById(R.id.iv_item_product_sub);

        //设置控件值
        tv_item_product_name.setText(product.getProduct_name());
        tv_item_product_num.setText(String.valueOf(product.getProduct_num()));
        tv_item_product_price.setText(String.valueOf(product.getProduct_price()));

        iv_item_product_add.setOnClickListener(onClickListener);
        iv_item_product_add.setTag(position);
        iv_item_product_sub.setOnClickListener(onClickListener);
        iv_item_product_sub.setTag(position);

        return view;
    }

}
