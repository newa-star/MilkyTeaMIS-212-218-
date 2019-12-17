package com.example.milkyteamis.bean;

public class ResultBean_Bar {
    private int status;
    private int[] data;

    public ResultBean_Bar(int status, int[] data) {
        this.status = status;
        this.data = data;
    }

    public int[] getData() {
        return data;
    }
}
