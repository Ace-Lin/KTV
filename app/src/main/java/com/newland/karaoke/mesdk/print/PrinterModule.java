package com.newland.karaoke.mesdk.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.newland.karaoke.R;
import com.newland.karaoke.mesdk.BaseModule;
import com.newland.karaoke.mesdk.device.SDKDevice;
import com.newland.karaoke.utils.LogUtil;
import com.newland.mtype.module.common.printer.PrintContext;
import com.newland.mtype.module.common.printer.Printer;
import com.newland.mtype.module.common.printer.PrinterResult;
import com.newland.mtype.module.common.printer.PrinterStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Author by bxy, Date on 2019/5/11 0011.
 */
public class PrinterModule extends BaseModule {

    private static final int INDEX_PRINTER_STATE = 1;
    private static final int INDEX_PRINT_BILL = 2;
    private static final int INDEX_PRINT_TTF = 3;

    public PrinterModule(Context context) {
        super(context);
    }



    public void getPrinterState(){
        Printer printer = SDKDevice.getInstance().getPrinter();
        try {
            LogUtil.debug(context.getString(R.string.msg_printer_status) + printer.getStatus(), getClass());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.debug(context.getString(R.string.msg_get_printer_status_error) + e, getClass());
        }
    }


    public void printBill(){

        Printer printer = SDKDevice.getInstance().getPrinter();
        if (printer.getStatus() != PrinterStatus.NORMAL) {
            LogUtil.debug(context.getString(R.string.msg_print_error_and_printer_status_abnormal), getClass());
        } else {
            try {
                // ------------------------------------------------------------
                // 备注：脚本规则请参考压缩资料包 doc目录下的《脚本打印命令规范-TTF.pdf》！！！
                // ------------------------------------------------------------
                Map<String, Bitmap> map = new HashMap<String, Bitmap>();
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_npt);
                Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.sign);
                map.put("logo", bitmap);
                map.put("sign", bitmap1);
                LogUtil.debug(context.getString(R.string.msg_start_print_bill), getClass());
                StringBuffer scriptBuffer = new StringBuffer();
                String fontsPath = printer.getFontsPath(context, "simsun.ttc", true);
                if(fontsPath!=null){
                    scriptBuffer.append("!font "+fontsPath+"\n");//设置字体
                }
                scriptBuffer.append("*image l 370*80 path:");
                scriptBuffer.append("logo\n");
                scriptBuffer.append("!hz l\n !asc l\n !gray 5\n");// 设置标题字体为大号
                scriptBuffer.append("!yspace 5\n");// 设置行间距,取值【0,60】，默认6
                scriptBuffer.append("*text l ++++++++++++++ X ++++++++\n");
                scriptBuffer.append("*text c 签购单\n");
                scriptBuffer.append("!hz n\n !asc n\n !gray 5\n");// 设置内容字体为中号
                scriptBuffer.append("!yspace 10\n");// 设置内容行间距
                scriptBuffer.append("*line" + "\n");// 打印虚线
                scriptBuffer.append("*text l 商户存根/MERCHANT COPY\n");
                scriptBuffer.append("*line" + "\n");// 打印虚线
                scriptBuffer.append("*text l 商户名称:比亚迪1\n");
                scriptBuffer.append("*text l 商户编号:123455432112345\n");
                scriptBuffer.append("*text l 终端编号:20130717\n");
                scriptBuffer.append("*line" + "\n");// 打印虚线
                scriptBuffer.append("*text l 卡别:工商银行\n");
                scriptBuffer.append("*text l 卡号:\n");
                scriptBuffer.append("*text l 621226*********1973\n");
                scriptBuffer.append("*text l 交易类型:消费/SALE\n");
                scriptBuffer.append("*text l 凭证号:000014\n");
                scriptBuffer.append("*text l 参考号:000029279932\n");
                scriptBuffer.append("*text l 授权码:867785\n");
                scriptBuffer.append("*text l 批次号:000001\n");
                scriptBuffer.append("*text l 票据号:000001\n");
                scriptBuffer.append("*text l 日期时间:2015/12/25 11:41:06\n");
                scriptBuffer.append("*text l 卡 组 织:内卡\n");
                scriptBuffer.append("*text l 金 额:RMB 1.16\n");
                scriptBuffer.append("*text l 操作员号:001\n");
                scriptBuffer.append("*text l 程序版本:1.0.18\n");
                scriptBuffer.append("*text l 程序版本:1.0.18\n");
                scriptBuffer.append("*text l ARQC:83FE10558FF2C85F\n");
                scriptBuffer.append("*text l AID:A000000333010101\n");
                scriptBuffer.append("*text l CSN:001 CVM:020301\n");
                scriptBuffer.append("*text l 持卡人签名:\n");
                scriptBuffer.append("*image l 144*256 path:");
                scriptBuffer.append("sign\n");
                scriptBuffer.append("*text l 本人确认以上交易，同意将其记入本卡账户\n");
                scriptBuffer.append("*underline l 服务热线:888888888\n");
                scriptBuffer.append("!barcode 2 72\n *barcode c 12345678901234567890\n");
                scriptBuffer.append("*feedline 2\n");
                scriptBuffer.append("!qrcode 200 2\n *qrcode c 12345678901234567890\n");
                scriptBuffer.append("*feedline 2\n");
                scriptBuffer.append("!NLFONT 4 10 3\n");
                scriptBuffer.append("*text l ++++++++++++++ X ++++++++++++++ \n");
                scriptBuffer.append("!NLPRNOVER\n");
                PrinterResult printerResult = printer.printByScript(PrintContext.defaultContext(),
                        scriptBuffer.toString(), map, 60, TimeUnit.SECONDS);
                if (printerResult.equals(PrinterResult.SUCCESS)) {
                    LogUtil.debug(context.getString(R.string.msg_print_script_success), getClass());
                } else {
                    LogUtil.debug(context.getString(R.string.msg_print_script_error) + printerResult.toString(), getClass());
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.debug(context.getString(R.string.msg_print_script_error) + e, getClass());
            }

        }

    }

    public void printTTF(){

        Printer printer = SDKDevice.getInstance().getPrinter();
        if (printer.getStatus() != PrinterStatus.NORMAL ) {
            LogUtil.debug(context.getString(R.string.msg_print_error_and_printer_status_abnormal), getClass());
        } else {
            try {
                // ------------------------------------------------------------
                // 备注：脚本规则请参考压缩资料包 doc目录下的《脚本打印命令规范-TTF.pdf》！！！
                // ------------------------------------------------------------
                StringBuffer scriptBuffer = new StringBuffer();
                String fontsPath = printer.getFontsPath(context, "simsun.ttc", true);
                if(fontsPath!=null){
                    scriptBuffer.append("!font "+fontsPath+"\n");//设置字体
                }
                scriptBuffer.append("*line\n!gray 3\n!yspace 10\n");
                scriptBuffer.append("!hz s\n!asc s\n*text l 小号字体ABC,浓度3,间距10\n"); //中文、英文都设成s字体，居左打印
                scriptBuffer.append("*line\n!gray 6\n!yspace 15\n");
                scriptBuffer.append("!hz n\n!asc n\n*text c 标准字体ABC,浓度6,间距15\n"); //中文、英文都设成s字体，居中打印
                scriptBuffer.append("*line\n!gray 10\n!yspace 20\n");
                scriptBuffer.append("!hz l\n!asc l\n*text r 大字体ABC,浓度10,间距20\n");//中文、英文都设成s字体，居右打印
                scriptBuffer.append("*line\n!gray 6\n!yspace 10\n");
                scriptBuffer.append("!hz sn\n!asc sn\n*underline l 小字体宽度,标准字体高度ABC,浓度6\n");//中文、英文都设成s字体，居左打印，加下划线
                scriptBuffer.append("!hz sl\n!asc sl\n*underline c 小字体宽度，大字体高度ABC，浓度6\n");//中文、英文都设成s字体，居中打印,加下划线
                scriptBuffer.append("!hz nl\n!asc nl\n*underline r 标准字体宽度，大字体高度ABC，浓度6\n");//中文、英文都设成s字体，居右打印,加下划线
                scriptBuffer.append("*line\n!gray 6\n!yspace 15\n");
                scriptBuffer.append("!barcode 1 96\n*barcode l 123456712345678888\n");//条码宽度1，高度96，居左打印
                scriptBuffer.append("!NLPRNOVER\n");
                scriptBuffer.append("!barcode 2 72\n*barcode c 123456712345678888\n");//条码宽度2，高度120，居中打印
                scriptBuffer.append("!NLPRNOVER\n");
                scriptBuffer.append("!barcode 2 160\n*barcode r 123456712345678888\n");//条码宽度2，高度160，居右打印
                scriptBuffer.append("*line\n!gray 6\n!yspace 15\n");
                scriptBuffer.append("!qrcode 100 2\n*qrcode l ABCDEFG\n");//二维码高度100，纠错级别2，居左打印
                scriptBuffer.append("!NLPRNOVER\n");
                scriptBuffer.append("!qrcode 200 3\n*qrcode c ABCDEFGH\n");//二维码高度200，纠错级别3，居中打印
                scriptBuffer.append("!NLPRNOVER\n");
                scriptBuffer.append("!qrcode 300 1\n*qrcode r ABCDEFGHJ\n");//二维码高度300，纠错级别1，居右打印

                Map<String, Bitmap> map = new HashMap<String, Bitmap>();
                Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.fffffffff);
                String bmp0 = "bmp0",bmp1 = "bmp1",bmp2 = "bmp2",bmp3 = "bmp3",bmp4 = "bmp4",bmp5 = "bmp5";
                map.put(bmp0, bitmap1);
                map.put(bmp1, bitmap1);
                map.put(bmp2, bitmap1);
                map.put(bmp3, bitmap1);
                map.put(bmp4, bitmap1);
                map.put(bmp5, bitmap1);
                scriptBuffer.append("*line\n!yspace 5\n");
                scriptBuffer.append("*image l 200*200 path:"+bmp0+"\n");//使用大津阈值法，居左打印
                scriptBuffer.append("*image l 200*200 path:yz:240;"+bmp1+"\n");//设置固定阈值为128，居中打印 [1-254]
                scriptBuffer.append("*image l 200*200 path:yz:128;"+bmp2+"\n");//设置固定阈值为128，居中打印 [1-254]
                scriptBuffer.append("*image l 200*200 path:yz:68;"+bmp3+"\n");//设置固定阈值为128，居中打印 [1-254]
                scriptBuffer.append("*image l 200*200 path:yz:0;"+bmp4+"\n");//使用WellnerAdaptiveThreshold阈值算法，居右打印

                scriptBuffer.append("*line\n!yspace 50\n");
                scriptBuffer.append("!NLFONT 2 2 0\n*TEXT l 00ll国\n!NLFONT 2 2 3\n*text r 横向、纵向2倍放大\n");//横向、纵向2倍放大
                scriptBuffer.append("!NLFONT 2 2 1\n*TEXT l 00ll国\n!NLFONT 2 2 3\n*text r 横向2倍放大，纵向正常\n");//横向2倍放大，纵向正常
                scriptBuffer.append("!NLFONT 2 2 2\n*TEXT l 00ll国\n!NLFONT 2 2 3\n*text r 横向正常，纵向2倍放大\n");//横向正常，纵向2倍放大
                scriptBuffer.append("!NLFONT 2 2 3\n*TEXT l 00ll国\n!NLFONT 2 2 3\n*text r 横向、纵向正常\n");//横向、纵向正常
                scriptBuffer.append("!NLFONT 2 2 4\n*TEXT l 00ll国\n!NLFONT 2 2 3\n*text r 横向、纵向3倍放大\n");//横向、纵向3倍放大
                scriptBuffer.append("!NLFONT 2 2 5\n*TEXT l 00ll国\n!NLFONT 2 2 3\n*text r 横向3倍放大、纵向正常\n");//横向3倍放大、纵向正常
                scriptBuffer.append("!NLFONT 2 2 6\n*TEXT l 00ll国\n!NLFONT 2 2 3\n*text r 横向正常，纵向3倍放大\n");//横向正常，纵向3倍放大

                scriptBuffer.append("*line\n!gray 6\n!yspace 20\n");

                scriptBuffer.append("!NLFONT 2 2 3\n*TEXT l 宋体常规1A\n");//设置汉字字体为24x24,英文字体为16x16,横向、纵向正常，居左打印，不会立即打印，待后面两条打印命令处理完毕，会打印在同一行
                fontsPath = printer.getFontsPath(context, "DroidSansFallback.ttf", false);
                if(fontsPath!=null){
                    scriptBuffer.append("!font "+fontsPath+"\n");//设置字体
                }
                scriptBuffer.append("!NLFONT 1 1 6\n*UNDERLINE c 默认\n");//设置汉字字体16x16,英文字体12x12,横向正常、纵向3倍放大，居中打印，带下划线，不会立即打印
                scriptBuffer.append("!NLFONT 2 2 0\n*text r 字体1A\n");//设置汉字字体24x24,英文字体16x16,横向、纵向2倍放大，居右打印。连同上面两条命令的数据一起打印在同一行。

                fontsPath = printer.getFontsPath(context, "simsun.ttc", true);
                if(fontsPath!=null){
                    scriptBuffer.append("!font "+fontsPath+"\n");//设置字体
                }

                scriptBuffer.append("*line\n!gray 6\n!yspace 20\n");
                scriptBuffer.append("!BARCODE 8 120 0 0\n*BARCODE c A123456789B\n");//CODABAR:第一位和最后一位为'A'、'B'、'C'、'D'中的任一位,中间为数字。
                scriptBuffer.append("!BARCODE 8 120 0 1\n*BARCODE c 1-.$/+% \n");//CODE39:只支持10个数字、26大写英文字母、7个特殊字符('-'、'.'、'$'、'/'、'+'、'%'、空格)共43个字符，能够对任意长度的数据进行编码。
                scriptBuffer.append("!BARCODE 8 120 0 2\n*BARCODE c 1BC-.$/56\n");//CODE93:只支持10个字数、26大写英文字母、7个特殊字符('-'、'.'、'$'、'/'、'+'、'%'、空格)。
                scriptBuffer.append("!BARCODE 8 120 0 3\n*BARCODE c ABC123456123\n");//CODE128:可表示从ASCII 0到ASCII 127共128个字符,其中包含了数字、大小写字母和符号字符。
                scriptBuffer.append("!BARCODE 8 120 1 4\n*BARCODE c 1234567\n");//EAN-8/JAN-8:只支持数字，长度固定8位，7位数据，1位校验位，数据不足7位会在前面补0。
                scriptBuffer.append("!BARCODE 8 120 1 4\n*BARCODE c 123456789\n");//EAN-13/JAN-13:只支持数字，长度固定13位，12位数据，1位校验位，数据不足12位会在前面补0。
                scriptBuffer.append("!BARCODE 8 120 0 5\n*BARCODE c 123456789123\n");//ITF:只支持数字，长度固定14位，13位数据，1位校验位，数据不足13位会在前面补0。
                scriptBuffer.append("!BARCODE 8 120 1 6\n*BARCODE c 12345678912\n");//UPC-A:只支持数字，长度固定12位，11位数据，1位校验位，数据不足11位会在前面补0。
                scriptBuffer.append("!BARCODE 8 120 1 7\n*BARCODE c 1123456\n");//UPC-E:只支持数字，长度固定8位，第一位是0或1，最后一位是校验位，中间6位是数据。

                scriptBuffer.append("!QRCODE 300 0 0\n*QRCODE c Test000:二维码打印测试\n");//DC(Data Matrix):可编码字元集包括全部的ASCII字元及扩充ASCII字元，共256个字元。
                scriptBuffer.append("!QRCODE 200 0 0\n*QRCODE c Test111:二维码打印测试\n");//DC(Data Matrix):可编码字元集包括全部的ASCII字元及扩充ASCII字元，共256个字元。
                scriptBuffer.append("!QRCODE 100 0 0\n*QRCODE c Test222:二维码打印测试\n");//DC(Data Matrix):可编码字元集包括全部的ASCII字元及扩充ASCII字元，共256个字元。
                scriptBuffer.append("!QRCODE 300 0 1\n*QRCODE c Test555:二维码打印测试\n");//同DC(Data Matrix)
                scriptBuffer.append("!QRCODE 300 0 2\n*QRCODE c Test666:二维码打印测试\n");//PDF-417:支持字母、数字,中文支持较差(约500个)。
                scriptBuffer.append("!QRCODE 300 0 3\n*QRCODE c Test777:二维码打印测试\n");//QR Code:中文支持好。


                scriptBuffer.append("*line\n!gray 6\n!yspace 15\n");
                scriptBuffer.append("!hz n\n!asc n\n*text c 以下是偏移量测试\n");
                scriptBuffer.append("!hz n\n!asc n\n*text x:50 x值为50\n");
                scriptBuffer.append("!hz n\n!asc n\n*underline x:60 x值为60\n");
                scriptBuffer.append("*feedline 1\n");
                scriptBuffer.append("!barcode 1 72\n*barcode x:70 100017986685631304\n");
                scriptBuffer.append("*feedline 1\n");
                scriptBuffer.append("!qrcode 50 2\n*qrcode x:80 ABC123456789DEFGH\n");
                scriptBuffer.append("*feedline 1\n");
                scriptBuffer.append("*image x:90 200*200 path:"+bmp5+"\n");

                scriptBuffer.append("!NLFONT 13 7 0\n*TEXT x:30 X:30\n");
                scriptBuffer.append("!NLFONT 1 2 3\n*UNDERLINE x:180 X:180\n");
                scriptBuffer.append("!NLFONT 6 2 3\n*text x:270 X:270\n");
                scriptBuffer.append("!BARCODE 6 120 0 0\n*BARCODE x:50 A1234567890A\n");
                scriptBuffer.append("!QRCODE 200 0 0\n*QRCODE x:20 Test123:二维码打印测试\n");
                scriptBuffer.append("!hz n\n!asc n\n*text c 以上是偏移量测试\n");
                scriptBuffer.append("*feedline 3\n");

                scriptBuffer.append("*line\n!gray 6\n!yspace 20\n");
                scriptBuffer.append("!NLFONT 1 1 3\n*TEXT l ABC大小\n!NLFONT 2 2 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 3 3 3\n*TEXT l ABC大小\n!NLFONT 4 4 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 5 5 3\n*TEXT l ABC大小\n!NLFONT 6 6 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 7 7 3\n*TEXT l ABC大小\n!NLFONT 8 8 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 9 9 3\n*TEXT l ABC大小\n!NLFONT 10 10 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 11 11 3\n*TEXT l ABC大小\n!NLFONT 12 12 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 13 13 3\n*TEXT l ABC大小\n!NLFONT 14 14 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 15 15 3\n*TEXT l ABC大小\n!NLFONT 16 16 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 17 17 3\n*TEXT l ABC大小\n!NLFONT 18 18 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 19 19 3\n*TEXT l ABC大小\n!NLFONT 20 20 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 21 21 3\n*TEXT l ABC大小\n!NLFONT 22 22 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 23 23 3\n*TEXT l ABC大小\n!NLFONT 23 24 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 25 25 3\n*TEXT l ABC大小\n!NLFONT 25 26 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 25 27 3\n*TEXT l ABC大小\n!NLFONT 25 28 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 25 29 3\n*TEXT l ABC大小\n!NLFONT 25 30 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 25 31 3\n*TEXT l ABC大小\n!NLFONT 25 32 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 25 33 3\n*TEXT l ABC大小\n!NLFONT 25 34 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 25 35 3\n*TEXT l ABC大小\n!NLFONT 25 36 3\n*text r ABC大小\n");
                scriptBuffer.append("!NLFONT 25 37 3\n*TEXT l ABC大小\n!NLFONT 25 38 3\n*text r ABC大小\n");
                scriptBuffer.append("*feedline 3\n");

//									scriptBuffer.append("*line\n*feedline l:1\n");
//									scriptBuffer.append("*line\n*feedline l:2\n");
//									scriptBuffer.append("*line\n*feedline p:24\n");
//									scriptBuffer.append("*line\n*feedline p:42\n");
//									scriptBuffer.append("*line\n");
//									scriptBuffer.append("*feedline 3\n");

                PrinterResult printerResult = printer.printByScript(PrintContext.defaultContext(),
                        scriptBuffer.toString(),map,60, TimeUnit.SECONDS);

                if (printerResult.equals(PrinterResult.SUCCESS)) {
                    LogUtil.debug(context.getString(R.string.msg_print_script_success), getClass());
                } else {
                    LogUtil.debug(context.getString(R.string.msg_print_script_error) + printerResult.toString(), getClass());
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.debug(context.getString(R.string.msg_print_script_error) + e, getClass());
            }
        }
    }

    public void printOrder(){

        Printer printer = SDKDevice.getInstance().getPrinter();
        if (printer.getStatus() != PrinterStatus.NORMAL) {
            LogUtil.debug(context.getString(R.string.msg_print_error_and_printer_status_abnormal), getClass());
        } else {
            try {
                // ------------------------------------------------------------
                // 备注：脚本规则请参考压缩资料包 doc目录下的《脚本打印命令规范-TTF.pdf》！！！
                // ------------------------------------------------------------
                Map<String, Bitmap> map = new HashMap<String, Bitmap>();
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_npt);
                Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.sign);
                map.put("logo", bitmap);
                map.put("sign", bitmap1);
                LogUtil.debug(context.getString(R.string.msg_start_print_bill), getClass());
                StringBuffer scriptBuffer = new StringBuffer();
                String fontsPath = printer.getFontsPath(context, "simsun.ttc", true);
                if(fontsPath!=null){
                    scriptBuffer.append("!font "+fontsPath+"\n");//设置字体
                }
                scriptBuffer.append("*image l 370*80 path:");
                scriptBuffer.append("logo\n");
                scriptBuffer.append("*text l ++++++++++++++ X ++++++++\n");
                //上面设置属性才能对下面的文本生效
                scriptBuffer.append("!hz l\n !asc l\n !gray 5\n");// 设置标题字体为大号
                scriptBuffer.append("!yspace 5\n");// 设置行间距,取值【0,60】，默认6
                scriptBuffer.append("*text c 签购单\n");
                scriptBuffer.append("!hz n\n !asc n\n !gray 5\n");// 设置内容字体为中号
                scriptBuffer.append("!yspace 10\n");// 设置内容行间距
                scriptBuffer.append("*text l 本人确认以上交易，同意将其记入本卡账户\n");
                scriptBuffer.append("!NLFONT 4 10 3\n");
                scriptBuffer.append("*text l ++++++++++++++ X ++++++++++++++ \n");
                //需要添加这一行才能完全打印出来
                scriptBuffer.append("!NLPRNOVER\n");
                PrinterResult printerResult = printer.printByScript(PrintContext.defaultContext(),
                        scriptBuffer.toString(), map, 60, TimeUnit.SECONDS);
                if (printerResult.equals(PrinterResult.SUCCESS)) {
                    LogUtil.debug(context.getString(R.string.msg_print_script_success), getClass());
                } else {
                    LogUtil.debug(context.getString(R.string.msg_print_script_error) + printerResult.toString(), getClass());
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.debug(context.getString(R.string.msg_print_script_error) + e, getClass());
            }

        }

    }



}
