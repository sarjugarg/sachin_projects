package com.gl.ceir.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gl.ceir.entity.RegularizeDeviceDb;

@Repository
public interface RegularizedDeviceDbRepository extends JpaRepository<RegularizeDeviceDb, Long>, 
JpaSpecificationExecutor<RegularizeDeviceDb	> {

	public RegularizeDeviceDb getByDeviceSerialNumber(String serialNumber);

	public void deleteByDeviceSerialNumber(String serialNumber);

	public List<RegularizeDeviceDb> getByNid(String nid);

	public RegularizeDeviceDb getByFirstImei(String imei1);

	public Long countByNid(String nid);

	public RegularizeDeviceDb getByTxnId(String txnid);

	@Query("SELECT r FROM RegularizeDeviceDb r WHERE firstImei = :imei OR secondImei = :imei OR thirdImei = :imei OR fourthImei = :imei") 
	public RegularizeDeviceDb getByImei(String imei);

}
