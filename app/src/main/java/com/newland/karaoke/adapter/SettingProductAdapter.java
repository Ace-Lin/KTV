package com.newland.karaoke.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.newland.karaoke.R;
import com.newland.karaoke.database.KTVProduct;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.utils.LoadLocalImageUtil;

import java.util.List;
import java.util.Random;

import static com.newland.karaoke.utils.DensityUtil.df_two;
import static com.newland.karaoke.utils.Utility.getPirBitMap;
import static com.newland.karaoke.utils.Utility.imageUrl;

public class SettingProductAdapter extends BaseAdapter {

    private List<KTVProduct> productsDatas;
    private Context mContext;

    public SettingProductAdapter(List<KTVProduct> mData, Context mContext) {
        this.productsDatas = mData;
        this.mContext = mContext;

    }
    @Override
    public int getCount() {
        return productsDatas.size();
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
        KTVProduct ktvProduct = productsDatas.get(i);
        ViewHolder viewHolder;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.product_list_item,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.txt_product_name = (TextView) view.findViewById(R.id.set_product_name);
            viewHolder.txt_product_picture = (ImageView) view.findViewById(R.id.set_product_picture);
            viewHolder.txt_product_count = (TextView) view.findViewById(R.id.set_product_count);
            viewHolder.txt_product_price = (TextView) view.findViewById(R.id.set_product_price);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.txt_product_name.setText(ktvProduct.getProduct_name());

        //设置图片
        Bitmap bitmap = getPirBitMap(mContext,ktvProduct.getProduct_picture());
        if (bitmap==null)
            viewHolder.txt_product_picture.setImageResource(Integer.valueOf(ktvProduct.getProduct_picture()));
         else
           viewHolder.txt_product_picture.setImageBitmap(bitmap);
       // LoadLocalImageUtil.getInstance().displayFromDrawable(imageUrl[new Random().nextInt(imageUrl.length)],viewHolder.txt_product_picture);
        viewHolder.txt_product_count.setText(String.valueOf(ktvProduct.getProduct_count()));
        viewHolder.txt_product_price.setText(df_two.format(ktvProduct.getProduct_price()));
        return view;
    }

    private class ViewHolder{
        TextView txt_product_name;
        TextView txt_product_count;
        TextView txt_product_price;
        ImageView txt_product_picture;
    }



}
