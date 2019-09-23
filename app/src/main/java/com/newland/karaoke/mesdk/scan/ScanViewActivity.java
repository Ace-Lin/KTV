package com.newland.karaoke.mesdk.scan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.newland.SettingsManager;
import android.newland.content.NlContext;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.newland.karaoke.R;
import com.newland.karaoke.mesdk.AppConfig;
import com.newland.karaoke.mesdk.SoundPoolImpl;
import com.newland.mtype.log.DeviceLogger;
import com.newland.mtype.log.DeviceLoggerFactory;
import com.newland.mtype.module.common.scanner.ScanLightType;
import com.newland.mtype.module.common.scanner.ScannerListener;

import java.util.concurrent.TimeUnit;

public class ScanViewActivity extends Activity {
	
	private SurfaceView surfaceView;
	private Context context;
	private int scanType;
	private int timeout;
	private static DeviceLogger logger = DeviceLoggerFactory.getLogger(ScanViewActivity.class);
	private ImageView scanIV;
	private SoundPoolImpl spi;
	private RelativeLayout frontLL;
	private LinearLayout switch_fr;
	private LinearLayout switch_bc;
	private boolean isFinish = false;
	private boolean isTimeout = true;
	private AnimationDrawable scanAnim;
	private FrameLayout backFL;
	private static final int Code_PERMISSION=100;
	private TextView picTv,posTv;
	private SettingsManager settingManager;

