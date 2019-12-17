package com.example.milkyteamis.bean;

import com.example.milkyteamis.model.SoldData;

public class ResultBean_Linchart {
    private int status;
    private SoldData data;

    public ResultBean_Linchart(int status, SoldData data) {
        this.status = status;
        this.data = data;
    }

    public SoldData getData() {
        return data;
    }
}
