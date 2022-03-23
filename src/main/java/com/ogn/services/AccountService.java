package com.ogn.services;

import com.ogn.entities.AppRole;
import com.ogn.entities.AppUser;
import com.ogn.entities.Entite;

public interface AccountService {
    public AppUser saveUser(String username, String password, Entite entite); //, String confirmedPassword
    public AppRole saveRole(AppRole role);
    public AppUser loadUserByUsername(String username);
    public void addRoleToUser(String username, String roleName);
    public void updateRoleToUser(String username, String old_roleName, String new_roleName);
    public AppUser updatUser(Long id, String password);
}
