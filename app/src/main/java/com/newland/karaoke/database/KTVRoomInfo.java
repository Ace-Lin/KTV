package com.newland.karaoke.database;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class KTVRoomInfo extends LitePalSupport {
    private int id;
    private String room_name;
    private int room_type;
    private int room_status;
    private double room_price;
    private List<KTVOrderInfo> product = new ArrayList<KTVOrderInfo>();

    public double getRoom_price() {
        return room_price;
    }

    public void setRoom_price(double room_price) {
        this.room_price = room_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public int getRoom_type() {
        return room_type;
    }

    public void setRoom_type(int room_type) {
        this.room_type = room_type;
    }

    public int getRoom_status() {
        return room_status;
    }

    public void setRoom_status(int room_status) {
        this.room_status = room_status;
    }

    public List<KTVOrderInfo> getProduct() {
        return product;
    }

    public void setProduct(List<KTVOrderInfo> product) {
        this.product = product;
    }

    @Override
    public String toString()
    {
        return "KTVRoomInfo{" +
                "id=" + id +
                " 房间名字=" + room_name +
                " 房间数量=" + room_type +
                ", 房间价格=" + room_price + '\'' +
                '}';
    }
}
