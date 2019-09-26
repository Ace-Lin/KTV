package com.newland.karaoke;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.newland.karaoke.activity.BaseActivity;
import com.newland.karaoke.activity.CardPayActivity;
import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.mesdk.AppConfig;
import com.newland.karaoke.mesdk.LoadKey;
import com.newland.karaoke.mesdk.scan.ScanListener;
import com.newland.karaoke.mesdk.scan.ScannerModule;
import com.newland.karaoke.model.PrintModel;
import com.newland.karaoke.utils.LogUtil;
import com.newland.karaoke.view.PayDialog;
import com.newland.karaoke.view.TipDialog;
import com.newland.mtype.util.ISOUtils;

import org.litepal.LitePal;

import java.util.Date;

import static com.newland.karaoke.utils.DateUtil.getNoFormatDate;

public class PayHandler implements PayDialog.NoticeDialogListener {

    private PayDialog payDialog;
    private Context context;
    private  int currPayType;
    private KTVOrderInfo currOrderInfo;
    private Runnable finshCallBack;

    public PayHandler(Context context, KTVOrderInfo currOrderInfo,Runnable finshCallBack) {
        this.context = context;
        this.currOrderInfo = currOrderInfo;
        this.finshCallBack = finshCallBack;
        OpenPayDialog();
    }

    public  void OpenPayDialog(){
        payDialog= new PayDialog(currOrderInfo.getPay_amount(),this);
        payDialog.show(((BaseActivity)context).getSupportFragmentManager(),"payDialog");
        //开线程加载秘钥，不然会延迟出现，加载慢
        new Thread(() -> LoadKey.getInstance(context)).start();
    }


    @Override
    public void onDialogBtnClick(int payType) {
        currPayType = payType;
        switch (payType){
            case KTVType.PayType.CASH:
                finshOrder();
                break;
            case KTVType.PayType.CARD:
                Intent intent = new Intent(context, CardPayActivity.class);
                intent.putExtra("Amount",currOrderInfo.getPay_amount());
                ((BaseActivity)context).startActivityForResult(intent, AppConfig.ResultCode.BANK_CARD);
                ((BaseActivity)context).overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                break;
            case KTVType.PayType.QRCODE:
                new ScannerModule(context).startScan(scanListener);
                break;
        }
        payDialog.dismiss();
    }

    public void onCardPayResult(int requestCode, int resultCode, Intent data){
        if (requestCode == AppConfig.ResultCode.BANK_CARD) {
            if (resultCode == AppConfig.ResultCode.SWIP_RESULT ) {
                int type = data.getIntExtra("type",0);
                switch (type){
                    case AppConfig.InputResultType.FREE:
                        LogUtil.debug("免密支付",getClass());
                        break;
                    case AppConfig.InputResultType.SUCC:
                        //打印账号，加密密码信息
                        openTipDialog(getString(R.string.tips_print),"account:"+data.getStringExtra("account")+"\npassword:"+ISOUtils.hexString(data.getByteArrayExtra("password")),false);
                        break;
                    case AppConfig.InputResultType.CANCEL:
                    case AppConfig.InputResultType.INPUTFAIL:
                        openTipDialog(getString(R.string.tips_error),getString(R.string.tips_rebrush),true);
                        break;
                }
            }else if (resultCode == AppConfig.ResultCode.IC_RESULT){
                openTipDialog(getString(R.string.tips_print),"account:"+data.getStringExtra("account")+"\npassword:"+ISOUtils.hexString(data.getByteArrayExtra("password")),false);
            }else if (resultCode == AppConfig.ResultCode.RF_RESULT){
                LogUtil.debug(data.getStringExtra("account"),getClass());
                if (data.getStringExtra("account")!=null)
                    openTipDialog(getString(R.string.tips_print),"account:"+data.getStringExtra("account"),false);
                else
                    openTipDialog(getString(R.string.tips_error),getString(R.string.tips_rebrush),true);
                LogUtil.debug("免密支付",getClass());
            }else if (resultCode == AppConfig.ResultCode.EXCEPTION){
                openTipDialog(getString(R.string.tips_error),data.getStringExtra("exception"),true);
            }
        }
    }


    //刷卡之后的提示窗口信息
    private void openTipDialog(String title, String info, boolean isError){
        TipDialog dialog = new TipDialog(title,info,isError);
        new Handler().postDelayed(() -> dialog.show(((BaseActivity)context).getSupportFragmentManager(),"tip"), 500);
        new Handler().postDelayed(() -> {if (!isError)   finshOrder();},600);
    }

    //完成订单
    private void finshOrder(){
        KTVRoomInfo roomInfo = currOrderInfo.getRoom_id();
        roomInfo.setRoom_status(KTVType.RoomStatus.FREE);
        roomInfo.save();

        currOrderInfo.setOrder_pay_type(currPayType);
        currOrderInfo.setOrder_status(KTVType.OrderStatus.PAID);
        currOrderInfo.setOrder_number(getNoFormatDate(new Date()));
        currOrderInfo.setOrder_end_time(new Date());
        currOrderInfo.save();

//        PrintManager printManager = PrintManager.getInstance(context);
//        printManager.printBill(new PrintModel(LitePal.find(KTVOrderInfo.class,currOrderInfo.getId())));

        if (finshCallBack!=null)
            finshCallBack.run();
    }


    // 扫码回调监听
    public ScanListener scanListener = new ScanListener() {

        @Override
        public void scanResponse(String var1) {
            openTipDialog(context.getString(R.string.tips_print),var1,false);
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


    //获取字符串
    private String getString(int id){   return context.getString(id);    }



}
