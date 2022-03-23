package com.ogn.services;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ogn.entities.AppUser;
import com.ogn.repositories.AppUserRepository;




@Service
public class ConnexionServiceImpl implements ConnexionService {
	@Autowired
	private AppUserRepository userRest;
	
	@Override
	public Boolean isLdap(String login, String password) {
		
		Hashtable<String, String> env=new Hashtable<String, String>();
		
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://10.100.55.80:389");
		//env.put(Context.PROVIDER_URL, "ldap://orange-sonatel.com:389");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL,   login +"@orange-sonatel.com");
		env.put(Context.SECURITY_CREDENTIALS, password);
		
		@SuppressWarnings("unused")
		DirContext dirContext;
		
		try {
			if(!login.isEmpty() && !password.isEmpty()) {
				dirContext=new InitialDirContext(env);
				return true;
			}else {
				return false;
			}
		}catch (NamingException e) {
			return false;
		}
	}
	
	@Override
	public Boolean isAuth(String username) {
		// TODO Auto-generated method stub
		AppUser user = new AppUser();
		user=userRest.findByUsername(username).orElse(null);
		if(user!=null) {
			return true;
		}else {
			return false;
		}
	}

	

}
