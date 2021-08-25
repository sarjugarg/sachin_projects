/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.Rule_engine.BlackList;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author user
 */
//@Entity

public class BlacklistTacDb implements Serializable {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Column
    private String RefCode;
    private String deviceid;
    private String BlacklistStatus;

//    @OneToOne(mappedBy = "blacklistTacDb", cascade = CascadeType.ALL)
    BlacklistTacDeviceDetailsDb DeviceDetails;

//    @OneToMany(mappedBy = "blacklistTacDb" , cascade = CascadeType.ALL)
    List<BlacklistTacDeviceHistoryDb> DeviceHistory ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRefCode() {
        return RefCode;
    }

    public void setRefCode(String RefCode) {
        this.RefCode = RefCode;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getBlacklistStatus() {
        return BlacklistStatus;
    }

    public void setBlacklistStatus(String BlacklistStatus) {
        this.BlacklistStatus = BlacklistStatus;
    }

    public BlacklistTacDeviceDetailsDb getDeviceDetails() {
        return DeviceDetails;
    }

    public void setDeviceDetails(BlacklistTacDeviceDetailsDb DeviceDetails) {
        this.DeviceDetails = DeviceDetails;
    }

    public List<BlacklistTacDeviceHistoryDb> getDeviceHistory() {
        return DeviceHistory;
    }

    public void setDeviceHistory(List<BlacklistTacDeviceHistoryDb> DeviceHistory) {
        this.DeviceHistory = DeviceHistory;
    }

    public BlacklistTacDb() {
    }

    public BlacklistTacDb(int id, String RefCode, String deviceid, String BlacklistStatus) {
        this.id = id;
        this.RefCode = RefCode;
        this.deviceid = deviceid;
        this.BlacklistStatus = BlacklistStatus;
//        this.DeviceDetails = DeviceDetails;
    }

    @Override
    public String toString() {
        return "BlacklistTacDb{" + "id=" + id + ", RefCode=" + RefCode + ", deviceid=" + deviceid + ", BlacklistStatus=" + BlacklistStatus + ", DeviceDetails=" + DeviceDetails + ", DeviceHistory=" + DeviceHistory + '}';
    }

    
    
}
