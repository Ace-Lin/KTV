package com.newland.karaoke.mesdk.scan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.newland.karaoke.R;
import com.newland.karaoke.mesdk.AppConfig;
import com.newland.karaoke.mesdk.BaseModule;
import com.newland.karaoke.mesdk.device.SDKDevice;
import com.newland.karaoke.utils.LogUtil;
import com.newland.mtype.module.common.scanner.BarcodeScanner;

/**
 * Author by bxy, Date on 2019/5/11 0011.
 */
public class ScannerModule extends BaseModule {
    public static BarcodeScanner scanner = null;
    public ScanListener scanListener;

    /**
     * 用于接收扫码数据
     */
    private static Handler scanEventHandler;

    public ScannerModule(Context context) {
        super(context);
    }

    public void initData() {
        scanner = SDKDevice.getInstance().getBarcodeScanner();
        scanEventHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case AppConfig.ScanResult.SCAN_FINISH: {
                        LogUtil.debug(context.getString(R.string.msg_sanner_stop) , getClass());
                        break;

                    }
                    case AppConfig.ScanResult.SCAN_RESPONSE: {
                        Bundle bundle = msg.getData();
                        String[] barcodes = bundle.getStringArray("barcodes");
                        LogUtil.debug(context.getString(R.string.msg_scan_result)+ barcodes[0] , getClass());
                        scanListener.scanResponse(barcodes[0]);
                        break;
                    }
                    case AppConfig.ScanResult.SCAN_ERROR: {
                        Bundle bundle = msg.getData();
                        int errorCode = bundle.getInt("errorCode");
                        String errorMess = bundle.getString("errormessage");
                        LogUtil.debug(context.getString(R.string.msg_scanner_error) + errorCode + context.getString(R.string.msg_error_info) + errorMess, getClass());
                        scanListener.scanError();
                        break;
                    }
                    case AppConfig.ScanResult.SCAN_TIMEOUT: {
                        Bundle bundle = msg.getData();
                        LogUtil.debug(context.getString(R.string.msg_scan_timeout),getClass());
                        scanListener.scanTimeout();
                        break;
                    }
                    case AppConfig.ScanResult.SCAN_CANCEL: {
                        Bundle bundle = msg.getData();
                        LogUtil.debug(context.getString(R.string.msg_scan_cancel), getClass());
                        scanListener.scanCancel();
                        break;
                    }
                    default:
                        break;
                }
            }

        };
    }

    /**
     * 开启后置相机扫码
     * @param listener ScanListener
     */
    public void startScan(ScanListener listener){
        initData();
        scanListener = listener;

        LogUtil.debug(context.getString(R.string.msg_choice_back_scanner_mode) , getClass());
        Intent intent = new Intent(context, ScanViewActivity.class);
        intent.putExtra("scanType", 0x00);
        context.startActivity(intent);
    }


    public void stopScan(){
        try {
            LogUtil.debug(context.getString(R.string.msg_stop_scan_begin) , getClass());
            scanner.stopScan();
            LogUtil.debug(context.getString(R.string.msg_stop_scan_success) , getClass());
        } catch (Exception e) {
            LogUtil.debug(context.getString(R.string.msg_stop_scan_error) + e.getMessage() , getClass());
        }
    }

    public static Handler getScanEventHandler() {
        return scanEventHandler;
    }

    public static void setScanEventHandler(Handler scanEventHandler) {
        ScannerModule.scanEventHandler = scanEventHandler;
    }
}
