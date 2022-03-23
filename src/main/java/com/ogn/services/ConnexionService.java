package com.ogn.services;


public interface ConnexionService {
	
	Boolean isLdap(String login, String password);
	Boolean isAuth(String username);
	
}
