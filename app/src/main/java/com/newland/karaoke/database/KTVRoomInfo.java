package com.newland.karaoke.database;

import org.litepal.crud.LitePalSupport;

public class KTVRoomInfo extends LitePalSupport {
    private int id;
    private String room_type;
    private String room_status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getRoom_status() {
        return room_status;
    }

    public void setRoom_status(String room_status) {
        this.room_status = room_status;
    }
}
