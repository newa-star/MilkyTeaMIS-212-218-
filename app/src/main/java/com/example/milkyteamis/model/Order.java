package com.example.milkyteamis.model;

import java.io.Serializable;

//该表为订单商品详情表
public class Order implements Serializable {
    //标识该条记录的id
    private int id;
    //标识订单id
    private int orderId;
    //表示商品id
    private int goodId;
    //商品名称
    private String goodName;
    //商品类别（0—奶茶，1-果茶，2-鲜茶，3-芝士）
    private int type;
    //标识温度 0-正常冰，1-去冰，2-常温，3-加热
    private int temperature;
    //标识甜度 0-正常糖，1-五分糖，2-无糖
    private int sugar;
    //数量
    private int sum;
    //该商品的单价
    private double realPrice;
    //标识是否加珍珠 0-不加珍珠，1-加珍珠
    private int if_add_pearl;
    //标识是否加椰果 0-不加椰果，1-加椰果
    private int if_add_coconuts;

    public Order(int id, int orderId, int goodId, String goodName,int type, int temperature, int sugar, int soldNum, double realPrice, int if_add_pearl, int if_add_coconuts) {
        this.id = id;
        this.orderId = orderId;
        this.goodId = goodId;
        this.goodName = goodName;
        this.type = type;
        this.temperature = temperature;
        this.sugar = sugar;
        this.sum = soldNum;
        this.realPrice = realPrice;
        this.if_add_pearl = if_add_pearl;
        this.if_add_coconuts = if_add_coconuts;
    }

    public Order() {
        this(0,0,0,"",0,0,0,0,0,0,0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getSoldNum() {
        return sum;
    }

    public void setSoldNum(int soldNum) {
        this.sum = soldNum;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public int getIf_add_pearl() {
        return if_add_pearl;
    }

    public void setIf_add_pearl(int if_add_pearl) {
        this.if_add_pearl = if_add_pearl;
    }

    public int getIf_add_coconuts() {
        return if_add_coconuts;
    }

    public void setIf_add_coconuts(int if_add_coconuts) {
        this.if_add_coconuts = if_add_coconuts;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
