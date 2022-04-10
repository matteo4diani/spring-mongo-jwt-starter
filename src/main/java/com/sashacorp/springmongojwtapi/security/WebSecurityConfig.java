package com.sashacorp.springmongojwtapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sashacorp.springmongojwtapi.appconfig.AppConfigurator;
import com.sashacorp.springmongojwtapi.security.filter.JwtRequestFilter;

/**
 * Global security configuration class.
 * 
 * @author matteo
 *
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService myUserDetailsService;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
	}
	@Bean
	public AppConfigurator appConfigurator() {
		return new AppConfigurator();
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * Mappings are secured here. <br>
	 * <span style='color: orange;'>WARNING:</span> Controllers must be annotated
	 * with @CrossOrigin to function.
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().authorizeRequests().antMatchers("/authenticate").permitAll()
				.antMatchers("/startup").access("@appConfigurator.isAppUninitialized()")
				.antMatchers("/me", "/me/*", "/events").hasAnyAuthority("USER", "HR", "MANAGER", "ADMIN")
				.antMatchers("/users", "/users/*", "/messages", "/messages/*", "/events/*", "/register").hasAnyAuthority("HR", "MANAGER", "ADMIN")
				.antMatchers("/admin", "/admin/*").hasAnyAuthority("MANAGER", "ADMIN")
				.antMatchers("/reset").hasAuthority("ADMIN")
				.anyRequest().authenticated().and().exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		/**
		 * Calling httpSecurity.cors() disables HTTP OPTIONS pre-flight calls for CORS
		 * negotiation, allowing us to work with a decoupled front-end.
		 */
		httpSecurity.cors();

	}

}