package com.newland.karaoke.mesdk.pin;

import android.content.Context;

import com.newland.karaoke.R;
import com.newland.karaoke.mesdk.AppConfig;
import com.newland.karaoke.mesdk.BaseModule;
import com.newland.karaoke.mesdk.device.SDKDevice;
import com.newland.karaoke.utils.LogUtil;
import com.newland.mtype.DeviceInvokeException;
import com.newland.mtype.module.common.pin.CheckKeyResult;
import com.newland.mtype.module.common.pin.EncryptAlgorithm;
import com.newland.mtype.module.common.pin.K21Pininput;
import com.newland.mtype.module.common.pin.KekUsingType;
import com.newland.mtype.module.common.pin.KeyManageType;
import com.newland.mtype.module.common.pin.KeyType;
import com.newland.mtype.module.common.pin.MacAlgorithm;
import com.newland.mtype.module.common.pin.TusnData;
import com.newland.mtype.module.common.pin.WorkingKey;
import com.newland.mtype.module.common.pin.WorkingKeyType;
import com.newland.mtype.util.Dump;
import com.newland.mtype.util.ISOUtils;
import com.newland.ndk.NdkApiManager;
import com.newland.ndk.SecN;
import com.newland.ndk.h.ST_SEC_KCV_INFO;
import com.newland.ndk.h.ST_SEC_KEY_INFO;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Author by bxy, Date on 2019/5/10 0010.
 */
public class PinMKSKModule extends BaseModule {

    private K21Pininput pinInput;
    private KeyManageType keySysAlg;
    private int indexMK, indexWKPin, indexWKTrack, indexWKMac;

    public PinMKSKModule(Context context) {
        super(context);
    }




