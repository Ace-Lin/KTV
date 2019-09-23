package com.newland.karaoke.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.newland.karaoke.R;
import com.newland.karaoke.adapter.OrderAdapter;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.mesdk.LoadKey;
import com.newland.karaoke.mesdk.card.SwipModule;
import com.newland.karaoke.mesdk.device.SDKDevice;
import com.newland.karaoke.mesdk.print.PrinterModule;
import com.newland.karaoke.mesdk.scan.ScannerModule;
import com.newland.karaoke.view.PayDialogFragment;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.newland.karaoke.utils.DateUtil.getCurrentDayBegin;
import static com.newland.karaoke.utils.DateUtil.getNoFormatDate;
import static com.newland.karaoke.utils.ToastUtil.showShortText;

public class OrderActivity extends BaseActivity implements  OrderAdapter.Callback, PayDialogFragment.NoticeDialogListener {

    private  TextView txt_title;
    private  ListView list_order;
    private  OrderAdapter orderAdapter;
    private  List<KTVOrderInfo>   ktvOrderInfoList;
    private  PayDialogFragment payDialog;
    private  KTVOrderInfo currOrderInfo;//当前订单
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        hideStatusBar();
        initView();
        showListView();
        //初始化,链接设备
        SDKDevice.getInstance().connectDevice(this);
        context = this;
    }

    //初始化数据
    private void initView(){

        Toolbar commonToolBar = (Toolbar)findViewById(R.id.setting_toolbar);
        commonToolBar.setNavigationIcon(R.drawable.icon_back_left);
        commonToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basefinish();
            }
        });

        txt_title = findViewById(R.id.setting_title);
        list_order = (ListView)findViewById(R.id.order_listview);

        txt_title.setText(R.string.order_today);

    }

    //初始化数据
    private  void showListView(){

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
            showShortText(this,position + "order_btn_details");
            Bundle bundle=new Bundle();
            bundle.putInt("id",orderId);
            Intent intentOrderDetail=new Intent(OrderActivity.this,OrderDetailActivity.class);
            intentOrderDetail.putExtras(bundle);
            startActivity(intentOrderDetail);
            finish();
        }
         else if (view.getId()==R.id.order_btn_pay) {
            payDialog= new PayDialogFragment(ktvOrderInfoList.get(position).getPay_amount(),this);
            payDialog.show(getSupportFragmentManager(),"payDialog");
            //开线程加载秘钥，不然会延迟出现，加载慢
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LoadKey.getInstance(context);
                }
            }).start();
        }


    }

    @Override
    public void basefinish() {
        finish();
    }

    @Override
    public void onDialogBtnClick(int payType) {
        switch (payType){
            case KTVType.PayType.CASH:
                showShortText(this,payType);
                new PrinterModule(this).printOrder();
                break;
            case KTVType.PayType.CARD:
                // showShortText(this,payType);
                Intent intent = new Intent(this, CardPayActivity.class);
                intent.putExtra("Amount",currOrderInfo.getPay_amount());
                startActivity(intent);
                break;
            case KTVType.PayType.QRCODE:
                showShortText(this,payType);
                new ScannerModule(context).startScan();
                break;
        }
//
//        KTVRoomInfo roomInfo = currOrderInfo.getRoom_id();
//        roomInfo.setRoom_status(KTVType.RoomStatus.FREE);
//        roomInfo.save();
//
//        currOrderInfo.setOrder_pay_type(payType);
//        currOrderInfo.setOrder_status(KTVType.OrderStatus.PAID);
//        currOrderInfo.setOrder_number(getNoFormatDate(new Date()));
//        currOrderInfo.setOrder_end_time(new Date());
//        currOrderInfo.save();

       // payDialog.dismiss();
    }




}
