package com.example.milkyteamis.bean;

import com.example.milkyteamis.model.Orders;

import java.util.List;

public class ResultBean_Order {
    private int status;
    private List<Orders> data;

    public ResultBean_Order(int status, List<Orders> data) {
        this.status = status;
        this.data = data;
    }

    public List<Orders> getData() {
        return data;
    }
}
