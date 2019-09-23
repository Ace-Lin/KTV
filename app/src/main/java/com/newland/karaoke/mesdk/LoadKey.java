package com.newland.karaoke.mesdk;

import android.content.Context;

import com.newland.karaoke.R;
import com.newland.karaoke.mesdk.emv.EMVModule;
import com.newland.karaoke.mesdk.pin.PinMKSKModule;
import com.newland.karaoke.utils.LogUtil;

/**
 * author : Jeffrey
 * date   : 2019/9/2220:02
 * desc   :
 */
public class LoadKey {

    private PinMKSKModule pinMKSKModule;
    private EMVModule emvModule;
    private Context context;
    private static LoadKey loadKey;

    public static LoadKey getInstance(Context context){
        if(loadKey == null){
            synchronized (LoadKey.class){
                if(loadKey == null){
                    loadKey = new LoadKey(context);
                }
            }
        };
        return loadKey;
    }

    private LoadKey(Context context) {
        this.context = context;
        initData();
    }

    public void  initData() {
        pinMKSKModule = new PinMKSKModule(context);
        pinMKSKModule.initData();
        emvModule = new EMVModule(context);
        emvModule.initData();
        loadmk();
        loadwk();
        addAid();
        addPublicKey();
    }


    public void loadmk(){
        if(AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)||AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)||AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)){
            try {
                pinMKSKModule.loadMainKey();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.debug(context.getString(R.string.msg_load_mk_failed) + e.getMessage(), getClass());
            }
        }
    }


    public void loadwk(){
        if(AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)||AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)||AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)) {
            try {
                pinMKSKModule.loadWorkingKey();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.debug(e.getMessage(),getClass());
            }
        }
    }


    public void addAid(){
        try {
            emvModule.addAid();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_add_aid_e) + e, getClass());
        }
    }


    public void addPublicKey(){
        try {
            emvModule.addCapk();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_add_pk_result) + context.getString(R.string.common_exception) + e.getMessage(), getClass());
        }
    }

}
