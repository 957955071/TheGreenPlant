package com.example.a95795.thegreenplant.custom;

import java.util.Date;

public class User {
    private Integer id;

    private String userId;

    private String userName;

    private Integer userSex;

    private String userCall;

    private Date userFirstjob;

    private Integer userWork;

    private Integer userWorkshop;

    private String userPassword;

    private String userNum;

    private String userIdentity;

    private String userMac;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getUserSex() {
        return userSex;
    }

    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }

    public String getUserCall() {
        return userCall;
    }

    public void setUserCall(String userCall) {
        this.userCall = userCall == null ? null : userCall.trim();
    }

    public Date getUserFirstjob() {
        return userFirstjob;
    }

    public void setUserFirstjob(Date userFirstjob) {
        this.userFirstjob = userFirstjob;
    }

    public Integer getUserWork() {
        return userWork;
    }

    public void setUserWork(Integer userWork) {
        this.userWork = userWork;
    }

    public Integer getUserWorkshop() {
        return userWorkshop;
    }

    public void setUserWorkshop(Integer userWorkshop) {
        this.userWorkshop = userWorkshop;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword == null ? null : userPassword.trim();
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum == null ? null : userNum.trim();
    }

    public String getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(String userIdentity) {
        this.userIdentity = userIdentity == null ? null : userIdentity.trim();
    }

    public String getUserMac() {
        return userMac;
    }

    public void setUserMac(String userMac) {
        this.userMac = userMac == null ? null : userMac.trim();
    }
}