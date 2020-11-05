/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author dream
 */
public class Devices implements Serializable{
    public static final long serialVersionUID = 2020;
    private String deviceName;
    private String deviceUUID;
    private String ip;
    private String status = "OFFLINE";
    private String username;
    private String note = "NULL";

    public Devices() {
    }

    public Devices(String deviceName, String deviceUUID, String ip) {
        this.deviceName = deviceName;
        this.deviceUUID = deviceUUID;
        this.ip = ip;
    }
    
    public Devices(String deviceName, String deviceUUID) {
        this.deviceName = deviceName;
        this.deviceUUID = deviceUUID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public Object[] toObject () {
        return new Object[]{deviceName, status, username, note};
    }
    
}
