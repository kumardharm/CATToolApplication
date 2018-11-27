package com.cattool.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cattool.application.entity.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>{

	

	Application findByApplicationName(String applicationName);
	void  deleteByApplicationId(int applicationId);
	Application findByApplicationId(int applicationId);
	Application findByUserId(int userId);
	List<Application> findByClientNameAndIsDeactivate(String clientName,Boolean isDeactivate);
	//List<Application> findByClientNameAndNotIsDeactivate(String clientName);
}
