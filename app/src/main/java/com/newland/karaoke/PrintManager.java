package com.newland.karaoke;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.newland.karaoke.mesdk.device.SDKDevice;
import com.newland.karaoke.model.PrintModel;
import com.newland.karaoke.utils.ToastUtil;
import com.newland.mtype.module.common.printer.PrintContext;
import com.newland.mtype.module.common.printer.Printer;
import com.newland.mtype.module.common.printer.PrinterResult;
import com.newland.mtype.module.common.printer.PrinterStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PrintManager {
    private Printer printer;
    private Context context;
    private static PrintManager printManager;

    public PrintManager(Context context) {
        this.context = context;
        initData();
    }

    public static PrintManager getInstance(Context context){
        if(printManager == null){
            synchronized (PrintManager.class){
                if(printManager == null){
                    printManager = new PrintManager(context);
                }
            }
        }
        printManager.context=context;
        return printManager;
    }
    private void initData(){
        if(printer==null){
            printer=SDKDevice.getInstance().getPrinter();
        }
    }
    public void printBill(PrintModel printModel){
        if (printer.getStatus() != PrinterStatus.NORMAL) {
            ToastUtil.showLongText(context,context.getString(R.string.msg_print_error_and_printer_status_abnormal));
        } else {
            try {
                Map<String, Bitmap> map = new HashMap<String, Bitmap>();
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.print_logo);
                map.put("logo", bitmap);
                //ToastUtil.showLongText(context,context.getString(R.string.msg_start_print_bill));
                StringBuffer scriptBuffer = new StringBuffer();
                String fontsPath = printer.getFontsPath(context, "simsun.ttc", true);
                if(fontsPath!=null){
                    scriptBuffer.append("!font "+fontsPath+"\n");//设置字体
                }
                scriptBuffer.append("*image l 370*80 path:");
                scriptBuffer.append("logo\n");
                scriptBuffer.append("!hz l\n !asc l\n !gray 5\n");// 设置标题字体为大号
                scriptBuffer.append("!yspace 5\n");// 设置行间距,取值【0,60】，默认6
                scriptBuffer.append("*line" + "\n");// 打印虚线
                scriptBuffer.append("*text c PAYMENT RECEIPT\n");
                scriptBuffer.append("*line" + "\n");// 打印虚线
                scriptBuffer.append("!hz n\n !asc n\n !gray 5\n");// 设置内容字体为中号
                scriptBuffer.append("!yspace 10\n");// 设置内容行间距
                scriptBuffer.append("*text l Bill No:"+printModel.getOrderNum()+"\n");
                scriptBuffer.append("*text l Date:"+printModel.getDateToString()+"\n");
                scriptBuffer.append("*text l Amout:"+printModel.getAmount()+"\n");
                scriptBuffer.append("*text l Pay Type:"+printModel.getPay_typeToString()+"\n");
                scriptBuffer.append("*text l Room Name:"+printModel.getRoomToString()+"\n");
                scriptBuffer.append("!hz l\n !asc l\n !gray 5\n");// 设置标题字体为大号
                scriptBuffer.append("!yspace 5\n");// 设置行间距,取值【0,60】，默认6
                scriptBuffer.append("*line" + "\n");// 打印虚线
                scriptBuffer.append("!NLPRNOVER\n");
                PrinterResult printerResult = printer.printByScript(PrintContext.defaultContext(),
                        scriptBuffer.toString(), map, 60, TimeUnit.SECONDS);
                if (printerResult.equals(PrinterResult.SUCCESS)) {
                    ToastUtil.showLongText(context,context.getString(R.string.msg_print_script_success));
                } else {
                    ToastUtil.showLongText(context,context.getString(R.string.msg_print_script_error));

                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtil.showLongText(context,context.getString(R.string.msg_print_script_error));
            }

        }

    }









}
