package com.newland.karaoke.mesdk.card;

import android.content.Context;
import android.util.Log;

import com.newland.karaoke.R;
import com.newland.karaoke.mesdk.AppConfig;
import com.newland.karaoke.mesdk.BaseModule;
import com.newland.karaoke.mesdk.device.SDKDevice;
import com.newland.karaoke.utils.LogUtil;
import com.newland.me.SupportMSDAlgorithm;
import com.newland.mtype.common.MESeriesConst;
import com.newland.mtype.module.common.pin.WorkingKey;
import com.newland.mtype.module.common.swiper.K21Swiper;
import com.newland.mtype.module.common.swiper.MSDAlgorithm;
import com.newland.mtype.module.common.swiper.SwipResult;
import com.newland.mtype.module.common.swiper.SwipResultType;
import com.newland.mtype.module.common.swiper.SwiperReadModel;

/**
 * Author by bxy, Date on 2019/5/11 0011.
 */
public class SwipModule extends BaseModule {


    public SwipModule(Context context) {
        super(context);
    }

    private int getWKIndex(){
        if(AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)){
            return AppConfig.Pin.MKSK_DES_INDEX_WK_TRACK;
        }else if(AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)){
            return AppConfig.Pin.MKSK_SM4_INDEX_WK_TRACK;
        }else if(AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)){
            return AppConfig.Pin.MKSK_AES_INDEX_WK_TRACK;
        }else if(AppConfig.DUKPT_DES.equals(AppConfig.KEY_SYS_ALG)){
            return AppConfig.Pin.DUKPT_DES_INDEX;
        }else if(AppConfig.DUKPT_AES.equals(AppConfig.KEY_SYS_ALG)){
            return AppConfig.Pin.DUKPT_AES_INDEX;
        }
        return -1;
    }

    //读取掩码方式的加密的磁道信息
    public void readEncryResultByMask(){
        try {
            K21Swiper swiper = SDKDevice.getInstance().getK21Swiper();
             LogUtil.debug(context.getString(R.string.msg_read_encrypted_trackdata), getClass());
            SwiperReadModel[] readModels = new SwiperReadModel[] { SwiperReadModel.READ_SECOND_TRACK, SwiperReadModel.READ_THIRD_TRACK};
            byte[] acctMask = new byte[] { 0x00, 0x00, 0x00, 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00, 0x00, 0x00 };
            MSDAlgorithm MSDAlgorithm = SupportMSDAlgorithm.getMSDAlgorithm(MESeriesConst.TrackEncryptAlgorithm.BY_UNIONPAY_MODEL);

            SwipResult swipRslt = swiper.readEncryptResult(readModels, new WorkingKey(getWKIndex()), acctMask, MSDAlgorithm);

            if (null != swipRslt && swipRslt.getRsltType() == SwipResultType.SUCCESS) {
                AppConfig.EMV.swipResult = swipRslt;
                byte[] secondTrack = swipRslt.getSecondTrackData();
                byte[] thirdTrack = swipRslt.getThirdTrackData();
                 LogUtil.debug(context.getString(R.string.msg_mask_card_No) + swipRslt.getAccount().getAcctNo(),  getClass());
                 LogUtil.debug(context.getString(R.string.msg_second_trackdata) + new String(secondTrack,"gbk"),  getClass());
                 LogUtil.debug(context.getString(R.string.msg_third_trackdata) +(thirdTrack==null?"null": new String(thirdTrack,"gbk")),  getClass());
            } else {
                 LogUtil.debug(context.getString(R.string.msg_empty_swipeResult_and_swipe_again),  getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
             LogUtil.debug(context.getString(R.string.msg_read_masked_trackdata_error)+e.getMessage(),  getClass());
             LogUtil.debug(context.getString(R.string.msg_check_mainkey_workingkey_AID_RID), getClass());
             LogUtil.debug(context.getString(R.string.msg_check_cardReader_and_swipe_again),  getClass());
        }
    }
    
    //读取加密的磁道信息
    public void readEncryResult(){
        try {
            K21Swiper swiper = SDKDevice.getInstance().getK21Swiper();
             LogUtil.debug(context.getString(R.string.msg_read_encrypted_trackdata_begin),  getClass());

            SwiperReadModel[] readModels = new SwiperReadModel[] { SwiperReadModel.READ_SECOND_TRACK, SwiperReadModel.READ_THIRD_TRACK };
            MSDAlgorithm MSDAlgorithm = SupportMSDAlgorithm.getMSDAlgorithm(MESeriesConst.TrackEncryptAlgorithm.BY_UNIONPAY_MODEL);

            SwipResult swipRslt = swiper.readEncryptResult(readModels, new WorkingKey(getWKIndex()), MSDAlgorithm);

            if (null != swipRslt && swipRslt.getRsltType() == SwipResultType.SUCCESS) {
                AppConfig.EMV.swipResult = swipRslt;
                byte[] secondTrack = swipRslt.getSecondTrackData();
                byte[] thirdTrack = swipRslt.getThirdTrackData();
                 LogUtil.debug(context.getString(R.string.msg_card_NO) + swipRslt.getAccount(),  getClass());
                 LogUtil.debug(context.getString(R.string.msg_second_trackdata) + new String(secondTrack,"gbk"),  getClass());
                 LogUtil.debug(context.getString(R.string.msg_third_trackdata) + (thirdTrack==null?"null": new String(thirdTrack,"gbk")),  getClass());
            } else {
                 LogUtil.debug(context.getString(R.string.msg_swipeResult_empty),  getClass());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
             LogUtil.debug(context.getString(R.string.msg_swipe_error) + e2,  getClass());
             LogUtil.debug(context.getString(R.string.msg_check_mainkey_workingkey_AID_RID),  getClass());
             LogUtil.debug(context.getString(R.string.msg_check_cardReader_and_swipe_again) ,  getClass());
        }
    }
    

    //普通卡的明文方式返回刷卡结果
    public void readPlainResult(){
        try {
            K21Swiper swiper = SDKDevice.getInstance().getK21Swiper();
             LogUtil.debug(context.getString(R.string.msg_return_swipeResult_plain),  getClass());

            SwiperReadModel[] readModels = new SwiperReadModel[] { SwiperReadModel.READ_SECOND_TRACK, SwiperReadModel.READ_THIRD_TRACK};

            SwipResult swipRslt = swiper.readPlainResult(readModels);

            if (null != swipRslt && swipRslt.getRsltType() == SwipResultType.SUCCESS) {
                AppConfig.EMV.swipResult = swipRslt;
                byte[] secondTrack = swipRslt.getSecondTrackData();
                byte[] thirdTrack = swipRslt.getThirdTrackData();
                 LogUtil.debug(context.getString(R.string.msg_card_NO) + swipRslt.getAccount(),  getClass());
                 LogUtil.debug(context.getString(R.string.msg_second_trackdata) + new String(secondTrack,"gbk"),  getClass());
                 LogUtil.debug(context.getString(R.string.msg_third_trackdata) +(thirdTrack==null?"null": new String(thirdTrack,"gbk")),  getClass());
                 LogUtil.debug("服务号" + swipRslt.getServiceCode(),  getClass());
            } else {
                 LogUtil.debug(context.getString(R.string.msg_swipeResult_empty),  getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
             LogUtil.debug(context.getString(R.string.msg_get_trackdata_plain_error) + e,  getClass());
             LogUtil.debug(context.getString(R.string.msg_check_mainkey_workingkey_AID_RID),  getClass());
             LogUtil.debug(context.getString(R.string.msg_check_cardReader_and_swipe_again),  getClass());
        }
    }
    
}
