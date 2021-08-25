package com.gl.ceir.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;

@Entity
@Audited
public class Userrole {
	private static long serialVersionUID = 1L;

	@Id    
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@CreationTimestamp
	private Date createdOn;
	@UpdateTimestamp
	private Date modifiedOn; 

	@ManyToOne 
	@JoinColumn(name = "user_id",nullable = false) 
	private User userData;

	@ManyToOne 
	@JoinColumn(name = "usertype_id",nullable = false) 
	private Usertype usertypeData; 
	
	public Userrole() {
		super();
	}

	public Userrole( User userData, Usertype usertypeData) {
		super();
		this.userData = userData;
		this.usertypeData = usertypeData;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public User getUserData() {
		return userData;
	}

	public void setUserData(User userData) {
		this.userData = userData;
	}

	public Usertype getUsertypeData() {
		return usertypeData;
	}

	public void setUsertypeData(Usertype usertypeData) {
		this.usertypeData = usertypeData;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Userrole [id=");
		builder.append(id);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", userData=");
		builder.append(userData);
		builder.append("]");
		return builder.toString();
	}

}
