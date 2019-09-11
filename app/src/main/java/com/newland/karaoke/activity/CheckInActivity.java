package com.newland.karaoke.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.newland.karaoke.KTVApplication;
import com.newland.karaoke.R;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVOrderProduct;
import com.newland.karaoke.database.KTVProduct;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.fragment.FragmentProduct;
import com.newland.karaoke.fragment.FragmentRoom;
import com.newland.karaoke.model.ProductModel;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckInActivity extends AppCompatActivity implements View.OnClickListener{
    //控件
    private ImageView iv_top_npt;
    private ImageView iv_top_room;
    private ImageView iv_top_product;
    private ImageView iv_top_submit;
    //保存房间选择
    private KTVRoomInfo mKTVRoomInfo;

    //切换的fragment
    private FragmentRoom mFragmentRoom;
    private FragmentProduct mFragmentProduct;

    private Fragment mCurrentFragment;

    public KTVRoomInfo getmKTVRoomInfo() {
        return mKTVRoomInfo;
    }

    public void setmKTVRoomInfo(KTVRoomInfo mKTVRoomInfo) {
        this.mKTVRoomInfo = mKTVRoomInfo;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(Color.TRANSPARENT);//防止5.x以后半透明影响效果，使用这种透明方式
        }
        initUI();//初始化UI
        initData();//初始化数据
    }

    public void initUI(){
        //初始化UI
        iv_top_npt=(ImageView)findViewById(R.id.iv_top_npt);
        iv_top_room=(ImageView)findViewById(R.id.iv_top_room);
        iv_top_product=(ImageView)findViewById(R.id.iv_top_product);
        iv_top_submit=(ImageView)findViewById(R.id.iv_top_submit);
        //监听
        iv_top_npt.setOnClickListener(this);
        iv_top_submit.setOnClickListener(this);


    }
//    public  void SwitchFragment(Fragment fragment) {
//        if (fragment != mCurrentFragment) {
//            if (!fragment.isAdded()) {
//                getSupportFragmentManager().beginTransaction().hide(mCurrentFragment)
//                        .add(R.id.fl_checkin, fragment).commit();
//            } else {
//                getSupportFragmentManager().beginTransaction().hide(mCurrentFragment)
//                        .show(fragment).commit();
//            }
//            mCurrentFragment = fragment;
//        }
//    }
    public void HideAllFragment(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        if(mFragmentRoom!=null){
            fragmentTransaction.hide(mFragmentRoom);

        }
        if(mFragmentProduct!=null){
            fragmentTransaction.hide(mFragmentProduct);
        }
        fragmentTransaction.commit();

    }
    public void ShowFragment(Fragment fragment){
        HideAllFragment();
        if(fragment!=null){
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.show(fragment).commit();
        }
    }
    public void AddFragment(Fragment fragment){
        if(fragment!=null){
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fl_checkin,fragment).commit();
        }
    }

    public  void initData(){
        mFragmentRoom=new FragmentRoom();
        mFragmentProduct=new FragmentProduct();

        AddFragment(mFragmentRoom);
        AddFragment(mFragmentProduct);
        ShowFragment(mFragmentRoom);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.iv_top_npt:
                Intent intent_npt=new Intent(CheckInActivity.this,MainActivity.class);
                startActivity(intent_npt);
                finish();
                break;
            case R.id.iv_top_submit:
                Toast.makeText(KTVApplication.getContext(),"您点击了提交",Toast.LENGTH_SHORT).show();
                OrderSubmit();
                Intent intent_submit=new Intent(CheckInActivity.this,MainActivity.class);
                startActivity(intent_submit);
                finish();
                break;
        }
    }

    public FragmentRoom getmFragmentRoom() {
        return mFragmentRoom;
    }

    public FragmentProduct getmFragmentProduct() {
        return mFragmentProduct;
    }
    private void OrderSubmit(){
        double totalMoney=mKTVRoomInfo.getRoom_price()+mFragmentProduct.getCurrentAmount();
        //添加房间与商品订单至订单表
        KTVOrderInfo ktvOrder=new KTVOrderInfo();
        ktvOrder.setOrder_start_time(new Date());
        ktvOrder.setRoom_id(mKTVRoomInfo);
        ktvOrder.setProductList(new ArrayList<KTVOrderProduct>());
        ktvOrder.setOrder_status(KTVType.OrderStatus.UNPAID);
        ktvOrder.setPay_amount(totalMoney);
        ktvOrder.save();
        //房间的价格


        //修改房间状态
        mKTVRoomInfo.setRoom_status(KTVType.RoomStatus.NO_FREE);
//        List<KTVOrderInfo> orderInfos=mKTVRoomInfo.getProduct();
//        if(orderInfos==null){
//            orderInfos=new ArrayList<>();
//        }
//        orderInfos.add(ktvOrder);
//        mKTVRoomInfo.setProduct(orderInfos);
        mKTVRoomInfo.save();



        //遍历添加商品订单
        List<ProductModel> productModels=mFragmentProduct.getProductModels();
        for(ProductModel model:productModels){
            if(model.getProduct_num()!=0){
                //创建商品订单
                KTVOrderProduct ktvOrderProduct=new KTVOrderProduct();
                ktvOrderProduct.setProduct_quantity(model.getProduct_num());

                //获取商品
                KTVProduct ktvProduct= LitePal.find(KTVProduct.class,model.getProduct_id(),true);
                ktvOrderProduct.setProduct(ktvProduct);
                //保存商品订单
                ktvOrderProduct.save();

                //添加商品订单至商品表
                //ktvProduct.getProduct().add(ktvOrderProduct);
                List<KTVOrderProduct> ktvProductTemp=ktvProduct.getProduct();
                ktvProductTemp.add(ktvOrderProduct);
                ktvProduct.setProduct(ktvProductTemp);
                ktvProduct.save();

                //添加商品至商品订单表
                ktvOrderProduct.setProduct(ktvProduct);
                //添加订单至商品订单表
                ktvOrderProduct.setKtvOrderInfo(ktvOrder);
                ktvOrderProduct.save();//保存商品订单表记录

                //修改商品库存
                int productCountUpdate=ktvProduct.getProduct_count()-model.getProduct_num();
                ktvProduct.setProduct_count(productCountUpdate);
                ktvProduct.save();


                //添加商品订单至订单表
                List<KTVOrderProduct> ktvOrderTemp=ktvOrder.getProductList();
                ktvOrderTemp.add(ktvOrderProduct);
                ktvOrder.setProductList(ktvOrderTemp);
                ktvOrder.save();

            }

        }


    }
}
