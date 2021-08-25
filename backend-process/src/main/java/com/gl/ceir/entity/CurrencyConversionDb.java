package com.gl.ceir.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CurrencyConversionDb {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreationTimestamp
	@JsonIgnore
	private Date createdOn;
	
	@UpdateTimestamp
	private LocalDateTime modifiedOn;
	
	private Integer currency;
	
	@Column(length = 7)
	@NonNull
	private String monthAndYear;
	
	@NonNull
	private Double dollarRate;
	
	@NonNull
	private Double reilRate;

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

	public LocalDateTime getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(LocalDateTime modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public String getMonthAndYear() {
		return monthAndYear;
	}

	public void setMonthAndYear(String monthAndYear) {
		this.monthAndYear = monthAndYear;
	}

	public Double getDollarRate() {
		return dollarRate;
	}

	public void setDollarRate(Double dollarRate) {
		this.dollarRate = dollarRate;
	}

	public Double getReilRate() {
		return reilRate;
	}

	public void setReilRate(Double reilRate) {
		this.reilRate = reilRate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CurrencyConversionDb [id=");
		builder.append(id);
		builder.append(", createdOn=");
		builder.append(createdOn);
		builder.append(", modifiedOn=");
		builder.append(modifiedOn);
		builder.append(", currency=");
		builder.append(currency);
		builder.append(", monthAndYear=");
		builder.append(monthAndYear);
		builder.append(", dollarRate=");
		builder.append(dollarRate);
		builder.append(", reilRate=");
		builder.append(reilRate);
		builder.append("]");
		return builder.toString();
	}
	
}
