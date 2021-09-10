package com.ceir.SLAModule.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class ConsignmentMgmt implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String supplierId;

	// @NotNull
	private String supplierName;

	@Column(length = 15)
	private String consignmentNumber;

	@Column(length = 10)
	private Integer taxPaidStatus;

	@CreationTimestamp
	@JsonFormat(pattern="yyyy-MM-dd HH:mm")
	private LocalDateTime createdOn;

	@UpdateTimestamp
	private LocalDateTime modifiedOn;

	@NotNull
	private Integer userId;

	@NotNull
	@Column(length = 20)
	private String txnId;

	private String fileName;

	@Column(length = 3)
	private int consignmentStatus;

	private int previousConsignmentStatus;

	private String organisationCountry;

	@Column(length = 25)
	private String expectedDispatcheDate;

	@Column(length = 25)
	private String expectedArrivaldate;

	private Integer expectedArrivalPort;
	@Transient
	private String expectedArrivalPortInterp;

	private int quantity;

	private String remarks;

	private Integer currency;
	@Transient
	private String currencyInterp;

	private Double totalPrice;
	
	@Transient
	private String stateInterp;
	
	@Transient
	private String taxInterp;

	// @NotNull
	@OneToOne(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name="local_user_id", updatable = false)
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSupplierld() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getConsignmentNumber() {
		return consignmentNumber;
	}

	public void setConsignmentNumber(String consignmentNumber) {
		this.consignmentNumber = consignmentNumber;
	}

	public Integer getTaxPaidStatus() {
		return taxPaidStatus;
	}

	public void setTaxPaidStatus(Integer taxPaidStatus) {
		this.taxPaidStatus = taxPaidStatus;
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


	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getConsignmentStatus() {
		return consignmentStatus;
	}

	public void setConsignmentStatus(int consignmentStatus) {
		this.consignmentStatus = consignmentStatus;
	}

	public String getOrganisationCountry() {
		return organisationCountry;
	}

	public void setOrganisationCountry(String organisationCountry) {
		this.organisationCountry = organisationCountry;
	}

	public String getExpectedDispatcheDate() {
		return expectedDispatcheDate;
	}

	public void setExpectedDispatcheDate(String expectedDispatcheDate) {
		this.expectedDispatcheDate = expectedDispatcheDate;
	}

	public String getExpectedArrivaldate() {
		return expectedArrivaldate;
	}

	public void setExpectedArrivaldate(String expectedArrivaldate) {
		this.expectedArrivaldate = expectedArrivaldate;
	}

	public Integer getExpectedArrivalPort() {
		return expectedArrivalPort;
	}

	public void setExpectedArrivalPort(Integer expectedArrivalPort) {
		this.expectedArrivalPort = expectedArrivalPort;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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

	public ConsignmentMgmt setUser(User user) {
		this.user = user;
		return this;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}


	public int getPreviousConsignmentStatus() {
		return previousConsignmentStatus;
	}

	public void setPreviousConsignmentStatus(int previousConsignmentStatus) {
		this.previousConsignmentStatus = previousConsignmentStatus;
	}

	public String getStateInterp() {
		return stateInterp;
	}

	public void setStateInterp(String stateInterp) {
		this.stateInterp = stateInterp;
	}

	public String getTaxInterp() {
		return taxInterp;
	}

	public void setTaxInterp(String taxInterp) {
		this.taxInterp = taxInterp;
	}

	public String getExpectedArrivalPortInterp() {
		return expectedArrivalPortInterp;
	}

	public void setExpectedArrivalPortInterp(String expectedArrivalPortInterp) {
		this.expectedArrivalPortInterp = expectedArrivalPortInterp;
	}

	public String getCurrencyInterp() {
		return currencyInterp;
	}

	public void setCurrencyInterp(String currencyInterp) {
		this.currencyInterp = currencyInterp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConsignmentMgmt [id=");
		builder.append(id);
		builder.append(", supplierId=");
		builder.append(supplierId);
		builder.append(", supplierName=");
		builder.append(supplierName);
		builder.append(", consignmentNumber=");
		builder.append(consignmentNumber);
		builder.append(", taxPaidStatus=");
		builder.append(taxPaidStatus);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", consignmentStatus=");
		builder.append(consignmentStatus);
		builder.append(", previousConsignmentStatus=");
		builder.append(previousConsignmentStatus);
		builder.append(", organisationCountry=");
		builder.append(organisationCountry);
		builder.append(", expectedDispatcheDate=");
		builder.append(expectedDispatcheDate);
		builder.append(", expectedArrivaldate=");
		builder.append(expectedArrivaldate);
		builder.append(", expectedArrivalPort=");
		builder.append(expectedArrivalPort);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", remarks=");
		builder.append(remarks);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", totalPrice=");
		builder.append(totalPrice);
		builder.append(", user=");
		builder.append(user);
		builder.append("]");
		return builder.toString();
	}
	
}
