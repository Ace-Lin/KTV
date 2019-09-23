package com.newland.karaoke.mesdk;

import com.newland.mtype.module.common.emv.EmvTransInfo;
import com.newland.mtype.module.common.swiper.SwipResult;

import java.math.BigDecimal;

/**
 * Author by bxy, Date on 2019/5/14 0014.
 */
public class AppConfig {
    public static final String MKSK_DES  = "MKSK_DES";
    public static final String MKSK_SM4  = "MKSK_SM4";
    public static final String MKSK_AES  = "MKSK_AES";
    public static final String DUKPT_DES = "DUKPT_DES";
    public static final String DUKPT_AES = "DUKPT_AES";
    public static String KEY_SYS_ALG = MKSK_DES;

    public static class Pin{
        public static final int MKSK_DES_INDEX_MK = 1;
        public static final int MKSK_DES_INDEX_WK_PIN = 2;
        public static final int MKSK_DES_INDEX_WK_TRACK = 3;
        public static final int MKSK_DES_INDEX_WK_MAC = 4;

        public static final int MKSK_SM4_INDEX_MK = 1;
        public static final int MKSK_SM4_INDEX_WK_PIN = 2;
        public static final int MKSK_SM4_INDEX_WK_TRACK = 3;
        public static final int MKSK_SM4_INDEX_WK_MAC = 4;

        public static final int MKSK_AES_INDEX_MK = 1;
        public static final int MKSK_AES_INDEX_WK_PIN = 2;
        public static final int MKSK_AES_INDEX_WK_TRACK = 3;
        public static final int MKSK_AES_INDEX_WK_MAC = 4;

        public static final int DUKPT_DES_INDEX = 1;
        public static final int DUKPT_AES_INDEX = 2;

        public static byte[] encryptResult = null;
    }

    public static class EMV{
        /**
         * Password entered is complete
         */
        public static final int PIN_FINISH = 1;
        public static BigDecimal amt;
        public static int isECSwitch;
        public static String icCardNum;
        public static EmvTransInfo emvTransInfo;// Pass ic/rf Consumption message params
        public static String ic55Data;
        public static byte[] pinBlock;
        public static int consumeType; // Consumption type, magnetic stripe card consumption did not pass the IC55-tag data, 0-magnetic 1-IC 2-RF. Distinguish the passing of parameters
        public static SwipResult swipResult;// Swiping result
    }
    public static int INDEX_FRAGMENT_START = -1;
    public static int INDEX_FRAGMENT_EMV = -1;
    public static int INDEX_FRAGMENT_PIN = -1;

    public static class ScanResult{
        public static final int SCAN_FINISH = 0;
        public static final int SCAN_RESPONSE = 1;
        public static final int SCAN_ERROR = 2;
        public static final int SCAN_TIMEOUT = 3;
        public static final int SCAN_CANCEL = 4;
    }

    public static class ScanType {
        /**
         * Front scan
         */
        public static final int FRONT = 1;
        /**
         * Back scan
         */
        public static final int BACK = 0;
    }
}

