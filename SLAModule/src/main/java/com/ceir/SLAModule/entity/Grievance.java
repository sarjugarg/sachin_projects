package com.ceir.SLAModule.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.UpdateTimestamp;

import com.ceir.SLAModule.model.constants.GrievanceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@NamedQuery(name = "Grievance.getAllGrievanceStatusNotClosed",
query = "select g from Grievance g where g.grievanceId = ?1 and grievanceStatus != ?1")
@NamedQuery(name = "Grievance.getAllGrievanceStatusNotClosedForAdmin",
query = "select g from Grievance g where grievanceStatus != ?1")
@NamedQuery(name = "Grievance.getGrievanceByUserId",
query = "select g from Grievance g where g.grievanceId = ?1")

public class Grievance {

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="grievance_id")
	private String grievanceId;
	
	@Column(name="user_id")
	private Integer userId ;

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

	@Column(length = 1000,columnDefinition="Text")
	private String remarks;
	
	@Transient
	private String stateInterp;
	
	@OneToOne(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name="user_id",insertable = false, updatable = false)
	@JsonIgnore
	private User user;
	
	public String getGrievanceId() {
		return grievanceId;
	}

	public void setGrievanceId(String grievanceId) {
		this.grievanceId = grievanceId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getStateInterp() {
		return stateInterp;
	}

	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
	}

	@PostLoad
    public void postLoad() {
        if(stateInterp == null || stateInterp.isEmpty()) {
        	this.stateInterp = GrievanceStatus.getActionNames( this.grievanceStatus ).toString();
        }
    }
	
	@Override
	public String toString() {
		return "Grievance:{grievanceId:"+grievanceId+",userId:"+userId+",userType:"+userType+",grievanceStatus"+grievanceStatus+","
				+ "txnId:"+txnId+",categoryId:"+categoryId+",fileName"+fileName+",createdOn"+createdOn+",modifiedOn:"+modifiedOn+","
						+ "remarks"+remarks+"}";
	}
	
}
