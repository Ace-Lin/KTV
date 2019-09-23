package com.newland.karaoke.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.newland.karaoke.R;
import com.newland.karaoke.mesdk.AppConfig;
import com.newland.karaoke.mesdk.device.SDKDevice;
import com.newland.karaoke.mesdk.pin.KeyBoardNumberActivity;
import com.newland.karaoke.mesdk.emv.SimpleTransferListener;
import com.newland.karaoke.utils.LogUtil;
import com.newland.me.SupportMSDAlgorithm;
import com.newland.mtype.DeviceRTException;
import com.newland.mtype.ModuleType;
import com.newland.mtype.common.ExCode;
import com.newland.mtype.common.InnerProcessingCode;
import com.newland.mtype.common.MESeriesConst;
import com.newland.mtype.common.ProcessingCode;
import com.newland.mtype.event.AbstractProcessDeviceEvent;
import com.newland.mtype.event.DeviceEvent;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.cardreader.CommonCardType;
import com.newland.mtype.module.common.cardreader.K21CardReader;
import com.newland.mtype.module.common.cardreader.K21CardReaderEvent;
import com.newland.mtype.module.common.cardreader.OpenCardReaderEvent;
import com.newland.mtype.module.common.cardreader.OpenCardReaderResult;
import com.newland.mtype.module.common.cardreader.SearchCardRule;
import com.newland.mtype.module.common.emv.EmvModule;
import com.newland.mtype.module.common.emv.EmvTransController;
import com.newland.mtype.module.common.pin.KeyManageType;
import com.newland.mtype.module.common.pin.PinInputEvent;
import com.newland.mtype.module.common.pin.WorkingKey;
import com.newland.mtype.module.common.swiper.K21Swiper;
import com.newland.mtype.module.common.swiper.MSDAlgorithm;
import com.newland.mtype.module.common.swiper.SwipResult;
import com.newland.mtype.module.common.swiper.SwipResultType;
import com.newland.mtype.module.common.swiper.SwiperReadModel;
import com.newland.mtype.util.Dump;
import com.newland.mtype.util.ISOUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.newland.karaoke.activity.OrderActivity.IC_RESULT;
import static com.newland.karaoke.activity.OrderActivity.SWIP_RESULT;
import static com.newland.karaoke.utils.DensityUtil.df_two;

public class CardPayActivity extends AppCompatActivity {

    private K21CardReader cardReader;
    private K21Swiper k21swiper;
    private EmvModule emvModule;
    private EmvTransController controller;
    private TextView tip_textview;
    private double pay_amount;
    private String currAccNO;
    private boolean isMagCard;


