package com.example.acer.wififingerprinting;

/**
 * Created by ACER on 1/10/2018.
 */

public class dbInstanceBean {
    String macAddress;
    String SSID;
    int level;
    int crowded;
    String locationLabel;

    public dbInstanceBean(String pMacAddress, String pSSID, int pLevel, int pCrowded, String pLocationLabel) {
        macAddress = pMacAddress;
        SSID = pSSID;
        level = pLevel;
        crowded = pCrowded;
        locationLabel = pLocationLabel;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String pMacAddress) {
        macAddress = pMacAddress;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String pSSID) {
        SSID = pSSID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int pLevel) {
        level = pLevel;
    }

    public int getCrowded() {
        return crowded;
    }

    public void setCrowded(int pCrowded) {
        crowded = pCrowded;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String pLocationLabel) {
        locationLabel = pLocationLabel;
    }

    @Override
    public boolean equals(Object pO) {
        if (this == pO) return true;
        if (pO == null || getClass() != pO.getClass()) return false;

        dbInstanceBean that = (dbInstanceBean) pO;

        if (!macAddress.equals(that.macAddress)) return false;
        if (!SSID.equals(that.SSID)) return false;
        return locationLabel.equals(that.locationLabel);
    }

    @Override
    public int hashCode() {
        int result = macAddress.hashCode();
        result = 31 * result + SSID.hashCode();
        result = 31 * result + locationLabel.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "dbInstanceBean{" +
                "macAddress='" + macAddress + '\'' +
                ", SSID='" + SSID + '\'' +
                ", level=" + level +
                ", crowded=" + crowded +
                ", locationLabel='" + locationLabel + '\'' +
                '}';
    }
}
