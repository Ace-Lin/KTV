package com.newland.karaoke.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.newland.karaoke.KTVApplication;
import com.newland.karaoke.R;
import com.newland.karaoke.activity.CheckInActivity;
import com.newland.karaoke.adapter.ProductAdapter;
import com.newland.karaoke.adapter.RoomAdapter;
import com.newland.karaoke.database.KTVProduct;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.model.ProductModel;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class FragmentProduct extends Fragment implements View.OnClickListener{
    private ImageView iv_fg_product_back;
    private ListView lv_fg_product_info;
    private TextView tv_fg_product_amount_show;

    public List<ProductModel> getProductModels() {
        return productModels;
    }
    private List<ProductModel> productModels;
    private  ProductAdapter adapter;
    private CheckInActivity mCheckIn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fagment_product,container,false);
        initUI(view);
        return view;
    }

    public  void initUI(View view){

        List<KTVProduct> productInfos=LitePal.findAll(KTVProduct.class,true);
        productModels=new ArrayList<>();
        for(KTVProduct product:productInfos)
        {
            productModels.add(new ProductModel(product));
        }
        //初始化back按钮
        iv_fg_product_back=view.findViewById(R.id.iv_fg_product_back);
        iv_fg_product_back.setOnClickListener(this);
        //初始化ListView
        lv_fg_product_info=(ListView)view.findViewById(R.id.lv_fg_product_info);
        adapter=new ProductAdapter(getActivity(),R.layout.item_fragment_product,productModels);
        adapter.setOnClickListener(this);
        lv_fg_product_info.setAdapter(adapter);

        //初始化金额显示
        tv_fg_product_amount_show=view.findViewById(R.id.tv_fg_product_amount_show);
    }

    @Override
    public void onClick(View view) {
        Object tag = view.getTag();
        switch(view.getId()){
            case R.id.iv_fg_product_back:
                //更新Activity上方图标
                ImageView iv_top_product=mCheckIn.findViewById(R.id.iv_top_product);
                ImageView iv_top_room=mCheckIn.findViewById(R.id.iv_top_room);
                iv_top_room.setImageResource(R.drawable.room_pressed);
                iv_top_product.setImageResource(R.drawable.snack_normal);
                //切换Fragment
//                mCheckIn.HideAllFragment();
                  mCheckIn.ShowFragment(mCheckIn.getmFragmentRoom());
                break;
            case R.id.iv_item_product_add:
                if (tag != null && tag instanceof Integer) {
                    int position = (Integer) tag;
                    //更改集合的数据
                    int num = productModels.get(position).getProduct_num();
                    if(num<productModels.get(position).getProduct_count()){
                        productModels.get(position).setProduct_num(++num);
                        adapter.notifyDataSetChanged();
                        SetAmount();
                    }
                    else {
                        Toast.makeText(KTVApplication.getContext(),"商品库存不足",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            case R.id.iv_item_product_sub:
                if (tag != null && tag instanceof Integer) {
                    int position = (Integer) tag;
                    //更改集合的数据
                    int num = productModels.get(position).getProduct_num();
                    if(num>0){
                        productModels.get(position).setProduct_num(--num);
                        adapter.notifyDataSetChanged();
                        SetAmount();
                    }
                    else{
                        Toast.makeText(KTVApplication.getContext(),"数量为0",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                break;
        }

    }

    public void SetAmount(){
        double amount=0;
        for(ProductModel productModel:productModels){
            if(productModel.getProduct_num()>0){
                amount+=productModel.getProduct_num()*productModel.getProduct_price();
            }
        }
        tv_fg_product_amount_show.setText(String.valueOf(amount));

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context!=null){
            mCheckIn=(CheckInActivity)context;
        }

    }
}
