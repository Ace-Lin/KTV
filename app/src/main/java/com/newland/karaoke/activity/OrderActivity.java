package com.newland.karaoke.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.newland.karaoke.PrintManager;
import com.newland.karaoke.R;
import com.newland.karaoke.adapter.OrderAdapter;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.mesdk.LoadKey;
import com.newland.karaoke.mesdk.device.SDKDevice;
import com.newland.karaoke.mesdk.print.PrinterModule;
import com.newland.karaoke.mesdk.scan.ScanListener;
import com.newland.karaoke.mesdk.scan.ScannerModule;
import com.newland.karaoke.model.PrintModel;
import com.newland.karaoke.utils.LogUtil;
import com.newland.karaoke.view.PayDialog;
import com.newland.karaoke.view.TipDialog;
import com.newland.mtype.util.ISOUtils;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.newland.karaoke.mesdk.AppConfig.InputResultType;
import static com.newland.karaoke.mesdk.AppConfig.ResultCode;
import static com.newland.karaoke.utils.DateUtil.getCurrentDayBegin;
import static com.newland.karaoke.utils.ToastUtil.showShortText;

public class OrderActivity extends BaseActivity implements  OrderAdapter.Callback, PayDialog.NoticeDialogListener {

    private  ListView list_order;
    private  OrderAdapter orderAdapter;
    private  List<KTVOrderInfo>   ktvOrderInfoList;
    private PayDialog payDialog;
    private  KTVOrderInfo currOrderInfo;//当前订单
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        hideStatusBar();
        showToolBar(R.string.order_today);
        showListView();
        //初始化,链接设备
        SDKDevice.getInstance().connectDevice(this);
        context = this;
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
            showShortText(this,position + "order_btn_details");
            Bundle bundle=new Bundle();
            bundle.putInt("id",orderId);
            Intent intentOrderDetail=new Intent(OrderActivity.this,OrderDetailActivity.class);
            intentOrderDetail.putExtras(bundle);
            startActivity(intentOrderDetail);
            finish();
        }
         else if (view.getId()==R.id.order_btn_pay) {
           payDialog= new PayDialog(ktvOrderInfoList.get(position).getPay_amount(),this);
           payDialog.show(getSupportFragmentManager(),"payDialog");
            //开线程加载秘钥，不然会延迟出现，加载慢
            new Thread(() -> LoadKey.getInstance(context)).start();
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
               new PrinterModule(this).printOrder();
                break;
            case KTVType.PayType.CARD:
                Intent intent = new Intent(this, CardPayActivity.class);
                intent.putExtra("Amount",currOrderInfo.getPay_amount());
                startActivityForResult(intent,ResultCode.BANK_CARD);
                overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case KTVType.PayType.QRCODE:
                new ScannerModule(context).startScan(scanListener);
                break;
        }

        payDialog.dismiss();

//        KTVRoomInfo roomInfo = currOrderInfo.getRoom_id();
//        roomInfo.setRoom_status(KTVType.RoomStatus.FREE);
//        roomInfo.save();
//
//        currOrderInfo.setOrder_pay_type(payType);
//        currOrderInfo.setOrder_status(KTVType.OrderStatus.PAID);
//        currOrderInfo.setOrder_number(getNoFormatDate(new Date()));
//        currOrderInfo.setOrder_end_time(new Date());
//        currOrderInfo.save();
    }


    //刷卡数据的返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ResultCode.BANK_CARD) {
            if (resultCode == ResultCode.SWIP_RESULT ) {
                int type = data.getIntExtra("type",0);
                switch (type){
                    case InputResultType.FREE:
                        LogUtil.debug("免密支付",getClass());
                        break;
                    case InputResultType.SUCC:
                        //打印账号，加密密码信息
                        openTipDialog(getString(R.string.tips_print),data.getStringExtra("account")+"/n"+ISOUtils.hexString(data.getByteArrayExtra("password")),false);
                       break;
                    case InputResultType.CANCEL:
                    case InputResultType.INPUTFAIL:
                        openTipDialog(getString(R.string.tips_error),getString(R.string.tips_rebrush),true);
                        break;
                }
            }else if (resultCode == ResultCode.IC_RESULT){
                     openTipDialog(getString(R.string.tips_print),data.getStringExtra("account")+"/n"+ISOUtils.hexString(data.getByteArrayExtra("password")),false);
               }else if (resultCode == ResultCode.RF_RESULT){
                if (data.getStringExtra("account")!=null)
                    openTipDialog(getString(R.string.tips_print),data.getStringExtra("account"),false);
                 else
                    openTipDialog(getString(R.string.tips_error),getString(R.string.tips_rebrush),true);
            }else if (resultCode == ResultCode.EXCEPTION){
                    openTipDialog(getString(R.string.tips_error),data.getStringExtra("exception"),true);
            }
        }

    }

    // 扫码回调监听
    public ScanListener scanListener = new ScanListener() {

        @Override
        public void scanResponse(String var1) {
            openTipDialog(getString(R.string.tips_print),var1,false);
            LogUtil.error(var1, getClass());
        }

        @Override
        public void scanError() {

        }

        @Override
        public void scanTimeout() {

        }

        @Override
        public void scanCancel() {

        }
    };

   //刷卡之后的提示窗口信息
    private void openTipDialog(String title, String info, boolean isError){
        if (!isError)
            startPrint();

        TipDialog dialog = new TipDialog(title,info,isError);
        new Handler().postDelayed(() -> dialog.show(getSupportFragmentManager(),"tip"), 500);
    }

    //完成订单开始打印数据
    private  void startPrint(){

    }

}
