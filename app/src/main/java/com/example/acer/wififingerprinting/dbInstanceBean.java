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
    String time;

    public dbInstanceBean(String pMacAddress, String pSSID, int pLevel, int pCrowded, String pLocationLabel, String pTime) {
        macAddress = pMacAddress;
        SSID = pSSID;
        level = pLevel;
        crowded = pCrowded;
        locationLabel = pLocationLabel;
        time = pTime;
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

    public String getTime() {
        return time;
    }

    public void setTime(String pTime) {
        time = pTime;
    }

    @Override
    public boolean equals(Object pO) {
        if (this == pO) return true;
        if (pO == null || getClass() != pO.getClass()) return false;

        dbInstanceBean that = (dbInstanceBean) pO;

        if (macAddress != null ? !macAddress.equals(that.macAddress) : that.macAddress != null)
            return false;
        if (SSID != null ? !SSID.equals(that.SSID) : that.SSID != null) return false;
        return locationLabel != null ? locationLabel.equals(that.locationLabel) : that.locationLabel == null;
    }

    @Override
    public int hashCode() {
        int result = macAddress != null ? macAddress.hashCode() : 0;
        result = 31 * result + (SSID != null ? SSID.hashCode() : 0);
        result = 31 * result + (locationLabel != null ? locationLabel.hashCode() : 0);
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
                ", time='" + time + '\'' +
                '}';
    }
}
