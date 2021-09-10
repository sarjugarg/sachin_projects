package com.ceir.SLAModule.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceir.SLAModule.entity.User;

public interface UserRepo extends JpaRepository<User, Long>{

	
	public User findById(long id);
	public List<User> findByCurrentStatus(Integer status);
}
