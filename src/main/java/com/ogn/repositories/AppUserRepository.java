package com.ogn.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ogn.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    
	Optional<AppUser> findByUsername(String username);
	
	@Query("select u from AppUser u left join u.roles r where r.roleName like %:appName% ")
	public List<AppUser> getUserByRoleAppName(@Param("appName")String appName);
	
	
}
