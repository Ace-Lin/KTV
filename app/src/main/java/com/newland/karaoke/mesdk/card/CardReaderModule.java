package com.newland.karaoke.mesdk.card;

import android.content.Context;
import android.os.Handler;

import com.newland.karaoke.R;
import com.newland.karaoke.mesdk.BaseModule;
import com.newland.karaoke.mesdk.device.SDKDevice;
import com.newland.karaoke.utils.LogUtil;
import com.newland.mtype.ModuleType;
import com.newland.mtype.ProcessTimeoutException;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.cardreader.CommonCardType;
import com.newland.mtype.module.common.cardreader.K21CardReader;
import com.newland.mtype.module.common.cardreader.K21CardReaderEvent;
import com.newland.mtype.module.common.cardreader.OpenCardReaderResult;
import com.newland.mtype.module.common.cardreader.SearchCardRule;
import com.newland.mtype.module.common.rfcard.RFCardType;

import java.util.concurrent.TimeUnit;


public class CardReaderModule extends BaseModule {

    private long beginTime;

    public CardReaderModule(Context context) {
        super(context);
    }

    /**
     * 开卡监听器
     */
    private DeviceEventListener<K21CardReaderEvent> cardReaderEvent = new DeviceEventListener<K21CardReaderEvent>(){
        @Override
        public void onEvent(K21CardReaderEvent openCardReaderEvent, Handler handler) {
            OpenCardReaderResult cardResult = openCardReaderEvent.getOpenCardReaderResult();
            CommonCardType[] openedModuleTypes = null;
            if (openCardReaderEvent.isFailed()) {
                if (openCardReaderEvent.getException() instanceof ProcessTimeoutException) {
                    LogUtil.debug(context.getString(R.string.msg_timeout), getClass());
                }
                LogUtil.debug(context.getString(R.string.msg_reader_open_failed), getClass());
            }
            if (cardResult == null || (openedModuleTypes = cardResult.getResponseCardTypes()) == null || openedModuleTypes.length <= 0) {
                LogUtil.debug(context.getString(R.string.msg_card_info_null), getClass());
            } else if (openCardReaderEvent.isSuccess()) {
                String showMsg = "";
                switch (openCardReaderEvent.getOpenCardReaderResult().getResponseCardTypes()[0]) {
                    case MSCARD:
                        showMsg = context.getString(R.string.msg_cardreader_swiper);
                        break;
                    case ICCARD:
                        showMsg = context.getString(R.string.msg_cradreader_insert);
                        break;
                    case RFCARD:
                        RFCardType rfCardType = openCardReaderEvent.getOpenCardReaderResult().getResponseRFCardType();
                        if (null == rfCardType) {
                            showMsg = context.getString(R.string.msg_cardreader_rfcard);
                            break;
                        }
                        switch (rfCardType) {
                            case ACARD:
                            case BCARD:
                                showMsg = context.getString(R.string.msg_cardreader_rf_cpu);
                                break;
                            case M1CARD:
                                byte sak = openCardReaderEvent.getOpenCardReaderResult().getSAK();
                                if (sak == 0x08) {
                                    showMsg = context.getString(R.string.msg_cardreader_rf_s50);
                                } else if (sak == 0x18) {
                                    showMsg = context.getString(R.string.msg_cardreader_rf_s70);
                                } else if (sak == 0x28) {
                                    showMsg = context.getString(R.string.msg_cardreader_rf_s50_pro);
                                } else if (sak == 0x38) {
                                    showMsg = context.getString(R.string.msg_cardreader_rf_s70_pro);
                                } else {
                                    showMsg = "sak=" + sak;
                                    showMsg = showMsg + context.getString(R.string.msg_cardreader_undefind);
                                }
                                break;
                            default:
                                showMsg = context.getString(R.string.msg_cardreader_undefind_rf);
                                break;
                        }
                        break;
                    default:
                        break;
                }
                long endTime = System.currentTimeMillis();
                LogUtil.debug(showMsg, getClass());
                LogUtil.debug(context.getString(R.string.msg_time_consume) + (endTime - beginTime), getClass());
            } else if (openCardReaderEvent.isUserCanceled()) {
                LogUtil.debug(context.getString(R.string.msg_cancel_open_reader), getClass());
            }
        }
        @Override
        public Handler getUIHandler() {
            return null;
        }
    };



    /**
     * 开启读卡器
     */
    public void openCardReader(){
        try {
            K21CardReader cardReader = SDKDevice.getInstance().getCardReaderModuleType();
            LogUtil.debug(context.getString(R.string.msg_swipe_insert_rf_card), getClass());
            beginTime = System.currentTimeMillis();

            ModuleType[] openReaders = new ModuleType[]{ModuleType.COMMON_SWIPER, ModuleType.COMMON_ICCARDREADER, ModuleType.COMMON_RFCARDREADER };

            cardReader.openCardReader(openReaders, true, 60000, TimeUnit.MILLISECONDS, cardReaderEvent, SearchCardRule.RFCARD_FIRST);

        } catch (Exception e) {
            LogUtil.debug(context.getString(R.string.msg_reader_open_exception), getClass());
            LogUtil.debug(e.getMessage(), getClass());
        }
    }

    /**
     * 关闭读卡器
     */
    public void closeCardReader(){
        try {

            K21CardReader cardReader = SDKDevice.getInstance().getCardReaderModuleType();
            cardReader.cancelCardRead();

            LogUtil.debug(context.getString(R.string.msg_revocate_reader) + context.getString(R.string.msg_succ), getClass());
        } catch (Exception e) {
            LogUtil.debug(context.getString(R.string.msg_revocate_reader_exception) + e, getClass());
        }
    }

}
