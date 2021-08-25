package com.gl.ceir.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class GrievanceHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="grievance_id")
	private String grievanceId;
	
	private Long userId;

	private String userType;
	
	@Column(length = 3)
	private int grievanceStatus;
	
	@NotNull
	@Column(length = 20)
	private String txnId;
	
	@Column(length = 3)
	private int categoryId;
	
	private String fileName;
	
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;


	@UpdateTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime modifiedOn;

	@Column(length = 1000)
	private String remarks;
	
	private Long closedByUseId;
	
	private String closedByUserType;
	
	public GrievanceHistory() {

	}
	
	public GrievanceHistory(String grievanceId, Long userId, String userType, int grievanceStatus,
			String txnId, int categoryId, String fileName, String remarks, Long closedByUseId, String closedByUserType) {
		this.grievanceId = grievanceId;
		this.userId = userId;
		this.userType = userType;
		this.grievanceStatus = grievanceStatus;
		this.txnId = txnId;
		this.categoryId = categoryId;
		this.fileName = fileName;
		this.remarks = remarks;
		this.closedByUseId = closedByUseId;
		this.closedByUserType = closedByUserType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGrievanceId() {
		return grievanceId;
	}

	public void setGrievanceId(String grievanceId) {
		this.grievanceId = grievanceId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getGrievanceStatus() {
		return grievanceStatus;
	}

	public void setGrievanceStatus(int grievanceStatus) {
		this.grievanceStatus = grievanceStatus;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getClosedByUseId() {
		return closedByUseId;
	}

	public void setClosedByUseId(Long closedByUseId) {
		this.closedByUseId = closedByUseId;
	}

	public String getClosedByUserType() {
		return closedByUserType;
	}

	public void setClosedByUserType(String closedByUserType) {
		this.closedByUserType = closedByUserType;
	}
	
}
