package com.accenture.rishikeshpoorun.configs;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
     * Global http security configuration.
     * @param http HTTP security configuration
     * @throws Exception thrown when a configuration error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	http.authorizeRequests().antMatchers("/index","/").permitAll();
    	
    	
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/customer").hasRole("CUSTOMER")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/index")
            .permitAll();
        
    	
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/secured").hasRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/index")
            .permitAll();
        
        http.csrf().disable()
	        .authorizeRequests()
	        .antMatchers("/rest")
	        .hasRole("WS")
	        .anyRequest().authenticated()
	        .and()
	        .formLogin()
	        .permitAll();
	      
    }


    /**
     * In memory authentication configuration using AuthenticationManagerBuilder
     */
    //*/
     @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("customer").password("1234").roles("CUSTOMER");
        auth.inMemoryAuthentication().withUser("admin").password("1234").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("webservice").password("1234").roles("WS");
    }
     //*/

    /**
     * Database authentication configuration using AuthenticationManagerBuilder
     */
   /*/
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
        String userQuery = "SELECT national_id, password FROM user_table WHERE national_id =?";
        String authoritiesQuery ="select national_id, 'ROLE_ADMIN' FROM user_table where national_id =?";
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(userQuery)
                .authoritiesByUsernameQuery(authoritiesQuery)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
    //*/

    /**
     * Encoder bean definition.
     * @return Encoder bean instance
     */
    @Bean
    public PasswordEncoder encoder(){
        // Use a PasswordEncoder not performing any encoding. Do not use this one in production. BCryptEncoder is recommended
       // 
    	return  NoOpPasswordEncoder.getInstance();
        // return new BCryptEncoder
    	//return BCryptPasswordEncoder;
    }
    
   /* @Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
    }*/
	
    
    
/*
 * Method by Yoven incomplete
 */
	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//
//		http.authorizeRequests().antMatchers("/index").permitAll().anyRequest().authenticated();
//
//		http.authorizeRequests().antMatchers("/secured").hasRole("ADMIN").anyRequest().authenticated().and().formLogin()
//				.loginPage("/login").permitAll();
//
//		http.authorizeRequests().antMatchers("/rent").hasRole("USER").anyRequest().authenticated().and().formLogin()
//				.loginPage("/login").permitAll();
//	}
//
//	// In Memory Authentication
//	@Autowired
//	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("admin").password("1234").roles("ADMIN");
//		auth.inMemoryAuthentication().withUser("customer").password("1234").roles("USER");
//	}
//
//	// No Password Encoder
//	@SuppressWarnings("deprecation")
//	@Bean
//	public PasswordEncoder encoder() {
//		return NoOpPasswordEncoder.getInstance();
//	}
//
//	
//	@Bean
//	public BCryptPasswordEncoder bCryptPasswordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
}
