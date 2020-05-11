package com.obss.jss.project13_1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.obss.jss.project13_1.filter.RequestFilter;
import com.obss.jss.project13_1.service.UserService;

@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationEntryPointImp authenticationEntryPointImp;

	@Autowired
	private UserService userService;

	@Autowired
	private RequestFilter requestFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests().antMatchers("/authenticate").permitAll()
				.antMatchers(HttpMethod.DELETE, "/movies/**", "/users/*", "/directors/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.PUT, "/movies/**", "/users/**", "/directors/**").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/movies/**", "/directors/**", "/users", "/omdb**").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/users/watched-list/*", "/users/favorite-list/*").hasAnyRole("USER", "ADMIN")
				.antMatchers(HttpMethod.POST, "/users/watched-list/*", "/users/favorite-list/*").hasAnyRole("USER", "ADMIN")
				.antMatchers(HttpMethod.GET, "/users/watched-list/**", "/users/favorite-list/**", "/users/token", "/movies/").hasAnyRole("USER", "ADMIN")
				.antMatchers(HttpMethod.GET, "/users/**", "/movies/*").hasRole("ADMIN")
				.anyRequest().authenticated().and().exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPointImp).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
