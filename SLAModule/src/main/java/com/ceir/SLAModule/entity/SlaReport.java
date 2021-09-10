package com.ceir.SLAModule.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class SlaReport {


	private static long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long featureId;
	private Integer state;
	private String stateInterp;
	private String username;
	private long usertypeId;

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User userSlaReport;
	
	private String txnId;
	
	@Column(nullable =false)
	@CreationTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdOn;
	
	@Column(nullable =false)
	@UpdateTimestamp
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime modifiedOn;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	public long getFeatureId() {
		return featureId;
	}

	public void setFeatureId(long featureId) {
		this.featureId = featureId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public User getUserSlaReport() {
		return userSlaReport;
	}

	public void setUserSlaReport(User userSlaReport) {
		this.userSlaReport = userSlaReport;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
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
	
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public static void setSerialVersionUID(long serialVersionUID) {
		SlaReport.serialVersionUID = serialVersionUID;
	}

	public String getStateInterp() {
		return stateInterp;
	}

	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
	}

	public SlaReport() {

	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	


	public long getUsertypeId() {
		return usertypeId;
	}

	public void setUsertypeId(long usertypeId) {
		this.usertypeId = usertypeId;
	}

	public SlaReport(Integer featureId, Integer state,String stateInterp, User user, String txnId,String username,long usertypeId) {
		super();
		this.featureId = featureId;
		this.state = state;
		userSlaReport=user;
		this.txnId = txnId;
		this.stateInterp=stateInterp;
		this.username=username;
		this.usertypeId=usertypeId;
	}
 
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SlaReport [id=");
		builder.append(id);
		builder.append(", featureId=");
		builder.append(featureId);
		builder.append(", state=");
		builder.append(state);
		builder.append(", stateInterup=");
		builder.append(stateInterp);
		builder.append(", username=");
		builder.append(username);
		builder.append(", usertypeId=");
		builder.append(usertypeId);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append("]");
		return builder.toString();
	}



}
