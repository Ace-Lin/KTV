package com.newland.karaoke.database;

import org.litepal.crud.LitePalSupport;

public class KTVUserLogin extends LitePalSupport {
    private int id;
    private String user_account;
    private String user_password;
    private KTVUserInfo user_info;

    public KTVUserInfo getUser_info() {
        return user_info;
    }

    public void setUser_info(KTVUserInfo user_info) {
        this.user_info = user_info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}
