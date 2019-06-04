package com.example.a95795.thegreenplant.custom;

/**
 * Created by 95795 on 2019-04-28.
 */

public class Phone {
     private String number;
    private String Mac;

    @Override
    public String toString() {
        return "Phone{" +
                "number='" + number + '\'' +
                ", Mac='" + Mac + '\'' +
                '}';
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }

    public Phone(String number, String mac) {
        this.number = number;
        Mac = mac;
    }

}
