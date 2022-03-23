package com.ogn.securities;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ogn.entities.AppRole;
import com.ogn.entities.AppUser;
import com.ogn.repositories.AppUserRepository;
import com.ogn.services.AccountService;
import com.ogn.services.ApplicationContextHolder;
import com.ogn.services.ConnexionException;
import com.ogn.services.ConnexionService;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
    private AuthenticationManager authenticationManager;
    
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
   
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

    	AppUserRepository userRepository = ApplicationContextHolder.getContext().getBean(AppUserRepository.class);
    	ConnexionService conService = ApplicationContextHolder.getContext().getBean(ConnexionService.class);
    	AccountService accountService = ApplicationContextHolder.getContext().getBean(AccountService.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


        try {
           AppUser appUser = new ObjectMapper().readValue(request.getInputStream(),AppUser.class);
           AppUser appUser2 = userRepository.findByUsername(appUser.getUsername()).orElse(null);
            //System.out.println(appUser.getPassword());
           // System.out.println(encoder.matches(appUser.getPassword(), appUser2.getPassword()));
           if(appUser2!=null) {
        	   if(encoder.matches(appUser.getPassword(), appUser2.getPassword())) {//conService.isLdap(appUser.getUsername(), appUser.getPassword())
        		   List<AppRole> roles = new ArrayList<>();
        		   appUser2.getRoles().forEach(a->{
        	            roles.add(a);
        	        });
        		   accountService.updatUser(appUser2.getId(), appUser.getPassword());
          		}
        	   else {
          			throw new UsernameNotFoundException("Ldap login not found");
          		}  
			}
           else {
				throw new ConnexionException("Erreur Login introuvable");
			}
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(appUser.getUsername(),appUser.getPassword()));
        } catch (IOException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
    	
    	
        User user = (User) authResult.getPrincipal();
        List<String> roles = new ArrayList<>();
        authResult.getAuthorities().forEach(a->{
            roles.add(a.getAuthority());
        });
        String jwt = JWT.create()
                .withIssuer(request.getRequestURI())
                .withSubject(user.getUsername())
                .withArrayClaim("roles",roles.toArray(new String[roles.size()]))
                .withExpiresAt(new Date(System.currentTimeMillis()+SecurityParams.EXPIRATION))
                .sign(Algorithm.HMAC256(SecurityParams.SECRET));
        //response.addHeader(SecurityParams.JWT_HEADER_NAME,jwt);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"" + SecurityParams.JWT_HEADER_NAME + "\":\"" + jwt + "\"}");
    }

}