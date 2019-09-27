package com.newland.karaoke.activity;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.newland.karaoke.KTVApplication;
import com.newland.karaoke.R;
import com.newland.karaoke.adapter.OrderDetailChangeAdapter;
import com.newland.karaoke.adapter.OrderDetailProductBoughtAdapter;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVOrderProduct;
import com.newland.karaoke.database.KTVProduct;
import com.newland.karaoke.model.ProductModel;
import com.newland.karaoke.view.ScollViewListView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ChangeOrderInfoActivity extends BaseActivity implements View.OnClickListener{
    private int id=-1;
    private TextView txt_title;
    private Toolbar commonToolBar;
    private ImageView order_change_submit;
    private KTVOrderInfo currentOrder;
    private List<KTVOrderProduct> currentOrderProducts;
    private ListView sv_order_detail_check_product;
    private ListView sv_order_detail_change_product;
    private OrderDetailChangeAdapter adapterChange;
    private OrderDetailProductBoughtAdapter adapterCheck;
    private List<ProductModel> productModelList;
    private List<ProductModel> productModelBoughtList;
    private double currentAmount;
    private TextView tv_order_detail_change_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_order_info);
        initData();
        initUI();
        initEvent();
    }

    private void initData(){
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            id=bundle.getInt("id");
            currentOrder= LitePal.where("id=?",String.valueOf(id)).findFirst(KTVOrderInfo.class,true);
            currentOrderProducts=currentOrder.getProductList();
        }
        List<KTVProduct> productInfos=LitePal.findAll(KTVProduct.class,true);
        productModelList=new ArrayList<>();
        boolean flag=false;
        for(KTVProduct product:productInfos)
        {
            for(KTVOrderProduct tempProduct:currentOrderProducts){
                if(product.getId()==tempProduct.getProduct().getId())
                {
                    flag=true;
                    break;
                }
            }
            if(!flag){
                productModelList.add(new ProductModel(product));
            }
            flag=false;
        }

        productModelBoughtList=new ArrayList<>();
        for (KTVOrderProduct orderProduct:currentOrderProducts){
            ProductModel model=new ProductModel(orderProduct.getProduct());
            model.setProduct_order(orderProduct.getProduct_quantity());
            model.setProduct_num(orderProduct.getProduct_quantity());
            productModelBoughtList.add(model);
        }

    }

    private void initUI(){
        hideStatusBar();
        commonToolBar = (Toolbar)findViewById(R.id.order_detail_change_toolbar);
        commonToolBar.setNavigationIcon(R.drawable.icon_back_left);
        commonToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basefinish();
            }
        });
        //加载标题
        txt_title = (TextView) findViewById(R.id.order_detail_change_title);
        txt_title.setText("Order Change");
        //加载修改按钮
        order_change_submit=(ImageView)findViewById(R.id.iv_order_detail_change_submit);
        order_change_submit.setOnClickListener(this);

        //加载订单商品信息
        sv_order_detail_check_product=(ScollViewListView)findViewById(R.id.sv_order_detail_check_product);
        adapterCheck=new OrderDetailProductBoughtAdapter(ChangeOrderInfoActivity.this,R.layout.item_order_detail_bought_product,productModelBoughtList);
        adapterCheck.setOnClickListener(this);
        sv_order_detail_check_product.setAdapter(adapterCheck);
        //加载订单商品信息
        sv_order_detail_change_product=(ScollViewListView)findViewById(R.id.sv_order_detail_change_product);
        adapterChange=new OrderDetailChangeAdapter(ChangeOrderInfoActivity.this,R.layout.item_order_detail_product,productModelList);
        adapterChange.setOnClickListener(this);
        sv_order_detail_change_product.setAdapter(adapterChange);

        tv_order_detail_change_amount=(TextView) findViewById(R.id.tv_order_detail_change_amount);
        SetAmount();

    }

    private void initEvent(){

    }



    @Override
    public void onClick(View view) {
        Object tag = view.getTag();
        switch(view.getId()){
            case R.id.iv_order_detail_change_submit:
                OrderInfoSubmit();
                basefinish();
                break;
            case  R.id.iv_item_detail_add:
                if (tag != null && tag instanceof Integer) {
                    int position = (Integer) tag;
                    //更改集合的数据
                    int num = productModelList.get(position).getProduct_num();
                    if(num<productModelList.get(position).getProduct_count()){
                        productModelList.get(position).setProduct_num(++num);
                        adapterChange.notifyDataSetChanged();
                        SetAmount();
                    }
                    else {
                        Toast.makeText(KTVApplication.getContext(),"商品库存不足",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                break;
            case R.id.iv_item_detail_sub:
                if (tag != null && tag instanceof Integer) {
                    int position = (Integer) tag;
                    //更改集合的数据
                    int num = productModelList.get(position).getProduct_num();
                    if(num>0){
                        productModelList.get(position).setProduct_num(--num);
                        adapterChange.notifyDataSetChanged();
                        SetAmount();
                    }
                    else{
                        Toast.makeText(KTVApplication.getContext(),"数量为0",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                break;
            case  R.id.iv_item_detail_bought_add:
                if (tag != null && tag instanceof Integer) {
                    int position = (Integer) tag;
                    //更改集合的数据
                    int num = productModelBoughtList.get(position).getProduct_num();
                    int totalAssets=productModelBoughtList.get(position).getProduct_count()
                            +productModelBoughtList.get(position).getProduct_order();
                    if(num<totalAssets){
                        productModelBoughtList.get(position).setProduct_num(++num);
                        adapterCheck.notifyDataSetChanged();
                        SetAmount();
                    }
                    else {
                        Toast.makeText(KTVApplication.getContext(),"商品库存不足",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                break;
            case R.id.iv_item_detail_bought_sub:
                if (tag != null && tag instanceof Integer) {
                    int position = (Integer) tag;
                    //更改集合的数据
                    int num = productModelBoughtList.get(position).getProduct_num();
                    if(num>0){
                        productModelBoughtList.get(position).setProduct_num(--num);
                        adapterCheck.notifyDataSetChanged();
                        SetAmount();
                    }
                    else{
                        Toast.makeText(KTVApplication.getContext(),"数量为0",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                break;
            default:
                    break;

        }
    }
    public void SetAmount(){
        currentAmount=0;
        for(ProductModel model1:productModelList){
            if(model1.getProduct_num()>0){
                currentAmount+=model1.getProduct_num()*model1.getProduct_price();
            }
        }
        for(ProductModel model2:productModelBoughtList){
            if(model2.getProduct_num()>0){
                currentAmount+=model2.getProduct_num()*model2.getProduct_price();
            }
        }
        tv_order_detail_change_amount.setText("Amount:$"+currentAmount);

    }

    private void OrderInfoSubmit(){
        //修改已购买商品信息
        for(ProductModel modelChange:productModelBoughtList){
            if(modelChange.getProduct_order()!=modelChange.getProduct_num()){
                int delta=modelChange.getProduct_num()-modelChange.getProduct_order();

                //修改商品订单表
                for(KTVOrderProduct orderProduct:currentOrderProducts){
                    if(orderProduct.getProduct().getId()==modelChange.getProduct_id()){
                        orderProduct.setProduct_quantity(modelChange.getProduct_num());
                        orderProduct.setKtvOrderInfo(currentOrder);
                        orderProduct.setProduct(LitePal.find(KTVProduct.class,modelChange.getProduct_id(),true));
                        orderProduct.save();
                        break;
                    }
                }

                //修改商品库存数量
                KTVProduct changeProduct= LitePal.find(KTVProduct.class,modelChange.getProduct_id(),true);
                int productCountUpdate=changeProduct.getProduct_count()-delta;
                changeProduct.setProduct_count(productCountUpdate);
                changeProduct.save();
            }

        }
        //新增商品
        for(ProductModel modelAdd:productModelList){
            if(modelAdd.getProduct_num()!=0){
                //创建商品订单
                KTVOrderProduct ktvOrderProduct=new KTVOrderProduct();
                ktvOrderProduct.setProduct_quantity(modelAdd.getProduct_num());
                //保存商品订单
                ktvOrderProduct.save();

                //获取商品
                KTVProduct ktvProduct= LitePal.find(KTVProduct.class,modelAdd.getProduct_id(),true);
                //添加商品至商品订单表
                ktvOrderProduct.setProduct(ktvProduct);
                ktvOrderProduct.save();

                //添加商品订单至商品表
                List<KTVOrderProduct> ktvProductTemp=ktvProduct.getProduct();
                ktvProductTemp.add(ktvOrderProduct);
                ktvProduct.setProduct(ktvProductTemp);
                ktvProduct.save();

                //添加订单至商品订单表
                ktvOrderProduct.setKtvOrderInfo(currentOrder);
                ktvOrderProduct.save();//保存商品订单表记录

                //修改商品库存
                int productCountUpdate=ktvProduct.getProduct_count()-modelAdd.getProduct_num();
                ktvProduct.setProduct_count(productCountUpdate);
                ktvProduct.save();


                //添加商品订单至订单表
                List<KTVOrderProduct> ktvOrderTemp=currentOrder.getProductList();
                ktvOrderTemp.add(ktvOrderProduct);
                currentOrder.setProductList(ktvOrderTemp);
                currentOrder.save();

            }
        }


    }
    private void JumpToDetail(){
        Intent intentBack=new Intent(ChangeOrderInfoActivity.this,OrderDetailActivity.class);
        Bundle bundleBack=new Bundle();
        bundleBack.putInt("id",id);
        intentBack.putExtras(bundleBack);
        startActivity(intentBack);
    }




    @Override
    public void basefinish() {
        JumpToDetail();
        finish();
    }
}
