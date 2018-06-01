package com.accenture.rishikeshpoorun.configs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.accenture.rishikeshpoorun.dao.entities.User;
import com.accenture.rishikeshpoorun.dao.repositories.UserRepo;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private UserRepo userRepo;
	private PasswordEncoder passwordEncoder;
	private static String id;

	@Autowired
	public CustomAuthenticationProvider(UserRepo userRepo, PasswordEncoder passwordEncoder) {
		super();
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// Get UserName and password from loginForm
		String nationalId = authentication.getName();
		String password = authentication.getCredentials().toString();
		System.out.println(nationalId);

		// TODO: Optional<User> op = userRepo.findByNationalId(username);

		User uTest = userRepo.findByNationalId(nationalId);
		
		List authorities = getAuthorities(uTest);
		
		// Get encoded password from database
		String encodedPassword = uTest.getPassword();
		
		if(passwordEncoder.matches(password, encodedPassword)) {
			
			return new UsernamePasswordAuthenticationToken(nationalId, encodedPassword, authorities);
		}

		
		throw new BadCredentialsException("Invalid credentials");

}

	
	
	  private List<GrantedAuthority> getAuthorities(User user) {
	        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
	        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
	        
	        return grantedAuthorities;
	    }

	  @Override
	    public boolean supports(Class<?> authentication) {
	        return authentication.equals(
	                UsernamePasswordAuthenticationToken.class);
	    }


		public static String getId() {
			return id;
		}


		public static void setId(String id) {
			CustomAuthenticationProvider.id = id;
		}
	}

