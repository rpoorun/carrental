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

	@Autowired
	public CustomAuthenticationProvider(UserRepo userRepo, PasswordEncoder passwordEncoder) {
		super();
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// Get UserName and password from loginForm
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		// TODO: Optional<User> op = userRepo.findByNationalId(username);

		User uTest = userRepo.findByNationalId(username);

		if (uTest != null) {

			// Get encoded password from database
			String encodedPassword = uTest.getPassword();
			
			List<GrantedAuthority> authorities = new ArrayList<>();
			
			if(uTest.getRole().equalsIgnoreCase("ADMIN")) {
				authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
				authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
			}
			
			if(uTest.getRole().equalsIgnoreCase("CUSTOMER")) {
				authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
			}

			// Compares the encrypted password from database to the password from form
			if (passwordEncoder.matches(password, encodedPassword)) {
				return new UsernamePasswordAuthenticationToken(uTest.getNationalId(), encodedPassword);
			}
			else {
				throw new BadCredentialsException("Invalid Password");
			}
			
			
			
		} throw new BadCredentialsException("Invalid credentials");

		

	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
