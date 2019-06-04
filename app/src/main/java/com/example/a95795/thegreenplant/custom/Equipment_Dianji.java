package com.example.a95795.thegreenplant.custom;

/**
 * Created by 95795 on 2019-05-12.
 */

public class Equipment_Dianji {
    private Integer id;

    private String equipmentDianji;

    private String equipmentNow;

    private String equipmentPic;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEquipmentDianji() {
        return equipmentDianji;
    }

    public void setEquipmentDianji(String equipmentDianji) {
        this.equipmentDianji = equipmentDianji == null ? null : equipmentDianji.trim();
    }

    public String getEquipmentNow() {
        return equipmentNow;
    }

    public void setEquipmentNow(String equipmentNow) {
        this.equipmentNow = equipmentNow == null ? null : equipmentNow.trim();
    }

    public String getEquipmentPic() {
        return equipmentPic;
    }

    public void setEquipmentPic(String equipmentPic) {
        this.equipmentPic = equipmentPic == null ? null : equipmentPic.trim();
    }
}