package com.example.a95795.thegreenplant.custom;

public class WorkShopJudge {
    private int workshop;

    public int getWorkshop() {
        return workshop;
    }

    @Override
    public String toString() {
        return "WorkShopJudge{" +
                "workshop=" + workshop +
                '}';
    }

    public void setWorkshop(int workshop) {
        this.workshop = workshop;
    }

    public WorkShopJudge(int workshop) {
        this.workshop = workshop;
    }
}
