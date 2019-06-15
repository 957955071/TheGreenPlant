package com.example.a95795.thegreenplant.bean;

import java.util.List;

public class SetValue {

    private List<GetValueBean> getValue;

    public List<GetValueBean> getGetValue() {
        return getValue;
    }

    public void setGetValue(List<GetValueBean> getValue) {
        this.getValue = getValue;
    }

    public static class GetValueBean {
        /**
         * id : 1
         * pmMin : 0
         * pmMax : 50
         * pmDiff : 50
         * tmpMim : 18
         * tmpMax : 25
         * tmpDiff : 5
         * humMin : 30
         * humMax : 60
         * humDiff : 20
         */

        private int id;
        private int pmMin;
        private int pmMax;
        private int pmDiff;
        private int tmpMim;
        private int tmpMax;
        private int tmpDiff;
        private int humMin;
        private int humMax;
        private int humDiff;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPmMin() {
            return pmMin;
        }

        public void setPmMin(int pmMin) {
            this.pmMin = pmMin;
        }

        public int getPmMax() {
            return pmMax;
        }

        public void setPmMax(int pmMax) {
            this.pmMax = pmMax;
        }

        public int getPmDiff() {
            return pmDiff;
        }

        public void setPmDiff(int pmDiff) {
            this.pmDiff = pmDiff;
        }

        public int getTmpMim() {
            return tmpMim;
        }

        public void setTmpMim(int tmpMim) {
            this.tmpMim = tmpMim;
        }

        public int getTmpMax() {
            return tmpMax;
        }

        public void setTmpMax(int tmpMax) {
            this.tmpMax = tmpMax;
        }

        public int getTmpDiff() {
            return tmpDiff;
        }

        public void setTmpDiff(int tmpDiff) {
            this.tmpDiff = tmpDiff;
        }

        public int getHumMin() {
            return humMin;
        }

        public void setHumMin(int humMin) {
            this.humMin = humMin;
        }

        public int getHumMax() {
            return humMax;
        }

        public void setHumMax(int humMax) {
            this.humMax = humMax;
        }

        public int getHumDiff() {
            return humDiff;
        }

        public void setHumDiff(int humDiff) {
            this.humDiff = humDiff;
        }

        public GetValueBean(int pmMin, int pmMax, int pmDiff, int tmpMim, int tmpMax, int tmpDiff, int humMin, int humMax, int humDiff) {
            this.pmMin = pmMin;
            this.pmMax = pmMax;
            this.pmDiff = pmDiff;
            this.tmpMim = tmpMim;
            this.tmpMax = tmpMax;
            this.tmpDiff = tmpDiff;
            this.humMin = humMin;
            this.humMax = humMax;
            this.humDiff = humDiff;
        }
    }
}