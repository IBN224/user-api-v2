package com.ogn.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ogn.entities.AppRole;
import com.ogn.entities.AppUser;
import com.ogn.entities.Entite;
import com.ogn.entities.UserStatus;
import com.ogn.repositories.AppRoleRepository;
import com.ogn.repositories.AppUserRepository;
import com.ogn.repositories.UserStatusRepository;
import com.ogn.services.AccountService;

import lombok.Data;

@RestController
@CrossOrigin("*")
public class UserController {
	
    @Autowired
    private AccountService accountService;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private AppRoleRepository appRoleRepository;
    @Autowired
    private UserStatusRepository userStatusRepository;
     
    private static final String defaultPassword = "12345678";
    

    
    @PostMapping(value = "/register")
    public AppUser addUser(@RequestBody UserForm userForm){
    	AppUser appUser = accountService.saveUser(userForm.getUsername(), defaultPassword, userForm.getEntite()); 
    	
    	/********************* add role to user ******************************/
    	accountService.addRoleToUser(userForm.getUsername(), userForm.getRole());
    	
    	/********************* add status of user **************************/
    	 UserStatus userStatus =new UserStatus();
    	 userStatus.setActived(true);
    	 userStatus.setAppName(userForm.getRole().substring(userForm.getRole().lastIndexOf("_")+1));
    	 userStatus.setAppUser(appUser);
    	 userStatusRepository.save(userStatus);
    	
        return appUser;        		
    }
    
    
    @PutMapping(value = "/register")
    public AppUser updateAppUser(@RequestBody UserForm userForm){
    	AppUser appUser = appUserRepository.findByUsername(userForm.getUsername()).get();
    	appUser.setEntite(userForm.getEntite());
    	appUserRepository.save(appUser);
    	
    	/*************** update of role********************/
    	accountService.updateRoleToUser(userForm.getUsername(), userForm.getOldRoleName(), userForm.getRole());   	
    	
        return appUser;        		
    }

   
    @GetMapping("/user")
    public List<AppUser> getUsers() {
    	return appUserRepository.findAll();
    }
    
    @GetMapping("/user/{username}")
    public AppUser getUserByUsername(@PathVariable(value = "username")String userName) {
    	return appUserRepository.findByUsername(userName).orElse(null);
    }
    
    @GetMapping("/userBy/{appName}")
   // @RequestMapping(value = "/userBy/{appName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppUser> getUserByRoleAppName(@PathVariable(value = "appName")String appName) {
    	return appUserRepository.getUserByRoleAppName(appName);
    }
    
    @GetMapping("/role/{appName}")
    public List<AppRole> getRolesByRoleAppName(@PathVariable(value = "appName")String appName) {
    	return appRoleRepository.getRolesByRoleAppName(appName);
    }
    
    
}


@Data
class UserForm{
    private String username;
    private String role;
    private String oldRoleName;
    private Entite entite;
}
