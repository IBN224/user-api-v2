package com.ogn.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogn.entities.AppRole;


public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
    
	public AppRole findByRoleName(String role);
	
	@Query("select r from AppRole r where r.roleName like %:appName% ")
	public List<AppRole> getRolesByRoleAppName(@Param("appName")String appName);
	
}