    private static final int GET_TRACKTEXT_FAILED = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_pay);
        initData();
        startReadCard();
    }


    /**
     * 初始化获取模块，获取ui
     */
    public void initData() {
        emvModule = SDKDevice.getInstance().getEmvModuleType();
        emvModule.initEmvModule(this);
        cardReader = SDKDevice.getInstance().getCardReaderModuleType();
        k21swiper = SDKDevice.getInstance().getK21Swiper();
        tip_textview = findViewById(R.id.tip_read_card);
        pay_amount = getIntent().getDoubleExtra("Amount",0);
        TextView pay = findViewById(R.id.txt_pay_amount);
        pay.setText(getString(R.string.dollar)+""+ df_two.format(pay_amount));
    }


    /**
     * 开启读卡操作
     */
    private void startReadCard(){
        //设置AppConfig.EMV.amt = amt，保证startEmv参数不为空，不然监听 onRequestAmountEntry 会重新出现输入框
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            BigDecimal amt = new BigDecimal(pay_amount);
            AppConfig.EMV.amt = amt;
            LogUtil.debug(getString(R.string.msg_trans_money_is) + df.format(amt), getClass());

        } catch (Exception e) {
            LogUtil.debug(getString(R.string.msg_enter_money_exception) + e,getClass());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                executReadCard();
            }
        }).start();
    }

    /**
     * 执行读卡流程
     */
    public void executReadCard(){
        try {
            LogUtil.debug(getString(R.string.msg_pl_swipe_or_insert1), getClass());
            EventHolder<K21CardReaderEvent> listener = new EventHolder<K21CardReaderEvent>();
            cardReader.openCardReader(new ModuleType[] { ModuleType.COMMON_SWIPER, ModuleType.COMMON_ICCARDREADER, ModuleType.COMMON_RFCARDREADER }, true, 60, TimeUnit.SECONDS, listener, SearchCardRule.RFCARD_FIRST);
            try {
                listener.startWait();
            } catch (InterruptedException e) {
                cardReader.cancelCardRead();
                LogUtil.debug(getString(R.string.msg_user_revocate_swiper), getClass());

            }
            OpenCardReaderEvent event = listener.event;
            event = preEvent(event, GET_TRACKTEXT_FAILED);
            if (event == null) {
                LogUtil.debug(getString(R.string.msg_user_revocate_swiper), getClass());
                return;
            }
            OpenCardReaderResult cardResult = event.getOpenCardReaderResult();
            CommonCardType[] openedModuleTypes = cardResult.getResponseCardTypes();
            LogUtil.debug(getString(R.string.msg_cardreader_open_succ), getClass());
            if (cardResult == null || openedModuleTypes.length <= 0) {
                if (event.isUserCanceled()) {
                    LogUtil.debug(getString(R.string.msg_user_cancel), getClass());
                } else {
                    LogUtil.debug(getString(R.string.msg_cradreader_finish), getClass());
                }
                return;
            }
            if (openedModuleTypes.length > 1) {
                LogUtil.debug(getString(R.string.msg_cardreader_return_fause) + openedModuleTypes.length, getClass());
                throw new DeviceRTException(GET_TRACKTEXT_FAILED, "Should return only one type of cardread action!but is " + openedModuleTypes.length);

            }
            switch (openedModuleTypes[0]) {
                case MSCARD:
                    LogUtil.debug(getString(R.string.msg_current_card_mc), getClass());
                    boolean isCorrent = cardResult.isMSDDataCorrectly();
                    if (!isCorrent) {
                        throw new DeviceRTException(GET_TRACKTEXT_FAILED, "Swip failed!");
                    }
                    SwipResult swipRslt = null;
                    if(AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)){
                        swipRslt = k21swiper.readEncryptResult(new SwiperReadModel[] { SwiperReadModel.READ_SECOND_TRACK, SwiperReadModel.READ_THIRD_TRACK }, new WorkingKey(AppConfig.Pin.MKSK_DES_INDEX_WK_TRACK), SupportMSDAlgorithm.getMSDAlgorithm(MESeriesConst.TrackEncryptAlgorithm.BY_UNIONPAY_MODEL));
                    }else if(AppConfig.DUKPT_DES.equals(AppConfig.KEY_SYS_ALG)){
                        swipRslt = k21swiper.readEncryptResult(new SwiperReadModel[] { SwiperReadModel.READ_SECOND_TRACK, SwiperReadModel.READ_THIRD_TRACK }, new WorkingKey(AppConfig.Pin.DUKPT_DES_INDEX), new MSDAlgorithm(KeyManageType.DUKPT, 0x31, 0x00));
                    }else{
                        //todo
                    }
                    if (null != swipRslt && swipRslt.getRsltType() == SwipResultType.SUCCESS) {
                        AppConfig.EMV.swipResult = swipRslt;
                        byte[] secondTrack = swipRslt.getSecondTrackData();
                        byte[] thirdTrack = swipRslt.getThirdTrackData();
                        LogUtil.debug(getString(R.string.common_second_track) + (secondTrack == null ? "null" : Dump.getHexDump(secondTrack)), getClass());
                        LogUtil.debug(getString(R.string.common_third_track) + (thirdTrack == null ? "null" : Dump.getHexDump(thirdTrack)), getClass());
                        LogUtil.debug(getString(R.string.msg_siwper_succ), getClass());
                        LogUtil.debug(getString(R.string.msg_pl_enter_pwd), getClass());
                        startOnlinePinInput(swipRslt.getAccount().getAcctNo(),true);
                    } else {
                        LogUtil.debug(getString(R.string.msg_swiper_null_reswiper), getClass());
                    }
                    AppConfig.EMV.consumeType = 0;// 消费报文区分
                    break;
                case ICCARD:
                    tipHandler.sendMessage(new Message());
                    LogUtil.debug(getString(R.string.msg_current_card_is_iccard), getClass());
                    try {
                        int transType = InnerProcessingCode.USING_STANDARD_PROCESSINGCODE;

                        SimpleTransferListener simpleTransferListener = new SimpleTransferListener(this, emvModule, transType);
                        controller = emvModule.getEmvTransController(simpleTransferListener);
                        if (controller != null) {
                            LogUtil.debug(getString(R.string.msg_iccard_start_emv), getClass());
                            //第五个参数设置为true,不会出现是否是电子钱包的弹窗 方法ecSwitch是否调用
                            controller.startEmv(ProcessingCode.GOODS_AND_SERVICE, transType, AppConfig.EMV.amt, new BigDecimal("0"), true, true);
                            AppConfig.EMV.consumeType = 1; // 消费报文区分
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.debug(getString(R.string.msg_iccard_emv_exception) + e, getClass());
                    }
                    break;
                case RFCARD:
                    tipHandler.sendMessage(new Message());
                    AppConfig.EMV.isECSwitch = 1;
                    int transType = InnerProcessingCode.USING_STANDARD_PROCESSINGCODE;
                    SimpleTransferListener simpleTransferListener = new SimpleTransferListener(this, emvModule, transType);
                    controller = emvModule.getEmvTransController(simpleTransferListener);
                    if (controller != null) {
                        LogUtil.debug(getString(R.string.msg_rfcard_strat_emv), getClass());
                        controller.startEmv(ProcessingCode.GOODS_AND_SERVICE, transType, AppConfig.EMV.amt, new BigDecimal("0"), true, false);
                        AppConfig.EMV.consumeType = 2;
                    }
                    break;
                default:
                    throw new DeviceRTException(GET_TRACKTEXT_FAILED, "Not support cardreader module:" + openedModuleTypes[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(getString(R.string.msg_reader_open_exception), getClass());
            LogUtil.debug(e.getMessage(), getClass());
        }
    }

    private class EventHolder<T extends DeviceEvent> implements DeviceEventListener<T> {

        private T event;

        private final Object syncObj = new Object();

        private boolean isClosed = false;

        public void onEvent(T event, Handler handler) {
            this.event = event;
            if (event instanceof PinInputEvent && ((PinInputEvent) event).isProcessing()) {
                return;
            } //Thread waiting until the password input is completed,
            synchronized (syncObj) {
                isClosed = true;
                syncObj.notify();
            }
        }
        public Handler getUIHandler() {
            return null;
        }

        void startWait() throws InterruptedException {
            synchronized (syncObj) {
                if (!isClosed)
                    syncObj.wait();
            }
        }

    }

    private <T extends AbstractProcessDeviceEvent> T preEvent(T event, int defaultExCode) {
        if (!event.isSuccess()) {
            if (event.isUserCanceled()) {
                return null;
            }
            if (event.getException() != null) {
                if (event.getException() instanceof RuntimeException) {// Throw  the Runtime Exception
                    throw (RuntimeException) event.getException();
                }
                throw new DeviceRTException(GET_TRACKTEXT_FAILED, "Open card reader meet error!", event.getException());
            }
            throw new DeviceRTException(ExCode.UNKNOWN, "Unknown exception!defaultExCode:" + defaultExCode);
        }
        return event;
    }


    //读取到rf卡和ic进行提示
    public Handler tipHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            tip_textview.setText(getString(R.string.tip_read_card));
            tip_textview.setVisibility(View.VISIBLE);
        }
    };

    /**
     * 开启pin键盘
     * @param accNo 账户
     */
    public void startOnlinePinInput(String accNo ,boolean isMag){
        currAccNO = accNo ;
        isMagCard = isMag;
        Intent intent = new Intent(this, KeyBoardNumberActivity.class);
        intent.putExtra("accNo", accNo);
        startActivityForResult(intent, 002);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 002) {
            Intent intent = new Intent();
            if (resultCode == RESULT_OK) {
                byte[] pin = data.getByteArrayExtra("pin");
                if (pin!=null && pin.length == 0) {
                    intent.putExtra("type",0);
                } else if(pin!=null && Arrays.equals(pin, new byte[8]) ){
                    intent.putExtra("type",1);
                   // int pinLen = data.getIntExtra("pinLength", 0);
                }else{
                    intent.putExtra("type",2);
                    intent.putExtra("password",pin);
                }
            } else if (resultCode == RESULT_CANCELED) {
                intent.putExtra("type",3);
            }else if(resultCode == -2){
                intent.putExtra("type",4);
            }
            //返回数据到orderactivity
            intent.putExtra("account",currAccNO);
            if (isMagCard)
                 setResult(SWIP_RESULT,intent);
            else
                 setResult(IC_RESULT,intent);

            finish();
        }
    }

}
