package com.example.milkyteamis.model;

import java.util.List;

public class ResultBean_Order_Detail {
    private int status;
    private List<Order> data;

    public ResultBean_Order_Detail(int status, List<Order> data) {
        this.status = status;
        this.data = data;
    }

    public List<Order> getData() {
        return data;
    }
}
