package com.example.milkyteamis.bean;

public class ResultBean_Msg {

    private int status;
    private String msg;

    public ResultBean_Msg(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
