package com.ceir.BlackListProcess.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class FileDumpMgmt  implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private Date createdOn;

	@JsonIgnore
	@UpdateTimestamp
	private Date modifiedOn;
	
	private String FileName;
	
	private String fileType;
	
	private int serviceDump;

	private String dumpType;
	
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
	
	public String getFileName() {
		return FileName;
	}

	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public int getServiceDump() {
		return serviceDump;
	}

	public void setServiceDump(int serviceDump) {
		this.serviceDump = serviceDump;
	}

	public String getDumpType() {
		return dumpType;
	}

	public void setDumpType(String dumpType) {
		this.dumpType = dumpType;
	}

	@Override
	public String toString() {
		return "FileDumpMgmt [id=" + id + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", FileName="
				+ FileName + ", fileType=" + fileType + ", serviceDump=" + serviceDump + ", dumpType=" + dumpType + "]";
	}
	
	
	
	
	
	
}
