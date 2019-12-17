package com.example.milkyteamis.bean;

import com.example.milkyteamis.model.Good;

public class ResultBean2 {
    private int status;
    private Good data;

    public ResultBean2(int status, Good data) {
        this.status = status;
        this.data = data;
    }

    public Good getData() {
        return data;
    }

}
