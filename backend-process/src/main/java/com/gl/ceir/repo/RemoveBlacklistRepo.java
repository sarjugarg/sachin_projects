/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.ceir.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author maverick
 */

public interface RemoveBlacklistRepo extends JpaRepository<BlackList, Long>, JpaSpecificationExecutor<BlackList> {
     @Query("delete from  Black_list   where  created_on < ?1 ")
     public void deleteByDate(String date);

}
