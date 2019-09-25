package com.newland.karaoke.model;

import com.newland.karaoke.constant.KTVType;
import com.newland.karaoke.database.KTVOrderInfo;
import com.newland.karaoke.database.KTVRoomInfo;
import com.newland.karaoke.mesdk.device.SDKDevice;
import com.newland.mtype.module.common.printer.Printer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintModel {
    private String orderNum;
    private Date date;
    private double amount;
    private int pay_type;
    private KTVRoomInfo roomId;
    PrintModel(){

    }
    public PrintModel(KTVOrderInfo order){
        orderNum=order.getOrder_number();
        date=order.getOrder_end_time();
        amount=order.getPay_amount();
        pay_type=order.getOrder_pay_type();
        roomId=order.getRoom_id();
    }

    public String getOrderNum() {
        return orderNum;
    }

    public Date getDate() {
        return date;
    }
    public String getDateToString() {
        SimpleDateFormat formatManager=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String temp=formatManager.format(date);
        return temp;
    }

    public double getAmount() {
        return amount;
    }

    public int getPay_type() {
        return pay_type;
    }
    public String getPay_typeToString() {
        String pay="cash";
        switch(pay_type){
            case KTVType.PayType.CARD:
                pay="Card";
                break;
            case KTVType.PayType.CASH:
                pay="Cash";
                break;
            case KTVType.PayType.QRCODE:
                pay="QRCode";
                break;
                default:
                    break;
        }
        return pay;
    }


    public KTVRoomInfo getRoomId() {
        return roomId;
    }
    public String getRoomToString() {
        String room="";
        if(roomId!=null){
            room=roomId.getRoom_name();
        }
        return room;
    }

    public static class Builder{
        private String bld_order;
        private Date bld_date;
        private double bld_amount;
        private int bld_pay_type;
        private KTVRoomInfo bld_roomId;
        public Builder(){
            bld_order="";
            bld_amount=0;
            bld_date=new Date();
            bld_pay_type=KTVType.PayType.CASH;
        }
        public Builder SetOrderNo(String bld_order){
            this.bld_order=bld_order;
            return this;
        }
        public Builder SetDate(Date bld_date){
            this.bld_date=bld_date;
            return this;
        }
        public Builder SetAmount(double bld_amount){
            this.bld_amount=bld_amount;
            return this;
        }
        public Builder SetPayType(int bld_pay_type){
            this.bld_pay_type=bld_pay_type;
            return this;
        }
        public Builder SetRoomId(KTVRoomInfo bld_roomId){
            this.bld_roomId=bld_roomId;
            return this;
        }
        private void Apply(PrintModel printModel){
            printModel.orderNum=bld_order;
            printModel.date=bld_date;
            printModel.amount=bld_amount;
            printModel.pay_type=bld_pay_type;
            printModel.roomId=bld_roomId;
        }
        public PrintModel Create(){
            PrintModel model=new PrintModel();
            Apply(model);
            return model;
        }


    }




}
