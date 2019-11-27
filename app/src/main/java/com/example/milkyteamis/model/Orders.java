package com.example.milkyteamis.model;

import java.sql.Timestamp;
import java.util.List;

//订单表
public class Orders {
    //订单id
    private int id;
    //操作人id
    private int userId;
    //标识订单状态
    private int state;
    //订单创建时间
    private Timestamp createTime;
    //订单总价格
    private double totalPrice;
    //订单商品详情
    private List<Order> orderGoods;

    public Orders(int id, int soldUserId, int state, Timestamp createTime, double total_price, List<Order> orderGoods) {
        this.id = id;
        this.userId = soldUserId;
        this.state = state;
        this.createTime = createTime;
        this.totalPrice = total_price;
        this.orderGoods = orderGoods;
    }

    public Orders(){
        this(0,0,0,null,0,null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoldUserId() {
        return userId;
    }

    public void setSoldUserId(int soldUserId) {
        this.userId = soldUserId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getTotal_price() {
        return totalPrice;
    }

    public void setTotal_price(double total_price) {
        this.totalPrice = total_price;
    }

    public List<Order> getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(List<Order> orderGoods) {
        this.orderGoods = orderGoods;
    }
}
