package com.gl.ceir.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class ConsignmentRevenueDailyDb {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @CreationTimestamp
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime createdOn;
  
  @UpdateTimestamp
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime modifiedOn;
  
  private Double totalAmountInDollar;
  
  private Double totalAmountInRiel;
  
  private Integer countOfConsignment;
  
  private Integer countOfDevices;
  
  private Integer countOfImei;
  
  private Integer countOfConsignmentWhenPrice;
  
  private Integer countOfDevicesWhenPrice;
  
  private Integer countOfImeiWhenPrice;
  
  public ConsignmentRevenueDailyDb() {}
  
  public ConsignmentRevenueDailyDb(double totalAmountInDollar, double totalAmountInRiel) {
    this.totalAmountInDollar = Double.valueOf(totalAmountInDollar);
    this.totalAmountInRiel = Double.valueOf(totalAmountInRiel);
  }
  
  public ConsignmentRevenueDailyDb(Double totalAmountInDollar, Double totalAmountInRiel, Integer countOfConsignment, Integer countOfDevices, Integer countOfImei, Integer countOfConsignmentWhenPrice, Integer countOfDevicesWhenPrice, Integer countOfImeiWhenPrice) {
    this.totalAmountInDollar = totalAmountInDollar;
    this.totalAmountInRiel = totalAmountInRiel;
    this.countOfConsignment = countOfConsignment;
    this.countOfDevices = countOfDevices;
    this.countOfImei = countOfImei;
    this.countOfConsignmentWhenPrice = countOfConsignmentWhenPrice;
    this.countOfDevicesWhenPrice = countOfDevicesWhenPrice;
    this.countOfImeiWhenPrice = countOfImeiWhenPrice;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public LocalDateTime getCreatedOn() {
    return this.createdOn;
  }
  
  public void setCreatedOn(LocalDateTime createdOn) {
    this.createdOn = createdOn;
  }
  
  public Double getTotalAmountInDollar() {
    return this.totalAmountInDollar;
  }
  
  public void setTotalAmountInDollar(Double totalAmountInDollar) {
    this.totalAmountInDollar = totalAmountInDollar;
  }
  
  public Double getTotalAmountInRiel() {
    return this.totalAmountInRiel;
  }
  
  public void setTotalAmountInRiel(Double totalAmountInRiel) {
    this.totalAmountInRiel = totalAmountInRiel;
  }
  
  public Integer getCountOfConsignment() {
    return this.countOfConsignment;
  }
  
  public void setCountOfConsignment(Integer countOfConsignment) {
    this.countOfConsignment = countOfConsignment;
  }
  
  public Integer getCountOfDevices() {
    return this.countOfDevices;
  }
  
  public void setCountOfDevices(Integer countOfDevices) {
    this.countOfDevices = countOfDevices;
  }
  
  public Integer getCountOfImei() {
    return this.countOfImei;
  }
  
  public void setCountOfImei(Integer countOfImei) {
    this.countOfImei = countOfImei;
  }
  
  public Integer getCountOfConsignmentWhenPrice() {
    return this.countOfConsignmentWhenPrice;
  }
  
  public void setCountOfConsignmentWhenPrice(Integer countOfConsignmentWhenPrice) {
    this.countOfConsignmentWhenPrice = countOfConsignmentWhenPrice;
  }
  
  public Integer getCountOfDevicesWhenPrice() {
    return this.countOfDevicesWhenPrice;
  }
  
  public void setCountOfDevicesWhenPrice(Integer countOfDevicesWhenPrice) {
    this.countOfDevicesWhenPrice = countOfDevicesWhenPrice;
  }
  
  public Integer getCountOfImeiWhenPrice() {
    return this.countOfImeiWhenPrice;
  }
  
  public void setCountOfImeiWhenPrice(Integer countOfImeiWhenPrice) {
    this.countOfImeiWhenPrice = countOfImeiWhenPrice;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ConsignmentRevenueDailyDb [id=");
    stringBuilder.append(this.id);
    stringBuilder.append(", createdOn=");
    stringBuilder.append(this.createdOn);
    stringBuilder.append(", totalAmountInDollar=");
    stringBuilder.append(this.totalAmountInDollar);
    stringBuilder.append(", totalAmountInRiel=");
    stringBuilder.append(this.totalAmountInRiel);
    stringBuilder.append(", countOfConsignment=");
    stringBuilder.append(this.countOfConsignment);
    stringBuilder.append(", countOfDevices=");
    stringBuilder.append(this.countOfDevices);
    stringBuilder.append(", countOfImei=");
    stringBuilder.append(this.countOfImei);
    stringBuilder.append(", countOfConsignmentWhenPrice=");
    stringBuilder.append(this.countOfConsignmentWhenPrice);
    stringBuilder.append(", countOfDevicesWhenPrice=");
    stringBuilder.append(this.countOfDevicesWhenPrice);
    stringBuilder.append(", countOfImeiWhenPrice=");
    stringBuilder.append(this.countOfImeiWhenPrice);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}
