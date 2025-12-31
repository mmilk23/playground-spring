package com.milklabs.playground.util.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import com.milklabs.playground.views.LoginView;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;

@Configuration
public class SecurityConfiguration {

	private final UserDetailsService userDetailsService;

	public SecurityConfiguration(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new CustomAuthenticationProvider(userDetailsService);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		var m = PathPatternRequestMatcher.withDefaults();

		http.authorizeHttpRequests(auth -> auth.requestMatchers(m.matcher("/images/**")).permitAll().requestMatchers(m.matcher("/line-awesome/**")).permitAll());

		http.authenticationProvider(authenticationProvider());

		return http.with(VaadinSecurityConfigurer.vaadin(), vaadin -> {
			vaadin.loginView(LoginView.class);
		}).build();
	}
}