    public void initData() {
        pinInput = SDKDevice.getInstance().getK21Pininput();
        if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
            keySysAlg = KeyManageType.MKSK;
            indexWKPin = AppConfig.Pin.MKSK_DES_INDEX_WK_PIN;
            indexWKTrack = AppConfig.Pin.MKSK_DES_INDEX_WK_TRACK;
            indexWKMac = AppConfig.Pin.MKSK_DES_INDEX_WK_MAC;
        } else if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
            keySysAlg = KeyManageType.SM4;
            indexWKPin = AppConfig.Pin.MKSK_SM4_INDEX_WK_PIN;
            indexWKTrack = AppConfig.Pin.MKSK_SM4_INDEX_WK_TRACK;
            indexWKMac = AppConfig.Pin.MKSK_SM4_INDEX_WK_MAC;
        } else if (AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {
            keySysAlg = KeyManageType.MKSK_AES;
            indexWKPin = AppConfig.Pin.MKSK_AES_INDEX_WK_PIN;
            indexWKTrack = AppConfig.Pin.MKSK_AES_INDEX_WK_TRACK;
            indexWKMac = AppConfig.Pin.MKSK_AES_INDEX_WK_MAC;
        }
    }


    public void loadMainKey() {
        try {
            if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
                final String mkDES = "253C9D9D7C2FBBFA253C9D9D7C2FBBFA";//PLAIN KEY：11111111111111111111111111111111
                byte[] mainKey = pinInput.loadMainKey(KekUsingType.ENCRYPT_TMK, AppConfig.Pin.MKSK_DES_INDEX_MK, ISOUtils.hex2byte(mkDES), ISOUtils.hex2byte("82E13665"), -1);

                if (mainKey != null && Arrays.equals(mainKey, new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00})) {
                    LogUtil.debug(context.getString(R.string.msg_load_mk_succ), getClass());
                } else {
                    LogUtil.debug(context.getString(R.string.msg_load_mk_failed), getClass());
                }
            }

            if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
                String mkSM4 = "0812C8D3ED259C7EB0167D647748FF35";//PLAIN KEY:11111111111111111111111111111111
                byte[] mainKey = pinInput.loadMainKey(KekUsingType.ENCRYPT_TMK_SM4, AppConfig.Pin.MKSK_SM4_INDEX_MK, ISOUtils.hex2byte(mkSM4), ISOUtils.hex2byte("F8D06870B9EA"), -1);

                if (mainKey != null && Arrays.equals(mainKey, new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00})) {
                    LogUtil.debug(context.getString(R.string.msg_load_mk_succ), getClass());
                } else {
                    LogUtil.debug(context.getString(R.string.msg_load_mk_failed), getClass());
                }

            }

            if (AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {
                String mkAES = "053A5184A74CC33188E5FDC351D0CF06";//PLAIN KEY:11111111111111111111111111111111
                byte[] mainKey = pinInput.loadMainKey(KekUsingType.ENCRYPT_TMK_AES, AppConfig.Pin.MKSK_AES_INDEX_MK, ISOUtils.hex2byte(mkAES), ISOUtils.hex2byte("EE23D81C"), -1, null);

                if (mainKey != null && Arrays.equals(mainKey, new byte[]{0x00, 0x00, 0x00, 0x00})) {
                    LogUtil.debug(context.getString(R.string.msg_load_mk_succ), getClass());
                } else {
                    LogUtil.debug(context.getString(R.string.msg_load_mk_failed), getClass());
                }
            }

        } catch (Exception e) {
            LogUtil.debug(context.getString(R.string.msg_load_mk_failed) + e.getMessage(), getClass());
        }
    }

    private void loadWorkingKeyPin() {
        byte[] kcv;
        if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
            byte[] pinKeyDES = ISOUtils.hex2byte("D2CEEE5C1D3AFBAF00374E0CC1526C86");//PLAIN KEY：2A288F61348FEE93FE9C0FC714BCDD73
            byte[] checkKcv = ISOUtils.hex2byte("58A2BBF9");
            kcv = pinInput.loadWorkingKey(WorkingKeyType.PININPUT, AppConfig.Pin.MKSK_DES_INDEX_MK, AppConfig.Pin.MKSK_DES_INDEX_WK_PIN, pinKeyDES, checkKcv);

            if (kcv != null && Arrays.equals(kcv, new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00})) {
                LogUtil.debug(context.getString(R.string.msg_load_pin_wk_succ), getClass());
            } else {
                LogUtil.debug(context.getString(R.string.msg_load_pin_wk_failed), getClass());
            }
        }

        if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
            byte[] pinKeySM4 = ISOUtils.hex2byte("3526A987BBC659EC3219956DC1FF38B0");//PLAIN KEY:33333333333333333333333333333333
            byte[] checkKcv = ISOUtils.hex2byte("A43FDDA62EA0");
            kcv = pinInput.loadWorkingKey(WorkingKeyType.PININPUT_SM4, AppConfig.Pin.MKSK_SM4_INDEX_MK, AppConfig.Pin.MKSK_SM4_INDEX_WK_PIN, pinKeySM4, checkKcv);

            if (kcv != null && Arrays.equals(kcv, new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00})) {
                LogUtil.debug(context.getString(R.string.msg_load_pin_wk_succ), getClass());
            } else {
                LogUtil.debug(context.getString(R.string.msg_load_pin_wk_failed), getClass());
            }
        }

        if (AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {
            byte[] pinKeyAES = ISOUtils.hex2byte("34A9575EEFE69DE078B4E29A24D04CD7");//PLAIN KEY:66666666666666666666666666666666
            byte[] checkKcv = ISOUtils.hex2byte("2DB6A815");
            kcv = pinInput.loadWorkingKey(WorkingKeyType.PININPUT_AES, AppConfig.Pin.MKSK_AES_INDEX_MK, AppConfig.Pin.MKSK_AES_INDEX_WK_PIN, pinKeyAES, checkKcv);
            if (kcv != null && Arrays.equals(kcv, new byte[]{0x00, 0x00, 0x00, 0x00})) {
                LogUtil.debug(context.getString(R.string.msg_load_pin_wk_succ), getClass());
            } else {
                LogUtil.debug(context.getString(R.string.msg_load_pin_wk_failed), getClass());
            }
        }
    }

    private void loadWorkingKeyTrack() {
        byte[] kcv;
        if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
            byte[] trackKeyDES = ISOUtils.hex2byte("DBFE96D0A5F09D24DBFE96D0A5F09D24");//PLAIN KEY:4DE5E8B8A9DCDDF94DE5E8B8A9DCDDF9
            byte[] checkKcv = ISOUtils.hex2byte("5B4C8BED");
            kcv = pinInput.loadWorkingKey(WorkingKeyType.DATAENCRYPT, AppConfig.Pin.MKSK_DES_INDEX_MK, AppConfig.Pin.MKSK_DES_INDEX_WK_TRACK, trackKeyDES, checkKcv);
            if (kcv != null && Arrays.equals(kcv, new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00})) {
                LogUtil.debug(context.getString(R.string.msg_load_track_wk_succ), getClass());
            } else {
                LogUtil.debug(context.getString(R.string.msg_load_track_wk_failed), getClass());
            }
        }

        if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
            byte[] trackKeySM4 = ISOUtils.hex2byte("585B9D3F745C8EA95400BD2A3EBDFABF");//PLAIN KEY:22222222222222222222222222222222
            byte[] checkKcv = ISOUtils.hex2byte("AD9358BA9110");
            kcv = pinInput.loadWorkingKey(WorkingKeyType.DATAENCRYPT_SM4, AppConfig.Pin.MKSK_SM4_INDEX_MK, AppConfig.Pin.MKSK_SM4_INDEX_WK_TRACK, trackKeySM4, checkKcv);
            if (kcv != null && Arrays.equals(kcv, new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00})) {
                LogUtil.debug(context.getString(R.string.msg_load_track_wk_succ), getClass());
            } else {
                LogUtil.debug(context.getString(R.string.msg_load_track_wk_failed), getClass());
            }
        }

        if (AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {
            byte[] trackKeyAES = ISOUtils.hex2byte("06C8D45B628EAEA8A30C75579F321211");//PLAIN KEY:88888888888888888888888888888888
            byte[] checkKcv = ISOUtils.hex2byte("8B4217FE");
            kcv = pinInput.loadWorkingKey(WorkingKeyType.DATAENCRYPT_AES, AppConfig.Pin.MKSK_AES_INDEX_MK, AppConfig.Pin.MKSK_AES_INDEX_WK_TRACK, trackKeyAES, checkKcv);
            if (kcv != null && Arrays.equals(kcv, new byte[]{0x00, 0x00, 0x00, 0x00})) {
                LogUtil.debug(context.getString(R.string.msg_load_track_wk_succ), getClass());
            } else {
                LogUtil.debug(context.getString(R.string.msg_load_track_wk_failed), getClass());
            }
        }
    }

    private void loadWorkingKeyMac() {
        byte[] kcv;
        if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
            byte[] macKeyDES = ISOUtils.hex2byte("A30FE2C1D07BCC11A30FE2C1D07BCC11");//PLAIN KEY:66666666666666666666666666666666
            byte[] checkKcv = ISOUtils.hex2byte("B0B563C2");

            kcv = pinInput.loadWorkingKey(WorkingKeyType.MAC, AppConfig.Pin.MKSK_DES_INDEX_MK, AppConfig.Pin.MKSK_AES_INDEX_WK_MAC, macKeyDES, checkKcv);

            if (kcv != null && Arrays.equals(kcv, new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00})) {
                LogUtil.debug(context.getString(R.string.msg_load_mac_wk_succ), getClass());
            } else {
                LogUtil.debug(context.getString(R.string.msg_load_mac_wk_failed), getClass());
            }
        }

        if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
            byte[] macKeySM4 = ISOUtils.hex2byte("E97748E56A3D1F883832852C305242E8");//PLAIN KEY:17171717171717171717171717171717
            byte[] checkKcv = ISOUtils.hex2byte("6642C0C20B6C");
            kcv = pinInput.loadWorkingKey(WorkingKeyType.MAC_SM4, AppConfig.Pin.MKSK_AES_INDEX_MK, AppConfig.Pin.MKSK_AES_INDEX_WK_MAC, macKeySM4, checkKcv);

            if (kcv != null && Arrays.equals(kcv, new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00})) {
                LogUtil.debug(context.getString(R.string.msg_load_mac_wk_succ), getClass());
            } else {
                LogUtil.debug(context.getString(R.string.msg_load_mac_wk_failed), getClass());
            }
        }

        if (AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {
            byte[] macKeyAES = ISOUtils.hex2byte("DC44B424BCB85288CE3BE42430864E8B");//PLAIN KEY:77777777777777777777777777777777
            byte[] checkKcv = ISOUtils.hex2byte("2C3F18B2");
            kcv = pinInput.loadWorkingKey(WorkingKeyType.MAC_AES, AppConfig.Pin.MKSK_AES_INDEX_MK, AppConfig.Pin.MKSK_AES_INDEX_WK_TRACK, macKeyAES, checkKcv);
            if (kcv != null && Arrays.equals(kcv, new byte[]{0x00, 0x00, 0x00, 0x00})) {
                LogUtil.debug(context.getString(R.string.msg_load_mac_wk_succ), getClass());
            } else {
                LogUtil.debug(context.getString(R.string.msg_load_mac_wk_failed), getClass());
            }
        }
    }

    /**
     * 三种工作秘钥都加载
     */
    public void loadWorkingKey() {
        loadWorkingKeyPin();
        loadWorkingKeyTrack();
        loadWorkingKeyMac();
    }



    private void canleInputPin() {
        try {
            pinInput.cancelPinInput();
            LogUtil.debug(context.getString(R.string.msg_revocate_last_pwd_succ), getClass());
        } catch (Exception e) {
            LogUtil.debug(context.getString(R.string.msg_revocate_last_pwd_ex) + e, getClass());
        }
    }


    private void calMac(String data) {

        try {
            byte[] input = data.getBytes("GBK");
            MacAlgorithm macAlgorithm = MacAlgorithm.MAC_ECB;
            byte[] ouput = pinInput.calcMac(macAlgorithm, KeyManageType.MKSK, new WorkingKey(indexWKMac), input).getMac();
            LogUtil.debug(context.getString(R.string.msg_enter_value) + data, getClass());
            LogUtil.debug(context.getString(R.string.msg_mac_algorithm) + macAlgorithm, getClass());
            LogUtil.debug(context.getString(R.string.msg_mac_cal_result) + Dump.getHexDump(ouput), getClass());
        } catch (DeviceInvokeException e) {
            LogUtil.debug(context.getString(R.string.msg_enter_value_not_null) + e.getMessage(), getClass());
        } catch (UnsupportedEncodingException e) {
            LogUtil.debug(context.getString(R.string.msg_enter_value_error), getClass());
        } catch (Exception e) {
            LogUtil.debug(e.getMessage(), getClass());
        }

    }


    private void encryption(String data) {
        byte[] input = ISOUtils.hex2byte(data);
        byte[]  cbciv = new byte[]{0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
        EncryptAlgorithm.KeyMode keyMode = EncryptAlgorithm.KeyMode.CBC;
        EncryptAlgorithm.ManufacturerAlgorithm algorithm = EncryptAlgorithm.ManufacturerAlgorithm.STANDARD;
        try {

            byte[] result = pinInput.encrypt(new EncryptAlgorithm(keyMode, algorithm), new WorkingKey(indexWKTrack), input, cbciv);

            LogUtil.debug(context.getString(R.string.msg_encrypt_succ), getClass());
            LogUtil.debug(context.getString(R.string.msg_encrypt_data) + data, getClass());
            LogUtil.debug(context.getString(R.string.msg_encrypt_key) + AppConfig.KEY_SYS_ALG, getClass());
            LogUtil.debug(context.getString(R.string.msg_encrypt_mode) + algorithm + "-" + keyMode, getClass());
            LogUtil.debug(context.getString(R.string.msg_encrypt_result) + ISOUtils.hexString(result), getClass());
            AppConfig.Pin.encryptResult = result;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_encrypt_ex) + e.getMessage(), getClass());
            LogUtil.debug(context.getString(R.string.msg_encrypt_data) + data, getClass());
            LogUtil.debug(context.getString(R.string.msg_encrypt_key) + AppConfig.KEY_SYS_ALG, getClass());
            LogUtil.debug(context.getString(R.string.msg_encrypt_mode) + algorithm + "-" + keyMode, getClass());

        }
    }


    private void decryption(String data) {
        byte[] input = ISOUtils.hex2byte(data);
        byte[]  cbciv = new byte[]{0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
        EncryptAlgorithm.KeyMode keyMode = EncryptAlgorithm.KeyMode.CBC;
        EncryptAlgorithm.ManufacturerAlgorithm algorithm = EncryptAlgorithm.ManufacturerAlgorithm.STANDARD;

        try {

            byte[] decryresult = pinInput.decrypt(new EncryptAlgorithm(keyMode, algorithm), new WorkingKey(indexWKTrack), input, cbciv);

            LogUtil.debug(context.getString(R.string.msg_decrypt_data) + data, getClass());
            LogUtil.debug(context.getString(R.string.msg_decrypt_key) + AppConfig.KEY_SYS_ALG, getClass());
            LogUtil.debug(context.getString(R.string.msg_decrypt_mode) + algorithm + "-" + keyMode, getClass());
            LogUtil.debug(context.getString(R.string.msg_decrypt_result) + ISOUtils.hexString(decryresult), getClass());
            LogUtil.debug(context.getString(R.string.msg_check_hint), getClass());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_decrypt_ex) + e.getMessage(), getClass());
            LogUtil.debug(context.getString(R.string.msg_decrypt_data) + data, getClass());
            LogUtil.debug(context.getString(R.string.msg_decrypt_key) + AppConfig.KEY_SYS_ALG, getClass());
            LogUtil.debug(context.getString(R.string.msg_decrypt_mode) + algorithm + "-" + keyMode, getClass());
        }

    }


    private void deleteMK() {
        try {
            boolean isSucc = false;
            if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
                isSucc = pinInput.deleteMainKey(AppConfig.Pin.MKSK_DES_INDEX_MK, false);
            } else if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
                isSucc = pinInput.deleteMainKey(AppConfig.Pin.MKSK_SM4_INDEX_MK, true);
            }
            if (isSucc == true) {
                LogUtil.debug(context.getString(R.string.msg_mk_del_succ), getClass());
            } else {
                LogUtil.debug(context.getString(R.string.msg_mk_del_failed), getClass());
            }
        } catch (Exception e) {
            LogUtil.debug(context.getString(R.string.msg_mk_del_failed) + e, getClass());
        }
    }

    private void deleteWKPin() {
        boolean isSucc = false;
        if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
            pinInput.deleteWorkingKey(WorkingKeyType.PININPUT, AppConfig.Pin.MKSK_DES_INDEX_WK_PIN);
        } else if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
            pinInput.deleteWorkingKey(WorkingKeyType.PININPUT_SM4, AppConfig.Pin.MKSK_SM4_INDEX_WK_PIN);
        } else if (AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {

        }
        if (isSucc = true) {
            LogUtil.debug(context.getString(R.string.msg_del_pin_wk_succ), getClass());
        } else {
            LogUtil.debug(context.getString(R.string.msg_del_pin_wk_failed), getClass());
        }

    }

    private void deleteWKTrack() {
        boolean isSucc = false;
        if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
            pinInput.deleteWorkingKey(WorkingKeyType.DATAENCRYPT, AppConfig.Pin.MKSK_DES_INDEX_WK_TRACK);
        } else if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
            pinInput.deleteWorkingKey(WorkingKeyType.DATAENCRYPT_SM4, AppConfig.Pin.MKSK_SM4_INDEX_WK_TRACK);
        } else if (AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {

        }

        if (isSucc = true) {
            LogUtil.debug(context.getString(R.string.msg_del_track_wk_succ), getClass());
        } else {
            LogUtil.debug(context.getString(R.string.msg_del_track_wk_failed), getClass());
        }

    }

    private void deleteWKMac() {
        boolean isSucc = false;
        if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
            pinInput.deleteWorkingKey(WorkingKeyType.MAC, AppConfig.Pin.MKSK_DES_INDEX_WK_MAC);
        } else if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
            pinInput.deleteWorkingKey(WorkingKeyType.MAC_SM4, AppConfig.Pin.MKSK_SM4_INDEX_WK_MAC);
        } else if (AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {

        }
        if (isSucc = true) {
            LogUtil.debug(context.getString(R.string.msg_del_mac_wk_succ), getClass());
        } else {
            LogUtil.debug(context.getString(R.string.msg_del_mac_wk_failed), getClass());
        }
    }


    private void deleteWK() {
        final String[] arrayWorkingKey = new String[]{context.getString(R.string.msg_wk_array_pinwk), context.getString(R.string.msg_wk_array_trackwk), context.getString(R.string.msg_wk_array_macwk)};
        deleteWKPin();
        deleteWKTrack();
        deleteWKMac();

    }


    private void deleteAllKey() {
        boolean isSucc = pinInput.deleteKeyStore();
        if (isSucc == true) {
            LogUtil.debug(context.getString(R.string.msg_all_key_del_succ), getClass());
        } else {
            LogUtil.debug(context.getString(R.string.msg_all_key_del_fail), getClass());
        }
    }


    private void checkKey() {
//        DialogUtils.createCustomDialog(context, context.getString(R.string.msg_check_key_exist), null, R.layout.dialog_checkkey, new DialogUtils.CustomDialogCallback() {
//            @Override
//            public void onResult(int id, View dialogView) {
//                try {
//                    RadioGroup group1 = dialogView.findViewById(R.id.radioGroup_keytype);
//                    EditText value = dialogView.findViewById(R.id.edit_keyindex);
//                    int keyIndex = Integer.parseInt(value.getText().toString());
//                    KeyType keyType = null;
//                    if (R.id.radio_transkey == group1.getCheckedRadioButtonId()) {
//                        if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.TRANSPORT_KEY;
//                        } else if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.TRANSPORT_KEY_SM4;
//                        } else if (AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.TRANSPORT_KEY_AES;
//                        }
//                    } else if (R.id.radio_mainkey == group1.getCheckedRadioButtonId()) {
//                        if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.MAIN_KEY;
//                        } else if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.MAIN_KEY_SM4;
//                        } else if (AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.MAIN_KEY_AES;
//                        }
//                    } else if (R.id.radio_pinkey == group1.getCheckedRadioButtonId()) {
//                        if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.PIN_KEY;
//                        } else if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.PIN_KEY_SM4;
//                        } else if (AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.PIN_KEY_AES;
//                        }
//                    } else if (R.id.radio_trackkey == group1.getCheckedRadioButtonId()) {
//                        if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.TRACK_KEY;
//                        } else if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.TRACK_KEY_SM4;
//                        } else if (AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.TRACK_KEY_AES;
//                        }
//                    } else if (R.id.radio_mackey == group1.getCheckedRadioButtonId()) {
//                        if (AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.MAC_KEY;
//                        } else if (AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.MAC_KEY_SM4;
//                        } else if (AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {
//                            keyType = KeyType.MAC_KEY_AES;
//                        }
//                    }
                try {    int keyIndex = Integer.parseInt("data");
                    KeyType keyType = null;
                    CheckKeyResult rslt = pinInput.checkKeyIsExist(keyType, keyIndex, null);
                    LogUtil.debug(context.getString(R.string.msg_key_type) + keyType, getClass());
                    LogUtil.debug(context.getString(R.string.msg_key_index) + keyIndex, getClass());
                    LogUtil.debug(context.getString(R.string.msg_is_exist) + rslt.isExist, getClass());
                    LogUtil.debug(context.getString(R.string.msg_return_kcv) + (rslt.getCheckValue() == null ? "null" : ISOUtils.hexString(rslt.getCheckValue())), getClass());
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.debug(context.getString(R.string.msg_check_key_ex) + e.getMessage(), getClass());
                }
    }


    private void getpbc21Info() {
        NdkApiManager ndkApiManager = NdkApiManager.getNdkApiManager();
        SecN secNdk = ndkApiManager.getSecN();
        try {
            LogUtil.debug(context.getString(R.string.msg_255_key), getClass());
            int setOwnerRslt = secNdk.NDK_SecSetKeyOwner("_NL_TERM_MGR"); // 人行21号表
            if (setOwnerRslt != 0) {
                LogUtil.debug(context.getString(R.string.msg_set_255_key_fail) + setOwnerRslt, getClass());
                return;
            }
            LogUtil.debug(context.getString(R.string.msg_set_255_key_succ) + setOwnerRslt, getClass());

            ST_SEC_KEY_INFO keyInfo = new ST_SEC_KEY_INFO();
            ST_SEC_KCV_INFO kcvInfo = new ST_SEC_KCV_INFO();

            int SEC_KEY_SM4 = (1 << 6);
            int SEC_KEY_TYPE_TDK = 4;

            keyInfo.ucScrKeyIdx = 0; // Plain text type
            keyInfo.ucScrKeyType = 0; // Terminal loading key
            keyInfo.ucDstKeyIdx = (byte) 255; //  key index
            keyInfo.ucDstKeyType = (byte) (SEC_KEY_TYPE_TDK | SEC_KEY_SM4); // 数据加解密密钥
            Arrays.fill(keyInfo.DstKeyValue, (byte) 0x32);
            keyInfo.nDstKeyLen = 16;

            kcvInfo.nCheckMode = 1;
            byte[] checkValue = new byte[]{(byte) 0x2B, (byte) 0x44, 0x59, (byte) 0xDE};
            kcvInfo.nLen = 4;
            kcvInfo.sCheckBuf = Arrays.copyOf(checkValue, 4);

            int ndkRslt = secNdk.NDK_SecLoadKey(keyInfo, kcvInfo);
            if (ndkRslt != 0) {
                LogUtil.debug(context.getString(R.string.msg_255_index_failed_return) + ndkRslt, getClass());
            } else {
                LogUtil.debug(context.getString(R.string.msg_255_index_succ) + ndkRslt, getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_255_index_failed) + e.getMessage(), getClass());
        } finally {
            secNdk.NDK_SecSetKeyOwner("*"); // 回到共享密钥区
        }
        try {
            TusnData info = pinInput.getTusnData("123456");
            LogUtil.debug(context.getString(R.string.msg_randow_factor), getClass());
            LogUtil.debug(context.getString(R.string.msg_get_21_data_device_type) + info.getDeviceType(), getClass());
            LogUtil.debug(context.getString(R.string.msg_get_21_data_hard_sn) + info.getSn(), getClass());
            LogUtil.debug(context.getString(R.string.msg_get_21_data_hard_sn_en) + info.getEncryptedData(), getClass());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_get_21_data_ex), getClass());
        }
    }
}
