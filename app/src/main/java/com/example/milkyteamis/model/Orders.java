package com.example.milkyteamis.model;

//订单表
public class Orders {
    //订单id
    private int id;
    //操作人id
    private int soldUserId;
    //标识订单状态
    private int state;
    //订单总价格
    private double total_price;

    public Orders(int id, int soldUserId, int state, double total_price) {
        this.id = id;
        this.soldUserId = soldUserId;
        this.state = state;
        this.total_price = total_price;
    }

    public Orders(){
        this(0,0,0,0.0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoldUserId() {
        return soldUserId;
    }

    public void setSoldUserId(int soldUserId) {
        this.soldUserId = soldUserId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
}
