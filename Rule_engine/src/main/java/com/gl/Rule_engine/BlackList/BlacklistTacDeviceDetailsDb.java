/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.Rule_engine.BlackList;

import java.io.Serializable;

/**
 *
 * @author user
 */

//@Entity

class BlacklistTacDeviceDetailsDb implements Serializable {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Column
    private String Manufacturer;
    private String BrandName;
    private String ModelName;
    private String MarketingName;
    private String OperatingSystem;
    private String WLAN;
    private String NFC;
    private String Bluetooth;
    private String DeviceType;
//    private int blacklistTacDbId;

//    @OneToOne
//    @JoinColumn(name = "blacklist_tac_db_id")
        private BlacklistTacDb blacklistTacDb;

   public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String Manufacturer) {
        this.Manufacturer = Manufacturer;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String BrandName) {
        this.BrandName = BrandName;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String ModelName) {
        this.ModelName = ModelName;
    }

    public String getMarketingName() {
        return MarketingName;
    }

    public void setMarketingName(String MarketingName) {
        this.MarketingName = MarketingName;
    }

    public String getOperatingSystem() {
        return OperatingSystem;
    }

    public void setOperatingSystem(String OperatingSystem) {
        this.OperatingSystem = OperatingSystem;
    }

    public String getWLAN() {
        return WLAN;
    }

    public void setWLAN(String WLAN) {
        this.WLAN = WLAN;
    }

    public String getNFC() {
        return NFC;
    }

    public void setNFC(String NFC) {
        this.NFC = NFC;
    }

    public String getBluetooth() {
        return Bluetooth;
    }

    public void setBluetooth(String Bluetooth) {
        this.Bluetooth = Bluetooth;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String DeviceType) {
        this.DeviceType = DeviceType;
    }

    public BlacklistTacDb getBlacklistTacDb() {
        return blacklistTacDb;
    }

    public void setBlacklistTacDb(BlacklistTacDb blacklistTacDb) {
        this.blacklistTacDb = blacklistTacDb;
    }

   
    

   
   

    BlacklistTacDeviceDetailsDb() {
    }

    @Override
    public String toString() {
        return "BlacklistTacDeviceDetailsDb{" + "id=" + id + ", Manufacturer=" + Manufacturer + ", BrandName=" + BrandName + ", ModelName=" + ModelName + ", MarketingName=" + MarketingName + ", OperatingSystem=" + OperatingSystem + ", WLAN=" + WLAN + ", NFC=" + NFC + ", Bluetooth=" + Bluetooth + ", DeviceType=" + DeviceType + ", blacklistTacDb=" + blacklistTacDb + '}';
    }

    public BlacklistTacDeviceDetailsDb(int id, String Manufacturer, String BrandName, String ModelName, String MarketingName, String OperatingSystem, String WLAN, String NFC, String Bluetooth, String DeviceType, BlacklistTacDb blacklistTacDb) {
        this.id = id;
        this.Manufacturer = Manufacturer;
        this.BrandName = BrandName;
        this.ModelName = ModelName;
        this.MarketingName = MarketingName;
        this.OperatingSystem = OperatingSystem;
        this.WLAN = WLAN;
        this.NFC = NFC;
        this.Bluetooth = Bluetooth;
        this.DeviceType = DeviceType;
        this.blacklistTacDb = blacklistTacDb;
    }

   

   
    
    
    
    
    
    
    
    
}
