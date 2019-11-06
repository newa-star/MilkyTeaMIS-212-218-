package com.example.milkyteamis.model;

public class User {
    //标识用户的id的编号
    private int id;
    //用户账号
    private String code;
    //用户昵称
    private String name;
    //用户密码
    private String password;
    //用户身份
    private int identity;

    public User(int id, String code, String name, String password, int identity) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.password = password;
        this.identity = identity;
    }

    public User(){
        this(0,"","","",0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }
}
