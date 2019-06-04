package com.example.a95795.thegreenplant.table;

import comc.example.administrator.form.annotation.SmartColumn;

/**
 * Created by huang on 2017/11/1.
 */

public class ChildData {

    @SmartColumn(id =5,name = "子类",autoCount = true)
    private String child;

    public ChildData(String child) {
        this.child = child;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }
}
