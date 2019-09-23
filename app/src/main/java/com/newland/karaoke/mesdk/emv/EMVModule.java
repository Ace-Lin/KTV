package com.newland.karaoke.mesdk.emv;

import android.content.Context;
import android.os.Handler;

import com.newland.karaoke.R;
import com.newland.karaoke.mesdk.BaseModule;
import com.newland.karaoke.mesdk.device.SDKDevice;
import com.newland.karaoke.utils.LogUtil;
import com.newland.mtype.ModuleType;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.cardreader.K21CardReader;
import com.newland.mtype.module.common.cardreader.K21CardReaderEvent;
import com.newland.mtype.module.common.emv.AIDConfig;
import com.newland.mtype.module.common.emv.CAPublicKey;
import com.newland.mtype.module.common.emv.EcTransLog;
import com.newland.mtype.module.common.emv.EmvCardInfo;
import com.newland.mtype.module.common.emv.EmvModule;
import com.newland.mtype.module.common.emv.PbocTransLog;
import com.newland.mtype.util.Dump;
import com.newland.mtype.util.ISOUtils;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Author by bxy, Date on 2019/5/11 0011.
 */
public class EMVModule extends BaseModule {

    private EmvModule emvModule;
    private K21CardReader cardReader;

    public EMVModule(Context context) {
        super(context);
        initData();
    }




    public void initData() {
        emvModule = SDKDevice.getInstance().getEmvModuleType();
        emvModule.initEmvModule(context);
        cardReader = SDKDevice.getInstance().getCardReaderModuleType();
    }

