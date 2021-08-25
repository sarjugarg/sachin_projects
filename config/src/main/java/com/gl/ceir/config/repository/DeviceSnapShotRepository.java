package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.DeviceSnapShot;

@Repository
public interface DeviceSnapShotRepository extends JpaRepository<DeviceSnapShot, Long> {

}
