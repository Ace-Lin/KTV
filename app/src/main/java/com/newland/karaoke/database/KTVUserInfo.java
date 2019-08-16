package com.newland.karaoke.database;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

public class KTVUserInfo extends LitePalSupport {

    private int id ;
    private KTVUserLogin user_id;
    private String user_name;
    private String identity_card_type;
    private String identity_card_no;
    private String mobile_phone;
    private String user_email;
    private Date register_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public KTVUserLogin getUser_id() {
        return user_id;
    }

    public void setUser_id(KTVUserLogin user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getIdentity_card_type() {
        return identity_card_type;
    }

    public void setIdentity_card_type(String identity_card_type) {
        this.identity_card_type = identity_card_type;
    }

    public String getIdentity_card_no() {
        return identity_card_no;
    }

    public void setIdentity_card_no(String identity_card_no) {
        this.identity_card_no = identity_card_no;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public Date getRegister_time() {
        return register_time;
    }

    public void setRegister_time(Date register_time) {
        this.register_time = register_time;
    }
}
