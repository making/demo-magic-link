package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.ott.JdbcOneTimeTokenService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JdbcTemplate jdbcTemplate) throws Exception {
		http.authorizeHttpRequests(
				authz -> authz.requestMatchers("/login", "/login/ott", "/ott/sent", "/signup", "/error", "/style.css")
					.permitAll()
					.anyRequest()
					.authenticated())
			.formLogin(form -> form.loginPage("/login"))
			.oneTimeTokenLogin(ott -> ott.defaultSubmitPageUrl("/login/ott").showDefaultSubmitPage(false));
		return http.build();
	}

	@Bean
	public JdbcOneTimeTokenService jdbcOneTimeTokenService(JdbcTemplate jdbcTemplate) {
		// cleanupExpiredTokens fails.
		// See https://github.com/spring-projects/spring-security/pull/16344
		return new JdbcOneTimeTokenService(jdbcTemplate);
	}

}
