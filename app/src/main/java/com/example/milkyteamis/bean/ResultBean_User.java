package com.example.milkyteamis.bean;

import com.example.milkyteamis.model.User;

public class ResultBean_User {
    private int status;
    private User data;

    public ResultBean_User(int status, User user) {
        this.status = status;
        this.data = user;
    }

    public User getData() {
        return data;
    }
}
