package com.helseapps.task.config;

import com.helseapps.task.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private CorsConfigurationSource corsConfigurationSource;
	@Autowired
	private UserService userService;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors().configurationSource(corsConfigurationSource);

		http.csrf().disable().authorizeRequests()
				//.antMatchers(HttpMethod.POST, Endpoints.USERS + SIGN_UP_URL).permitAll()
				.antMatchers("/v3/api-docs",
						"/v3/api-docs/**",
						"/swagger-resources/configuration/ui",
						"/swagger-resources",
						"/swagger-resources/configuration/security",
						"/swagger-ui/**",
						"/swagger-ui.html",
						"/webjars/**",
						"/actuator/health").permitAll()
				.anyRequest().authenticated()
				.and()
				.addFilter(new BasicAuthorizationFilter(authenticationManager(), userService))
				// this disables session creation on Spring Security
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
