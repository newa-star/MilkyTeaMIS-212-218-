package com.example.milkyteamis.model;

import com.google.gson.annotations.SerializedName;

public class Good {
    //商品名称
    @SerializedName("goodName")
    private String name;
    //标识商品的id
    @SerializedName("id")
    private int id;
    //商品的价格
    @SerializedName("price")
    private double price;
    //商品的大小规格
    @SerializedName("size")
    private int size;
    //商品的图片
    //private String picture;
    //商品的类别（0—奶茶，1-果茶，2-鲜茶，3-芝士）
    @SerializedName("classify")

    private int classfication;

    public Good(String name,int id,double price,int size,int classfication){
        this.name = name;
        this.id = id;
        this.price = price;
        this.size = size;
        this.classfication = classfication;
    }

    public Good(){
        this("",0,0,0,0);
    }

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

 /**   public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }*/

    public int getClassfication() {
        return classfication;
    }

    public void setClassfication(int classfication) {
        this.classfication = classfication;
    }
}
