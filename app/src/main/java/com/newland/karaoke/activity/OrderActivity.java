package com.newland.karaoke.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.newland.karaoke.PayHandler;
import com.newland.karaoke.R;
import com.newland.karaoke.adapter.OrderAdapter;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.mesdk.device.SDKDevice;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.newland.karaoke.utils.DateUtil.getCurrentDayBegin;
import static com.newland.karaoke.utils.ToastUtil.showShortText;

public class OrderActivity extends BaseActivity implements  OrderAdapter.Callback {

    private  ListView list_order;
    private  OrderAdapter orderAdapter;
    private  List<KTVOrderInfo>   ktvOrderInfoList;
    private  KTVOrderInfo currOrderInfo;//当前订单
    private PayHandler payHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        hideStatusBar();
        showToolBar(R.string.order_today);
        showListView();
        //初始化,链接设备
        SDKDevice.getInstance().connectDevice(this);
    }


    //初始化数据
    private  void showListView(){
        list_order = (ListView)findViewById(R.id.order_listview);
        ktvOrderInfoList = LitePal.where("order_status= ? and (order_start_time>? and order_start_time<?) ",
                String.valueOf(KTVType.OrderStatus.UNPAID),  getCurrentDayBegin(Calendar.getInstance()),String.valueOf(new Date().getTime())).find(KTVOrderInfo.class,true);


        orderAdapter = new OrderAdapter(ktvOrderInfoList, this,this);
        list_order.setAdapter(orderAdapter);
    }



    @Override
    public void listSubClick(View view) {
        int  position = (Integer)view.getTag();//adapter设置了tag
        currOrderInfo = ktvOrderInfoList.get(position);
        int orderId=currOrderInfo.getId();
        if (view.getId()==R.id.order_btn_details) {
            Bundle bundle=new Bundle();
            bundle.putInt("id",orderId);
            Intent intentOrderDetail=new Intent(OrderActivity.this,OrderDetailActivity.class);
            intentOrderDetail.putExtras(bundle);
            startActivity(intentOrderDetail);
            finish();
        }
         else if (view.getId()==R.id.order_btn_pay) {
           payHandler = new PayHandler(this,currOrderInfo,()->{ktvOrderInfoList.remove(position);orderAdapter.notifyDataSetChanged();});
        }


    }

    @Override
    public void basefinish() {
        finish();
    }

    //刷卡数据的返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        payHandler.onCardPayResult(requestCode, resultCode, data);
    }


}
