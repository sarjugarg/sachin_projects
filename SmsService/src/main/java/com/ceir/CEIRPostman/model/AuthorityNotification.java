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
public class AuthorityNotification implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;

	@UpdateTimestamp
	private LocalDateTime modifiedOn;

	private String channelType;

	@Column(length = 1000)
	private String message;

	private Long userId;

	private Long featureId;

	private String featureTxnId;

	private String featureName;

	private String subFeature;

	private Integer status;

	private String subject;

	private Integer retryCount;
	
	@Column(length = 10)
	private String referTable;
	
	@Column(length = 20)
	private String roleType;
	
	@Column(length = 50)
	private String receiverUserType;

	public AuthorityNotification() {

	}

	
	public AuthorityNotification(String channelType, String message,
			Long userId, Long featureId, String featureTxnId, String featureName, String subFeature, Integer status,
			String subject, Integer retryCount, String referTable, String roleType, String receiverUserType) {
		super();
		this.channelType = channelType;
		this.message = message;
		this.userId = userId;
		this.featureId = featureId;
		this.featureTxnId = featureTxnId;
		this.featureName = featureName;
		this.subFeature = subFeature;
		this.status = status;
		this.subject = subject;
		this.retryCount = retryCount;
		this.referTable = referTable;
		this.roleType = roleType;
		this.receiverUserType = receiverUserType;
	}


	public AuthorityNotification(String channelType, String message, Long userId, Long featureId, String featureName, 
			String subFeature, String featureTxnId, String subject, Integer retryCount, String referTable,
			String roleType, String receiverUserType) {
		this.channelType = channelType;
		this.message = message;
		this.userId = userId;
		this.featureId = featureId;
		this.featureName = featureName;
		this.subFeature = subFeature;
		this.featureTxnId = featureTxnId;
		this.status=status;
		this.subject = subject;
		this.retryCount = retryCount;
		this.referTable = referTable;
		this.roleType = roleType;
		this.receiverUserType = receiverUserType;
	}


	public String getReceiverUserType() {
		return receiverUserType;
	}

	public void setReceiverUserType(String receiverUserType) {
		this.receiverUserType = receiverUserType;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}
	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getFeatureId() {
		return featureId;
	}
	public void setFeatureId(Long featureId) {
		this.featureId = featureId;
	}
	public String getFeatureName() {
		return featureName;
	}
	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}
	public String getSubFeature() {
		return subFeature;
	}
	public void setSubFeature(String subFeature) {
		this.subFeature = subFeature;
	}
	public String getFeatureTxnId() {
		return featureTxnId;
	}
	public void setFeatureTxnId(String featureTxnId) {
		this.featureTxnId = featureTxnId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}
	
	public String getReferTable() {
		return referTable;
	}

	public void setReferTable(String referTable) {
		this.referTable = referTable;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Notification [id=");
		builder.append(id);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", channelType=");
		builder.append(channelType);
		builder.append(", message=");
		builder.append(message);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", featureId=");
		builder.append(featureId);
		builder.append(", featureTxnId=");
		builder.append(featureTxnId);
		builder.append(", featureName=");
		builder.append(featureName);
		builder.append(", subFeature=");
		builder.append(subFeature);
		builder.append("]");
		return builder.toString();
	}
}
