package com.example.a95795.thegreenplant.bean;

import java.util.List;

public class EnvironmentInfoWeek {

    private List<EnvironmentInfoListBean> EnvironmentInfoList;

    public List<EnvironmentInfoListBean> getEnvironmentInfoList() {
        return EnvironmentInfoList;
    }

    public void setEnvironmentInfoList(List<EnvironmentInfoListBean> EnvironmentInfoList) {
        this.EnvironmentInfoList = EnvironmentInfoList;
    }

    public static class EnvironmentInfoListBean {
        /**
         * weekEmId : 64
         * emName : 车间4号
         * tmp : 82
         * hum : 55
         * pm : 66
         * isTmp : false
         * isHum : true
         * isPm : false
         * weekTime : 2019-04-23
         * isEvent : false
         * emType : 4
         */

        private int weekEmId;
        private String emName;
        private int tmp;
        private int hum;
        private int pm;
        private boolean isTmp;
        private boolean isHum;
        private boolean isPm;
        private String weekTime;
        private boolean isEvent;
        private int emType;

        public EnvironmentInfoListBean(String emName, int tmp, int hum, int pm, boolean isTmp, boolean isHum,
                                       boolean isPm, String weekTime, boolean isEvent) {
            this.emName = emName;
            this.tmp = tmp;
            this.hum = hum;
            this.pm = pm;
            this.isTmp = isTmp;
            this.isHum = isHum;
            this.isPm = isPm;
            this.weekTime = weekTime;
            this.isEvent = isEvent;
        }

        public int getWeekEmId() {
            return weekEmId;
        }

        public void setWeekEmId(int weekEmId) {
            this.weekEmId = weekEmId;
        }

        public String getEmName() {
            return emName;
        }

        public void setEmName(String emName) {
            this.emName = emName;
        }

        public int getTmp() {
            return tmp;
        }

        public void setTmp(int tmp) {
            this.tmp = tmp;
        }

        public int getHum() {
            return hum;
        }

        public void setHum(int hum) {
            this.hum = hum;
        }

        public int getPm() {
            return pm;
        }

        public void setPm(int pm) {
            this.pm = pm;
        }

        public boolean isIsTmp() {
            return isTmp;
        }

        public void setIsTmp(boolean isTmp) {
            this.isTmp = isTmp;
        }

        public boolean isIsHum() {
            return isHum;
        }

        public void setIsHum(boolean isHum) {
            this.isHum = isHum;
        }

        public boolean isIsPm() {
            return isPm;
        }

        public void setIsPm(boolean isPm) {
            this.isPm = isPm;
        }

        public String getWeekTime() {
            return weekTime;
        }

        public void setWeekTime(String weekTime) {
            this.weekTime = weekTime;
        }

        public boolean isIsEvent() {
            return isEvent;
        }

        public void setIsEvent(boolean isEvent) {
            this.isEvent = isEvent;
        }

        public int getEmType() {
            return emType;
        }

        public void setEmType(int emType) {
            this.emType = emType;
        }
    }
}
