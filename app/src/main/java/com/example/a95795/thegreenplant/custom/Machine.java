package com.example.a95795.thegreenplant.custom;

import java.util.Date;

public class Machine {
    private Integer machineId;

    private String machineType;

    private String machineLeading;

    private Date machineBuytime;

    private Date machineUsetime;

    private Integer machineSwitch;

    private Integer machineFs;

    private Integer machineWorkshop;

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public String getMachineType() {
        return machineType;
    }

    public void setMachineType(String machineType) {
        this.machineType = machineType == null ? null : machineType.trim();
    }

    public String getMachineLeading() {
        return machineLeading;
    }

    public void setMachineLeading(String machineLeading) {
        this.machineLeading = machineLeading == null ? null : machineLeading.trim();
    }

    public Date getMachineBuytime() {
        return machineBuytime;
    }

    public void setMachineBuytime(Date machineBuytime) {
        this.machineBuytime = machineBuytime;
    }

    public Date getMachineUsetime() {
        return machineUsetime;
    }

    public void setMachineUsetime(Date machineUsetime) {
        this.machineUsetime = machineUsetime;
    }

    public Integer getMachineSwitch() {
        return machineSwitch;
    }

    public void setMachineSwitch(Integer machineSwitch) {
        this.machineSwitch = machineSwitch;
    }

    public Integer getMachineFs() {
        return machineFs;
    }

    public void setMachineFs(Integer machineFs) {
        this.machineFs = machineFs;
    }

    public Integer getMachineWorkshop() {
        return machineWorkshop;
    }

    public void setMachineWorkshop(Integer machineWorkshop) {
        this.machineWorkshop = machineWorkshop;
    }
}
