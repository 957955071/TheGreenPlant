package com.example.a95795.thegreenplant.real;

public class RealBean {
    private String shopWorkName;
    private int tmp;
    private int pm;
    private int hum;

    public RealBean(String shopWorkName, int tmp, int pm, int hum) {
        this.shopWorkName = shopWorkName;
        this.tmp = tmp;
        this.pm = pm;
        this.hum = hum;
    }

    public String getShopWorkName() {
        return shopWorkName;
    }

    public void setShopWorkName(String shopWorkName) {
        this.shopWorkName = shopWorkName;
    }

    public int getTmp() {
        return tmp;
    }

    public void setTmp(int tmp) {
        this.tmp = tmp;
    }

    public int getPm() {
        return pm;
    }

    public void setPm(int pm) {
        this.pm = pm;
    }

    public int getHum() {
        return hum;
    }

    public void setHum(int hum) {
        this.hum = hum;
    }
}
