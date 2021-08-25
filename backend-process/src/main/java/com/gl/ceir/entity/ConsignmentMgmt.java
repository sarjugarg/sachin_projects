package com.gl.ceir.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gl.ceir.entity.User;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Entity
@Audited
public class ConsignmentMgmt implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private String supplierId;
  
  private String supplierName;
  
  @Column(length = 15)
  private String consignmentNumber;
  
  @Column(length = 10)
  private Integer taxPaidStatus;
  
  @CreationTimestamp
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
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
  
  private Integer deviceQuantity;
  
  @Transient
  private String stateInterp;
  
  @Transient
  private String taxInterp;
  
  @NotAudited
  @OneToOne
  @JoinColumn(name = "local_user_id", updatable = false)
  private User user;
  
  @Column(length = 1)
  private String pendingTacApprovedByCustom;
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getSupplierld() {
    return this.supplierId;
  }
  
  public void setSupplierId(String supplierId) {
    this.supplierId = supplierId;
  }
  
  public String getSupplierName() {
    return this.supplierName;
  }
  
  public void setSupplierName(String supplierName) {
    this.supplierName = supplierName;
  }
  
  public String getConsignmentNumber() {
    return this.consignmentNumber;
  }
  
  public void setConsignmentNumber(String consignmentNumber) {
    this.consignmentNumber = consignmentNumber;
  }
  
  public Integer getTaxPaidStatus() {
    return this.taxPaidStatus;
  }
  
  public void setTaxPaidStatus(Integer taxPaidStatus) {
    this.taxPaidStatus = taxPaidStatus;
  }
  
  public LocalDateTime getCreatedOn() {
    return this.createdOn;
  }
  
  public void setCreatedOn(LocalDateTime createdOn) {
    this.createdOn = createdOn;
  }
  
  public LocalDateTime getModifiedOn() {
    return this.modifiedOn;
  }
  
  public void setModifiedOn(LocalDateTime modifiedOn) {
    this.modifiedOn = modifiedOn;
  }
  
  public Integer getUserId() {
    return this.userId;
  }
  
  public void setUserId(Integer userId) {
    this.userId = userId;
  }
  
  public String getSupplierId() {
    return this.supplierId;
  }
  
  public String getTxnId() {
    return this.txnId;
  }
  
  public void setTxnId(String txnId) {
    this.txnId = txnId;
  }
  
  public String getFileName() {
    return this.fileName;
  }
  
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  
  public int getConsignmentStatus() {
    return this.consignmentStatus;
  }
  
  public void setConsignmentStatus(int consignmentStatus) {
    this.consignmentStatus = consignmentStatus;
  }
  
  public String getOrganisationCountry() {
    return this.organisationCountry;
  }
  
  public void setOrganisationCountry(String organisationCountry) {
    this.organisationCountry = organisationCountry;
  }
  
  public String getExpectedDispatcheDate() {
    return this.expectedDispatcheDate;
  }
  
  public void setExpectedDispatcheDate(String expectedDispatcheDate) {
    this.expectedDispatcheDate = expectedDispatcheDate;
  }
  
  public String getExpectedArrivaldate() {
    return this.expectedArrivaldate;
  }
  
  public void setExpectedArrivaldate(String expectedArrivaldate) {
    this.expectedArrivaldate = expectedArrivaldate;
  }
  
  public Integer getExpectedArrivalPort() {
    return this.expectedArrivalPort;
  }
  
  public void setExpectedArrivalPort(Integer expectedArrivalPort) {
    this.expectedArrivalPort = expectedArrivalPort;
  }
  
  public int getQuantity() {
    return this.quantity;
  }
  
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  
  public String getRemarks() {
    return this.remarks;
  }
  
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }
  
  public User getUser() {
    return this.user;
  }
  
  public com.gl.ceir.entity.ConsignmentMgmt setUser(User user) {
    this.user = user;
    return this;
  }
  
  public Integer getCurrency() {
    return this.currency;
  }
  
  public Double getTotalPrice() {
    return this.totalPrice;
  }
  
  public void setTotalPrice(Double totalPrice) {
    this.totalPrice = totalPrice;
  }
  
  public int getPreviousConsignmentStatus() {
    return this.previousConsignmentStatus;
  }
  
  public void setPreviousConsignmentStatus(int previousConsignmentStatus) {
    this.previousConsignmentStatus = previousConsignmentStatus;
  }
  
  public String getStateInterp() {
    return this.stateInterp;
  }
  
  public void setStateInterp(String stateInterp) {
    this.stateInterp = stateInterp;
  }
  
  public String getTaxInterp() {
    return this.taxInterp;
  }
  
  public void setTaxInterp(String taxInterp) {
    this.taxInterp = taxInterp;
  }
  
  public String getExpectedArrivalPortInterp() {
    return this.expectedArrivalPortInterp;
  }
  
  public void setExpectedArrivalPortInterp(String expectedArrivalPortInterp) {
    this.expectedArrivalPortInterp = expectedArrivalPortInterp;
  }
  
  public String getCurrencyInterp() {
    return this.currencyInterp;
  }
  
  public void setCurrencyInterp(String currencyInterp) {
    this.currencyInterp = currencyInterp;
  }
  
  public static long getSerialversionuid() {
    return 1L;
  }
  
  public void setCurrency(Integer currency) {
    this.currency = currency;
  }
  
  public String getPendingTacApprovedByCustom() {
    return this.pendingTacApprovedByCustom;
  }
  
  public void setPendingTacApprovedByCustom(String pendingTacApprovedByCustom) {
    this.pendingTacApprovedByCustom = pendingTacApprovedByCustom;
  }
  
  public Integer getDeviceQuantity() {
    return this.deviceQuantity;
  }
  
  public void setDeviceQuantity(Integer deviceQuantity) {
    this.deviceQuantity = deviceQuantity;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ConsignmentMgmt [id=");
    stringBuilder.append(this.id);
    stringBuilder.append(", supplierId=");
    stringBuilder.append(this.supplierId);
    stringBuilder.append(", supplierName=");
    stringBuilder.append(this.supplierName);
    stringBuilder.append(", consignmentNumber=");
    stringBuilder.append(this.consignmentNumber);
    stringBuilder.append(", taxPaidStatus=");
    stringBuilder.append(this.taxPaidStatus);
    stringBuilder.append(", createdOn=");
    stringBuilder.append(this.createdOn);
    stringBuilder.append(", modifiedOn=");
    stringBuilder.append(this.modifiedOn);
    stringBuilder.append(", userId=");
    stringBuilder.append(this.userId);
    stringBuilder.append(", txnId=");
    stringBuilder.append(this.txnId);
    stringBuilder.append(", fileName=");
    stringBuilder.append(this.fileName);
    stringBuilder.append(", consignmentStatus=");
    stringBuilder.append(this.consignmentStatus);
    stringBuilder.append(", previousConsignmentStatus=");
    stringBuilder.append(this.previousConsignmentStatus);
    stringBuilder.append(", organisationCountry=");
    stringBuilder.append(this.organisationCountry);
    stringBuilder.append(", expectedDispatcheDate=");
    stringBuilder.append(this.expectedDispatcheDate);
    stringBuilder.append(", expectedArrivaldate=");
    stringBuilder.append(this.expectedArrivaldate);
    stringBuilder.append(", expectedArrivalPort=");
    stringBuilder.append(this.expectedArrivalPort);
    stringBuilder.append(", expectedArrivalPortInterp=");
    stringBuilder.append(this.expectedArrivalPortInterp);
    stringBuilder.append(", quantity=");
    stringBuilder.append(this.quantity);
    stringBuilder.append(", remarks=");
    stringBuilder.append(this.remarks);
    stringBuilder.append(", currency=");
    stringBuilder.append(this.currency);
    stringBuilder.append(", currencyInterp=");
    stringBuilder.append(this.currencyInterp);
    stringBuilder.append(", totalPrice=");
    stringBuilder.append(this.totalPrice);
    stringBuilder.append(", deviceQuantity=");
    stringBuilder.append(this.deviceQuantity);
    stringBuilder.append(", stateInterp=");
    stringBuilder.append(this.stateInterp);
    stringBuilder.append(", taxInterp=");
    stringBuilder.append(this.taxInterp);
    stringBuilder.append(", user=");
    stringBuilder.append(this.user);
    stringBuilder.append(", pendingTacApprovedByCustom=");
    stringBuilder.append(this.pendingTacApprovedByCustom);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}
