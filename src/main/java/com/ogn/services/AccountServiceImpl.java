package com.ogn.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ogn.entities.AppRole;
import com.ogn.entities.AppUser;
import com.ogn.entities.Entite;
import com.ogn.repositories.AppRoleRepository;
import com.ogn.repositories.AppUserRepository;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	
    @Autowired
    private AppUserRepository userRepository;
    
    @Autowired
    private AppRoleRepository roleRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    

    @Override
    public AppUser saveUser(String username, String password, Entite entite) { 
        AppUser user = userRepository.findByUsername(username).orElse(null);
        AppUser appUser = new AppUser();
        
        if (user==null) {
        	
        	appUser.setUsername(username);
	        appUser.setPassword(passwordEncoder.encode(password));
	        appUser.setEntite(entite);
	        userRepository.save(appUser);
	        
        }else if(user!=null) {
        	
        	appUser=user;
        	
        }

        return appUser;
    }

    @Override
    public AppRole saveRole(AppRole role) {
        return roleRepository.save(role);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser = userRepository.findByUsername(username).orElse(null);
        AppRole appRole = roleRepository.findByRoleName(roleName);
        appUser.getRoles().add(appRole);
        
    }

	@Override
	public AppUser updatUser(Long id, String password) {
		AppUser appUser = userRepository.findById(id).get();
		appUser.setPassword(passwordEncoder.encode(password));
		return userRepository.save(appUser);
	}

	@Override
	public void updateRoleToUser(String username, String old_roleName, String new_roleName) {
		AppUser appUser = userRepository.findByUsername(username).orElse(null);
        AppRole appRole_old = roleRepository.findByRoleName(old_roleName);
        AppRole appRole_new = roleRepository.findByRoleName(new_roleName);
        List<AppRole> roles = new ArrayList<AppRole>(appUser.getRoles());
        roles.remove(appRole_old);
        roles.add(appRole_new);
        appUser.setRoles(roles);
		
	}

	


	
    
    
}
