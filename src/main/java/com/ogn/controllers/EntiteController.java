package com.ogn.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ogn.entities.Entite;
import com.ogn.repositories.EntiteRepository;

@RestController
@CrossOrigin("*")
public class EntiteController {
	
    @Autowired
    private EntiteRepository entiteRepository;
    
    

    @GetMapping("/entite")
    public List<Entite> getEntites() {
    	return entiteRepository.findAll();
    }
      
    
}
