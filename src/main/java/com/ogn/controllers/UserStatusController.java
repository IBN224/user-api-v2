package com.ogn.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ogn.entities.UserStatus;
import com.ogn.repositories.UserStatusRepository;

import lombok.Data;

@RestController
@CrossOrigin("*")
public class UserStatusController {

	 @Autowired
	 private UserStatusRepository userStatusRepository;
	    
	    
	 
     @PutMapping(value = "/userStatus/{id}")
     public void updateUserStatus(@PathVariable(value = "id")Long id, @RequestBody UserStatusForm form){
    	UserStatus status = userStatusRepository.findById(id).get();
    	status.setActived(form.getActived());
        userStatusRepository.save(status);        		
     }
	
	 
}
@Data
class UserStatusForm{
	private Long id;
	private Boolean actived;
}
