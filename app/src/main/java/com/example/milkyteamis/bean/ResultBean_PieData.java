package com.example.milkyteamis.bean;

public class ResultBean_PieData {
    private int status;
    private int[] data;

    public ResultBean_PieData(int status, int[] data) {
        this.status = status;
        this.data = data;
    }

    public int[] getData() {
        return data;
    }
}
