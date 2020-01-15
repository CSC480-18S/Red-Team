package com.csc480red.gamedb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/*
 * Web Security Configurer Adapter
 * Configures allowed users and authentication of users.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
 
	/*
	 * Initalize the customer basic authentication entry point
	 */
    @Autowired
    private GameBasicAuthEntryPoint authenticationEntryPoint;
 
    /*
     * Configures user, password, and user role. This is the login information to access the database.
     * !!!! ONCE YOU LOG IN ONCE, YOU DO NOT NEED TO LOG IN AGAIN UNTIL YOU CLOSE THE DATABASE !!!
     * Username and password were randomly generated. The role is a regular user role.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
          .withUser("ChiefJellyfish").password("y>TCmk86x#MjmA&M")
          .authorities("ROLE_USER");
    }
 
    /*
     * Configures authentication and error handling. Once a user logs in once, they do not need to keep
     * logging in for each request until logging out of the database.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
          .antMatchers("/securityNone").permitAll()
          .anyRequest().authenticated()
          .and()
          .httpBasic()
          .authenticationEntryPoint(authenticationEntryPoint);
 
    }
}
