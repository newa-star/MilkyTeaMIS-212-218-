package com.example.milkyteamis.model;

public class SoldData {
    private int[] milkyTea;
    private int[] fruitTea;
    private int[] freshTea;
    private int[] cheeseTea;

    public SoldData(int[] milkyTea, int[] fruitTea, int[] freshTea, int[] cheeseTea) {
        this.milkyTea = milkyTea;
        this.fruitTea = fruitTea;
        this.freshTea = freshTea;
        this.cheeseTea = cheeseTea;
    }

    public SoldData() {
        this.cheeseTea = new int[12];
        this.freshTea = new int[12];
        this.fruitTea = new int[12];
        this.milkyTea = new int[12];
    }

    public int[] getMilkyTea() {
        return milkyTea;
    }

    public void setMilkyTea(int[] milkyTea) {
        this.milkyTea = milkyTea;
    }

    public int[] getFruitTea() {
        return fruitTea;
    }

    public void setFruitTea(int[] fruitTea) {
        this.fruitTea = fruitTea;
    }

    public int[] getFreshTea() {
        return freshTea;
    }

    public void setFreshTea(int[] freshTea) {
        this.freshTea = freshTea;
    }

    public int[] getCheeseTea() {
        return cheeseTea;
    }

    public void setCheeseTea(int[] cheeseTea) {
        this.cheeseTea = cheeseTea;
    }
}
