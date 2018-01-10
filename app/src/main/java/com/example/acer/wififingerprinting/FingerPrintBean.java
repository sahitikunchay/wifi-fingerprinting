package com.example.acer.wififingerprinting;

/**
 * Created by ACER on 1/8/2018.
 */

public class FingerPrintBean {
    String macAddress;
    String SSID;
    int level;

    public FingerPrintBean(String pMacAddress, String pSSID, int pLevel) {
        macAddress = pMacAddress;
        SSID = pSSID;
        level = pLevel;
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

    @Override
    public boolean equals(Object pO) {
        if (this == pO) return true;
        if (pO == null || getClass() != pO.getClass()) return false;

        FingerPrintBean that = (FingerPrintBean) pO;

        if (!macAddress.equals(that.macAddress)) return false;
        return SSID.equals(that.SSID);
    }

    @Override
    public int hashCode() {
        int result = macAddress.hashCode();
        result = 31 * result + SSID.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "FingerPrintBean{" +
                "macAddress='" + macAddress + '\'' +
                ", SSID='" + SSID + '\'' +
                ", level=" + level +
                '}';
    }
}
