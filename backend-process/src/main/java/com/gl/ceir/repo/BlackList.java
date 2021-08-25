/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.ceir.repo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author maverick
 */
@Entity
public class BlackList {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
     private LocalDateTime createdOn;

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

     @Override
     public String toString() {
          return "BlackList{" + "id=" + id + ", createdOn=" + createdOn + '}';
     }

     
     
}
