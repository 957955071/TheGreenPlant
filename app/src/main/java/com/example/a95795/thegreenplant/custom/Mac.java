package com.example.a95795.thegreenplant.custom;

public class Mac {
    private String mac;

    public String getMac() {
        return mac;
    }

    @Override
    public String toString() {
        return "Mac{" +
                "mac='" + mac + '\'' +
                '}';
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Mac(String mac) {
        this.mac = mac;
    }
}
