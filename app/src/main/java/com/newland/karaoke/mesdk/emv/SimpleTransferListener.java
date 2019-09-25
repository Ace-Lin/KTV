package com.newland.karaoke.mesdk.emv;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.newland.emv.jni.type.EmvConst;
import com.newland.karaoke.R;
import com.newland.karaoke.activity.CardPayActivity;
import com.newland.karaoke.activity.MainActivity;
import com.newland.karaoke.mesdk.AppConfig;
import com.newland.karaoke.mesdk.device.SDKDevice;
import com.newland.karaoke.utils.LogUtil;
import com.newland.me.SupportMSDAlgorithm;
import com.newland.mtype.ModuleType;
import com.newland.mtype.common.InnerProcessingCode;
import com.newland.mtype.common.MESeriesConst;
import com.newland.mtype.module.common.emv.EmvModule;
import com.newland.mtype.module.common.emv.EmvPinInputType;
import com.newland.mtype.module.common.emv.EmvTransController;
import com.newland.mtype.module.common.emv.EmvTransInfo;
import com.newland.mtype.module.common.emv.EmvTransInfo.AIDSelect;
import com.newland.mtype.module.common.emv.SecondIssuanceRequest;
import com.newland.mtype.module.common.emv.level2.EmvCardholderCertType;
import com.newland.mtype.module.common.emv.level2.EmvFinalAppSelectListener;
import com.newland.mtype.module.common.pin.WorkingKey;
import com.newland.mtype.module.common.swiper.K21Swiper;
import com.newland.mtype.module.common.swiper.SwipResult;
import com.newland.mtype.tlv.TLVPackage;
import com.newland.mtype.util.ISOUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleTransferListener implements EmvFinalAppSelectListener {

	private Context context;
	private static int[] L_55TAGS = new int[26];
	private static int[] L_SCRIPTTAGS = new int[21];
	private static int[] L_REVTAGS = new int[5];
	private int isECSwitch = 0;
	private static WaitThreat waitPinInputThreat = new WaitThreat();
	private static byte[] pinBlock = null;
	private String encryptAlgorithm;
	private EmvModule emvModule;
	private int transType;
	private int trackKeyIndex;
	private int mkIndex;
	//监听所需数据
     private Intent resultIntent = new Intent();
	//增加监听回调
	public CardPayActivity.ReadCardListener listener;


	static {
		L_55TAGS[0] = 0x9f26;
		L_55TAGS[1] = 0x9F10;
		L_55TAGS[2] = 0x9F27;
		L_55TAGS[3] = 0x9F37;
		L_55TAGS[4] = 0x9F36;
		L_55TAGS[5] = 0x95;
		L_55TAGS[6] = 0x9A;
		L_55TAGS[7] = 0x9C;
		L_55TAGS[8] = 0x9F02;
		L_55TAGS[9] = 0x5F2A;
		L_55TAGS[10] = 0x82;
		L_55TAGS[11] = 0x9F1A;
		L_55TAGS[12] = 0x9F03;
		L_55TAGS[13] = 0x9F33;
		L_55TAGS[14] = 0x9F34;
		L_55TAGS[15] = 0x9F35;
		L_55TAGS[16] = 0x9F1E;
		L_55TAGS[17] = 0x84;
		L_55TAGS[18] = 0x9F09;
		L_55TAGS[19] = 0x9F41;
		L_55TAGS[20] = 0x8a;
		L_55TAGS[21] = 0x9f63;
		L_55TAGS[22] = 0x50;
		L_55TAGS[23] = 0x4f;
		L_55TAGS[24] = 0x9f12;
		L_55TAGS[25] = 0x9B;

		L_SCRIPTTAGS[0] = 0x9F33;
		L_SCRIPTTAGS[1] = 0x9F34;
		L_SCRIPTTAGS[2] = 0x9F35;
		L_SCRIPTTAGS[3] = 0x95;
		L_SCRIPTTAGS[4] = 0x9F37;
		L_SCRIPTTAGS[5] = 0x9F1E;
		L_SCRIPTTAGS[6] = 0x9F10;
		L_SCRIPTTAGS[7] = 0x9F26;
		L_SCRIPTTAGS[8] = 0x9F27;
		L_SCRIPTTAGS[9] = 0x9F36;
		L_SCRIPTTAGS[10] = 0x82;
		L_SCRIPTTAGS[11] = 0xDF31;
		L_SCRIPTTAGS[12] = 0x9F1A;
		L_SCRIPTTAGS[13] = 0x9A;
		L_SCRIPTTAGS[14] = 0x9C;
		L_SCRIPTTAGS[15] = 0x9F02;
		L_SCRIPTTAGS[16] = 0x5F2A;
		L_SCRIPTTAGS[17] = 0x84;
		L_SCRIPTTAGS[18] = 0x9F09;
		L_SCRIPTTAGS[19] = 0x9F41;
		L_SCRIPTTAGS[20] = 0x9F63;

		L_REVTAGS[0] = 0x95;
		L_REVTAGS[1] = 0x9F1E;
		L_REVTAGS[2] = 0x9F10;
		L_REVTAGS[3] = 0x9F36;
		L_REVTAGS[4] = 0xDF31;
	}

	public SimpleTransferListener(Context context, EmvModule emvModule, int transType,CardPayActivity.ReadCardListener listener) {
		this.listener = listener;
		this.context = context;
		this.emvModule = emvModule;
		this.transType = transType;
		if(AppConfig.MKSK_DES.equals(AppConfig.KEY_SYS_ALG)){
			mkIndex = AppConfig.Pin.MKSK_DES_INDEX_MK;
			trackKeyIndex = AppConfig.Pin.MKSK_DES_INDEX_WK_TRACK;
			encryptAlgorithm = MESeriesConst.TrackEncryptAlgorithm.BY_UNIONPAY_MODEL;
		}else if(AppConfig.MKSK_SM4.equals(AppConfig.KEY_SYS_ALG)){
			mkIndex = AppConfig.Pin.MKSK_SM4_INDEX_MK;
			trackKeyIndex = AppConfig.Pin.MKSK_SM4_INDEX_WK_TRACK;
			encryptAlgorithm = MESeriesConst.TrackEncryptAlgorithm.BY_UNIONPAY_MODEL;
		}else if(AppConfig.MKSK_AES.equals(AppConfig.KEY_SYS_ALG)){
			mkIndex = AppConfig.Pin.MKSK_AES_INDEX_MK;
			trackKeyIndex = AppConfig.Pin.MKSK_AES_INDEX_WK_TRACK;
			encryptAlgorithm = MESeriesConst.TrackEncryptAlgorithm.BY_UNIONPAY_MODEL;
		}else if(AppConfig.DUKPT_DES.equals(AppConfig.KEY_SYS_ALG)){
			trackKeyIndex = AppConfig.Pin.DUKPT_DES_INDEX;
			encryptAlgorithm = MESeriesConst.TrackEncryptAlgorithm.BY_DUKPT_MODEL;
		}
	}

	@Override
	public void onEmvFinished(boolean isSuccess, EmvTransInfo emvTransInfo) throws Exception {
		// TTransaction code
		int executeRslt = emvTransInfo.getExecuteRslt();

		String resultMsg = null;
		switch (executeRslt) {
		case 0:
		case 1:
			resultMsg = context.getString(R.string.msg_trans_acc);
			break;
		case 2:
			resultMsg = context.getString(R.string.msg_trans_reject);
			break;
		case 3:
			resultMsg = context.getString(R.string.msg_request_online);
			break;
		case -2105:
			resultMsg = context.getString(R.string.msg_trans_exceed_limit);
			break;
		default:
			resultMsg = context.getString(R.string.msg_trans_failed);
			break;
		}
		LogUtil.debug("222222222222222222 onEmvFinished", getClass());
		LogUtil.debug(context.getString(R.string.msg_trans_result) + resultMsg, getClass());
		//The specific reason for the error code
		int errorCode = emvTransInfo.getErrorcode();
		switch (errorCode) {
		case -6:
			resultMsg = context.getString(R.string.msg_trans_error_no_supported_app);
			break;
		case -11:
			resultMsg = context.getString(R.string.msg_trans_error_offline_data_auth_failed);
			break;
		case -13:
			resultMsg = context.getString(R.string.msg_trans_error_holder_auth_failed);
			break;
		case -18:
			resultMsg = context.getString(R.string.msg_trans_error_card_locked);
			break;
		case -1531:
		case -2116:
			resultMsg = context.getString(R.string.msg_trans_error_card_expired);
			break;
		case -1532:
		case -2115:
			resultMsg = context.getString(R.string.msg_trans_error_card_not_effected);
			break;
		case -1822:
			resultMsg = context.getString(R.string.msg_trans_error_ecash_nsf);
			break;
		case -1903:
			resultMsg = context.getString(R.string.msg_error_ec_load_exceed_limit);
			break;
		case -1904:
		case -1905:
			resultMsg = context.getString(R.string.msg_trans_error_script_execut_error);
			break;
		case -1901:
			resultMsg = context.getString(R.string.msg_trans_error_script_transfinite);
			break;
		case -2105:
			resultMsg = context.getString(R.string.msg_trans_error_pre_money_limit);
			break;
		case -2120:
		case -1441:
			resultMsg = context.getString(R.string.msg_trans_error_pure_ecash_cant_online);
			break;
		case -2121:
			resultMsg = context.getString(R.string.msg_trans_error_card_reject);
			break;
		default:
			resultMsg = null;
			break;
		}
		if (errorCode != 0) {
			LogUtil.debug(context.getString(R.string.msg_trans_error_details) + errorCode + resultMsg, getClass());
			//添加错误信息
			resultIntent.putExtra("result", AppConfig.ReadCardResult.FAIL);
			resultIntent.putExtra("exception",errorCode +"||"+resultMsg);
		}
		else
			resultIntent.putExtra("result", AppConfig.ReadCardResult.SUCC);

		int[] emvTags = new int[3];
		emvTags[0] = 0x5a;
		emvTags[1] = 0x5F34;
		emvTags[2] = 0x57;

		String data = emvModule.fetchEmvData(emvTags);
		TLVPackage tlv = ISOUtils.newTlvPackage();
		tlv.unpack(ISOUtils.hex2byte(data));
		String track2 = tlv.getString(0x57); // Two track  data
		String cardNo = tlv.getString(0x5a);
		if (null == cardNo && track2 != null) {
			cardNo = track2.substring(0, track2.indexOf('D'));
		}
		String cardSN = tlv.getString(0x5F34);// PAN
		String expiredDate = null;

		if (track2 != null) {
			expiredDate = track2.substring(track2.indexOf('D') + 1, track2.indexOf('D') + 5);
		}
		if (cardSN == null) {
			cardSN = "000";
		} else {
			cardSN = ISOUtils.padleft(cardSN, 3, '0');
		}
		String serviceCode = "";
		if (null != track2) {
			serviceCode = track2.substring(track2.indexOf('D') + 5, track2.indexOf('D') + 8);
		}
		//Since the array is BCD encoded, the last digit of the card number needs to be removed if it is 'F'.
		if (null != cardNo && cardNo.endsWith("F"))
			cardNo = cardNo.substring(0, cardNo.length() - 1);
		LogUtil.debug(context.getString(R.string.msg_term_cardNo) + cardNo, getClass());
		LogUtil.debug(context.getString(R.string.msg_term_cardSN) + cardSN, getClass());
		LogUtil.debug(context.getString(R.string.msg_term_expiredDate) + expiredDate, getClass());
		LogUtil.debug(context.getString(R.string.msg_term_serviceCode) + serviceCode, getClass());
		LogUtil.debug(context.getString(R.string.msg_term_track2) + track2, getClass());

		AppConfig.EMV.icCardNum =  cardNo;
		AppConfig.EMV.emvTransInfo = emvTransInfo;
		if (null != track2) {
			K21Swiper swiper = SDKDevice.getInstance().getK21Swiper();
			SwipResult swipRslt = swiper.calculateTrackData(track2, null, new WorkingKey(trackKeyIndex), SupportMSDAlgorithm.getMSDAlgorithm(encryptAlgorithm));
			LogUtil.debug(context.getString(R.string.msg_term_track2_ciphertext) + (swipRslt.getSecondTrackData() == null ? null : ISOUtils.hexString(swipRslt.getSecondTrackData())), getClass());
		}

		String data55 = emvModule.fetchEmvData(L_55TAGS);
		LogUtil.debug(context.getString(R.string.msg_term_55tag) + data55, getClass());
		AppConfig.EMV.ic55Data = data55;// 55 filed data
		String dataScript = emvModule.fetchEmvData(L_SCRIPTTAGS);
		LogUtil.debug(context.getString(R.string.msg_term_script) + dataScript, getClass());
		String revData = emvModule.fetchEmvData(L_REVTAGS);
		LogUtil.debug(context.getString(R.string.msg_term_flushes_data) + revData, getClass());

		resultIntent.putExtra("account", cardNo);
		listener.resultInfo(resultIntent);
	}

	@Override
	public void onError(EmvTransController arg0, Exception arg1) {
		LogUtil.debug(context.getString(R.string.msg_emv_trans_failed) + arg1.getMessage(), getClass());
		arg1.printStackTrace();
	}

	@Override
	public void onFallback(EmvTransInfo arg0) throws Exception {
		LogUtil.debug(context.getString(R.string.msg_ic_env_notmeet_fallback), getClass());
	}

	/**
	 * 进行联网数据操作
	 * @param controller
	 * @param emvTransInfo
	 * @throws Exception
	 */
	@Override
    public void onRequestOnline(EmvTransController controller, EmvTransInfo emvTransInfo) throws Exception {
		int emvResult = emvTransInfo.getEmvrsltCode();
		String resultMsg = null;
		switch (emvResult) {
		case 3:
			resultMsg = context.getString(R.string.msg_pboc_online);
			break;
		case 15:
			resultMsg = context.getString(R.string.msg_rfqpboc_online);
			break;
		}
		LogUtil.debug("1111111111111111 onRequestOnline",getClass());
		LogUtil.debug(context.getString(R.string.msg_request_online_result) + resultMsg, getClass());

		int[] emvTags = new int[4];
		emvTags[0] = 0x5a;
		emvTags[1] = 0x5F34;
		emvTags[2] = 0x5f24;
		emvTags[3] = 0x57;
		String data = emvModule.fetchEmvData(emvTags);
		TLVPackage tlv = ISOUtils.newTlvPackage();
		tlv.unpack(ISOUtils.hex2byte(data));
		String cardNo = tlv.getString(0x5a);
		String cardSN = tlv.getString(0x5F34);// Card serial number == context.getCardSequenceNumber()
		String track2 = tlv.getString(0x57); // Two track data == context.getTrack_2_eqv_data()
		if (null == cardNo && track2 != null) {
			cardNo = track2.substring(0, track2.indexOf('D'));
		}
		String expiredDate = null;
		if (track2 != null) {
			expiredDate = track2.substring(track2.indexOf('D') + 1, track2.indexOf('D') + 5);
		}
		if (cardSN == null) {
			cardSN = "000";
		} else {
			cardSN = ISOUtils.padleft(cardSN, 3, '0');
		}
		String serviceCode = "";
		if (null != track2) {
			serviceCode = track2.substring(track2.indexOf('D') + 5, track2.indexOf('D') + 8);
		}
		//Since the array is BCD encoded, the last digit of the card number needs to be removed if it is 'F'.
		if (null != cardNo && cardNo.endsWith("F"))
			cardNo = cardNo.substring(0, cardNo.length() - 1);
		LogUtil.debug(context.getString(R.string.msg_term_cardNo) + cardNo, getClass());
		LogUtil.debug(context.getString(R.string.msg_term_cardSN) + cardSN, getClass());
		LogUtil.debug(context.getString(R.string.msg_term_expiredDate)  + expiredDate, getClass());
		LogUtil.debug(context.getString(R.string.msg_term_serviceCode) + serviceCode, getClass());
		LogUtil.debug(context.getString(R.string.msg_term_track2) + track2, getClass());
		AppConfig.EMV.icCardNum = emvTransInfo.getCardNo();
		AppConfig.EMV.emvTransInfo = emvTransInfo;//  Emv Trans Info
		if (null != track2) {
			K21Swiper swiper = SDKDevice.getInstance().getK21Swiper();
			SwipResult swipRslt = swiper.calculateTrackData(track2, null, new WorkingKey(trackKeyIndex), SupportMSDAlgorithm.getMSDAlgorithm(encryptAlgorithm));
			LogUtil.debug(context.getString(R.string.msg_term_track2_ciphertext) + (swipRslt.getSecondTrackData() == null ? null : ISOUtils.hexString(swipRslt.getSecondTrackData())), getClass());
		}
		String data55 = emvModule.fetchEmvData(L_55TAGS);
		LogUtil.debug(context.getString(R.string.msg_term_55tag) + data55, getClass());
		AppConfig.EMV.ic55Data = data55;// Trans info
		String dataScript = emvModule.fetchEmvData(L_SCRIPTTAGS);
		LogUtil.debug(context.getString(R.string.msg_term_script) + dataScript, getClass());
		String revData = emvModule.fetchEmvData(L_REVTAGS);
		LogUtil.debug(context.getString(R.string.msg_term_flushes_data) + revData, getClass());

		if (emvTransInfo.getOpenCardType() == ModuleType.COMMON_RFCARDREADER && transType != InnerProcessingCode.RF_EC_APPOINTED_LOAD && transType != InnerProcessingCode.RF_EC_NOT_APPOINTED_LOAD && transType != InnerProcessingCode.RF_EC_CASH_LOAD ) {
			if(emvTransInfo.getKernelId()== EmvConst.KERNEL_ID_UNIONPAY){
				//todo pin input
			}else{
				if(emvTransInfo.getCvm()== EmvConst.OP_ONLINE_PIN){// * NO CVM:0x00; OBTAIN SIGNATURE:0x10; ONLINE PIN:0x20;CONFIRMATION CODE VERIFIED:0x30;
					// todo pin input
				}
			}
			// [step1]：get ic card data from emvTransInfo then send to host
			// TODO Online transaction ....

 			// TODO  模拟联网操作的界面和耗时操作
			simulOnlineTrans(()->controller.doEmvFinish(true));

			// [step2].Online transaction failed or connectionless transaction End of the process，and return the result by calling onemvfinished.
			 //controller.doEmvFinish(true);// .Online transaction success
			// controller.doEmvFinish(false);Online transaction failed

		} else {
			// [step1]：get ic card data from emvTransInfo then send to host
			// TODO Online transaction ....
			SecondIssuanceRequest request = new SecondIssuanceRequest();
			request.setAuthorisationResponseCode("00");// 0x8a Transaction reply code:Taken from the 39 field value of unionpay 8583 specification, this parameter is populated with the actual value of the transaction.
			// request.setAuthorisationCode("504343");//0x89 Authorization code
			// request.setField55(ISOUtils.hex2byte("910A0B8B433AFD5C54F53030"));// 55 filed data of 8583 message
			//[Step2].Online transaction success or credit for load wait onemvfinished callback to end porcess after calling secondIssuance.
			//controller.secondIssuance(request);

			//TODO 模拟联网操作的界面和操作
			simulOnlineTrans(()->controller.secondIssuance(request));
		}
	}

	/**
	 * 模拟联网操作的界面和耗时操作
	 * @param runnable
	 */
	private void simulOnlineTrans(Runnable runnable){
		CardPayActivity.onlineHandler.sendMessage(new Message());
		//模拟耗时操作
		new Timer().schedule(new TimerTask() {
			public void run() {
				runnable.run();
				this.cancel();
			}
		}, 2600);
	}



	//Multi-application card will callback this method for  select application
	@Override
	public void onRequestSelectApplication(EmvTransController arg0, EmvTransInfo arg1) throws Exception {
		LogUtil.debug(context.getString(R.string.msg_select_app_hint), getClass());
		Map<byte[], AIDSelect> map = arg1.getAidSelectMap();
		List<String> nameList = new ArrayList<String>();
		List<byte[]> aidList = new ArrayList<byte[]>();

		for (Entry<byte[], AIDSelect> entry : map.entrySet()) {
			nameList.add(entry.getValue().getName());
			aidList.add(entry.getValue().getAid());
			LogUtil.debug(context.getString(R.string.msg_aid_name) + entry.getValue().getName(), getClass());
			LogUtil.debug(context.getString(R.string.msg_aid) + entry.getValue().getAid(), getClass());
		}
		// Select the first application by default
		arg0.selectApplication(aidList.get(0));
	}

	@Override
	public void onRequestTransferConfirm(EmvTransController controller, EmvTransInfo arg1) throws Exception {
		LogUtil.debug(context.getString(R.string.msg_trans_confirm_finish) , getClass());
		controller.transferConfirm(true);
	}

	// IM81 and N900 would call back，ME30 and ME31 would not call back.
	@Override
	public void onRequestPinEntry(final EmvTransController emvTransController, EmvTransInfo emvTransInfo) throws Exception {
		if (emvTransInfo.getCardNo() != null) {
			AppConfig.EMV.icCardNum = emvTransInfo.getCardNo();
			AppConfig.EMV.emvTransInfo = emvTransInfo;
		} else {
			LogUtil.debug(context.getString(R.string.msg_cardno_null), getClass());
		}
		if (emvTransInfo.getCardNo() != null) {
			LogUtil.debug(context.getString(R.string.msg_enter_6_pwd), getClass());
			if (emvTransInfo.getEmvPinInputType() == EmvPinInputType.OFFLINE || emvTransInfo.getEmvPinInputType() == EmvPinInputType.OFFLINE_ONLY) {
				doPinInput(false,emvTransInfo);
			}else{
				doPinInput(true,emvTransInfo);
			}
			
			waitPinInputThreat.waitForRslt();
			LogUtil.debug(context.getString(R.string.msg_pwd) + (pinBlock == null ? "null" : ISOUtils.hexString(pinBlock)), getClass());
			emvTransController.sendPinInputResult(pinBlock);
			//传递密码数据
            resultIntent.putExtra("password",pinBlock);
		} else {
			LogUtil.debug(context.getString(R.string.msg_swipresult_null), getClass());
		}
	}

	/**
	 * input password
	 * @param isOnline is it online pin?
	 * @param emvTransInfo emvTransInfo
	 * @throws Exception
	 */
	public void doPinInput(boolean isOnline, EmvTransInfo emvTransInfo) throws Exception {
		if(isOnline){
			LogUtil.debug("44444444444"+"\r\n",getClass());
			((CardPayActivity)context).startOnlinePinInput(emvTransInfo.getCardNo());
		}else{
			//((MainActivity)context).startOfflinePinInput(emvTransInfo.getModulus(),emvTransInfo.getExponent());
		}
	}

	/**
	 * Whether to intercept acctType select event
	 * @return
	 */
	@Override
	public boolean isAccountTypeSelectInterceptor() {
		return true;
	}

	/**
	 * Whether to intercept the cardHolder certificated confirmation  event
	 * @return
	 */
	@Override
	public boolean isCardHolderCertConfirmInterceptor() {
		return true;
	}

	/**
	 *  whether intercept electron cash confirmation event
	 * 
	 * @return
	 */
	@Override
	public boolean isEcSwitchInterceptor() {
		return true;
	}

	/**
	 * Whether intercept to use  external sequence number processor
	 * @return
	 */
	@Override
	public boolean isTransferSequenceGenerateInterceptor() {
		return true;
	}

	/**
	 * Whether intercept to show message on LCD event
	 * 
	 * @return
	 */
	@Override
	public boolean isLCDMsgInterceptor() {
		return true;
	}


	/**
	 *  account type selection
	 *  <p> 
	 *  return to int range
	 * <p>
	 * <ol>
	 * <li>default</li>
	 * <li>savings</li>
	 * <li>Cheque/debit</li>
	 * <li>Credit</li>
	 * </ol>
	 * 
	 *  @return 1-4:selection range， －1：failed
	 */
	@Override
	public int accTypeSelect() {
		return 1;
	}

	/**
	 *  cardHolder certificated confirmation
	 *  <p>
	 * 
	 *  @return true:confirmation succeed， false:confirmation failed
	 */
	@Override
	public boolean cardHolderCertConfirm(EmvCardholderCertType certType, String certno) {
		return true;
	}

	/**
	 *  Ecash /emv selection
	 *  <p>
	 *  transaction return：
	 *  <p>
	 *  <ul>
	 *  <li>1:continue electronic cash transactions</li>
	 *  <li>0:do not carry out electronic cash transactions</li>
	 *  <li>－1:user termination</li>
	 *  <li>－3:time out</li>
	 *  </ul>
	 */
	@Override
	public int ecSwitch() {
		try {
			final WaitThreat waitThreat = new WaitThreat();
			final Builder builder = new Builder(context);
			builder.setMessage(context.getString(R.string.msg_is_use_ecash));
			builder.setPositiveButton(context.getString(R.string.common_yes), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					AppConfig.EMV.isECSwitch = 1;
					dialog.dismiss();
					isECSwitch = 1;
					waitThreat.notifyThread();
				}
			});
			builder.setNegativeButton(context.getString(R.string.common_no), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					AppConfig.EMV.isECSwitch = 0;
					dialog.dismiss();
					isECSwitch = 0;
					waitThreat.notifyThread();
				}
			});
			((MainActivity)context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					builder.setCancelable(false);
					builder.show();
				}
			});
			waitThreat.waitForRslt();
			// electronic cash return 1,otherwise return 0
			return isECSwitch;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	/**
	 * serial number Add 1 and return
	 * 
	 * @return
	 */
	@Override
	public int incTsc() {
		return 0;
	}

	@Override
	public boolean isLanguageselectInterceptor() {
		return true;
	}

	/**
	 *  display info
	 * 
	 *  @param title
	 *             title
	 *  @param msg
	 *             message
	 *  @param yesnoShowed
	 *             whether show yes no
	 *  @param waittingTime
	 *             waiting time
	 *  @return if yesnoShow is equal to true, return 1 means confirmation.
	 *  		return 0 means cancel.
	 *  		if yesnoShow is equal to false,return value has no meaning.
	 */
	@Override
	public int lcdMsg(String title, String msg, boolean yesnoShowed, int waittingTime) {
		return 1;
	}

	@Override
	public byte[] languageSelect(byte[] language, int len) {
		if(len>=2){
			return new byte[]{language[0],language[1]};
		}
		return null;
	}

	// thread wait 、awake
	public static class WaitThreat {
		Object syncObj = new Object();

		void waitForRslt() throws InterruptedException {
			synchronized (syncObj) {
				syncObj.wait();
			}
		}

		void notifyThread() {
			synchronized (syncObj) {
				syncObj.notify();
			}
		}
	}

	/**
	 * 如果输入金额为空，会出现这个提示弹窗继续输入金额
	 * @param controller
	 * @param emvTransInfo
	 */
	@Override
	public void onRequestAmountEntry(final EmvTransController controller, EmvTransInfo emvTransInfo) {
		LogUtil.debug(context.getString(R.string.msg_money_callback_request), getClass());
		//		DialogUtils.createCustomDialog(context, context.getString(R.string.msg_enter_preauto_money), null, R.layout.dialog_amtinput, new DialogUtils.CustomDialogCallback() {
//			@Override
//			public void onResult(int id, View dialogView) {
//				if(id == 0){//sure
//					Editable editable = ((EditText) dialogView.findViewById(R.id.edit_amt_input)).getText();
//					if (editable.toString().equals("") || editable.toString() == null) {
//						LogUtil.debug(context.getString(R.string.msg_preauth_money_null), getClass());
//						controller.sendAmtInputResult(null);
//					} else {
//						DecimalFormat df = new DecimalFormat("#.00");
//						BigDecimal amt = new BigDecimal(editable.toString());
//						AppConfig.EMV.amt = amt;
//						LogUtil.debug(context.getString(R.string.msg_preauth_money) + df.format(amt), getClass());
//						controller.sendAmtInputResult(amt);
//					}
//				}else if(id == -1){//cancel
//					LogUtil.debug(context.getString(R.string.msg_trans_cancel), getClass());
//					controller.sendAmtInputResult(null); 	//When the amount of pre-authorization is empty ,it means to cancel the transaction.
//				}
//			}
//		});
	}

	@Override
	public void onFinalAppSelect(EmvTransController emvTransController, EmvTransInfo context) throws Exception {
		LogUtil.debug("onFinalAppSelect", getClass());
//		emvModule.setEmvData(0x9F02, ISOUtils.hex2byte("000000000800"));//You can set emv kernel data in this step
		emvTransController.transferConfirm(true);
	}

	private static Handler pinEventHandler = new Handler(Looper.getMainLooper()) {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case AppConfig.EMV.PIN_FINISH:
					if (msg.obj != null) {
						pinBlock = (byte[]) msg.obj;
					}
					waitPinInputThreat.notifyThread();
					break;
				default:
					break;
			}
		}

	};

	public static Handler getPinEventHandler() {
		return pinEventHandler;
	}

	public static void setPinEventHandler(Handler pinEventHandler) {
		SimpleTransferListener.pinEventHandler = pinEventHandler;
	}


}
