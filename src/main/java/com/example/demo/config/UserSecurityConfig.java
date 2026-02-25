package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

@EnableWebSecurity
public class UserSecurityConfig {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("UserDetailsServiceImpl")
	private UserDetailsService userDetailsService;

	@Autowired
	private MessageSource messageSource;

	AuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		provider.setMessageSource(messageSource);

		return provider;
	}

	@Bean
	@Order(2)
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/css/**").permitAll() // CSSファイルは認証不要
				.requestMatchers("/", "/register", "/login").permitAll() // トップページと新規会員登録は認証不要
				.anyRequest().authenticated() // 他のURLはログイン後アクセス可能
		).formLogin(login -> login.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/", true)
				.failureUrl("/login?error").permitAll().usernameParameter("mail").passwordParameter("password"))
				.authenticationProvider(daoAuthenticationProvider());
		return http.build();
	}
}