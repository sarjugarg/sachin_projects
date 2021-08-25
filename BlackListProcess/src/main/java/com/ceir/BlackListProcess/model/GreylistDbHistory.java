package com.ceir.BlackListProcess.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class GreylistDbHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//     @JsonIgnore
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createdOn;
//     @JsonIgnore
    @UpdateTimestamp
    private Date modifiedOn;
    private String imei;
    @Column(length = 15)
    private String roleType;
    private String userId;
    @Column(length = 20)
    private String txnId;
    private String deviceNumber;
    private String deviceType;
    private String deviceAction;
    private String deviceStatus;
    private String DeviceLaunchDate;
    private String multipleSimStatus;
    private String deviceId;
    private String imeiEsnMeid;
    private String actualImei;
    private int operation;
    private String reason;
    private String modeType;
    private String requestType;
    private String userType;
    private String complainType;

    private String operator_id;
    private String operator_name;

    @Type(type = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

 
    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getOperator_name() {
        return operator_name;
    }

    public void setOperator_name(String operator_name) {
        this.operator_name = operator_name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceAction() {
        return deviceAction;
    }

    public void setDeviceAction(String deviceAction) {
        this.deviceAction = deviceAction;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceLaunchDate() {
        return DeviceLaunchDate;
    }

    public void setDeviceLaunchDate(String deviceLaunchDate) {
        DeviceLaunchDate = deviceLaunchDate;
    }

    public String getMultipleSimStatus() {
        return multipleSimStatus;
    }

    public void setMultipleSimStatus(String multipleSimStatus) {
        this.multipleSimStatus = multipleSimStatus;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getImeiEsnMeid() {
        return imeiEsnMeid;
    }

    public void setImeiEsnMeid(String imeiEsnMeid) {
        this.imeiEsnMeid = imeiEsnMeid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getModeType() {
        return modeType;
    }

    public void setModeType(String modeType) {
        this.modeType = modeType;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getComplainType() {
        return complainType;
    }

    public void setComplainType(String complainType) {
        this.complainType = complainType;
    }

    public String getActualImei() {
        return actualImei;
    }

    public void setActualImei(String actualImei) {
        this.actualImei = actualImei;
    }

    public GreylistDbHistory(Date createdOn, Date modifiedOn, String imei, String roleType, String userId,
            String txnId, String deviceNumber, String deviceType, String deviceAction, String deviceStatus,
            String deviceLaunchDate, String multipleSimStatus, String deviceId, String imeiEsnMeid, int operation, String reason,
            String modeType, String requestType, String userType, String complainType, Date expiryDate, String operator_id, String operator_name, String actualImei) {
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.imei = imei;
        this.roleType = roleType;
        this.userId = userId;
        this.txnId = txnId;
        this.deviceNumber = deviceNumber;
        this.deviceType = deviceType;
        this.deviceAction = deviceAction;
        this.deviceStatus = deviceStatus;
        DeviceLaunchDate = deviceLaunchDate;
        this.multipleSimStatus = multipleSimStatus;
        this.deviceId = deviceId;
        this.imeiEsnMeid = imeiEsnMeid;
        this.operation = operation;
        this.reason = reason;
        this.modeType = modeType;
        this.requestType = requestType;
        this.userType = userType;
        this.complainType = complainType;
        this.expiryDate = expiryDate;
        this.operator_id = operator_id;
        this.operator_name = operator_name;
        this.actualImei = actualImei;
    }

    public GreylistDbHistory() {
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    
    
       public GreylistDbHistory(String imei, String roleType, String userId, String txnId, String deviceNumber, String deviceType, String deviceAction, String deviceStatus, String deviceLaunchDate, String multipleSimStatus, String deviceIdType, String imeiEsnMeid, int code, String moved_to_BlackList, String modeType, String requestType, String userType, String complainType, Date expiryDate, String operator_id, String operator_name, String actualImei) {

        this.imei = imei;
        this.roleType = roleType;
        this.userId = userId;
        this.txnId = txnId;
        this.deviceNumber = deviceNumber;
        this.deviceType = deviceType;
        this.deviceAction = deviceAction;
        this.deviceStatus = deviceStatus;
        this.DeviceLaunchDate = deviceLaunchDate;
        this.multipleSimStatus = multipleSimStatus;
        this.deviceId = deviceId;
        this.imeiEsnMeid = imeiEsnMeid;
        this.operation = operation;
        this.reason = reason;
        this.modeType = modeType;
        this.requestType = requestType;
        this.userType = userType;
        this.complainType = complainType;
        this.expiryDate = expiryDate;
        this.operator_id = operator_id;
        this.operator_name = operator_name;
        this.actualImei = actualImei;

    }
}
