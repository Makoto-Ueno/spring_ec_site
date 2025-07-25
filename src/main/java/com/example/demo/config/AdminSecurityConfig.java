package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

@EnableWebSecurity
public class AdminSecurityConfig {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("AdminDetailsServiceImpl")
	private UserDetailsService userDetailsService;

	@Bean
	@Order(1)
	SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
		http.securityMatcher("/admin/**").authorizeHttpRequests(
				authorize -> authorize.requestMatchers("/admin/register").permitAll().anyRequest().hasRole("ADMIN"))
				.formLogin(login -> login.loginPage("/admin/login") // ログインフォームのURL
						.loginProcessingUrl("/admin/login") // フォームのPOST先
						.defaultSuccessUrl("/admin", true) // ログイン成功時の遷移先
						.failureUrl("/admin/login?error") // エラー時
						.permitAll().usernameParameter("mail").passwordParameter("password"));
		return http.build();
	}

}
