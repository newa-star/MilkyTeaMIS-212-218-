package com.example.milkyteamis.model;

public class Good {
    //商品名称
    private String name;
    //标识商品的id
    private int id;
    //商品的价格
    private double price;
    //商品的大小规格
    private int size;
    //商品的图片
    private String picture;
    //商品的类别（0—奶茶，1-果茶，2-鲜茶，3-芝士）
    private int classfication;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getClassfication() {
        return classfication;
    }

    public void setClassfication(int classfication) {
        this.classfication = classfication;
    }
}
