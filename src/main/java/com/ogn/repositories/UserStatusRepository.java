package com.ogn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ogn.entities.UserStatus;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {

}
