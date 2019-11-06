package com.example.milkyteamis.model;

//该表为订单商品表
public class Order {
    //标识订单id
    private int id;
    //订单经手人
    private int user_id;
    //商品名称
    private String goodName;
    //数量
    private int soldNum;
    //订单总价格
    private double soldPrice;

    public Order(int id, int user_id, String goodName, int soldNum, double soldPrice) {
        this.id = id;
        this.user_id = user_id;
        this.goodName = goodName;
        this.soldNum = soldNum;
        this.soldPrice = soldPrice;
    }

    public Order(){
        this(0,0,"",0,0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public double getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(double soldPrice) {
        this.soldPrice = soldPrice;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public int getSoldNum() {
        return soldNum;
    }

    public void setSoldNum(int soldNum) {
        this.soldNum = soldNum;
    }
}
