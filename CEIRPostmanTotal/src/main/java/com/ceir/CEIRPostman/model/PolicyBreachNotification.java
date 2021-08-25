package com.ceir.CEIRPostman.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class PolicyBreachNotification implements Serializable {

     private static final long serialVersionUID = 1L;

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
     private String channelType;

     private Long contactNumber;

     @CreationTimestamp
     @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
     private LocalDateTime createdOn;

     private String featureTxnId;

     private String imei;

     @Column(length = 1000)
     private String message;

     @UpdateTimestamp
     private LocalDateTime modifiedOn;
     private Integer retryCount;
     private Integer status;
     private String subject;

     private Long userId;

     public PolicyBreachNotification(Long id, String channelType, Long contactNumber, LocalDateTime createdOn, String featureTxnId, String imei, String message, LocalDateTime modifiedOn, Integer retryCount, Integer status, String subject, Long userId) {
          this.id = id;
          this.channelType = channelType;
          this.contactNumber = contactNumber;
          this.createdOn = createdOn;
          this.featureTxnId = featureTxnId;
          this.imei = imei;
          this.message = message;
          this.modifiedOn = modifiedOn;
          this.retryCount = retryCount;
          this.status = status;
          this.subject = subject;
          this.userId = userId;
     }

     public PolicyBreachNotification() {
     }

     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public String getChannelType() {
          return channelType;
     }

     public void setChannelType(String channelType) {
          this.channelType = channelType;
     }

     public Long getContactNumber() {
          return contactNumber;
     }

     public void setContactNumber(Long contactNumber) {
          this.contactNumber = contactNumber;
     }

     public LocalDateTime getCreatedOn() {
          return createdOn;
     }

     public void setCreatedOn(LocalDateTime createdOn) {
          this.createdOn = createdOn;
     }

     public String getFeatureTxnId() {
          return featureTxnId;
     }

     public void setFeatureTxnId(String featureTxnId) {
          this.featureTxnId = featureTxnId;
     }

     public String getImei() {
          return imei;
     }

     public void setImei(String imei) {
          this.imei = imei;
     }

     public String getMessage() {
          return message;
     }

     public void setMessage(String message) {
          this.message = message;
     }

     public LocalDateTime getModifiedOn() {
          return modifiedOn;
     }

     public void setModifiedOn(LocalDateTime modifiedOn) {
          this.modifiedOn = modifiedOn;
     }

     public Integer getRetryCount() {
          return retryCount;
     }

     public void setRetryCount(Integer retryCount) {
          this.retryCount = retryCount;
     }

     public Integer getStatus() {
          return status;
     }

     public void setStatus(Integer status) {
          this.status = status;
     }

     public String getSubject() {
          return subject;
     }

     public void setSubject(String subject) {
          this.subject = subject;
     }

     public Long getUserId() {
          return userId;
     }

     public void setUserId(Long userId) {
          this.userId = userId;
     }

     @Override
     public String toString() {
          StringBuilder sb = new StringBuilder();
          sb.append("PolicyBreachNotification{id=").append(id);
          sb.append(", channelType=").append(channelType);
          sb.append(", contactNumber=").append(contactNumber);
          sb.append(", createdOn=").append(createdOn);
          sb.append(", featureTxnId=").append(featureTxnId);
          sb.append(", imei=").append(imei);
          sb.append(", message=").append(message);
          sb.append(", modifiedOn=").append(modifiedOn);
          sb.append(", retryCount=").append(retryCount);
          sb.append(", status=").append(status);
          sb.append(", subject=").append(subject);
          sb.append(", userId=").append(userId);
          sb.append('}');
          return sb.toString();
     }
     
     
     
     

}

