package com.newland.karaoke.model;

import com.newland.karaoke.database.KTVUserInfo;
import com.newland.karaoke.database.KTVUserLogin;

import java.util.Date;

public class UserModel {
    private int id ;
    private String user_name;
    private String identity_card_no;
    private String mobile_phone;
    private String user_email;
    private String user_photo;
    private int user_acount_id=-1;
    public int getUser_acount_id() {
        return user_acount_id;
    }



    public UserModel(KTVUserInfo userInfo,int user_acount_id){
        id=userInfo.getId();
        user_name=userInfo.getUser_name();
        identity_card_no=userInfo.getIdentity_card_no();
        mobile_phone=userInfo.getMobile_phone();
        user_email=userInfo.getUser_email();
        user_photo=userInfo.getUser_photo();
        this.user_acount_id=user_acount_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }
}