    public void addAid() {
        try {
            boolean addResult = false;
            String aid1 = "9F0608A000000333010101DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000100000DF2106000000100000";
            addResult = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(aid1));
            LogUtil.debug("Add AID[A000000333010101]:" + (addResult == true ? context.getString(R.string.msg_common_succ) : context.getString(R.string.msg_common_failed)), getClass());
            String aid2 = "9F0608A000000333010102DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000100000DF2106000000100000";
            addResult = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(aid2));
            LogUtil.debug("Add AID[A000000333010102]:" + (addResult == true ? context.getString(R.string.msg_common_succ) : context.getString(R.string.msg_common_failed)), getClass());
            String aid3 = "9F0608A000000333010103DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000100000DF2106000000100000";
            addResult = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(aid3));
            LogUtil.debug("Add AID[A000000333010103]:" + (addResult == true ? context.getString(R.string.msg_common_succ) : context.getString(R.string.msg_common_failed)), getClass());
            String aid4 = "9F0608A000000333010106DF0101009F08020020DF1105D84000A800DF1205D84004F800DF130500100000009F1B0400000000DF150400000000DF160199DF170199DF14039F3704DF1801019F7B06000000100000DF1906000000100000DF2006000000100000DF2106000000100000";
            addResult = emvModule.addAIDWithDataSource(ISOUtils.hex2byte(aid4));
            LogUtil.debug("Add AID[A000000333010106]:" + (addResult == true ? context.getString(R.string.msg_common_succ) : context.getString(R.string.msg_common_failed)), getClass());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_add_aid_e) + e, getClass());
        }
    }


    private void delAid() {
        try {
            LogUtil.debug(context.getString(R.string.msg_start_del_aid), getClass());
            boolean delAID = false;
            byte[] aid = new byte[]{(byte) 0xA0, 0x00, 0x00, 0x03, 0x33, 0x01, 0x01, 0x02};
            emvModule.initEmvModule(context);
            delAID = emvModule.deleteAID(aid);
            if (delAID) {
                LogUtil.debug(context.getString(R.string.msg_del_aid) + context.getString(R.string.msg_common_succ), getClass());
            } else {
                LogUtil.debug(context.getString(R.string.msg_del_aid) + context.getString(R.string.msg_common_failed), getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_del_aid_ex) + e.getMessage(), getClass());
        }
    }


    private void getAllAid() {
        try {
            LogUtil.debug(context.getString(R.string.msg_get_all_aid_list), getClass());
            List<AIDConfig> listAIDConfig;
            emvModule.initEmvModule(context);
            listAIDConfig = emvModule.fetchAllAID();
            if (listAIDConfig != null) {
                for (Iterator i = listAIDConfig.iterator(); i.hasNext(); ) {
                    AIDConfig oneAidConfig = (AIDConfig) i.next();
                    int len = oneAidConfig.getAidLength();
                    byte[] aid = new byte[len];
                    System.arraycopy(oneAidConfig.getAid(), 0, aid, 0, len);
                    String aid1 = Dump.getHexDump(aid);
                    oneAidConfig.getNciccTransLimit();
                    LogUtil.debug(context.getString(R.string.msg_get_aid) + aid1, getClass());
                    LogUtil.debug(context.getString(R.string.msg_get_aid1) + ISOUtils.hexString(oneAidConfig.getEcTransLimit()), getClass());
                    LogUtil.debug(context.getString(R.string.msg_get_aid2) + ISOUtils.hexString(oneAidConfig.getNciccOffLineFloorLimit()), getClass());
                    LogUtil.debug(context.getString(R.string.msg_get_aid3) + ISOUtils.hexString(oneAidConfig.getNciccCVMLimit()), getClass());
                }
                if (listAIDConfig.size() == 0) {
                    LogUtil.debug(context.getString(R.string.msg_no_aid), getClass());
                }
            } else {
                LogUtil.debug(context.getString(R.string.msg_get_all_aid_list_failed), getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_get_all_aid_list_exception) + e.getMessage(), getClass());
        }

    }


    private void cleanAllAid() {
        try {
            LogUtil.debug(context.getString(R.string.msg_clean_all_aid), getClass());
            boolean clearAllAID = false;
            emvModule.initEmvModule(context);
            clearAllAID = emvModule.clearAllAID();
            LogUtil.debug(context.getString(R.string.msg_clean_aid_result) + clearAllAID, getClass());
        } catch (Exception e) {
            LogUtil.debug(context.getString(R.string.msg_clean_aid) + context.getString(R.string.common_exception) + e.getMessage(), getClass());
        }
    }


    public void addCapk() {
        try {
            boolean addResult = false;
            String capk = "9F0605A0000003339F220101DF05083230303931323331DF060101DF070101DF028180BBE9066D2517511D239C7BFA77884144AE20C7372F515147E8CE6537C54C0A6A4D45F8CA4D290870CDA59F1344EF71D17D3F35D92F3F06778D0D511EC2A7DC4FFEADF4FB1253CE37A7B2B5A3741227BEF72524DA7A2B7B1CB426BEE27BC513B0CB11AB99BC1BC61DF5AC6CC4D831D0848788CD74F6D543AD37C5A2B4C5D5A93BDF040103DF0314E881E390675D44C2DD81234DCE29C3F5AB2297A0";
            addResult = emvModule.addCAPublicKeyWithDataSource(ISOUtils.hex2byte(capk));
            LogUtil.debug("Add public key,[rid:A000000333; index:01]:" + (addResult == true ? context.getString(R.string.msg_common_succ) : context.getString(R.string.msg_common_failed)), getClass());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_add_pk_result) + context.getString(R.string.common_exception) + e.getMessage(), getClass());
        }
    }


    private void delCapk() {
        try {
            LogUtil.debug(context.getString(R.string.msg_start_del_rids_pk), getClass());
            boolean delPKByRid = false;
            byte[] rid = new byte[]{(byte) 0xA0, 0x00, 0x00, 0x03, 0x33};
            int keyIndex = 01;
            emvModule.initEmvModule(context);
            delPKByRid = emvModule.deleteCAPublicKey(rid, keyIndex);
            if (delPKByRid) {
                LogUtil.debug(context.getString(R.string.msg_del_rids_pk) + context.getString(R.string.msg_common_succ), getClass());
            } else {
                LogUtil.debug(context.getString(R.string.msg_del_rids_pk) + context.getString(R.string.msg_common_failed), getClass());

            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_del_rids_pk) + context.getString(R.string.common_exception) + e, getClass());
        }
    }


    private void getAllCapk() {
        try {
            LogUtil.debug(context.getString(R.string.msg_get_pk_list), getClass());
            List<CAPublicKey> listCAPublicKey;
            emvModule.initEmvModule(context);
            listCAPublicKey = emvModule.fetchAllCAPublicKey();
            if (listCAPublicKey != null) {
                for (Iterator i = listCAPublicKey.iterator(); i.hasNext(); ) {
                    CAPublicKey caPublicKey = (CAPublicKey) i.next();
                    int index = caPublicKey.getIndex();
                    byte[] rid = caPublicKey.getRid();
                    String rid1 = Dump.getHexDump(rid);
                    LogUtil.debug(context.getString(R.string.msg_get_pk_index) + index, getClass());
                    LogUtil.debug(context.getString(R.string.msg_get_pk_rid) + rid1, getClass());

                }
                if (listCAPublicKey.size() == 0) {
                    LogUtil.debug(context.getString(R.string.msg_no_pk), getClass());
                }
            } else {
                LogUtil.debug(context.getString(R.string.msg_get_pk_list_failed), getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_get_pk_list_exception) + e.getMessage(), getClass());
        }
    }


    private void cleanAllCapk() {
        try {
            LogUtil.debug(context.getString(R.string.msg_clean_all_pk_start), getClass());
            boolean clearAllCAPublicKey = false;
            emvModule.initEmvModule(context);
            clearAllCAPublicKey = emvModule.clearAllCAPublicKey();
            LogUtil.debug(context.getString(R.string.msg_clean_all_pk_result) + clearAllCAPublicKey, getClass());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_clean_all_pk) + context.getString(R.string.common_exception) + e, getClass());
        }
    }

    private DeviceEventListener<K21CardReaderEvent> amountInfoCardEvent = new DeviceEventListener<K21CardReaderEvent>() {
        @Override
        public void onEvent(K21CardReaderEvent openCardReaderEvent, Handler handler) {
            try {
                if (openCardReaderEvent.isSuccess()) {
                    LogUtil.debug(context.getString(R.string.msg_open_cardreader_succ) + "\r\n", getClass());
                    LogUtil.debug(context.getString(R.string.msg_start_get_pboc_log) + "\r\n", getClass());
                    emvModule.initEmvModule(context);
                    EmvCardInfo emvCardInfo = emvModule.getAccountInfo();
                    if (emvCardInfo != null) {
                        String cardMo = emvCardInfo.getCardNo();
                        String cardSecuenceNumber = emvCardInfo.getCard_sequence_number();
                        String cardExpirationData = emvCardInfo.getCardExpirationDate();
                        String balance = emvCardInfo.getCardBalance();
                        LogUtil.debug(context.getString(R.string.msg_get_account_succ), getClass());
                        LogUtil.debug(context.getString(R.string.msg_ecash_balance) + balance, getClass());
                        LogUtil.debug(context.getString(R.string.msg_card_no) + cardMo, getClass());
                        LogUtil.debug(context.getString(R.string.msg_card_sn) + cardSecuenceNumber, getClass());
                        LogUtil.debug(context.getString(R.string.msg_card_vaild_date) + cardExpirationData, getClass());
                        LogUtil.debug(context.getString(R.string.msg_service_code) + emvCardInfo.getServiceCode(), getClass());
                        LogUtil.debug(context.getString(R.string.msg_equivalent_track2) + (null == emvCardInfo.getTrack2() ? null : emvCardInfo.getTrack2()), getClass());
                    } else {
                        LogUtil.debug(context.getString(R.string.msg_get_account_null), getClass());
                    }
                } else if (openCardReaderEvent.isUserCanceled()) {
                    LogUtil.debug(context.getString(R.string.msg_cancel_open_reader) + "\r\n", getClass());
                } else if (openCardReaderEvent.isFailed()) {
                    LogUtil.debug(context.getString(R.string.msg_reader_open_failed) + "\r\n", getClass());
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.debug(context.getString(R.string.msg_get_pboc_log_ex) + e + context.getString(R.string.msg_check_insert_rf), getClass());
            }
        }

        @Override
        public Handler getUIHandler() {
            return null;
        }
    };


    private void getamountinfo() {
        try {
            LogUtil.debug(context.getString(R.string.msg_pl_insert_or_rf) + "\r\n", getClass());
            emvModule.initEmvModule(context);
            ModuleType[] moduleTypes = new ModuleType[]{ModuleType.COMMON_SWIPER, ModuleType.COMMON_ICCARDREADER, ModuleType.COMMON_RFCARDREADER};
            cardReader.openCardReader(context.getString(R.string.msg_pl_insert_or_rf), moduleTypes, false, true, 60, TimeUnit.SECONDS, amountInfoCardEvent);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_get_pboc_log_ex) + e + context.getString(R.string.msg_check_insert_rf), getClass());
        }
    }

    private DeviceEventListener<K21CardReaderEvent> pbocLogCardEvent = new DeviceEventListener<K21CardReaderEvent>() {
        @Override
        public void onEvent(K21CardReaderEvent openCardReaderEvent, Handler handler) {
            try {
                if (openCardReaderEvent.isSuccess()) {
                    LogUtil.debug(context.getString(R.string.msg_open_cardreader_succ) + "\r\n", getClass());
                    LogUtil.debug(context.getString(R.string.msg_start_get_pboc_log) + "\r\n", getClass());
                    List<PbocTransLog> pbocTransLog = emvModule.fetchPbocLog();
                    if (pbocTransLog != null) {
                        LogUtil.debug(context.getString(R.string.msg_get_all_pboc_log) + context.getString(R.string.msg_common_succ), getClass());
                        for (PbocTransLog pbocLog : pbocTransLog) {
                            byte[] otherAmout = pbocLog.getOtherAmount();
                            byte[] merchantName = pbocLog.getMerchantName();
                            byte[] tradeAmount = pbocLog.getTradeAmount();
                            byte[] tradeData = pbocLog.getTradeDate();
                            byte[] tradeTime = pbocLog.getTradeTime();
                            byte[] contryCode = pbocLog.getCountryCode();
                            byte[] currencyCode = pbocLog.getCurrencyCode();
                            byte[] tradeType = pbocLog.getTradeType();
                            byte[] tradeCount = pbocLog.getTransCount();

                            String otherAmountString = Dump.getHexDump(otherAmout);
                            String merchantName1 = Dump.getHexDump(merchantName);
                            String tradeAmount1 = Dump.getHexDump(tradeAmount);
                            String tradeData1 = Dump.getHexDump(tradeData);
                            String tradeTimeString = Dump.getHexDump(tradeTime);
                            String contryCodeString = Dump.getHexDump(contryCode);
                            String currencyCodeString = Dump.getHexDump(currencyCode);
                            String tradeTypeString = Dump.getHexDump(tradeType);
                            String tradeCountString = Dump.getHexDump(tradeCount);
                            LogUtil.debug(context.getString(R.string.msg_get_pboc_date) + tradeData1, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_pboc_tradetime) + tradeTimeString, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_pboc_trans_money) + tradeAmount1, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_pboc_otheramount) + otherAmountString, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_pboc_contrycode) + contryCodeString, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_pboc_currencycode) + currencyCodeString, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_pboc_store_name) + merchantName1, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_pboc_tradetype) + tradeTypeString, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_pboc_tradecont) + tradeCountString, getClass());
                        }

                    } else {
                        LogUtil.debug(context.getString(R.string.msg_get_all_pboc_log) + context.getString(R.string.msg_common_failed) + context.getString(R.string.msg_check_in_emv), getClass());
                    }

                } else if (openCardReaderEvent.isUserCanceled()) {
                    LogUtil.debug(context.getString(R.string.msg_cancel_open_reader) + "\r\n", getClass());
                } else if (openCardReaderEvent.isFailed()) {
                    LogUtil.debug(context.getString(R.string.msg_reader_open_failed) + "\r\n", getClass());
                }
            } catch (Exception e) {
                LogUtil.debug(context.getString(R.string.msg_get_pboc_log_ex) + e + context.getString(R.string.msg_check_insert_rf), getClass());
            }
        }

        @Override
        public Handler getUIHandler() {
            return null;
        }
    };

    private void getpboclog() {
        try {
            LogUtil.debug(context.getString(R.string.msg_pl_insert_or_rf) + "\r\n", getClass());
            emvModule.initEmvModule(context);
            ModuleType[] moduleTypes = new ModuleType[]{ModuleType.COMMON_SWIPER, ModuleType.COMMON_ICCARDREADER, ModuleType.COMMON_RFCARDREADER};
            cardReader.openCardReader(context.getString(R.string.msg_pl_insert_or_rf), moduleTypes, false, true, 60, TimeUnit.SECONDS, pbocLogCardEvent);
        } catch (Exception e) {
            LogUtil.debug(context.getString(R.string.msg_get_pboc_log_ex) + e + context.getString(R.string.msg_check_insert_rf), getClass());
        }
    }

    private DeviceEventListener<K21CardReaderEvent> loadlogCardEvent = new DeviceEventListener<K21CardReaderEvent>() {
        @Override
        public void onEvent(K21CardReaderEvent openCardReaderEvent, Handler handler) {
            try {
                if (openCardReaderEvent.isSuccess()) {
                    LogUtil.debug(context.getString(R.string.msg_open_cardreader_succ) + "\r\n", getClass());
                    LogUtil.debug(context.getString(R.string.msg_start_get_pboc_log) + "\r\n", getClass());
                    List<EcTransLog> ecTransLog = emvModule.fetchEcLog();
                    if (ecTransLog != null) {
                        LogUtil.debug(context.getString(R.string.msg_get_all_load_log) + context.getString(R.string.msg_common_succ), getClass());
                        for (EcTransLog ecLog : ecTransLog) {
                            byte[] blanceOld = ecLog.getBlanceOld();
                            byte[] merchantName = ecLog.getMerchantName();
                            byte[] blanceNew = ecLog.getBlanceNew();
                            byte[] tradeData = ecLog.getTradeDate();
                            byte[] tradeTime = ecLog.getTradeTime();
                            byte[] contryCode = ecLog.getCountryCode();
                            byte[] tradeCount = ecLog.getTransCount();

                            String blanceOldString = Dump.getHexDump(blanceOld);
                            String merchantName1 = Dump.getHexDump(merchantName);
                            String blanceNew1 = Dump.getHexDump(blanceNew);
                            String tradeData1 = Dump.getHexDump(tradeData);
                            String tradeTimeString = Dump.getHexDump(tradeTime);
                            String contryCodeString = Dump.getHexDump(contryCode);
                            String tradeCountString = Dump.getHexDump(tradeCount);

                            LogUtil.debug(context.getString(R.string.msg_get_load_trans_date) + tradeData1, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_load_trans_tradetime) + tradeTimeString, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_load_trans_balance_before) + blanceOldString, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_load_trans_trade_balance_after) + blanceNew1, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_load_trans_contrycode) + contryCodeString, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_load_trans_store_name) + merchantName1, getClass());
                            LogUtil.debug(context.getString(R.string.msg_get_load_trans_tradecount) + tradeCountString, getClass());
                        }
                    } else {
                        LogUtil.debug(context.getString(R.string.msg_get_all_load_trans_log) + context.getString(R.string.msg_common_failed) + context.getString(R.string.msg_check_in_emv), getClass());
                    }
                } else if (openCardReaderEvent.isUserCanceled()) {
                    LogUtil.debug(context.getString(R.string.msg_cancel_open_reader) + "\r\n", getClass());
                } else if (openCardReaderEvent.isFailed()) {
                    LogUtil.debug(context.getString(R.string.msg_reader_open_failed) + "\r\n", getClass());
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.debug(context.getString(R.string.msg_get_all_load_trans_log_ex) + e + context.getString(R.string.msg_check_insert_rf), getClass());
            }
        }

        @Override
        public Handler getUIHandler() {
            return null;
        }
    };


    private void getloadlog() {
        try {
            LogUtil.debug(context.getString(R.string.msg_pl_insert_or_rf) + "\r\n", getClass());
            emvModule.initEmvModule(context);
            ModuleType[] moduleTypes = new ModuleType[]{ModuleType.COMMON_SWIPER, ModuleType.COMMON_ICCARDREADER, ModuleType.COMMON_RFCARDREADER};
            cardReader.openCardReader(context.getString(R.string.msg_pl_insert_or_rf), moduleTypes, false, true, 60, TimeUnit.SECONDS, loadlogCardEvent);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_get_all_load_trans_log_ex) + e + context.getString(R.string.msg_check_insert_rf), getClass());
        }
    }
}
