package com.example.milkyteamis.model;

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
