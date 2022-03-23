package com.ogn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ogn.entities.Entite;

public interface EntiteRepository extends JpaRepository<Entite, Long> {
    
}
