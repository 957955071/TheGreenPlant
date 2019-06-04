package com.example.a95795.thegreenplant.bean;

import java.util.List;

public class EnvironmentInfoDay {

    private List<EnvironmentInfoListBean> EnvironmentInfoList;

    public List<EnvironmentInfoListBean> getEnvironmentInfoList() {
        return EnvironmentInfoList;
    }

    public void setEnvironmentInfoList(List<EnvironmentInfoListBean> EnvironmentInfoList) {
        this.EnvironmentInfoList = EnvironmentInfoList;
    }

    public static class EnvironmentInfoListBean {
        /**
         * dayEmId : 1
         * emName : 车间1号
         * tmp : 86
         * hum : 66
         * pm : 57
         * isTmp : true
         * isHum : false
         * isPm : true
         * dayTime : 2019-05-01
         * isEvent : false
         * emType : 1
         */

        private int dayEmId;
        private String emName;
        private int tmp;
        private int hum;
        private int pm;
        private boolean isTmp;
        private boolean isHum;
        private boolean isPm;
        private String dayTime;
        private boolean isEvent;
        private int emType;

        public EnvironmentInfoListBean(String emName, int tmp, int hum, int pm, boolean isTmp, boolean isHum,
                                       boolean isPm, String dayTime, boolean isEvent) {
            this.emName = emName;
            this.tmp = tmp;
            this.hum = hum;
            this.pm = pm;
            this.isTmp = isTmp;
            this.isHum = isHum;
            this.isPm = isPm;
            this.dayTime = dayTime;
            this.isEvent = isEvent;
        }

        public int getDayEmId() {
            return dayEmId;
        }

        public void setDayEmId(int dayEmId) {
            this.dayEmId = dayEmId;
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

        public String getDayTime() {
            return dayTime;
        }

        public void setDayTime(String dayTime) {
            this.dayTime = dayTime;
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
