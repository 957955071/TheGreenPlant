package com.example.a95795.thegreenplant.custom;

public class Id {
    private int id;

    @Override
    public String toString() {
        return "Id{" +
                "id=" + id +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Id(int id) {
        this.id = id;
    }
}