	@SuppressLint({"WrongAppConfigant", "WrongConstant"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context=this;
		View view = View.inflate(this, R.layout.sacn_view, null);
		setContentView( view);
		
		
		spi = SoundPoolImpl.getInstance();
		spi.initLoad(this);
		init();
		try {
			settingManager = (SettingsManager) getSystemService(NlContext.SETTINGS_MANAGER_SERVICE);
			settingManager.setAppSwitchKeyEnabled(false);
			settingManager.setHomeKeyEnabled(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private Handler scanHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1: {
					startScan();
					break;
				}
				default:
					break;
			}
		}

	};

	private void init() {
		scanType=getIntent().getIntExtra("scanType", 0x01);//Front default
//		timeout=getIntent().getIntExtra("timeout", 60);
		timeout=60;
		surfaceView=(SurfaceView) findViewById(R.id.surfaceView);
		frontLL=(RelativeLayout) findViewById(R.id.ll_front);
		switch_fr = (LinearLayout) findViewById(R.id.ll_switch_front);
		switch_bc=(LinearLayout)findViewById(R.id.ll_switch_back);
		backFL=(FrameLayout) findViewById(R.id.fl_back);
		scanIV=(ImageView) findViewById(R.id.iv_scan);

		picTv=(TextView) findViewById(R.id.text_pic);
		posTv=(TextView) findViewById(R.id.text_pos);

		//默认使用900的扫码前置预览界面
//		if(NlBuild.VERSION.MODEL.equals("CPOS X5")){
//			scanIV.setImageResource(R.drawable.scan_x5_list);
//			picTv.setGravity(Gravity.LEFT);
//			posTv.setGravity(Gravity.LEFT);
//			picTv.setPadding(200,0,0,0);
//			posTv.setPadding(200,0,0,0);
//		}else if(NlBuild.VERSION.MODEL.equals("CPOS X3")){
//			scanIV.setImageResource(R.drawable.scan_x3_list);
//			picTv.setGravity(Gravity.LEFT);
//			posTv.setGravity(Gravity.LEFT);
//			picTv.setPadding(200,0,0,0);
//			posTv.setPadding(200,0,0,0);
//		}else if(NlBuild.VERSION.MODEL.equals("N910")){
//			scanIV.setImageResource(R.drawable.scan_910_list);
//		}else if(NlBuild.VERSION.MODEL.equals("N550")){
//			picTv.setTextSize(25);
//			posTv.setTextSize(25);
//			scanIV.setImageResource(R.drawable.scan_550_list);
//		}else if(NlBuild.VERSION.MODEL.equals("N850")){
//			scanIV.setImageResource(R.drawable.scan_850_list);
//		}else if(NlBuild.VERSION.MODEL.equals("N700")){
//			scanIV.setImageResource(R.drawable.scan_700_list);
//	}

		switch_fr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("---------------切换前置---------");
				switch_bc.setEnabled(true);
				switch_fr.setEnabled(false);
				isFinish = false;
				isTimeout = false;

				ScannerModule.scanner.operLight(ScanLightType.LED_LIGHT,0);
				ScannerModule.scanner.operLight(ScanLightType.RED_LIGHT,0);
				ScannerModule.scanner.operLight(ScanLightType.FLASH_LIGHT,0);
				ScannerModule.scanner.stopScan();
				scanType= AppConfig.ScanType.FRONT;

			}
		});

		switch_bc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				logger.debug("---------------切换后置---------");
				switch_bc.setEnabled(false);
				switch_fr.setEnabled(true);
				isFinish = false;
				isTimeout = false;
				ScannerModule.scanner.operLight(ScanLightType.LED_LIGHT,0);
				ScannerModule.scanner.operLight(ScanLightType.RED_LIGHT,0);
				ScannerModule.scanner.operLight(ScanLightType.FLASH_LIGHT,0);
				ScannerModule.scanner.stopScan();
				scanType=AppConfig.ScanType.BACK;

			}
		});

		if (Build.VERSION.SDK_INT>22){
			if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
				//先判断有没有权限 ，没有就在这里进行权限的申请
				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},Code_PERMISSION);
			}else {
				startScan();
			}
		} else {
			startScan();
		}


	}

	private void startScan(){
		try{
			isFinish = false;
			isTimeout = true;
			if(scanType==AppConfig.ScanType.BACK){//后置的
				frontLL.setVisibility(View.GONE);
				backFL.setVisibility(View.VISIBLE);
				boolean resutl = ScannerModule.scanner.isSupScanCode(1);
				switch_fr.setVisibility(View.GONE);
				if(resutl){
					switch_fr.setVisibility(View.VISIBLE);
				}
				ScannerModule.scanner.initScanner(context,surfaceView,scanType);
//				ScannerModule.scanner.operLight(ScanLightType.FLASH_LIGHT,1);

			}else if(scanType==AppConfig.ScanType.FRONT){
				backFL.setVisibility(View.GONE);
				frontLL.setVisibility(View.VISIBLE);
				scanAnim = (AnimationDrawable)scanIV.getDrawable();
				if (scanAnim != null && !scanAnim.isRunning()) {
					scanAnim.start();
				}
				boolean resutl = ScannerModule.scanner.isSupScanCode(0);
				switch_bc.setVisibility(View.GONE);
				if(resutl){
					switch_bc.setVisibility(View.VISIBLE);
				}
				ScannerModule.scanner.initScanner(context,null,scanType);
//				ScannerModule.scanner.operLight(ScanLightType.LED_LIGHT,1);
//				ScannerModule.scanner.operLight(ScanLightType.RED_LIGHT,1);

			}else{
				finish();
				Message scanMsg = new Message();
				scanMsg.what = AppConfig.ScanResult.SCAN_ERROR;
				Bundle scanBundle = new Bundle();
				scanBundle.putInt("errorCode", 0x00);
				scanBundle.putString("errormessage",context.getString(R.string.content_sacnview_error_message));
				scanMsg.setData(scanBundle);
				ScannerModule.getScanEventHandler().sendMessage(scanMsg);
			}



			ScannerModule.scanner.startScan(timeout, TimeUnit.SECONDS, new ScannerListener() {

				@Override
				public void onResponse(String[] barcodes) {
					logger.debug("---------------onResponse---------"+barcodes[0]);
					isTimeout = false;
					isFinish = true;
					spi.play();
					Message scanMsg = new Message();
					scanMsg.what = AppConfig.ScanResult.SCAN_RESPONSE;
					Bundle scanBundle = new Bundle();
					scanBundle.putStringArray("barcodes", barcodes);
					scanMsg.setData(scanBundle);
					ScannerModule.getScanEventHandler().sendMessage(scanMsg);

				}

				@Override
				public void onFinish() {
					logger.debug("---------------onFinish---------"+isFinish+isTimeout);
					if (isTimeout) {
						isFinish = true;
						finish();
						Message scanMsg = new Message();
						scanMsg.what = AppConfig.ScanResult.SCAN_TIMEOUT;
						ScannerModule.getScanEventHandler().sendMessage(scanMsg);
						//超时
					} else if (!isTimeout&&!isFinish) {

						Message scanMsg = new Message();
						scanMsg.what = 1;
						scanHandler.sendMessage(scanMsg);
					}else{
						finish();
						logger.debug("---------------onFinish---------");
					}
				}
			},true);
		}catch(Exception e){
			e.printStackTrace();
			isFinish = true;
			logger.debug("---------------Exception---------"+e.getMessage());
			finish();
			e.getStackTrace();
			Message scanMsg = new Message();
			scanMsg.what = AppConfig.ScanResult.SCAN_ERROR;
			Bundle scanBundle = new Bundle();
			scanBundle.putInt("errorCode", 0);
			scanBundle.putString("errormessage", e.getMessage());
			scanMsg.setData(scanBundle);
			ScannerModule.getScanEventHandler().sendMessage(scanMsg);

		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		logger.debug("------onPause--------" + isTimeout + "isFinish：" + isFinish);
		if (isTimeout && !isFinish) {
			//取消扫码
			Message scanMsg = new Message();
			scanMsg.what = AppConfig.ScanResult.SCAN_CANCEL;
			ScannerModule.getScanEventHandler().sendMessage(scanMsg);
		}
		isTimeout = false;
		isFinish = true;
//
//		ScannerModule.scanner.operLight(ScanLightType.LED_LIGHT,0);
//		ScannerModule.scanner.operLight(ScanLightType.RED_LIGHT,0);
//		ScannerModule.scanner.operLight(ScanLightType.FLASH_LIGHT,0);
		ScannerModule.scanner.stopScan();

		if (scanAnim != null && scanAnim.isRunning()) {
			scanAnim.stop();
		}
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		logger.debug("---------------keyCode---------"+keyCode);//700S设备 左边是24 右边是25
		logger.debug("---------------event---------"+event);
//		if((keyCode==KeyEvent.KEYCODE_VOLUME_UP&& event.getRepeatCount() == 0)){
//			logger.debug("发起700扫码");
//			startScan();
//		}else if((keyCode==KeyEvent.KEYCODE_VOLUME_DOWN&& event.getRepeatCount() == 0)){
//			logger.debug("发起700扫码");
//			startScan();
//		}

		if(keyCode== KeyEvent.KEYCODE_BACK){
			logger.debug("回退键");
			isTimeout=true;
			isFinish=false;
			finish();


		}

		return super.onKeyDown(keyCode, event);

	}

	@Override
	protected void onDestroy() {
		logger.debug("---------------onDestroy---------");
		try {
			spi.release();
			settingManager.setAppSwitchKeyEnabled(true);
			settingManager.setHomeKeyEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onDestroy();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		try{
			if (requestCode == Code_PERMISSION) {
				if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					//权限被用户同意,做相应的事情
					startScan();
				} else {
					//权限被用户拒绝，做相应的事情
					Message scanMsg = new Message();
					scanMsg.what = AppConfig.ScanResult.SCAN_ERROR;
					Bundle scanBundle = new Bundle();
					scanBundle.putInt("errorCode", 0);
					scanBundle.putString("errormessage","摄像头动态授权失败");
					scanMsg.setData(scanBundle);
					ScannerModule.getScanEventHandler().sendMessage(scanMsg);
					finish();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			finish();

		}

		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

	}
}
