package com.newland.karaoke.database;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class KTVOrderInfo extends LitePalSupport {
    private int id;
    private KTVRoomInfo room_id;
    private  String order_start_time;
    private  String order_end_time;
    private List<KTVOrderProduct> productList = new ArrayList<KTVOrderProduct>();
    private int order_pay_type;
    private double pay_amount;
    private int order_status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public KTVRoomInfo getRoom_id() {
        return room_id;
    }

    public void setRoom_id(KTVRoomInfo room_id) {
        this.room_id = room_id;
    }

    public String getOrder_start_time() {
        return order_start_time;
    }

    public void setOrder_start_time(String order_start_time) {
        this.order_start_time = order_start_time;
    }

    public String getOrder_end_time() {
        return order_end_time;
    }

    public void setOrder_end_time(String order_end_time) {
        this.order_end_time = order_end_time;
    }


    public List<KTVOrderProduct> getProductList() {

         String linkId=this.getClass().getSimpleName().toLowerCase();
         return LitePal.where(linkId+"_id = ?", String.valueOf(id)).find(KTVOrderProduct.class);
    }

    public void setProductList(List<KTVOrderProduct> productList) {
        this.productList = productList;
    }

    public int getOrder_pay_type() {
        return order_pay_type;
    }

    public void setOrder_pay_type(int order_pay_type) {
        this.order_pay_type = order_pay_type;
    }

    public double getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(double pay_amount) {
        this.pay_amount = pay_amount;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    @Override
    public String toString()
    {
        return "KTVOrderInfo{" +
                "id=" + id +
                " 下单时间=" + order_start_time +
                " 支付时间=" + order_end_time +
                " 支付类型=" + order_pay_type +
                " 支付金额=" + pay_amount +
                " 支付状态=" + order_status +
                ", 商品列表=" + getProductList().toString() + '\'' +
                '}';
    }

}
