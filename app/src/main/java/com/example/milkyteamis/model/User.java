package com.example.milkyteamis.model;

import java.io.Serializable;

public class User implements Serializable {
    //标识用户的id的编号
    private int id;
    //用户账号
    private String code;
    //用户昵称
    private String userName;
    //用户密码
    private String password;
    //用户身份
    private int identity;

    public User(int id, String code, String name, String password, int identity) {
        this.id = id;
        this.code = code;
        this.userName = name;
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
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
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
